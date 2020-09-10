package au.gov.defence.rosbridge.msg;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A standard ROS INT32 message.
 * ROS Documentation: http://docs.ros.org/jade/api/std_msgs/html/msg/Int32.html
 */

public class Int32Message extends Message{
    private static final String TAG = "au.gov.defence.dsa.ros.msg.Int32Message";
    int data;

    public int getData() {
        return data;
    }

    @Override
    public JSONObject getJSON() {
        JSONObject toReturn = new JSONObject();
        try {
            toReturn.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.println(Log.ERROR, TAG, e.getMessage());
        }
        return toReturn;
    }

    @Override
    public Message updateMessage(JSONObject inJSONObject) {
        try {
            JSONObject message = inJSONObject.getJSONObject("msg");
            this.data = message.getInt("data");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.println(Log.ERROR, TAG, e.getMessage());
        }
        return this;
    }
}
