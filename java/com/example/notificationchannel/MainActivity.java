package com.example.notificationchannel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.security.PrivateKey;

public class MainActivity extends AppCompatActivity {
private Button NotifyMe,buttonCancel,buttonUpdate;
private static  final String  PRIMARY_CHANNEL_ID ="primary_notifications_channel";
private NotificationManager notificationManager;
private static final int NOTIFICATION_ID =0;
    private static final String ACTION_UPDATE_NOTIFICATION = BuildConfig.APPLICATION_ID + ".ACTION_UPDATE_NOTIFICATION";
private NotificationReceiver notificationReceiver = new NotificationReceiver();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(notificationReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NotifyMe = (Button)findViewById(R.id.notify);
        registerReceiver(notificationReceiver,new IntentFilter(ACTION_UPDATE_NOTIFICATION));
        createNotificationChannel();
        NotifyMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNotification();
            }
        });
        buttonCancel = (Button)findViewById(R.id.cancel);
        buttonUpdate = (Button)findViewById(R.id.update);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
cancelNotification();
            }
        });
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
updateNotification();
            }
        });
    }

    public void updateNotification() {
        Bitmap androidImage = BitmapFactory.decodeResource(getResources(),R.drawable.dhrysanthemum);

        NotificationCompat.Builder builder = getNotificationBuilder();
        builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(androidImage).setBigContentTitle("Notification Updated"));
        notificationManager.notify(NOTIFICATION_ID,builder.build());
        NotifyMe.setEnabled(false);
        buttonUpdate.setEnabled(false);
        buttonCancel.setEnabled(true);
    }

    public void cancelNotification() {
        notificationManager.cancel(NOTIFICATION_ID);
        NotifyMe.setEnabled(true);
        buttonCancel.setEnabled(false);
        buttonUpdate.setEnabled(false);
    }

    public void sendNotification(){
        Intent intent = new Intent(ACTION_UPDATE_NOTIFICATION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,NOTIFICATION_ID,intent,PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder = getNotificationBuilder();
        builder.addAction(R.drawable.update,"Update Notification",pendingIntent);
notificationManager.notify(NOTIFICATION_ID,builder.build());
        buttonCancel.setEnabled(true);
        buttonUpdate.setEnabled(true);
    }
    public void createNotificationChannel(){
        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID,"Mascot Notification",NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.setDescription("Notification From Mascot");
            notificationManager.createNotificationChannel(notificationChannel);
        }




    }
    private NotificationCompat.Builder getNotificationBuilder(){
        Intent intent = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,NOTIFICATION_ID,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,PRIMARY_CHANNEL_ID)
                .setContentTitle("You have been notified")
                .setContentText("This is your notification text")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.notification);
        return notificationBuilder;
    }
    public class NotificationReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
updateNotification();
        }

    }
}