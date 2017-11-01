package com.sgnmkj.sportsquiz.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by bharat.l on 10/31/2017.
 */

public class values {
    private static NetworkInfo networkInfo;
    private static ConnectivityManager connectivityManager;

    public static String options[][];
    public static String questions[];
    public static int index_correct[];
    public static int quiz_count;

    public static int question_display=0;
    public static int option_clicked[];

    public static int answer_clicked[];

    public static boolean options_check[][];

    public static boolean isInternet(Context context) {
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable();
    }
}
