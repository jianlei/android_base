package com.daren.base.util;

import android.content.Context;
import android.widget.Toast;

/**
 * toast tool class
 *
 * Created by daren on 14-8-20.
 */
public class ToastUtil {

    public static void show(Context context, CharSequence message) {
        ToastUtil.show(context, message, false);
    }

    public static void show(
            Context context, CharSequence message, boolean longDuration) {

        int duration = Toast.LENGTH_SHORT;

        if (longDuration) {
            duration = Toast.LENGTH_LONG;
        }

        Toast toast = Toast.makeText(context, message, duration);

        toast.show();
    }

    public static void show(Context context, int messageId) {
        ToastUtil.show(context, messageId, false);
    }

    public static void show(
            Context context, int messageId, boolean longDuration) {

        CharSequence text = context.getResources().getText(messageId);

        ToastUtil.show(context, text, longDuration);
    }
}
