package co.nxtgrid.token.generators.tokensdecoder.class0;

import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.token.class0.TransferGasCreditToken;
import co.nxtgrid.token.generators.tokensdecoder.TokenDecoder;

public class TransferGasCreditDecoder extends TokenDecoder {

    public TransferGasCreditToken decode(String requestID,
                                         BitString _64bitStringDecryptedDataBlock,
                                         BitString _64bitStringEncryptedDataBlock) throws Exception {
        TransferGasCreditToken token = new TransferGasCreditToken(requestID);
        token.decode(_64bitStringDecryptedDataBlock, _64bitStringEncryptedDataBlock);
        return token;
    }
}
