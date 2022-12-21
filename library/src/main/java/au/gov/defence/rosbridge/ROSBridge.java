package au.gov.defence.rosbridge;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.gov.defence.rosbridge.operation.RequestTopicsOperation;
import io.crossbar.autobahn.websocket.WebSocketConnection;
import io.crossbar.autobahn.websocket.WebSocketConnectionHandler;
import io.crossbar.autobahn.websocket.exceptions.WebSocketException;
import io.crossbar.autobahn.websocket.types.ConnectionResponse;

public class ROSBridge {

    private static final String TAG = "au.gov.defence.rosbridge.ROSBridge";
    private static ROSBridge mROSBridge;
    private WebSocketConnection mROSBridgeWebSocketConnection;
    private WebSocketConnectionHandler mROSBridgeWebSocketConnectionHandler;
    private Map<String, Topic> mHandledTopics;

    private boolean mHeartbeatActive = true;
    public Thread mHeartBeat;           //temp - changed from private 

    private ROSBridge() {
        mHandledTopics = new HashMap<>();
        Log.v(TAG, "Created the view model store");
        setupHeartbeat();
    }

    public void addTopic(Topic inTopic)
    {
        mHandledTopics.put(inTopic.getName(), inTopic);
    }

    public void removeTopic(Topic inTopic)
    {
        mHandledTopics.remove(inTopic.getName());
    }

    public WebSocketConnection getROSBridgeConnection() {
        return mROSBridgeWebSocketConnection;
    }

    private void setupHeartbeat() {
        mHeartBeat = new Thread(new Runnable() {
            @Override
            public void run() {
                while (mHeartbeatActive) {
                    updateTopicData();
                    try {
                        Thread.sleep(10*1000); //update every 10s
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void updateTopicData() {
        Log.v(TAG, "Updating topic information");
        if (mROSBridgeWebSocketConnection != null) {
            if (mROSBridgeWebSocketConnection.isConnected()) {
                RequestTopicsOperation message = new RequestTopicsOperation();
                try {
                    message.sendMessage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Called when the rosbridge connection is established. It will run through topics which
     * have been added and ensure that they have been advertised (if set) and publish the data
     * they have buffered up.
     *
     */
    private void connectTopics()
    {
        for(Topic t: mHandledTopics.values())
        {
            t.handleConnected();
        }
    }

    public void connectROSBridge(String inURL) {
        mROSBridgeWebSocketConnection = new WebSocketConnection();
        mROSBridgeWebSocketConnectionHandler = new WSConnectionHandler();
            try {
            mROSBridgeWebSocketConnection.connect(inURL, mROSBridgeWebSocketConnectionHandler);
            Log.v(TAG, "sent the connection request to: " + inURL);
        } catch (WebSocketException e) {
            e.printStackTrace();
        }
    }

    public static ROSBridge getROSBridge() {
        if (mROSBridge == null)
            mROSBridge = new ROSBridge();
        return mROSBridge;
    }

    public class WSConnectionHandler extends WebSocketConnectionHandler {
        private static final String TAG = "au.gov.defence.rosbridge.ROSBridge.WSConnectionHandler";

        public WSConnectionHandler() {
            super();
        }

        @Override
        public void onConnect(ConnectionResponse response) {
            super.onConnect(response);
            Log.v(TAG, "Connected to ROS Bridge");
        }

        @Override
        public void onOpen() {
            super.onOpen();
            Log.v(TAG, "ROS Bridge Connection Opened");
            connectTopics();
//            mHeartBeat.start();
        }

        @Override
        public void onClose(int code, String reason) {
            super.onClose(code, reason);
            Log.v(TAG, "Connection closed: " + reason);
        }

        @Override
        public void onMessage(String payload) {
            super.onMessage(payload);
            Log.v(TAG, "Received Message: " + payload);
            JSONObject received = null;
            try {
                received = new JSONObject(payload);
                String op = received.getString("op");
                switch (op) {
                    case "service_response": {
                        List<Topic> topics = Topic.parseTopics(received);
                        for(Topic t: topics)
                        {
                            String topicName = t.getName();
                            if(!mHandledTopics.containsKey(topicName))
                            {
                                Log.v(TAG, "Found new topic: " + topicName);
                                mHandledTopics.put(topicName,t);
                            }
                        }
                        Log.v(TAG, "Received topics back from server");
                        break;
                    }
                    case "publish": {
                        String topicName = received.getString("topic");
                        Log.v(TAG,"Received publish update for topic: " + topicName);
                        Topic topic = mHandledTopics.get(topicName);
                        topic.handleUpdate(received);
                        }
                    }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
