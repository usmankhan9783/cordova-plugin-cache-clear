package com.anrip.cordova;

import org.json.JSONArray;
import org.json.JSONException;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import static android.content.Context.ACTIVITY_SERVICE;

@TargetApi(19)
public class CacheClear extends CordovaPlugin {

    private static final String LOG_TAG = "CacheClear";
    private static final String MESSAGE_TASK = "Cordova Android CacheClear() called.";
    private static final String MESSAGE_ERROR = "Error while clearing webview cache.";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("task")) {
            Log.v(LOG_TAG, MESSAGE_TASK);
            task(callbackContext);
            return true;
        }
        return false;
    }

    public void task(CallbackContext callbackContext) {
        final CacheClear self = this;
        final CallbackContext callback = callbackContext;

        cordova.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                try {
                    // clear the cache
                    //self.webView.clearCache();

                    final Context appContext = self.cordova.getActivity().getApplicationContext();

                    if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
                        ((ActivityManager)appContext.getSystemService(ACTIVITY_SERVICE))
                                .clearApplicationUserData(); // note: it has a return value!
                    } else {
                        // use old hacky way, which can be removed
                        // once minSdkVersion goes above 19 in a few years.
                    }
                    // send success result to cordova
                    PluginResult result = new PluginResult(PluginResult.Status.OK);
                    result.setKeepCallback(false);
                    callback.sendPluginResult(result);
                } catch (Exception e) {
                    Log.e(LOG_TAG, MESSAGE_ERROR);
                    // return error answer to cordova
                    PluginResult result = new PluginResult(PluginResult.Status.ERROR, MESSAGE_ERROR);
                    result.setKeepCallback(false);
                    callback.sendPluginResult(result);
                }
            }
        });
    }

}
