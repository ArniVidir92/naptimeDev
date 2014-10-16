package com.development.napptime.paydebt;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Napptime on 10/13/14.
 *
 * The message class serves as a way to create toast messages
 */
public class Message{
    // Displays a toast with the message message in the context context.
    public static void message(Context context, String message)
    {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
