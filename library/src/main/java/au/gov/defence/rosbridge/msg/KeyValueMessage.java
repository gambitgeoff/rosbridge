package au.gov.defence.rosbridge.msg;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * to process the diagnostic_msgs/KeyValue Message type.
 * ROS documentation: http://docs.ros.org/melodic/api/diagnostic_msgs/html/msg/KeyValue.html
 */

public class KeyValueMessage extends Message {

    private static final String TAG = "au.gov.defence.dsa.ros.msg.KeyValueMessage";

    String key;
    String value;

    public String getKey() { return key; }

    public String getValue() { return value; }

    @Override
    public JSONObject getJSON() {
        JSONObject toReturn = new JSONObject();
        try {
            toReturn.put("key", this.key);
            toReturn.put("value", this.value);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.println(Log.ERROR, TAG, e.getMessage());
        }
        return toReturn;
    }

    @Override
    public Message updateMessage(JSONObject inJSONObject) {
        // see if data is contained in 'msg' JSONObject
        // else assume we have been passed the 'msg' object.
        try {
            inJSONObject = inJSONObject.getJSONObject("msg");
            return updateWithProcessedMessage(inJSONObject);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.println(Log.ERROR, TAG, e.getMessage());
        }
        return this;
    }

    public Message updateWithProcessedMessage(JSONObject inJSONObject){
        // when being passed raw JSONObject, not contained in 'msg'
        // often used when messages are embedded
        try {
            this.key = inJSONObject.getString("key");
            this.value = inJSONObject.getString("value");
            return this;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.println(Log.ERROR, TAG, e.getMessage());
        }
        return this;
    }
}
