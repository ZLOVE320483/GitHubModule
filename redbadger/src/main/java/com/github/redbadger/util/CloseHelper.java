package com.github.redbadger.util;

import android.database.Cursor;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by zlove on 2018/1/2.
 */

public class CloseHelper {

    public static void close(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }


    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException var2) {

        }
    }
}
