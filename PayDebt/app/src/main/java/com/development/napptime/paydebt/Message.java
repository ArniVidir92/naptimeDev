package com.development.napptime.paydebt;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Napptime on 10/13/14.
 */
public class Message{
    public static void message(Context context, String message)
    {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
