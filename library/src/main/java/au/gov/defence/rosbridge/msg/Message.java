package au.gov.defence.rosbridge.msg;

import org.json.JSONObject;

public abstract class Message {

    public static final String ROS_MESSAGE_STRING = "std_msgs/String";
    public static final String ROS_MESSAGE_JOY = "sensor_msgs/Joy";
    public static final String ROS_MESSAGE_LOG = "rosgraph_msgs/Log";
    public static final String ROS_MESSAGE_ROSBRIDGE_CONNECTEDCLIENTS = "rosbridge_msgs/ConnectedClients";
    public static final String ROS_MESSAGE_INT32 = "std_msgs/Int32";
    public static final String ROS_HEADER = "std_msgs/Header";

    public static final String ROS_MESSAGE_GEOMETRY_TWIST = "geometry_msgs/Twist";
    public static final String ROS_MESSAGE_IMAGE_COMPRESSED = "sensor_msgs/CompressedImage";

    public enum MessageType {STRING, JOY, LOG, INT32, RB_CONNECTEDCLIENTS, HEADER, GEOMETRY_TWIST, IMAGE_COMPRESSED, REQUEST_TOPICS};

    public static String getMessageTypeJSON(MessageType inMessageType) {
        switch (inMessageType) {
            case STRING:
                return ROS_MESSAGE_STRING;
            case JOY:
                return ROS_MESSAGE_JOY;
            case LOG:
                return ROS_MESSAGE_LOG;
            case RB_CONNECTEDCLIENTS:
                return ROS_MESSAGE_ROSBRIDGE_CONNECTEDCLIENTS;
            case INT32:
                return ROS_MESSAGE_INT32;
            case HEADER:
                return ROS_HEADER;
            case GEOMETRY_TWIST:
                return ROS_MESSAGE_GEOMETRY_TWIST;
            case IMAGE_COMPRESSED:
                return ROS_MESSAGE_IMAGE_COMPRESSED;
        }
        return null;
    }

    public static MessageType getMessageType(String inJSONMessage) {
        switch (inJSONMessage) {
            case ROS_MESSAGE_STRING:
                return MessageType.STRING;
            case ROS_MESSAGE_JOY:
                return MessageType.JOY;
            case ROS_MESSAGE_LOG:
                return MessageType.LOG;
            case ROS_MESSAGE_INT32:
                return MessageType.INT32;
            case ROS_MESSAGE_ROSBRIDGE_CONNECTEDCLIENTS:
                return MessageType.RB_CONNECTEDCLIENTS;
            case ROS_HEADER:
                return MessageType.HEADER;
            case ROS_MESSAGE_GEOMETRY_TWIST:
                return MessageType.GEOMETRY_TWIST;
            case ROS_MESSAGE_IMAGE_COMPRESSED:
                return MessageType.IMAGE_COMPRESSED;
        }
        return null;
    }

    public abstract JSONObject getJSON();
    public abstract Message updateMessage(JSONObject inJSONObject);
}