package au.gov.defence.rosbridge.msg;

public class MessageFactory {

    public static Message createMessage(Message.MessageType inMessageType)
    {
        Message returnValue = null;
        switch (inMessageType)
        {
            case STRING:
            {
                returnValue = new StringMessage();
            }
            case JOY:
            {
                returnValue = new JoyMessage();
            }
        }
        return returnValue;
    }
}
