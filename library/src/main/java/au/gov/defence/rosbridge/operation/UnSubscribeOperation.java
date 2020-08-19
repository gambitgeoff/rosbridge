package au.gov.defence.rosbridge.operation;

import org.json.JSONException;
import org.json.JSONObject;

import au.gov.defence.rosbridge.Topic;

public class UnSubscribeOperation extends Operation {

    private Topic mTopic;

    public UnSubscribeOperation(Topic inTopic) {
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
            subscription.put("op", "unsubscribe");
            subscription.put("topic", mTopic.getName());
            subscription.put("id", "au.gov.defence.dca");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return subscription;
    }
}
