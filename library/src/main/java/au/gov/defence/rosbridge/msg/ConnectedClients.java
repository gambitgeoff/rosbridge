package au.gov.defence.rosbridge.msg;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

/**
 * An array of clients connected to ROSBridge.
 * https://github.com/RobotWebTools/rosbridge_suite/blob/develop/rosbridge_msgs/msg/ConnectedClients.msg
 *
 * Message looks like:
 * clients: []
 */

public class ConnectedClients extends Message{

    private static final String TAG = "au.gov.defence.dsa.ros.msg.ConnectedClients";

    LinkedList<ConnectedClient> clients;  // an array of clients connected to ROSBridge

    public LinkedList<ConnectedClient> getClients(){ return this.clients; }

    @Override
    public JSONObject getJSON() {
        JSONObject body = new JSONObject();
        JSONArray clients = new JSONArray();

        for (ConnectedClient c : this.clients){
            clients.put(c.getJSON());
        }
        try {
            body.put("clients", clients);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.println(Log.ERROR, TAG, e.getMessage());
        }
        return body;
    }

    @Override
    public Message updateMessage(JSONObject inJSONObject) {
        this.clients = new LinkedList<>();
        try {
            inJSONObject = inJSONObject.getJSONObject("msg");
            JSONArray array = inJSONObject.getJSONArray("clients");
            for (int i = 0; i < array.length(); i++){
                ConnectedClient client = new ConnectedClient();
                client.updateMessage(array.getJSONObject(i));
                this.clients.add(client);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.println(Log.ERROR, TAG, e.getMessage());
        }
        return this;
    }
}
