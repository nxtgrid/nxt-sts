package co.nxtgrid.token.generators.tokensdecoder.class2;

import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.token.class2.Set4thSectionDecoderKeyToken;
import co.nxtgrid.token.generators.tokensdecoder.TokenDecoder;

public class Set4thSectionDecoderKeyTokenDecoder extends TokenDecoder {

    @Override
    public Set4thSectionDecoderKeyToken decode(String requestID,
                                               BitString _64bitStringDecryptedDataBlock,
                                               BitString _64bitStringEncryptedDataBlock) throws Exception {
        Set4thSectionDecoderKeyToken token = new Set4thSectionDecoderKeyToken(requestID);
        token.decode(_64bitStringDecryptedDataBlock, _64bitStringEncryptedDataBlock);
        return token;
    }
}
