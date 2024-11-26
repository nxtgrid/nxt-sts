package co.nxtgrid.token.generators.tokensdecoder.class2;

import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.token.class2.Set3rdSectionDecoderKeyToken;
import co.nxtgrid.token.generators.tokensdecoder.TokenDecoder;

public class Set3rdSectionDecoderKeyTokenDecoder extends TokenDecoder {

    @Override
    public Set3rdSectionDecoderKeyToken decode(String requestID,
                                               BitString _64bitStringDecryptedDataBlock,
                                               BitString _64bitStringEncryptedDataBlock) throws Exception {
        Set3rdSectionDecoderKeyToken token = new Set3rdSectionDecoderKeyToken(requestID);
        token.decode(_64bitStringDecryptedDataBlock, _64bitStringEncryptedDataBlock);
        return token;
    }
}
