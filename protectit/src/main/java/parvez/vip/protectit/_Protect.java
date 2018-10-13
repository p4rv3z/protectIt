package parvez.vip.protectit;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Log;

/**
 * Project: ProtectIt
 * Created by Muhammad Harun-Or-Roshid
 * Email: md.parvez28@gmail.com
 * Web: https://parvez.vip
 * On 23 August 2018 at 6:51 PM
 */
public class _Protect {
    private static final String TAG = "_Protect";
    private static final String PLAY_STORE_APP_ID = "com.android.vending";
    public static final String[] LUCKYPATCHERS = {"com.dimonvideo.luckypatcher", "com.chelpus.lackypatch"
            , "com.android.vending.billing.InAppBillingService.LACK"};


    /**
     *  If Signature is matched thats means you are ok to go
     *  if failed kill the process without warning
     * @param context
     * @param signature
     * @return true if matched
     */
    public static boolean isSignatureMatched(Context context, String signature) {
        try {
            Signature[] signatures = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_SIGNATURES).signatures;

            if (signatures[0].toCharsString().equalsIgnoreCase(signature)) {
                // Kill the process without warning. If someone changed the certificate
                // is better not to give a hint about why the app stopped working
                //android.os.Process.killProcess(android.os.Process.myPid());
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            // Must never fail, so if it does, means someone played with the apk, so kill the process
            //android.os.Process.killProcess(android.os.Process.myPid());
            Log.e(TAG, e.getMessage());
        }
        return false;
    }


    /**
     * If billing is contain with this package
     * @param context
     * @return
     */
    public static boolean isGooglePlayInstaller(Context context) {

        String installer = context.getPackageManager().getInstallerPackageName(context.getPackageName());
        return installer != null && installer.startsWith(PLAY_STORE_APP_ID);
    }


    /**
     * check app is debuggable or not
     * @param context
     * @return true if app is debuggable
     */
    public static boolean isDebuggable(Context context){
        return (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }


    /**
     * kill the process if something went wrong
     */
    public static void killSelf() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }


    /**
     *  check the patcher with default package is exists or not
     * @param context
     * @return true if exists
     */
    public static boolean isPatcherExists(Context context) {
        return isPatcherExists(context, LUCKYPATCHERS);
    }


    /**
     *  check the patcher is exists or not
     * @param context
     * @return true if exists
     */
    public static boolean isPatcherExists(Context context, String[] patchers) {
        for (String packageName : patchers) {
            if (isPackageExists(context, packageName)) {
                return true;
            }
        }
        return false;
    }


    /**
     * check package name is exists or not
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isPackageExists(Context context, String packageName) {
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName, 0);

            if (info != null) {
                // No need really to test for null, if the package does not
                // exist it will really rise an exception. but in case Google
                // changes the API in the future lets be safe and test it
                return true;
            }
        } catch (Exception e) {
            // If we get here only means the Package does not exist
            Log.e(TAG, e.getMessage());
        }
        return false;
    }
}
