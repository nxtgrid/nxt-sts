package co.nxtgrid.token.generators.tokensdecoder.class2;

import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.token.Token;
import co.nxtgrid.token.domain.token.class2.ClearCreditToken;
import co.nxtgrid.token.generators.tokensdecoder.TokenDecoder;

public class ClearCreditTokenDecoder extends TokenDecoder {

    @Override
    public Token decode(String requestID,
                        BitString _64bitStringDecryptedDataBlock,
                        BitString _64bitStringEncryptedDataBlock) throws Exception {
        ClearCreditToken token = new ClearCreditToken(requestID);
        token.decode(_64bitStringDecryptedDataBlock, _64bitStringEncryptedDataBlock);
        return token;
    }
}
