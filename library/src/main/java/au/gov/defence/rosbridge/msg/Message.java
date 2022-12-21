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

    public static final String ROS_MESSAGE_DIAGNOSTIC_ARRAY = "diagnostic_msgs/DiagnosticArray";
    public static final String ROS_MESSAGE_DIAGNOSTIC_STATUS = "diagnostic_msgs/DiagnosticStatus";
    public static final String ROS_MESSAGE_KEY_VALUE = "diagnostic_msgs/KeyValue";

    //public static final String ROS_MESSAGE_CONNECTED_CLIENTS = "rosbridge_msgs/ConnectedClients";
    public static final String ROS_MESSAGE_ROSBRIDGE_CONNECTED_CLIENT = "rosbridge_msgs/ConnectedClient";

    public enum MessageType {STRING, JOY, LOG, INT32, RB_CONNECTEDCLIENTS, HEADER, GEOMETRY_TWIST,
        IMAGE_COMPRESSED, DIAGNOSTIC_ARRAY, DIAGNOSTIC_STATUS, KEY_VALUE, REQUEST_TOPICS,
        CONNECTED_CLIENTS, CONNECTED_CLIENT};

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
            case DIAGNOSTIC_ARRAY:
                return ROS_MESSAGE_DIAGNOSTIC_ARRAY;
            case DIAGNOSTIC_STATUS:
                return ROS_MESSAGE_DIAGNOSTIC_STATUS;
            case KEY_VALUE:
                return ROS_MESSAGE_KEY_VALUE;
            case CONNECTED_CLIENT:
                return ROS_MESSAGE_ROSBRIDGE_CONNECTED_CLIENT;
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
            case ROS_MESSAGE_DIAGNOSTIC_ARRAY:
                return MessageType.DIAGNOSTIC_ARRAY;
            case ROS_MESSAGE_DIAGNOSTIC_STATUS:
                return MessageType.DIAGNOSTIC_STATUS;
            case ROS_MESSAGE_KEY_VALUE:
                return MessageType.KEY_VALUE;
            case ROS_MESSAGE_ROSBRIDGE_CONNECTED_CLIENT:
                return MessageType.CONNECTED_CLIENT;
        }
        return null;
    }

    public abstract JSONObject getJSON();
    public abstract Message updateMessage(JSONObject inJSONObject);
}