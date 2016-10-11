package zxh.bdmusic.baseclass;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by dllo on 16/9/20.
 */
public class VolleySingleton {
    private static VolleySingleton mVolleySingleton;

    private RequestQueue mRequestQueue;

    public VolleySingleton() {
        mRequestQueue = Volley.newRequestQueue(MyApp.getContext());
    }

    public static VolleySingleton getInstance() {
        if (mVolleySingleton == null) {
            synchronized (VolleySingleton.class) {
                if (mVolleySingleton == null) {
                    mVolleySingleton = new VolleySingleton();
                }
            }
        }
        return mVolleySingleton;
    }

    public RequestQueue getRequestQueue(){
        return mRequestQueue;
    }

    public void addRequest(Request request){
        mRequestQueue.add(request);
    }

}
