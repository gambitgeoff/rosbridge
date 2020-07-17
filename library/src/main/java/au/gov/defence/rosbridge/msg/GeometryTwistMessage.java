package au.gov.defence.rosbridge.msg;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class GeometryTwistMessage extends Message {

    private double linear_x, linear_y, linear_z;
    private double angular_x, angular_y, angular_z;

    public GeometryTwistMessage(){ this(0, 0, 0, 0, 0, 0); }

    public GeometryTwistMessage(double l_x, double l_y, double l_z,
                                double a_x, double a_y, double a_z){
        linear_x = l_x;
        linear_y = l_y;
        linear_z = l_z;

        angular_x = a_x;
        angular_y = a_y;
        angular_z = a_z;
    }

    @Override
    public JSONObject getJSON() {
        JSONObject body = new JSONObject();
        JSONObject linear = new JSONObject();
        JSONObject angular = new JSONObject();
        try {
            linear.put("x", linear_x);
            linear.put("y", linear_y);
            linear.put("z", linear_z);

            angular.put("x", angular_x);
            angular.put("y", angular_y);
            angular.put("z", angular_z);

            body.put("linear", linear);
            body.put("angular", angular);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.println(Log.ERROR, "GEOMETRY JSON MESSAGE", "error in converting geo message to JSON: " + e.getMessage());
        }

        return body;
    }

    @Override
    public Message updateMessage(JSONObject json) {

        // untested

        try {
            json = json.getJSONObject("msg");
            JSONObject angular = json.getJSONObject("angular");
            JSONObject linear = json.getJSONObject("linear");
            angular_x = angular.getDouble("x");
            angular_y = angular.getDouble("y");
            angular_z = angular.getDouble("z");

            linear_x = linear.getDouble("x");
            linear_y = linear.getDouble("y");
            linear_z = linear.getDouble("z");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return this;
    }
}
