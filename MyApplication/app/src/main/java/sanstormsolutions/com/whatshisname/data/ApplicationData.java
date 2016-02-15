package sanstormsolutions.com.whatshisname.data;

import android.app.Application;
import android.content.Context;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;

import com.firebase.client.Firebase;

/**
 * Created by jsandersii on 2/14/16.
 */
public class ApplicationData extends Application {

    private static ApplicationData mInstance = null;
    private static AppCompatActivity mActivity = null;

    public static Context getContext() {
        return mInstance;
    }


    @Override
    public void onCreate(){
        super.onCreate();
        mInstance = this;
        Firebase.setAndroidContext(this); //Initialize Firebase

    }

    public static String getDeviceID(Context context){
        String androidID;
        androidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidID;
    }

    public static AppCompatActivity getActivity() {
        return mActivity;
    }

    public static void setActivity(AppCompatActivity mActivity) {
        ApplicationData.mActivity = mActivity;
    }
}
