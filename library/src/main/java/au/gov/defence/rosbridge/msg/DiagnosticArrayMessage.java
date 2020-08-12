package au.gov.defence.rosbridge.msg;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

/**
 * to process the diagnostic_msgs/DiagnosticArray Message type.
 * ROS documentation: http://docs.ros.org/melodic/api/diagnostic_msgs/html/msg/DiagnosticArray.html
 * "used to send diagnostic information about the state of the robot"
 */

public class DiagnosticArrayMessage extends Message {
    private static final String TAG = "au.gov.defence.dsa.ros.msg.DiagnosticArrayMessage";

    Header header;  // for timestamp
    LinkedList<DiagnosticStatusMessage> status;  // an array of components being reported on

    @Override
    public JSONObject getJSON() {
        JSONObject body = new JSONObject();
        JSONArray status = new JSONArray();

        for (DiagnosticStatusMessage m : this.status){
            status.put(m.getJSON());
        }
        try {
            body.put("header", this.header.getJSON());
            body.put("status", status);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        return body;
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
        this.header = new Header();
        this.status = new LinkedList<>();
        try {

            this.header.updateMessage(inJSONObject.getJSONObject("header"));

            // loop through array, convert each object to DiagnosticMessage, add to list.
            JSONArray array = inJSONObject.getJSONArray("status");
            for (int i = 0; i < array.length(); i++){

                DiagnosticStatusMessage message = new DiagnosticStatusMessage();
                message.updateWithProcessedMessage(array.getJSONObject(i));
                status.add(message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }
}
