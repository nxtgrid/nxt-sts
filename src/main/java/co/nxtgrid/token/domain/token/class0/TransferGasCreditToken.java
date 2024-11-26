package co.nxtgrid.token.domain.token.class0;

import co.nxtgrid.token.domain.Amount;
import co.nxtgrid.token.domain.Crc;
import co.nxtgrid.token.domain.RandomNo;
import co.nxtgrid.token.domain.TokenIdentifier;
import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.tokenclass.class0.GasCreditTransferTokenClass;
import co.nxtgrid.token.domain.tokensubclass.class0.GasCreditTransferTokenSubClass;
import co.nxtgrid.token.exceptions.InvalidRangeException;
import co.nxtgrid.token.exceptions.InvalidTokenException;
import co.nxtgrid.token.miscellaneous.Strings;

import java.util.Date;
import java.util.Optional;

public class TransferGasCreditToken extends Class0Token {

    public TransferGasCreditToken(String requestID)
            throws InvalidRangeException {
        super(requestID);
        setTokenClass(new GasCreditTransferTokenClass());
        setTokenSubClass(new GasCreditTransferTokenSubClass());
    }

    public TransferGasCreditToken(String requestID,
                                  TokenIdentifier tokenIdentifier,
                                  Optional<RandomNo> randomValue,
                                  Amount amountPurchased) throws InvalidRangeException {
        super(requestID,
                new GasCreditTransferTokenClass(),
                new GasCreditTransferTokenSubClass(),
                randomValue,
                tokenIdentifier,
                amountPurchased) ;
    }

    @Override
    public String getType() {
        return "Gas_02";
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
