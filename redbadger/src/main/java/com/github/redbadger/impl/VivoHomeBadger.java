package com.github.redbadger.impl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.github.redbadger.Badger;
import com.github.redbadger.RedBadgerException;

import java.util.Arrays;
import java.util.List;

/**
 * Created by zlove on 2018/1/3.
 */

public class VivoHomeBadger implements Badger {

    @Override
    public void executeBadge(Context context, ComponentName componentName, int badgeCount) throws RedBadgerException {
        try {
            Intent intent = new Intent("launcher.action.CHANGE_APPLICATION_NOTIFICATION_NUM");
            intent.putExtra("packageName", context.getPackageName());
            intent.putExtra("className", componentName.getClassName());
            intent.putExtra("notificationNum", badgeCount);
            context.sendBroadcast(intent);
        } catch (Throwable t) {
            // ignore
            t.printStackTrace();
        }
    }

    @Override
    public List<String> getSupportLaunchers() {
        return Arrays.asList("com.vivo.launcher");
    }
}
