package com.github.redbadger.impl;

import android.content.ComponentName;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.github.redbadger.Badger;
import com.github.redbadger.RedBadgerException;

import java.util.Collections;
import java.util.List;

/**
 * Created by zlove on 2018/1/3.
 */

public class ZukHomeBadger implements Badger {

    private final Uri CONTENT_URI = Uri.parse("content://com.android.badge/badge");

    @Override
    public void executeBadge(Context context, ComponentName componentName, int badgeCount) throws RedBadgerException {
        if(context == null || componentName == null){
            return;
        }
        if(badgeCount < 0){
            badgeCount = 0;
        }
        try {
            Bundle extra = new Bundle();
            extra.putInt("app_badge_count", badgeCount);
            context.getContentResolver().call(CONTENT_URI, "setAppBadgeCount", null, extra);
        } catch (Throwable t) {
            // ignore
            t.printStackTrace();
            throw new RedBadgerException(t.getMessage());
        }
    }

    @Override
    public List<String> getSupportLaunchers() {
        return Collections.singletonList("com.zui.launcher");
    }
}
