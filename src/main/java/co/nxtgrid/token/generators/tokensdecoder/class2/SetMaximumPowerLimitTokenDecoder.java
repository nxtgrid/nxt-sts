package co.nxtgrid.token.generators.tokensdecoder.class2;

import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.token.class2.SetMaximumPowerLimitToken;
import co.nxtgrid.token.generators.tokensdecoder.TokenDecoder;

public class SetMaximumPowerLimitTokenDecoder extends TokenDecoder {

    @Override
    public SetMaximumPowerLimitToken decode(String requestID,
                                            BitString _64bitStringDecryptedDataBlock,
                                            BitString _64bitStringEncryptedDataBlock) throws Exception {
        SetMaximumPowerLimitToken token = new SetMaximumPowerLimitToken(requestID);
        token.decode(_64bitStringDecryptedDataBlock, _64bitStringEncryptedDataBlock);
        return token;
    }
}
