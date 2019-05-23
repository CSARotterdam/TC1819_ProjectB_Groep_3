package groep3.hr.nl.techlabhr;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class NotificationWorker extends Worker {
    private String urlJsonObj = "https://eduardterlouw.com/test/check_notification.php";


    public NotificationWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
        Log.e("Notification", "Intitialized");
    }

    @Override
    public Result doWork() {
        Context context = getApplicationContext();
        SharedPreferences userEmail = context.getSharedPreferences(context.getString(R.string.sharedPreferenceKey), Context.MODE_PRIVATE);
        final String Email = userEmail.getString("Email", "notFound");
//        final String Email = "admin@";
        Log.i("Email", Email);
        if (Email == "notFound") {
            Log.i("Email", "Not found");
            return Result.success();

        }

        StringRequest stringReq = new StringRequest(Request.Method.POST,
                urlJsonObj, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject json = new JSONObject(response);

                    if (json.getInt("Success") == 1) {
                        Log.e("Response","received");
                        JSONArray Orders = (JSONArray) json.get("Orders");
                        Log.e("Response",response);
                        for (int i = 0; i < Orders.length(); i++) {

                            JSONObject Order = (JSONObject) Orders.get(i);
                            // Parsing json object response
                            // response will be a json object
                            String productID = Order.getString("ProductID");
                            String DateOfReady = Order.getString("DateOfReady");
                            String DateOfReturn = Order.getString("DateOfReturn");
                            String ReadyBroadCasted = Order.getString("ReadyBroadcasted");
                            String ReturnWarningBroadcasted = Order.getString("ReturnWarningBroadcasted");
                            Log.e("Info", productID + " " + DateOfReady);
                            checkData(productID, DateOfReady, DateOfReturn, ReadyBroadCasted, ReturnWarningBroadcasted);
                        }
                    }
                } catch (JSONException e) {
                    Log.e("Respone","is not JSON");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("email", Email);
                return params;
            }
        };

        // Adding request to request queue
        SingletonQueue.getInstance().addToRequestQueue(stringReq);
        Log.e("Executed","Notification");
        // Indicate whether the task finished successfully with the Result
        return Result.success();
    }

    public void checkData(String PruductID, String DateOfReady, String DateOfReturn, String ReadyBroadcasted, String ReturnWarningBroadcasted) {
        Log.d("Response","Checked");
        Log.d("Response", DateOfReady + " " + ReadyBroadcasted);
        if (ReadyBroadcasted.equals("False")) {
            if (!DateOfReady.equals("31-12-1999")){
                Log.e("Notification","should display now");
                simple_Notification("Lening gereed", "Uw lening is gereed om opgehaald te worden!");
            }
        }

        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        if(date.equals(DateOfReturn)){
            if(ReturnWarningBroadcasted == "False"){
                simple_Notification("Uw lening verloopt", "Uw lening verloopt vandaag, vergeet niet om het in te leveren!");
            }
        }
    }


    private void simple_Notification(String title, String contentText) {
        //declare an id for your notification
        //id is used in many things especially when setting action buttons and their intents
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {


            String CHANNEL_ID = "my_channel_01";
            CharSequence name = "my_channel";
            String Description = "This is my channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(false);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }

        //init notification and declare specifications

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), "my_channel_01");

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_info)
                .setTicker("Hearty365")
                .setPriority(Notification.PRIORITY_MAX) // this is deprecated in API 26 but you can still use for below 26. check below update for 26 API
                .setContentTitle(title)
                .setContentText(contentText)
                .setContentInfo("Info");

        notificationManager.notify(1, notificationBuilder.build());
    }

    
}
