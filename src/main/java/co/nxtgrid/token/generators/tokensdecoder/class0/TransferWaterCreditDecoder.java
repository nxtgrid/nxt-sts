package co.nxtgrid.token.generators.tokensdecoder.class0;

import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.token.class0.TransferWaterCreditToken;
import co.nxtgrid.token.generators.tokensdecoder.TokenDecoder;

public class TransferWaterCreditDecoder extends TokenDecoder {

    public TransferWaterCreditToken decode(String requestID,
                                           BitString _64bitStringDecryptedDataBlock,
                                           BitString _64bitStringEncryptedDataBlock) throws Exception {
        TransferWaterCreditToken token = new TransferWaterCreditToken(requestID);
        token.decode(_64bitStringDecryptedDataBlock, _64bitStringDecryptedDataBlock);
        return token;
    }
}
