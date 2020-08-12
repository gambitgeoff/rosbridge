package au.gov.defence.rosbridge.msg;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

/**
 * to process the 'diagnostic_msgs/DiagnosticStatus Message' type.
 * Ros documentation: http://docs.ros.org/melodic/api/diagnostic_msgs/html/msg/DiagnosticStatus.html
 * Many variable descriptions copied from ROS documentation
 */

public class DiagnosticStatusMessage extends Message {

    private static final String TAG = "au.gov.defence.dsa.ros.msg.DiagnosticStatusMessage";

    private int ok, warn, error, stale; // possible levels of operation

    private int level;  // level of operation, compare with above enumerations
    String name;  // description of test/component reporting
    String message;  // a description of the status
    String hardware_id;  // hardware unique string
    LinkedList<KeyValueMessage> values;  // array of values associated with status

    public int getOkEnum() { return ok; }
    public int getWarnEnum() { return warn; }
    public int getErrorEnum() { return error; }
    public int getStaleEnum() { return stale; }

    public int getLevel() { return level; }

    public String getName() { return name; }

    public String getMessage() { return message; }

    public String getHardware_id() { return hardware_id; }

    public LinkedList<KeyValueMessage> getKeyValueMessages(){ return values; }

    @Override
    public JSONObject getJSON() {
        JSONObject toReturn = new JSONObject();
        try {
            toReturn.put("OK", this.ok);
            toReturn.put("WARN", this.warn);
            toReturn.put("ERROR", this.error);
            toReturn.put("STALE", this.stale);

            toReturn.put("level", this.level);
            toReturn.put("name", this.name);
            toReturn.put("message", this.message);
            toReturn.put("hardware_id", this.hardware_id);
            JSONArray values = new JSONArray();
            for (KeyValueMessage value: this.values){
                values.put(value.getJSON());
            }
            toReturn.put("values", values);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.println(Log.ERROR, TAG, e.getMessage());
        }
        return null;
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
            // constants -- possible levels of operations
            this.ok = inJSONObject.getInt("OK");
            this.warn = inJSONObject.getInt("WARN");
            this.error = inJSONObject.getInt("ERROR");
            this.stale = inJSONObject.getInt("STALE");

            // compare this.level with the constants above
            this.level = inJSONObject.getInt("level");
            this.name = inJSONObject.getString("name");
            this.message = inJSONObject.getString("message");
            this.hardware_id = inJSONObject.getString("hardware_id");
            JSONArray array = inJSONObject.getJSONArray("values");
            this.values = new LinkedList<KeyValueMessage>();

            for (int i = 0; i < array.length(); i++){
                // convert each entry to KeyValueMessage and add to list.
                JSONObject value = array.getJSONObject(i);
                KeyValueMessage keyValue = new KeyValueMessage();
                keyValue.updateWithProcessedMessage(value);
                this.values.add(keyValue);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.println(Log.ERROR, TAG, e.getMessage());
        }
        return this;
    }
}
