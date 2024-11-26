package co.nxtgrid.token.generators.tokensdecoder.class2;

import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.token.class2.SetWaterMeterFactorToken;
import co.nxtgrid.token.generators.tokensdecoder.TokenDecoder;

public class SetWaterMeterFactorTokenDecoder extends TokenDecoder {

    @Override
    public SetWaterMeterFactorToken decode(String requestID,
                                           BitString _64bitStringDecryptedDataBlock,
                                           BitString _64bitStringEncryptedDataBlock) throws Exception {
        SetWaterMeterFactorToken token = new SetWaterMeterFactorToken(requestID);
        token.decode(_64bitStringDecryptedDataBlock, _64bitStringEncryptedDataBlock);
        return token;
    }
}
