package co.nxtgrid.tokens.service.impl.generate;

import co.nxtgrid.token.domain.*;
import co.nxtgrid.token.domain.encryptionalgorithm.DataEncryptionAlgorithm;
import co.nxtgrid.token.domain.encryptionalgorithm.EncryptionAlgorithm;
import co.nxtgrid.token.domain.encryptionalgorithm.Misty1AlgorithmEncryptionAlgorithm;
import co.nxtgrid.token.domain.encryptionalgorithm.StandardTransferAlgorithmEncryptionAlgorithm;
import co.nxtgrid.token.domain.keys.decoder.DecoderKey;
import co.nxtgrid.token.domain.keys.vending.VendingUniqueDESKey;
import co.nxtgrid.token.domain.meterprimaryaccountnumber.MeterPrimaryAccountNumber;
import co.nxtgrid.token.domain.supplygroupcode.SupplyGroupCode;
import co.nxtgrid.token.exceptions.InvalidBaseDateException;
import co.nxtgrid.token.exceptions.InvalidDecoderKeyGenerationAlgorithm;
import co.nxtgrid.token.generators.decoderkeygenerator.DecoderKeyGeneratorAlgorithm01;
import co.nxtgrid.token.generators.decoderkeygenerator.DecoderKeyGeneratorAlgorithm02;
import co.nxtgrid.token.generators.decoderkeygenerator.DecoderKeyGeneratorAlgorithm03;
import co.nxtgrid.token.generators.decoderkeygenerator.DecoderKeyGeneratorAlgorithm04;
import co.nxtgrid.token.generators.utils.LuhnAlgorithm;
import co.nxtgrid.tokens.constant.StringConstants;
import co.nxtgrid.tokens.service.impl.generate.exceptions.InvalidEncryptionAlgorithmException;

import jakarta.xml.bind.DatatypeConverter;
import java.util.Map;

import static co.nxtgrid.token.domain.meterprimaryaccountnumber.MeterPrimaryAccountNumber.Validate.NO_METER_PAN_VALIDATION;

public class DecoderKeyGeneratorManager {

    private KeyType keyType;
    private SupplyGroupCode supplyGroupCode;
    private TariffIndex tariffIndex;
    private KeyRevisionNumber keyRevisionNumber;
    private IssuerIdentificationNumber issuerIdentificationNumber;
    private IndividualAccountIdentificationNumber iain;
    private MeterPrimaryAccountNumber meterPrimaryAccountNumber;
    private EncryptionAlgorithm encryptionAlgorithm;
    private String dkga;

    public void initializeDecoderKeyGenerationParameters(Map<String, String> params)
            throws Exception {

        keyType = new KeyType(Integer.parseInt(params.get("key_type")));
        supplyGroupCode = new SupplyGroupCode(params.get("supply_group_code"));
        tariffIndex = new TariffIndex(params.get("tariff_index"));
        keyRevisionNumber = new KeyRevisionNumber(Integer.parseInt(params.get("key_revision_no")));

        String meterPAN = String.format("%s%s",
                                        params.get("issuer_identification_no"),
                                        params.get("decoder_reference_number"));
        meterPAN += LuhnAlgorithm.generateCheckDigit(Long.parseLong(meterPAN));
        meterPrimaryAccountNumber
                = new MeterPrimaryAccountNumber(meterPAN, NO_METER_PAN_VALIDATION);
        issuerIdentificationNumber = meterPrimaryAccountNumber.getIssuerIdentificationNumber();
        iain = meterPrimaryAccountNumber.getIndividualAccountIdentificationNumber();

        dkga = params.get("decoder_key_generation_algorithm");
    }

    public DecoderKey generateDecoderKey(Map<String, String> params)
            throws Exception {

        encryptionAlgorithm = getEncryptionAlgorithm(params.get("encryption_algorithm"));
        VendingUniqueDESKey vudk = new VendingUniqueDESKey(DatatypeConverter.parseHexBinary(params.get("vending_key")));

        DecoderKey decoderKey;

        switch (dkga) {
            case("01"):
                decoderKey = generateDKGA01DecoderKey(vudk, encryptionAlgorithm);
                break;
            case("02"):
                decoderKey = generateDKGA02DecoderKey(vudk, encryptionAlgorithm);
                break;
            case("03"):
                decoderKey = generateDKGA03DecoderKey(new VendingUniqueDESKey(
                        DatatypeConverter.parseHexBinary(params.get("vending_key_1"))),
                        new VendingUniqueDESKey(
                                DatatypeConverter.parseHexBinary(params.get("vending_key_2"))));
                break;
            case("04"):
                BaseDate baseDate = generateBaseDate(params.get("base_date"));
                decoderKey = generateDKGA04DecoderKey(baseDate, encryptionAlgorithm,
                                                    meterPrimaryAccountNumber, vudk);
                break;
            default:
                throw new InvalidDecoderKeyGenerationAlgorithm(
                        String.format("%s %s",
                                StringConstants.INVALID_DECODER_KEY_GENERATION_ALGORITHM, dkga));
        }
        return decoderKey;
    }

