package com.github.redbadger.impl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.github.redbadger.Badger;
import com.github.redbadger.RedBadgerException;
import com.github.redbadger.util.BroadcastHelper;

import java.util.Arrays;
import java.util.List;

/**
 * Created by zlove on 2018/1/2.
 */

public class NewHtcHomeBadger implements Badger {

    public static final String INTENT_UPDATE_SHORTCUT = "com.htc.launcher.action.UPDATE_SHORTCUT";
    public static final String INTENT_SET_NOTIFICATION = "com.htc.launcher.action.SET_NOTIFICATION";
    public static final String PACKAGENAME = "packagename";
    public static final String COUNT = "count";
    public static final String EXTRA_COMPONENT = "com.htc.launcher.extra.COMPONENT";
    public static final String EXTRA_COUNT = "com.htc.launcher.extra.COUNT";

    @Override
    public void executeBadge(Context context, ComponentName componentName, int badgeCount) throws RedBadgerException {
        if(context == null || componentName == null){
            return;
        }
        if(badgeCount < 0){
            badgeCount = 0;
        }
        Intent intent1 = new Intent(INTENT_SET_NOTIFICATION);
        intent1.putExtra(EXTRA_COMPONENT, componentName.flattenToShortString());
        intent1.putExtra(EXTRA_COUNT, badgeCount);

        Intent intent = new Intent(INTENT_UPDATE_SHORTCUT);
        intent.putExtra(PACKAGENAME, componentName.getPackageName());
        intent.putExtra(COUNT, badgeCount);

        if(BroadcastHelper.canResolveBroadcast(context, intent1) ||
                BroadcastHelper.canResolveBroadcast(context, intent)) {
            try {
                context.sendBroadcast(intent1);
                context.sendBroadcast(intent);
            } catch (Throwable t){
                // ignore
                t.printStackTrace();
            }
        } else {
            throw new RedBadgerException("unable to resolve intent: " + intent.toString());
        }
    }

    @Override
    public List<String> getSupportLaunchers() {
        return Arrays.asList("com.htc.launcher");
    }
}
