package au.gov.defence.rosbridge.operation;

import org.json.JSONException;
import org.json.JSONObject;

import au.gov.defence.rosbridge.Topic;
import au.gov.defence.rosbridge.msg.Message;

public class AdvertiseOperation extends Operation {

    private Topic mTopic;

    public AdvertiseOperation(Topic inTopic) {
        super();
        mTopic = inTopic;
    }

    @Override
    public Operation parseMessage(JSONObject inMessage) {
        return null;
    }

    @Override
    public JSONObject getJSON() {
        final JSONObject advert = new JSONObject();
        try {
            advert.put("op", "advertise");
            advert.put("topic", mTopic.getName());
            advert.put("type", Message.getMessageTypeJSON(mTopic.getMessageType()));
            advert.put("id", "au.gov.defence.dca");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return advert;
    }
}
