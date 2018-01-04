package com.github.redbadger;

/**
 * Created by zlove on 2018/1/4.
 */

public class RedBadgerException extends Exception {

    public RedBadgerException(String message) {
        super(message);
    }

    public RedBadgerException(String message, Exception e) {
        super(message, e);
    }
}
