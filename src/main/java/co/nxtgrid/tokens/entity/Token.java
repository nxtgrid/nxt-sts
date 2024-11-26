package co.nxtgrid.tokens.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

// import jakarta.persistence.*;
import java.time.Instant;

// @Entity
// @Table(name = "tokens")
public class Token {

    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private String ref;

    // @Column(name = "token_no")
    @JsonProperty("token_no")
    private String tokenNo;

    // @Column(name = "user_ref")
    @JsonProperty("user_ref")
    private String userRef;

    // @Column(name = "token_type")
    @JsonProperty("token_type")
    private String tokenType;

    // @Column(name = "meter_no")
    @JsonProperty("meter_no")
    private String meterNo;

    // @Column(name = "request_id")
    @JsonProperty("request_id")
    private String requestID;

    // @Column(name = "created_at")
    @JsonProperty("created_at")
    private Instant createdAt;

    public Token() {}

    public Token(String ref, String tokenNo, String userRef,
                 String tokenType, String meterNo, String requestID,
                 Instant createdAt) {
        setRef(ref);
        setTokenNo(tokenNo);
        setUserRef(userRef);
        setTokenType(tokenType);
        setMeterNo(meterNo);
        setRequestID(requestID);
        setCreatedAt(createdAt);
    }

    @Override
    public String toString() {
        return String.format("ref: %s", ref);
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getTokenNo() {
        return tokenNo;
    }

    public void setTokenNo(String tokenNo) {
        this.tokenNo = tokenNo;
    }

    public String getUserRef() {
        return userRef;
    }

    public void setUserRef(String userRef) {
        this.userRef = userRef;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getMeterNo() {
        return meterNo;
    }

    public void setMeterNo(String meterNo) {
        this.meterNo = meterNo;
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

}
