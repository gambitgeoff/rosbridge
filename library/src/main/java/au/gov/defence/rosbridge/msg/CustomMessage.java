package au.gov.defence.rosbridge.msg

import org.json.JSONObject;

import au.gov.defence.rosbridge.msg.Message;

public class CustomMessage extends Message
{
    //JSONObject to hold both message variable names and values
    protected JSONObject mJSONData;

    public CustomMessage() {this(new JSONObject());}

    public CustomMessage(JSONObject inJSONData) {mJSONData = inJSONData;}




    @Override
    public JSONObject getJSON()
    {
        return mJSONData;
    }

    @Override
    public Message updateMessage(JSONObject inJSONObject)
    {
        mJSONData = inJSONObject;
        return this;
    }
}
