package com.example.notificationexample;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private NotificationManager notificationManager;
    private final static String CHANNEL_ID = "primary channel";
    private int NOTIFICATION_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            //Importance kepentingan notif, HIGH (POPUP dan muncul di manager)
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "ap norif",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        findViewById(R.id.notify_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
            }
        });
    }
    //Untuk set title, icon, content
    private NotificationCompat.Builder getNotificationBuilder(){

        Intent noificationIntent =  new Intent(this, Main2Activity.class);
        PendingIntent notificationpendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID,
                noificationIntent, PendingIntent.FLAG_IMMUTABLE);


        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("HP ANDA TERHEK")
                .setContentText("CEPAT SERAHKAN HP ANDA KE SAYA!")
                .setSmallIcon(R.drawable.ic_copyright_black_24dp)
                .setContentIntent(notificationpendingIntent);
        return notifyBuilder;
    }

    private void sendNotification(){
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        //Supaya bisa mengirim, kita butuh yang namanya notification id
        notificationManager.notify(NOTIFICATION_ID, notifyBuilder.build());
    }
}
