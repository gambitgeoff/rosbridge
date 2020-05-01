package au.gov.defence.rosbridge.operation;

import org.json.JSONObject;

import au.gov.defence.rosbridge.ROSBridge;
import io.crossbar.autobahn.websocket.WebSocketConnection;

public abstract class Operation {
    protected WebSocketConnection mROSBridgeConnection;


    public Operation() {
        mROSBridgeConnection = ROSBridge.getROSBridge().getROSBridgeConnection();
    }

    public enum OperationType {PUBLISH, SUBSCRIBE, ADVERTISE, UNADVERTISE, UNSUBSCRIBE, CALL_SERVICE, STATUS}

    public abstract Operation parseMessage(JSONObject inMessage);

    public abstract JSONObject getJSON();

    public void sendMessage() throws Exception {
        if (mROSBridgeConnection.isConnected())
            mROSBridgeConnection.sendMessage(getJSON().toString());
        else
            throw new Exception("ROS Bridge not yet connected!!!!!!");
    }

    protected static String getID() {
        return "DCA-ROS";
    }
}