package com.socialcops.news;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;

public class MyNotificationPublisher extends BroadcastReceiver {
    ListView listNews;
    String filename = "SocialCopsNewsAPP";
    ProgressBar loader;
    FileOutputStream outputStream;
    FileInputStream inputStream;
    ObjectInputStream objectInputStream;
    ObjectOutputStream objectOutputStream;
    View v;
    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
    static final String NEWS_SOURCE = "name";
    static final String KEY_AUTHOR = "author";
    static final String KEY_TITLE = "title";
    static final String KEY_DESCRIPTION = "description";
    static final String KEY_URL = "url";
    static final String KEY_URLTOIMAGE = "urlToImage";
    static final String KEY_PUBLISHEDAT = "publishedAt";
    private static final String CHANNEL_ID = "com.socialcops.news.channelId";

    @Override
    public void onReceive(final Context context, Intent intent) {
        File file = new File(context.getFilesDir(), filename);
        ArrayList<HashMap<String, String>> dataList1 = null;
        try {
            inputStream = new FileInputStream(file);
            objectInputStream = new ObjectInputStream(inputStream);
            dataList1 = (ArrayList<HashMap<String, String>>) objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(context, MainActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.icon1)
                .setContentTitle(dataList1.get(0).get(KEY_TITLE))
                .setContentText(dataList1.get(0).get(KEY_DESCRIPTION))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(dataList1.get(0).get(KEY_DESCRIPTION)))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "NotificationDemo",
                    IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(100, builder.build());
    }
}