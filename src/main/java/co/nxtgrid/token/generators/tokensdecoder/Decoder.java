package co.nxtgrid.token.generators.tokensdecoder;

import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.token.Token;

public interface Decoder {
    Token decode(String requestID,
                 BitString _64bitStringDecryptedDataBlock,
                 BitString _64bitStringEncryptedDataBlock)
            throws Exception;
}
