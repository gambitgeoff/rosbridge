package au.gov.defence.rosbridge.msg;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * For the sensor_msgs/CompressedImage message type.
 * ROS documentation: http://docs.ros.org/melodic/api/sensor_msgs/html/msg/CompressedImage.html
 */

public class CompressedImageMessage extends Message {

    private static final String TAG = "au.gov.defence.dsa.ros.msg.CompressedImageMessage";

    private String format; // acceptable values: jpeg, png
    private byte[] data;
    private Header header;

    public CompressedImageMessage(){}

    public CompressedImageMessage(String format, byte[] data, int sequenceNumber, int timeStamp_s, int timeStamp_ns, String frameID){
        super();
        this.format = format;
        this.data = data;
        this.header = new Header(timeStamp_s, timeStamp_ns, frameID);
        this.header.setSeq(sequenceNumber);
    }

    // to load this class from a JSON message.
    public CompressedImageMessage(JSONObject json){
        this.updateMessage(json);
    }

    @Override
    public JSONObject getJSON() {
        JSONObject body = new JSONObject();
        try {
            body.put("header", this.header.getJSON());
            body.put("format", this.format);
            body.put("data", this.data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return body;
    }

    @Override
    public Message updateMessage(JSONObject json) {
        try {
            json = json.getJSONObject("msg");
            this.format = json.getString("format");

            // byte array conversion logic from https://stackoverflow.com/questions/26650225/put-get-byte-array-value-using-jsonobject
            this.data = json.getString("data").toString().getBytes();
            this.header = new Header(json.getJSONObject("header"));
        } catch (JSONException e) {
            Log.println(Log.ERROR, TAG, e.getMessage());
        }
        return this;
    }

    public Bitmap getImage(){
        // compressed as bgr8 'jpeg compressed bgr8'
        // https://stackoverflow.com/questions/14681643/image-isnt-creating-using-the-bitmapfactory-decodebytearray
        // https://github.com/mgorski-mg/ROS_Map/blob/master/Android/Map/android_gingerbread_mr1/src/main/java/org/ros/android/BitmapFromCompressedImage.java
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        byte[] converted = Base64.decode(this.data, Base64.DEFAULT);
        Bitmap image = BitmapFactory.decodeByteArray(converted, 0, converted.length);
        if (image == null){
            Log.println(Log.ERROR, TAG, "converted bitmap was null");
        }
        return image;
    }


}
