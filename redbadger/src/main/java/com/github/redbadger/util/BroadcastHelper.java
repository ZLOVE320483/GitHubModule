package com.github.redbadger.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.List;

/**
 * Created by zlove on 2018/1/2.
 */

public class BroadcastHelper {
    public static boolean canResolveBroadcast(Context context, Intent intent) {
        if(context == null || intent == null){
            return false;
        }
        PackageManager packageManager = context.getPackageManager();
        if(packageManager == null){
            return false;
        }
        List<ResolveInfo> receivers = packageManager.queryBroadcastReceivers(intent, 0);
        return receivers != null && receivers.size() > 0;
    }
}
