package com.iteso.marco.session11_2;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.support.v4.app.NotificationCompat.Builder;

/**
 * Created by marco on 4/14/15.
 */
public class SchedulingService extends IntentService
{
    public static final String TAG = "SCHEDULING DEMO";
    public static final int NOTIFICATION_ID = 1;
    public static final String SEARCH_STRING = "doodle";
    public static final String SEARCH_URL = "http://www.google.com";
    private NotificationManager mNotificationManager;
    Builder builder;

    @Override
    protected void onHandleIntent(Intent intent)
    {
        String urlString  = SEARCH_URL;
        String result = "";

        try
        {
            result = loadFromNetwork(urlString);
        }
        catch (IOException ex)
        {
            Log.i(TAG, getString(R.string.connection_error));
        }

        if(result.indexOf(SEARCH_STRING) != -1)
        {
            SendNotification(getString(R.string.doodle_found));
        }
        else
        {
            SendNotification(getString(R.string.no_doodle));
        }

        AlarmReceiver.completeWakefulIntent(intent);
    }

    public SchedulingService()
    {
        super("Scheduling Service");
    }

    private InputStream downloadUrl(String urlString) throws IOException
    {
        InputStream stream = null;
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);

        conn.connect();
        stream = conn.getInputStream();

        return stream;
    }

    private String readIt(InputStream stream) throws IOException
    {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        for (String line = reader.readLine(); line != null; line = reader.readLine())
        {
            sb.append(line);
        }
        reader.close();
        return sb.toString();
    }

    private String loadFromNetwork(String urlString) throws IOException
    {
        InputStream stream = null;
        String str = "";
        try
        {
            stream = downloadUrl(urlString);
            str = readIt(stream);
        }
        finally {
            if(stream != null) stream.close();
        }
        return str;
    }

    private void SendNotification(String msg)
    {
        mNotificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        //contentIntent = new PendingIntent.getActivity(null, 0, new )

        Builder mBuilder = new Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(getString(R.string.doodle_alert))
                .setStyle(new NotificationCompat.BigTextStyle()
                .bigText(msg))
                .setContentText(msg);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
