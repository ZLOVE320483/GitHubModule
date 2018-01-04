package com.github.redbadger.impl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.github.redbadger.Badger;
import com.github.redbadger.RedBadgerException;
import com.github.redbadger.util.BroadcastHelper;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zlove on 2018/1/2.
 */

public class XiaomiHomeBadger implements Badger {

    public static final String INTENT_ACTION = "android.intent.action.APPLICATION_MESSAGE_UPDATE";
    public static final String EXTRA_UPDATE_APP_COMPONENT_NAME = "android.intent.extra.update_application_component_name";
    public static final String EXTRA_UPDATE_APP_MSG_TEXT = "android.intent.extra.update_application_message_text";

    @Override
    public void executeBadge(Context context, ComponentName componentName, int badgeCount) throws RedBadgerException {
        if(context == null || componentName == null){
            return;
        }
        if(badgeCount < 0){
            badgeCount = 0;
        }
        try {
            Class miuiNotificationClass = Class.forName("android.app.MiuiNotification");
            Object miuiNotification = miuiNotificationClass.newInstance();
            Field field = miuiNotification.getClass().getDeclaredField("messageCount");
            field.setAccessible(true);
            try {
                field.set(miuiNotification, String.valueOf(badgeCount == 0 ? "" : badgeCount));
            } catch (Throwable t) {
                field.set(miuiNotification, badgeCount);
            }
        } catch (Throwable t) {
            Intent localIntent = new Intent(
                    INTENT_ACTION);
            localIntent.putExtra(EXTRA_UPDATE_APP_COMPONENT_NAME, componentName.getPackageName() + "/" + componentName.getClassName());
            localIntent.putExtra(EXTRA_UPDATE_APP_MSG_TEXT, String.valueOf(badgeCount == 0 ? "" : badgeCount));
            if (BroadcastHelper.canResolveBroadcast(context, localIntent)) {
                try {
                    context.sendBroadcast(localIntent);
                } catch (Throwable throwable){
                    // ignore
                    throwable.printStackTrace();
                }
            } else {
                throw new RedBadgerException("unable to resolve intent: " + localIntent.toString());
            }
        }
    }

    @Override
    public List<String> getSupportLaunchers() {
        return Arrays.asList(
                "com.miui.miuilite",
                "com.miui.home",
                "com.miui.miuihome",
                "com.miui.miuihome2",
                "com.miui.mihome",
                "com.miui.mihome2",
                "com.i.miui.launcher"
        );
    }
}
