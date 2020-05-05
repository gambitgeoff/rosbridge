package au.gov.defence.rosbridge;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Vector;

import au.gov.defence.rosbridge.msg.Message;
import au.gov.defence.rosbridge.msg.MessageFactory;
import au.gov.defence.rosbridge.operation.AdvertiseOperation;
import au.gov.defence.rosbridge.operation.Operation;
import au.gov.defence.rosbridge.operation.PublishOperation;
import au.gov.defence.rosbridge.operation.SubscribeOperation;
import au.gov.defence.rosbridge.viewmodel.TopicObservable;
import au.gov.defence.rosbridge.viewmodel.TopicObserver;

public class Topic extends TopicObservable {
    private static final String TAG = "au.gov.defence.rosbridge.Topic";
    private String mTopicName;
    private Message.MessageType mMessageType;
    private ROSBridge mROSBridge;
    private Vector<Operation> mOperationBuffer;
    private Message mMessage;

    public Topic(String inTopicName, Message.MessageType inMessageType) {
        super();
        mTopicName = inTopicName;
        mMessageType = inMessageType;
        mROSBridge = ROSBridge.getROSBridge();
        mROSBridge.addTopic(this);
        mOperationBuffer = new Vector<>();
    }

    @Override
    public boolean equals(Object inObject) {
        if (inObject instanceof Topic) {
            Topic t = (Topic) inObject;
            return t.mTopicName.equals(mTopicName);
        }
        return false;
    }

    public Message.MessageType getMessageType() {
        return mMessageType;
    }

    @Override
    public String toString() {
        return "Topic: name: " + mTopicName + ", type: " + Message.getMessageTypeJSON(mMessageType);
    }

    public String getName() {
        return mTopicName;
    }

    public Message getMessage() {return mMessage;}

    public static List<Topic> parseTopics(JSONObject inJSONTopics) {
        List<Topic> topicVec = new Vector<Topic>();
        Log.v(TAG, "Parsing topics");
        try {
            JSONArray topics = inJSONTopics.getJSONObject("values").getJSONArray("topics");
            JSONArray types = inJSONTopics.getJSONObject("values").getJSONArray("types");
            for (int i = 0; i < topics.length(); i++) {
                topicVec.add(new Topic(topics.getString(i), Message.getMessageType(types.getString(i))));
            }
            return topicVec;
        } catch (JSONException jse) {
            jse.printStackTrace();
        }
        return null;
    }

    /**
     * handle the update from the bridge. likely to update the message value i.e. from a publish.
     * @param inJSONObject
     */
    public void handleUpdate(JSONObject inJSONObject) {
        Log.v(TAG, "handleUpdate for Topic: " + mTopicName + ", type: " + mMessageType.toString() + ", data: " + inJSONObject.toString() + ", Observers: " + mObservers.size());
        if(mMessage == null)
            mMessage = MessageFactory.createMessage(mMessageType);
        mMessage.updateMessage(inJSONObject);
        updateTopicObservers(this);
    }

    public void handleConnected() {
        if (mOperationBuffer.size() > 0) {
            Log.v(TAG, "Flushing topic buffers");
            for (Operation o : mOperationBuffer) {
                try {
                    o.sendMessage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void advertise() {
        AdvertiseOperation message = new AdvertiseOperation(this);
        if (!mROSBridge.getROSBridgeConnection().isConnected()) {
            mOperationBuffer.add(message);
            return;
        }
        try {
            message.sendMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void subscribe(TopicObserver inTopicObserver) {
        SubscribeOperation message = new SubscribeOperation(this);
        if (!mROSBridge.getROSBridgeConnection().isConnected()) {
            mOperationBuffer.add(message);
            addTopicObserver(inTopicObserver);
            return;
        }
        try {
            message.sendMessage();
            addTopicObserver(inTopicObserver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void publish(Message inMessage) {
        PublishOperation operation = new PublishOperation(this, inMessage);
        mMessage = inMessage;
        if (!mROSBridge.getROSBridgeConnection().isConnected()) {
            mOperationBuffer.add(operation);
            return;
        }
        try {
            operation.sendMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
