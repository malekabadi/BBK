package com.aseman.bbk;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import java.util.List;

public class Splash extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;
    public static  List<Section> sections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        new AsyncTask<Integer, Integer, Boolean>() {
            ProgressDialog progressDialog = null;

            @Override
            protected void onPreExecute() {
            }

            @Override
            protected Boolean doInBackground(Integer... params) {
                CallRest cr=new CallRest();
                sections = cr.GetSections("?perpage=1000");
                return  true;
            }

            protected void onPostExecute(Boolean result) {
            }
        }.execute();

        new Handler().postDelayed(new Runnable() {

			/*
			 * Showing splash screen with a timer. This will be useful when you
			 * want to show case your app logo / company
			 */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(Splash.this, MenuRight.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}