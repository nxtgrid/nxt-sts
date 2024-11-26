package co.nxtgrid.tokens.utils.request;

import org.json.JSONObject;

import java.util.Map;

public class Payload {

    private Map<String, Object> params;

    public Payload(Map<String, Object> params) {
        setParams(params);
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        params.forEach((key,value) -> obj.put(key, value));
        return obj;
    }
}

