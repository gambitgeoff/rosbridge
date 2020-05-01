package au.gov.defence.rosbridge;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.gov.defence.rosbridge.viewmodel.ROSTopicsViewModel;
import io.crossbar.autobahn.websocket.WebSocketConnection;
import io.crossbar.autobahn.websocket.WebSocketConnectionHandler;
import io.crossbar.autobahn.websocket.exceptions.WebSocketException;
import io.crossbar.autobahn.websocket.types.ConnectionResponse;

public class ROSBridge implements ViewModelStoreOwner {

    private ViewModelStore mViewModelStore;

    private static final String TAG = "au.gov.defence.rosbridge.ROSBridge";
    private static ROSBridge mROSBridge;
    private WebSocketConnection mROSBridgeWebSocketConnection;
    private WebSocketConnectionHandler mROSBridgeWebSocketConnectionHandler;
    private Map<String, Topic> mHandledTopics;

//    private HeartRateManager mHeartRateManager;
//    private JoySupport mJoySupport;
//    private VIRBManager mVIRBManager;

    /**
     * A list of all the currently seen topics on the ROS MASTER
     **/
//    private List<Topic> mROSBridgeTopics;

    /**
     * A list containing all of the current subscriptions
     **/
//    private List<Topic> mSubscriptions;

    /**
     * A list containing all of the currrent publications
     **/
//    private List<Topic> mPublications;

    /**
     * A list of all the currently advertised topics
     **/
    private List<Topic> mAdvertised;
    private ROSTopicsViewModel mTopicsViewModel;
    private boolean mHeartbeatActive = true;
    private Thread mHeartBeat;

    private ROSBridge() {
        mViewModelStore = new ViewModelStore();
        mHandledTopics = new HashMap<>();
//        mROSBridgeTopics = new Vector<Topic>();
//        mSubscriptions = new Vector<Topic>();
        Log.v(TAG, "Created the view model store");
//        mTopicsViewModel = new ViewModelProvider(this).get(ROSTopicsViewModel.class);
//        mTopicsViewModel.getTopicsList().observeForever(new Observer<List<Topic>>() {
//            @Override
//            public void onChanged(List<Topic> inTopics) {
//                    Log.v(TAG, "Updated Topics:");
//                for (Topic t : inTopics) {
//                        if (!mROSBridgeTopics.contains(t))
//                            mROSBridgeTopics.add(t);
//                    if (t.getName().contains("soldier")) {
//                        Log.v(TAG, "found soldier: " + t.getName() + ", Type: " + t.getMessageType());
//                            if (!mSubscriptions.contains(t)) {
//                                t.subscribe();
//                                mSubscriptions.add(t);
//                            }
//                        }
//                    }
//            }
//        });
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
//                    updateTopicData();
//                    updatePhysiologicalData();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

//    private void updateTopicData() {
//        if (mROSBridgeWebSocketConnection != null) {
//            if (mROSBridgeWebSocketConnection.isConnected()) {
//                RequestTopicsOperation message = new RequestTopicsOperation();
//                try {
//                    message.sendMessage();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

//    private void updatePhysiologicalData() {
//        Collection<Soldier> soldiers = DCASupport.getSoldiers();
//        List<Soldier> newList = new Vector<Soldier>(soldiers);
//        mTeamHeartRatesViewModel.setSoldiers(newList);
//        Log.v(TAG, "Posting update for all soldiers physiological data.");
//    }

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
            Log.v(TAG, "sent the connection request");
        } catch (WebSocketException e) {
            e.printStackTrace();
        }
    }

    public static ROSBridge getROSBridge() {
        if (mROSBridge == null)
            mROSBridge = new ROSBridge();
        return mROSBridge;
    }

    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        return mViewModelStore;
    }


    public class WSConnectionHandler extends WebSocketConnectionHandler {
        private static final String TAG = "au.gov.defence.rosbridge.ROSBridge.DSAConnectionHandler";

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
            Log.v(TAG, "DCA ROS Bridge Connection Opened");
            connectTopics();
//            mHeartRateManager = new HeartRateManager();
//            mJoySupport = new JoySupport();
//            RemoteSensorView.setJoySupport(mJoySupport);
//            mVIRBManager = new VIRBManager();
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
                        mTopicsViewModel.setTopicsList(Topic.parseTopics(received));
                        Log.v(TAG, "Received topics back from server");
                        break;
                    }
                    case "publish": {
                        String topicName = received.getString("topic");
                        Topic topic = mHandledTopics.get(topicName);
                        topic.handleUpdate(received);
//                        boolean soldier_hr = topic.contains(HeartRateManager.TOPIC_HRM_HEARTRATE);
//                        if (soldier_hr) {
//                            String soldierName = topic.substring(9, topic.indexOf("/", 9));
//                            JSONObject msg = received.getJSONObject("msg");
//                            int heartRate = msg.getInt("data");
//                            Soldier s = DCASupport.getSoldier(soldierName);
//                            s.setHeartRate(heartRate);
                        }
                    }
//                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
