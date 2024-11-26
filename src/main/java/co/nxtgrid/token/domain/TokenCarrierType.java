package co.nxtgrid.token.domain;

public class TokenCarrierType implements Entity {

    public enum Code {
        RESERVED("00"),
        MAGNETIC_CARD("01"),
        NUMERIC("02"),
        VIRTUAL_TOKEN_CARRIER("07");

        // 03-06 Reserved: Legacy systems using proprietary co.nxtgridoco.nxtgriden carrier technologies
        // 08-99 Reserved: For future assignment
        // Values less than 10 shall be right justified and left padded with 0 (for example 01, 02-09)

        private String code;

        Code(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

    private Code tokenCarrierTypeCode;
    private final String NAME = "TokenCarrierType";

    public TokenCarrierType(Code tokenCarrierTypeCode) {
        setValue(tokenCarrierTypeCode);
    }

    public Code getValue() {
        return tokenCarrierTypeCode;
    }

    public void setValue(Code tokenCarrierTypeCode) {
        this.tokenCarrierTypeCode = tokenCarrierTypeCode;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
