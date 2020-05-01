package au.gov.defence.rosbridge.operation;

import org.json.JSONException;
import org.json.JSONObject;

import au.gov.defence.rosbridge.Topic;
import au.gov.defence.rosbridge.msg.Message;

public class PublishOperation extends Operation {

    private Topic mTopic;
    private Message mMessage;

    public PublishOperation(Topic inTopic, Message inMessage) {
        super();
        mTopic = inTopic;
        mMessage = inMessage;
    }

    @Override
    public Operation parseMessage(JSONObject inMessage) {
        return null;
    }

    @Override
    public JSONObject getJSON() {
        final JSONObject publication = new JSONObject();
        try {
            publication.put("op", "publish");
            publication.put("topic", mTopic.getName());
            publication.put("type", Message.getMessageTypeJSON(mTopic.getMessageType()));
            publication.put("id", "au.gov.defence.dca");
            publication.put("msg", mMessage.getJSON());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return publication;
    }
}