    private DecoderKey generateDKGA01DecoderKey(VendingUniqueDESKey vudk,
                                                       EncryptionAlgorithm encryptionAlgorithm)
            throws Exception {
        DecoderKeyGeneratorAlgorithm01 decoderKeyGeneratorAlgorithm01Generator
                = new DecoderKeyGeneratorAlgorithm01(keyType,
                                                        supplyGroupCode,
                                                        tariffIndex,
                                                        keyRevisionNumber,
                                                        issuerIdentificationNumber,
                                                        iain,
                                                        vudk,
                                                        encryptionAlgorithm);
        return decoderKeyGeneratorAlgorithm01Generator.generate();
    }

    private DecoderKey generateDKGA02DecoderKey(VendingUniqueDESKey vudk,
                                                       EncryptionAlgorithm encryptionAlgorithm)
            throws Exception {
        DecoderKeyGeneratorAlgorithm02 decoderKeyAlgorithm02Generator
                = new DecoderKeyGeneratorAlgorithm02(keyType,
                                                    supplyGroupCode,
                                                    tariffIndex,
                                                    keyRevisionNumber,
                                                    issuerIdentificationNumber,
                                                    iain,
                                                    vudk);
        return decoderKeyAlgorithm02Generator.generate();
    }

    private DecoderKey generateDKGA03DecoderKey(VendingUniqueDESKey vudk1,
                                                VendingUniqueDESKey vudk2)
            throws Exception {
        DecoderKeyGeneratorAlgorithm03 decoderKeyGeneratorAlgorithm03Generator
                = new DecoderKeyGeneratorAlgorithm03(keyType,
                                                    supplyGroupCode,
                                                    tariffIndex,
                                                    keyRevisionNumber,
                                                    issuerIdentificationNumber,
                                                    iain,
                                                    vudk1,
                                                    vudk2);
        return decoderKeyGeneratorAlgorithm03Generator.generate();
    }

    private DecoderKey generateDKGA04DecoderKey(BaseDate baseDate, EncryptionAlgorithm encryptionAlgorithm,
                                 MeterPrimaryAccountNumber meterPAN, VendingUniqueDESKey vudk)
            throws Exception {
        DecoderKeyGeneratorAlgorithm04 decoderKeyGeneratorAlgorithm04Generator
                = new DecoderKeyGeneratorAlgorithm04(baseDate,
                                                    tariffIndex,
                                                    supplyGroupCode,
                                                    keyType,
                                                    keyRevisionNumber,
                                                    encryptionAlgorithm,
                                                    meterPAN,
                                                    vudk);
        return decoderKeyGeneratorAlgorithm04Generator.generate();
    }

    private BaseDate generateBaseDate(String baseDateStr)
        throws InvalidBaseDateException {
        switch (baseDateStr) {
            case("1993"):
                return BaseDate._1993;
            case("2014"):
                return BaseDate._2014;
            case("2035"):
                return BaseDate._2035;
            default:
                throw new InvalidBaseDateException(String.format("%s %s",
                        StringConstants.INVALID_BASE_DATE, baseDateStr));
        }
    }

    private EncryptionAlgorithm getEncryptionAlgorithm(String encryptionAlgorithmStr)
            throws InvalidEncryptionAlgorithmException {
        switch (encryptionAlgorithmStr) {
            case ("dea"):
                return new DataEncryptionAlgorithm();
            case("misty1"):
                return new Misty1AlgorithmEncryptionAlgorithm();
            case("sta"):
                return new StandardTransferAlgorithmEncryptionAlgorithm();
            default:
                throw new InvalidEncryptionAlgorithmException(String.format("%s %s",
                        StringConstants.INVALID_ENCRYPTION_ALGORITHM, encryptionAlgorithmStr));
        }
    }

    public EncryptionAlgorithm getEncryptionAlgorithm() {
        return encryptionAlgorithm;
    }
}
