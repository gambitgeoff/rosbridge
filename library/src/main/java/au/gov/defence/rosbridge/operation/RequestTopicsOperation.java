package au.gov.defence.rosbridge.operation;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestTopicsOperation extends Operation {


    public RequestTopicsOperation() {
        super();
    }

    @Override
    public Operation parseMessage(JSONObject inMessage) {
        return null;
    }

    @Override
    public JSONObject getJSON() {
        JSONObject returnValue = new JSONObject();
        try {
            returnValue.put("op", "call_service");
            returnValue.put("service", "/rosapi/topics");
            returnValue.put("id", getID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnValue;
    }
}
