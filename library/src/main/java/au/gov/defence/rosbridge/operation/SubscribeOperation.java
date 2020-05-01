package au.gov.defence.rosbridge.operation;

import org.json.JSONException;
import org.json.JSONObject;

import au.gov.defence.rosbridge.Topic;

public class SubscribeOperation extends Operation {

    private Topic mTopic;

    public SubscribeOperation(Topic inTopic) {
        super();
        mTopic = inTopic;
    }

    @Override
    public Operation parseMessage(JSONObject inMessage) {
        return null;
    }

    @Override
    public JSONObject getJSON() {
        final JSONObject subscription = new JSONObject();
        try {
            subscription.put("op", "subscribe");
            subscription.put("topic", mTopic.getName());
            subscription.put("id", "au.gov.defence.dca");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return subscription;
    }
}
