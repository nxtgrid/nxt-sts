package co.nxtgrid.token.generators.tokensgenerator.nativetoken.class1;

import co.nxtgrid.token.domain.Control;
import co.nxtgrid.token.domain.ManufacturerCode;
import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.token.class1.InitiateMeterTestOrDisplay2Token;
import co.nxtgrid.token.domain.tokenclass.class1.InitiateMeterTestDisplayTokenClass;
import co.nxtgrid.token.domain.tokensubclass.class1.InitiateMeterTestDisplay2TokenSubClass;
import co.nxtgrid.token.exceptions.InvalidControlException;
import co.nxtgrid.token.exceptions.InvalidManufacturerCodeException;
import co.nxtgrid.token.exceptions.InvalidRangeException;
import co.nxtgrid.token.miscellaneous.Strings;

public class InitiateMeterTestOrDisplay2TokenGenerator extends Class1TokenGenerator {

    private InitiateMeterTestDisplay2TokenSubClass tokenSubClass;

    public InitiateMeterTestOrDisplay2TokenGenerator(String requestID,
                                                     Control control,
                                                     ManufacturerCode manufacturerCode)
            throws InvalidRangeException, InvalidControlException, InvalidManufacturerCodeException {
        super(requestID, control, manufacturerCode);
        if (manufacturerCode.getBitString().getLength() != 16) {
            if (control.getBitString().getLength() != 28) {
                throw new InvalidControlException(Strings.INVALID_CONTROL);
            }
            throw new InvalidManufacturerCodeException(Strings.INVALID_MANUFACTURER_CODE);
        }
        tokenSubClass = new InitiateMeterTestDisplay2TokenSubClass();
    }

    public InitiateMeterTestOrDisplay2Token generate() throws Exception {
        InitiateMeterTestDisplayTokenClass tokenClass = new InitiateMeterTestDisplayTokenClass();
        InitiateMeterTestOrDisplay2Token token = new InitiateMeterTestOrDisplay2Token(requestID,
                                                                                        tokenClass,
                                                                                        tokenSubClass,
                                                                                        control,
                                                                                        manufacturerCode);
        BitString _64BitDataBlock = generate64BitDataBlock(token);
        String transposed66BitString = transpose66BitString(tokenClass, _64BitDataBlock);
        token.setEncryptedTokenBitString(transposed66BitString);

        return token;
    }
}
