package co.nxtgrid.token.domain.token.class0;

import co.nxtgrid.token.domain.Amount;
import co.nxtgrid.token.domain.Crc;
import co.nxtgrid.token.domain.RandomNo;
import co.nxtgrid.token.domain.TokenIdentifier;
import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.tokenclass.class0.WaterCreditTransferTokenClass;
import co.nxtgrid.token.domain.tokensubclass.class0.WaterCreditTransferTokenSubClass;
import co.nxtgrid.token.exceptions.InvalidRangeException;
import co.nxtgrid.token.exceptions.InvalidTokenException;
import co.nxtgrid.token.miscellaneous.Strings;

import java.util.Date;
import java.util.Optional;

public class TransferWaterCreditToken extends Class0Token  {

    public TransferWaterCreditToken(String requestID)
            throws InvalidRangeException {
        super(requestID);
        setTokenClass(new WaterCreditTransferTokenClass());
        setTokenSubClass(new WaterCreditTransferTokenSubClass());
    }

    public TransferWaterCreditToken(String requestID,
                                    TokenIdentifier tokenIdentifier,
                                    Optional<RandomNo> randomValue,
                                    Amount amountPurchased) throws InvalidRangeException {
        super(requestID,
                new WaterCreditTransferTokenClass(),
                new WaterCreditTransferTokenSubClass(),
                randomValue,
                tokenIdentifier,
                amountPurchased ) ;
    }

    @Override
    public String getType() {
        return "Water_01";
    }

    public String getBitString() {
        return encryptedTokenBitString;
    }

    public void decode(BitString decryptedTokenBitString,
                       BitString encryptedTokenBitString) throws Exception {
        if (checkCrc(decryptedTokenBitString, getTokenClass())) {
            setRND(extractRandomNo(decryptedTokenBitString));
            setTokenIdentifier(extractTokenIdentifier(decryptedTokenBitString));
            setAmountPurchased(extractAmount(decryptedTokenBitString));
            setCrc(Optional.of(extractCrc(decryptedTokenBitString)));
            setEncryptedTokenBitString(Long.toBinaryString(encryptedTokenBitString.getValue()));
            setDecryptedTokenBitString(Long.toBinaryString(decryptedTokenBitString.getValue()));
        } else
            throw new InvalidTokenException(Strings.INVALID_TOKEN);
    }
}
