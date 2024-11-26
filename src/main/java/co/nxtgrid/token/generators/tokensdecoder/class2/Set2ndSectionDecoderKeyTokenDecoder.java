package co.nxtgrid.token.generators.tokensdecoder.class2;

import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.token.class2.Set2ndSectionDecoderKeyToken;
import co.nxtgrid.token.generators.tokensdecoder.TokenDecoder;

public class Set2ndSectionDecoderKeyTokenDecoder extends TokenDecoder {

    @Override
    public Set2ndSectionDecoderKeyToken decode(String requestID,
                                               BitString _64bitStringDecryptedDataBlock,
                                               BitString _64bitStringEncryptedDataBlock) throws Exception {
        Set2ndSectionDecoderKeyToken token = new Set2ndSectionDecoderKeyToken(requestID);
        token.decode(_64bitStringDecryptedDataBlock, _64bitStringEncryptedDataBlock);
        return token;
    }
}
