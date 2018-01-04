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

public class AsusHomeBadger implements Badger {
    private static final String INTENT_ACTION = "android.intent.action.BADGE_COUNT_UPDATE";
    private static final String INTENT_EXTRA_BADGE_COUNT = "badge_count";
    private static final String INTENT_EXTRA_PACKAGENAME = "badge_count_package_name";
    private static final String INTENT_EXTRA_ACTIVITY_NAME = "badge_count_class_name";

    @Override
    public void executeBadge(Context context, ComponentName componentName, int badgeCount) throws RedBadgerException {
        if(context == null || componentName == null){
            return;
        }
        if(badgeCount < 0){
            badgeCount = 0;
        }
        Intent intent = new Intent(INTENT_ACTION);
        intent.putExtra(INTENT_EXTRA_BADGE_COUNT, badgeCount);
        intent.putExtra(INTENT_EXTRA_PACKAGENAME, componentName.getPackageName());
        intent.putExtra(INTENT_EXTRA_ACTIVITY_NAME, componentName.getClassName());
        intent.putExtra("badge_vip_count", 0);
        if (BroadcastHelper.canResolveBroadcast(context, intent)) {
            try {
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
        return Arrays.asList("com.asus.launcher");
    }
}
