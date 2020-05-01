package au.gov.defence.rosbridge;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Vector;

import au.gov.defence.rosbridge.msg.Message;
import au.gov.defence.rosbridge.operation.AdvertiseOperation;
import au.gov.defence.rosbridge.operation.PublishOperation;
import au.gov.defence.rosbridge.operation.SubscribeOperation;

public class Topic {
    private static final String TAG = "au.gov.defence.dca.ros";
    private String mTopicName;
    private Message.MessageType mMessageType;
    private boolean mIsAdvertised = false;
    private boolean mAdvertise = false;
    private ROSBridge mROSBridge;

    public Topic(String inTopicName, Message.MessageType inMessageType) {
        mTopicName = inTopicName;
        mMessageType = inMessageType;
        mROSBridge = ROSBridge.getROSBridge();
        mROSBridge.addTopic(this);
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

    public void handleUpdate(JSONObject inJSONObject)
    {
        //handle the update from the bridge. likely to update the message value.
        Log.v(TAG, "handleUpdate for Topic: " + inJSONObject.toString());
    }

    public void handleConnected()
    {
        if(mAdvertise)
            this.advertise();
    }

    public void advertise() {
        mAdvertise = true;
        if (mROSBridge.getROSBridgeConnection().isConnected())
        {
            AdvertiseOperation message = new AdvertiseOperation(this);
            try {
                message.sendMessage();
                mIsAdvertised = true;
                mAdvertise = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void subscribe() {
        SubscribeOperation message = new SubscribeOperation(this);
        try {
            message.sendMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void publish(Message inMessage) {
        PublishOperation operation = new PublishOperation(this, inMessage);
        try {
            operation.sendMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}