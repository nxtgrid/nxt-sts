package co.nxtgrid;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import co.nxtgrid.token.domain.Amount;
import co.nxtgrid.token.domain.BaseDate;
import co.nxtgrid.token.domain.KeyExpiryNumber;
import co.nxtgrid.token.domain.RandomNo;
import co.nxtgrid.token.domain.TokenIdentifier;
import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.encryptionalgorithm.StandardTransferAlgorithmEncryptionAlgorithm;
import co.nxtgrid.token.domain.keys.decoder.DecoderKey;
import co.nxtgrid.token.domain.token.class0.TransferElectricityCreditToken;
import co.nxtgrid.token.generators.tokensgenerator.nativetoken.class0.TransferElectricityCreditTokenGenerator;

public class Main {
    public static void main(String[] args) {
        try {
            String requestID = "request_id";

            // initialize the TransferElectricityCreditToken Identifier
            // String dateTime = "25/03/1996 13:55:22";
            String dateTime = "25/11/2024 14:55:22";
            DateTime dateOfIssue = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss").parseDateTime(dateTime);
            TokenIdentifier tokenIdentifier = new TokenIdentifier(dateOfIssue, BaseDate._2014);

            // initialize the random value
            BitString randomValueBitString = new BitString(0xbl);
            randomValueBitString.setLength(4);
            RandomNo randomNo = new RandomNo(randomValueBitString);

            // initialize the amount
            double unitsPurchased = 25.8;
            Amount amountPurchased = new Amount(unitsPurchased);

            // Initialize the encryption algorithm keys
            DecoderKey decoderKey = createDecoderKey();
            // Set the Key Expiry number
            KeyExpiryNumber keyExpiryNumber = new KeyExpiryNumber(255);

            // initialize the transfer credit token generator instance
            StandardTransferAlgorithmEncryptionAlgorithm staEncryptionAlgorithm = new StandardTransferAlgorithmEncryptionAlgorithm();
            TransferElectricityCreditTokenGenerator tokenGenerator = new TransferElectricityCreditTokenGenerator(requestID, tokenIdentifier, randomNo, amountPurchased, keyExpiryNumber,
                                                                    decoderKey, staEncryptionAlgorithm );
            TransferElectricityCreditToken generatedToken = tokenGenerator.generate();

            String token = generatedToken.getTokenNo();
            System.out.println(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //-- UTILS
    private static DecoderKey createDecoderKey() {
        DecoderKey decoderKey = new DecoderKey();

        // keys is as specified in the standard
        // byte[] keyBytes = { -119, 103, 69, -13, -34, 18, -68, 10 };
        // byte[] keyBytes = {
        //         (byte) 0x89,
        //         (byte) 0x67,         
        //         (byte) 0x45,
        //         (byte) 0xF3,
        //         (byte) 0xDE,
        //         (byte) 0x12,
        //         (byte) 0xBC, 
        //         (byte) 0x0A
        // };

        // meter 47003341816
        // byte[] keyBytes = { 97, -7, -81, 98, -15, 20, -18, -102 };
        byte[] keyBytes = {
                (byte) 0x9A,
                (byte) 0xEE,
                (byte) 0x14,
                (byte) 0xF1,
                (byte) 0x62,
                (byte) 0xAF,
                (byte) 0xF9,                                            
                (byte) 0x61
        };
        
        decoderKey.setKeyData(keyBytes);
        return decoderKey;
    }
}