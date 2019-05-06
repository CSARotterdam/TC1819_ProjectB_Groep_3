package groep3.hr.nl.techlabhr;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class SingletonQueue extends Application {
    public static final String TAG = SingletonQueue.class.getSimpleName();
    private static SingletonQueue instance;
    private RequestQueue requestQueue;
    private static Context ctx;

    public SingletonQueue(){
        //Required empty constructor
    }

    @Override
    public void onCreate(){
        super.onCreate();
        instance = this;
    }

    private SingletonQueue(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();

    }


    public static synchronized SingletonQueue getInstance(Context context) {
        if (instance == null) {
            instance = new SingletonQueue(context);
        }
        return instance;
    }

    public static synchronized SingletonQueue getInstance() {
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (requestQueue != null) {
            requestQueue.cancelAll(tag);
        }
    }
}
