package com.github.redbadger;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.util.Log;

import com.github.redbadger.impl.AsusHomeBadger;
import com.github.redbadger.impl.DefaultBadger;
import com.github.redbadger.impl.HuaweiHomeBadger;
import com.github.redbadger.impl.NewHtcHomeBadger;
import com.github.redbadger.impl.OPPOHomeBader;
import com.github.redbadger.impl.SamsungHomeBadger;
import com.github.redbadger.impl.SonyHomeBadger;
import com.github.redbadger.impl.VivoHomeBadger;
import com.github.redbadger.impl.XiaomiHomeBadger;
import com.github.redbadger.impl.ZukHomeBadger;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by zlove on 2018/1/2.
 */

public class RedBadgerManager {
    private final static List<Class<? extends Badger>> BADGERS = new LinkedList<Class<? extends Badger>>();
    private Badger mRedBadger;
    private ComponentName mComponentName;
    private static volatile RedBadgerManager sInstance;

    static {
        BADGERS.add(NewHtcHomeBadger.class);
        BADGERS.add(SonyHomeBadger.class);
        BADGERS.add(XiaomiHomeBadger.class);
        BADGERS.add(AsusHomeBadger.class);
        BADGERS.add(HuaweiHomeBadger.class);
        BADGERS.add(OPPOHomeBader.class);
        BADGERS.add(VivoHomeBadger.class);
        BADGERS.add(ZukHomeBadger.class);
        BADGERS.add(SamsungHomeBadger.class);
    }

    private RedBadgerManager(){
    }

    public synchronized static RedBadgerManager inst() {
        if (sInstance == null) {
            synchronized (RedBadgerManager.class) {
                if (sInstance == null) {
                    sInstance = new RedBadgerManager();
                }
            }
        }
        return sInstance;
    }

    /**
     * Tries to update the notification count
     *
     * @param context    Caller context
     * @param badgeCount Desired badge count
     * @return true in case of success, false otherwise
     */
    public boolean applyCount(Context context, int badgeCount) {
        try {
            applyCountOrThrow(context, badgeCount);
            return true;
        } catch (RedBadgerException e) {
            Log.d("RedBadgerManager", "Unable to execute badge --- "+ e);
        }

        return false;
    }

    /**
     * Tries to update the notification count, throw a {@link RedBadgerException} if it fails
     *
     * @param context    Caller context
     * @param badgeCount Desired badge count
     */
    public void applyCountOrThrow(Context context, int badgeCount) throws RedBadgerException {
        if (mRedBadger == null) {
            boolean launcherReady = initBadger(context);
            if (!launcherReady)
                throw new RedBadgerException("No default launcher available");
        }

        try {
            mRedBadger.executeBadge(context, mComponentName, badgeCount);
        } catch (Exception e) {
            throw new RedBadgerException("Unable to execute badge", e);
        }
    }

    /**
     * Tries to remove the notification count
     *
     * @param context Caller context
     * @return true in case of success, false otherwise
     */
    public boolean removeCount(Context context) {
        return applyCount(context, 0);
    }

    /**
     * Tries to remove the notification count, throw a {@link RedBadgerException} if it fails
     *
     * @param context Caller context
     */
    public void removeCountOrThrow(Context context) throws RedBadgerException {
        applyCountOrThrow(context, 0);
    }

    // Initialize Badger if a launcher is availalble (eg. set as default on the device)
    // Returns true if a launcher is available, in this case, the Badger will be set and sShortcutBadger will be non null.
    private boolean initBadger(Context context) {
        boolean result = false;
        if (context == null) {
            return result;
        }
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        if (launchIntent == null) {
            Log.d("RedBadgerManager", "Unable to find launch intent for package " + context.getPackageName());
            return result;
        }

        mComponentName = launchIntent.getComponent();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        ResolveInfo resolveInfo = context.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);

        if (resolveInfo == null || resolveInfo.activityInfo == null || resolveInfo.activityInfo.name.toLowerCase().contains("resolver")) {
            return result;
        }
        String currentHomePackage = resolveInfo.activityInfo.packageName;
        for (Class<? extends Badger> badger : BADGERS) {
            Badger redBadger = null;
            try {
                redBadger = badger.newInstance();
            } catch (Throwable t) {
                t.printStackTrace();
            }
            if (redBadger != null && redBadger.getSupportLaunchers().contains(currentHomePackage)) {
                mRedBadger = redBadger;
                result = true;
                break;
            }
        }

        if (mRedBadger == null) {
            if (Build.MANUFACTURER.equalsIgnoreCase("OPPO")) {
                mRedBadger = new OPPOHomeBader();
            } else if (Build.MANUFACTURER.equalsIgnoreCase("VIVO")) {
                mRedBadger = new VivoHomeBadger();
            } else if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")) {
                mRedBadger = new XiaomiHomeBadger();
            } else if (Build.MANUFACTURER.equalsIgnoreCase("ZUK")) {
                mRedBadger = new ZukHomeBadger();
            } else {
                mRedBadger = new DefaultBadger();
            }
            result = true;
        }
        return result;
    }
}
