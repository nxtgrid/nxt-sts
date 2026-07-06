package co.nxtgrid.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.nxtgrid.token.domain.Amount;
import co.nxtgrid.token.domain.BaseDate;
import co.nxtgrid.token.domain.KeyExpiryNumber;
import co.nxtgrid.token.domain.MaximumPowerLimit;
import co.nxtgrid.token.domain.Pad;
import co.nxtgrid.token.domain.RandomNo;
import co.nxtgrid.token.domain.Register;
import co.nxtgrid.token.domain.TokenIdentifier;
import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.encryptionalgorithm.StandardTransferAlgorithmEncryptionAlgorithm;
import co.nxtgrid.token.domain.keys.decoder.DecoderKey;
import co.nxtgrid.token.domain.token.Token;
import co.nxtgrid.token.generators.tokensgenerator.nativetoken.class0.TransferElectricityCreditTokenGenerator;
import co.nxtgrid.token.generators.tokensgenerator.nativetoken.class2.ClearCreditTokenGenerator;
import co.nxtgrid.token.generators.tokensgenerator.nativetoken.class2.ClearTamperConditionTokenGenerator;
import co.nxtgrid.token.generators.tokensgenerator.nativetoken.class2.SetMaximumPowerLimitTokenGenerator;

@RestController
@RequestMapping("/token")
public class TokenController {

    @PostMapping
    TokenResponse generateToken(@RequestBody TokenRequest body) {
        try {
            TokenIdentifier tokenIdentifier = new TokenIdentifier(body.getIssueDate(), BaseDate._2014);

            BitString randomValueBitString = new BitString((long) body.getRandomNumber());
            randomValueBitString.setLength(4);
            RandomNo randomNo = new RandomNo(randomValueBitString);

            Amount amountPurchased = new Amount(body.getKwh());

            byte[] reversedDecoderKey = StsUtils.convertHexStringToReversedByteArray(body.getDecoderKey());
            DecoderKey decoderKey = new DecoderKey();
            decoderKey.setKeyData(reversedDecoderKey);

            KeyExpiryNumber keyExpiryNumber = new KeyExpiryNumber(255);
            String requestID = "asda";
            StandardTransferAlgorithmEncryptionAlgorithm staEncryptionAlgorithm =
                new StandardTransferAlgorithmEncryptionAlgorithm();

            Token generatedToken;
            String tokenType = body.getType();
            if (tokenType.equals("TOP_UP")) {
                TransferElectricityCreditTokenGenerator tokenGenerator =
                    new TransferElectricityCreditTokenGenerator(
                        requestID,
                        tokenIdentifier,
                        randomNo,
                        amountPurchased,
                        keyExpiryNumber,
                        decoderKey,
                        staEncryptionAlgorithm
                    );
                generatedToken = tokenGenerator.generate();
            } else if (tokenType.equals("CLEAR_CREDIT")) {
                BitString bitstring = new BitString();
                bitstring.setValue("0000000000000000");
                bitstring.setLength(16);
                Register register = new Register(bitstring);
                ClearCreditTokenGenerator tokenGenerator = new ClearCreditTokenGenerator(
                    requestID,
                    randomNo,
                    tokenIdentifier,
                    register,
                    decoderKey,
                    staEncryptionAlgorithm
                );
                generatedToken = tokenGenerator.generate();
            } else if (tokenType.equals("CLEAR_TAMPER")) {
                BitString bitstring = new BitString();
                bitstring.setValue("0000000000000000");
                bitstring.setLength(16);
                Pad pad = new Pad(bitstring);
                ClearTamperConditionTokenGenerator tokenGenerator =
                    new ClearTamperConditionTokenGenerator(
                        requestID,
                        randomNo,
                        tokenIdentifier,
                        pad,
                        decoderKey,
                        staEncryptionAlgorithm
                    );
                generatedToken = tokenGenerator.generate();
            } else if (tokenType.equals("SET_POWER_LIMIT")) {
                MaximumPowerLimit powerLimit = new MaximumPowerLimit(body.getPowerLimit());
                SetMaximumPowerLimitTokenGenerator tokenGenerator =
                    new SetMaximumPowerLimitTokenGenerator(
                        requestID,
                        randomNo,
                        tokenIdentifier,
                        powerLimit,
                        decoderKey,
                        staEncryptionAlgorithm
                    );
                generatedToken = tokenGenerator.generate();
            } else {
                return null;
            }

            return new TokenResponse(generatedToken.getTokenNo());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
