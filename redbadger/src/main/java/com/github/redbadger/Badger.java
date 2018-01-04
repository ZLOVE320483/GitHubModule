package com.github.redbadger;

import android.content.ComponentName;
import android.content.Context;

import java.util.List;

/**
 * Created by zlove on 2018/1/4.
 */

public interface Badger {
    /**
     * Called when user attempts to update notification count
     * @param context Caller context
     * @param componentName Component containing package and class name of calling application's
     *                      launcher activity
     * @param badgeCount Desired notification count
     * @throws RedBadgerException
     */
    void executeBadge(Context context, ComponentName componentName, int badgeCount) throws RedBadgerException;

    /**
     * Called to let {@link RedBadgerManager} knows which launchers are supported by this badger. It should return a
     * @return List containing supported launchers package names
     */
    List<String> getSupportLaunchers();
}
