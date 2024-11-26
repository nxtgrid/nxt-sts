package co.nxtgrid.tokens.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {

    private Status status;
    private Map<String,Object> data;

    public ApiResponse(){}

    public ApiResponse(int code, String message, String requestID,
                       Map<String, Object> data) {
        setStatus(createStatus(code, message, requestID));
        setData(data);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    private Status createStatus(int code, String message, String requestID) {
        return new Status(code, message, requestID);
    }

    static public class Status {
        private int code;
        private String message;

        @JsonProperty("request_id")
        private String requestID;

        Status(int code, String message, String requestID) {
            setCode(code);
            setMessage(message);
            setRequestID(requestID);
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getRequestID() {
            return requestID;
        }

        public void setRequestID(String requestID) {
            this.requestID = requestID;
        }
    }
}
