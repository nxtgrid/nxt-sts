package co.nxtgrid.tokens.service.impl.generate.class2;

import co.nxtgrid.token.domain.*;
import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.keys.decoder.DecoderKey;
import co.nxtgrid.token.domain.keys.vending.VendingUniqueDESKey;
import co.nxtgrid.token.domain.meterprimaryaccountnumber.MeterPrimaryAccountNumber;
import co.nxtgrid.token.domain.supplygroupcode.SupplyGroupCode;
import co.nxtgrid.token.generators.decoderkeygenerator.DecoderKeyGeneratorAlgorithm02;
import co.nxtgrid.token.generators.utils.LuhnAlgorithm;
import co.nxtgrid.tokens.service.impl.generate.Generator;

import jakarta.xml.bind.DatatypeConverter;
import java.util.Map;

public class Class2Generator extends Generator {

    public Class2Generator(String requestID, TokenType tokenType) {
        super(requestID, tokenType);
    }

    public static DecoderKey generateNewDecoderKey(Map<String, String> params) throws Exception {

        MeterPrimaryAccountNumber newMeterPrimaryAccountNumber = getNewMeterPrimaryAccountNo(params);

        IssuerIdentificationNumber newIssuerIdentificationNumber = newMeterPrimaryAccountNumber.getIssuerIdentificationNumber();
        IndividualAccountIdentificationNumber newIain = newMeterPrimaryAccountNumber.getIndividualAccountIdentificationNumber();
        DecoderKeyGeneratorAlgorithm02 decoderKeyAlgorithm02Generator
                                                        = new DecoderKeyGeneratorAlgorithm02(getNewKeyType(params),
                                                                                            getNewSupplyGroupCode(params),
                                                                                            getNewTariffIndex(params),
                                                                                            getNewKeyRevisionNo(params),
                                                                                            newIssuerIdentificationNumber,
                                                                                            newIain,
                                                                                            getNewVendingUniqueDESKey(params));
        return decoderKeyAlgorithm02Generator.generate();
    }

    public static Register getRegister(Map<String, String> params)
            throws Exception {
        BitString registerBitString = new BitString(Long.parseLong(params.get("register")));
        registerBitString.setLength(16);
        return new Register(registerBitString);
    }

    public static KeyType getNewKeyType(Map<String, String> params) throws Exception {
        return new KeyType(Integer.parseInt(params.get("new_key_type")));
    }

    public static SupplyGroupCode getNewSupplyGroupCode(Map<String, String> params) throws Exception {
        return new SupplyGroupCode(params.get("new_supply_group_code"));
    }

    public static TariffIndex getNewTariffIndex(Map<String, String> params) throws Exception {
        return new TariffIndex(params.get("new_tariff_index"));
    }

    public static boolean getAllow3Kct(Map<String, String> params) {
        if (params.containsKey("allow_3kct") && params.get("allow_3kct").toLowerCase().equals("true"))
            return true;
        return false;
    }

    public static KeyRevisionNumber getNewKeyRevisionNo(Map<String, String> params) throws Exception {
        return new KeyRevisionNumber(Integer.parseInt(params.get("new_key_revision_no")));
    }

    public static VendingUniqueDESKey getNewVendingUniqueDESKey(Map<String, String> params) throws Exception {
        return new VendingUniqueDESKey(DatatypeConverter.parseHexBinary(params.get("new_vending_key")));
    }

    public static MeterPrimaryAccountNumber getNewMeterPrimaryAccountNo(Map<String, String> params) throws Exception  {
        String meterPAN = String.format("%s%s",
                params.get("new_issuer_identification_no"),
                params.get("new_decoder_reference_number"));
        meterPAN += LuhnAlgorithm.generateCheckDigit(Long.parseLong(meterPAN));
        return new MeterPrimaryAccountNumber(meterPAN, MeterPrimaryAccountNumber.Validate.NO_METER_PAN_VALIDATION);
    }
}
