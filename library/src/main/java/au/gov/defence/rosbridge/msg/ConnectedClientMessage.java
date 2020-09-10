package au.gov.defence.rosbridge.msg;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * a client connected to ROSBridge
 * https://github.com/RobotWebTools/rosbridge_suite/blob/develop/rosbridge_msgs/msg/ConnectedClient.msg
 *
 * message looks like
 * ip_address: ""
 * connection_time:
 *  secs:
 *  nsecs:
 */

public class ConnectedClientMessage extends Message {

    private static final String TAG = "au.gov.defence.dsa.ros.msg.ConnectedClient";

    private String ip_address;
    private JSONObject connection_time; // secs, nsecs, ect
    // unsure if connection_time can have more values in it (eg, minutes, hours), so leaving as JSONObject

    public String getIp_address(){ return this.ip_address; }
    public JSONObject getConnection_time(){ return this.connection_time; }

    public void setIp_address(String ip_address){ this.ip_address = ip_address; }
    public void setConnection_time( JSONObject connection_time){ this.connection_time = connection_time; }

    @Override
    public JSONObject getJSON() {
        JSONObject body = new JSONObject();
        try {
            body.put("ip_address", this.ip_address);
            body.put("connection_time", this.connection_time);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.println(Log.ERROR, TAG, e.getMessage());
        }
        return body;
    }

    @Override
    public Message updateMessage(JSONObject inJSONObject) {
        try {
            if (inJSONObject.has("msg")){
                inJSONObject = inJSONObject.getJSONObject("msg");
            }
            this.ip_address = inJSONObject.getString("ip_address");
            this.connection_time = inJSONObject.getJSONObject("connection_time");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.println(Log.ERROR, TAG, e.getMessage());
        }
        return this;
    }
}
