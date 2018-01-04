package com.github.redbadger.impl;

import android.content.ComponentName;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.github.redbadger.Badger;
import com.github.redbadger.RedBadgerException;

import java.util.Arrays;
import java.util.List;

/**
 * Created by zlove on 2018/1/2.
 */

public class HuaweiHomeBadger implements Badger {

    @Override
    public void executeBadge(Context context, ComponentName componentName, int badgeCount) throws RedBadgerException {
        if(context == null || componentName == null){
            return;
        }
        if(badgeCount < 0){
            badgeCount = 0;
        }
        Bundle localBundle = new Bundle();
        localBundle.putString("package", context.getPackageName());
        localBundle.putString("class", componentName.getClassName());
        localBundle.putInt("badgenumber", badgeCount);
        try {
            context.getContentResolver().call(Uri.parse("content://com.huawei.android.launcher.settings/badge/"), "change_badge", null, localBundle);
        } catch (Exception e){

        }
    }

    @Override
    public List<String> getSupportLaunchers() {
        return Arrays.asList(
                "com.huawei.android.launcher"
        );
    }
}
