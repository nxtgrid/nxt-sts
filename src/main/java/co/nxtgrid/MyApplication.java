package co.nxtgrid;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
import co.nxtgrid.token.domain.token.class0.TransferElectricityCreditToken;
import co.nxtgrid.token.generators.tokensgenerator.nativetoken.class0.TransferElectricityCreditTokenGenerator;
import co.nxtgrid.token.generators.tokensgenerator.nativetoken.class2.ClearCreditTokenGenerator;
import co.nxtgrid.token.generators.tokensgenerator.nativetoken.class2.ClearTamperConditionTokenGenerator;
import co.nxtgrid.token.generators.tokensgenerator.nativetoken.class2.SetMaximumPowerLimitTokenGenerator;

@RestController
@SpringBootApplication
public class MyApplication {
    
    @RequestMapping("/token")
    Map<String, Object> home(
        @RequestBody() RequestData body
    ) {
        try {
            TokenIdentifier tokenIdentifier = new TokenIdentifier(body.getIssueDate(), BaseDate._2014);

            // initialize the random value
            BitString randomValueBitString = new BitString((long) body.getRandomNumber());
            randomValueBitString.setLength(4);
            RandomNo randomNo = new RandomNo(randomValueBitString);

            // initialize the amount
            Amount amountPurchased = new Amount(body.getAmount());

            // Initialize the encryption algorithm keys
            byte[] reversedDecoderKey = convertHexStringToReversedByteArray(body.getDecoderKey());
            DecoderKey decoderKey = new DecoderKey();
            decoderKey.setKeyData(reversedDecoderKey);
            // Set the Key Expiry number
            KeyExpiryNumber keyExpiryNumber = new KeyExpiryNumber(255);

            String requestID = "asda";
            // initialize the transfer credit token generator instance
            StandardTransferAlgorithmEncryptionAlgorithm staEncryptionAlgorithm = new StandardTransferAlgorithmEncryptionAlgorithm();
            
            Token generatedToken;
            String tokenType = body.getType(); 
            if(tokenType.equals("TOPUP")) {
                TransferElectricityCreditTokenGenerator tokenGenerator = new TransferElectricityCreditTokenGenerator(requestID, tokenIdentifier, randomNo, amountPurchased, keyExpiryNumber,
                    decoderKey, staEncryptionAlgorithm );
                generatedToken = tokenGenerator.generate();

            } else if (tokenType.equals("CLEAR_CREDIT")) {
                BitString bitstring = new BitString();
                bitstring.setValue("0000000000000000");
                bitstring.setLength(16);
                Register register = new Register(bitstring);
                ClearCreditTokenGenerator tokenGenerator = new ClearCreditTokenGenerator(requestID, randomNo, tokenIdentifier,
                    register, decoderKey, staEncryptionAlgorithm );
                generatedToken = tokenGenerator.generate();
            } else if (tokenType.equals("CLEAR_TAMPER")) {
                BitString bitstring = new BitString();
                bitstring.setValue("0000000000000000");
                bitstring.setLength(16);
                Pad pad = new Pad(bitstring); 
                ClearTamperConditionTokenGenerator tokenGenerator = new ClearTamperConditionTokenGenerator(requestID, randomNo, tokenIdentifier, pad, decoderKey, staEncryptionAlgorithm);
                generatedToken = tokenGenerator.generate();
            } else if (tokenType.equals("SET_POWER_LIMIT")) {
                MaximumPowerLimit powerLimit = new MaximumPowerLimit(body.getPowerLimit());
                SetMaximumPowerLimitTokenGenerator tokenGenerator = new SetMaximumPowerLimitTokenGenerator(requestID, randomNo, tokenIdentifier, powerLimit, decoderKey, staEncryptionAlgorithm);
                generatedToken = tokenGenerator.generate();
            } else {
                return null;
            }
                
            
            // String token = generatedToken.getTokenNo();
            // return Map.of(
            //     "token", generatedToken.getTokenNo()

            Map<String, Object> response = new HashMap<>();
            response.put("token", generatedToken.getTokenNo());
                
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }

    public static byte[] convertHexStringToReversedByteArray(String hexString) {
        // Validate input
        if (hexString == null || hexString.length() % 2 != 0) {
            throw new IllegalArgumentException("Invalid hexadecimal string.");
        }

        int length = hexString.length();
        byte[] byteArray = new byte[length / 2];

        // Process in reverse order
        for (int i = 0; i < byteArray.length; i++) {
            int startIndex = length - 2 * (i + 1);
            String hexPair = hexString.substring(startIndex, startIndex + 2);
            byteArray[i] = (byte) Integer.parseInt(hexPair, 16);
        }

        return byteArray;
    }

    //-- UTILS
    // private static DecoderKey createDecoderKey(byte[]) {
    //     DecoderKey decoderKey = new DecoderKey();

    //     // keys is as specified in the standard
    //     // byte[] keyBytes = { -119, 103, 69, -13, -34, 18, -68, 10 };
    //     // byte[] keyBytes = {
    //     //         (byte) 0x89,
    //     //         (byte) 0x67,         
    //     //         (byte) 0x45,
    //     //         (byte) 0xF3,
    //     //         (byte) 0xDE,
    //     //         (byte) 0x12,
    //     //         (byte) 0xBC, 
    //     //         (byte) 0x0A
    //     // };

    //     // meter 47003341816
    //     // byte[] keyBytes = { 97, -7, -81, 98, -15, 20, -18, -102 };
    //     byte[] keyBytes = {
    //             (byte) 0x9A,
    //             (byte) 0xEE,
    //             (byte) 0x14,
    //             (byte) 0xF1,
    //             (byte) 0x62,
    //             (byte) 0xAF,
    //             (byte) 0xF9,                                            
    //             (byte) 0x61
    //     };
        
    //     decoderKey.setKeyData(keyBytes);
    //     return decoderKey;
    // }
}