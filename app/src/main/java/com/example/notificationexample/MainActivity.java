package com.example.notificationexample;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private NotificationManager notificationManager;
    private final static String CHANNEL_ID = "primary channel";
    private int NOTIFICATION_ID = 0;
    //Pemberian nama untuk receiver
    private final static String ACTION_UPDATE_NOTIF = "ACTION_UPDATE_NOTIF";
    private final static String ACTION_CANCEL_NOTIF = "ACTION_CANCEL_NOTIF";
    private  NotificationReceiver receiverRegister = new NotificationReceiver();

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
        findViewById(R.id.update_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Menambah gambar pada notification
                updateNotification();
            }
        });
        findViewById(R.id.Cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationManager.cancel(NOTIFICATION_ID);
            }
        });
        registerReceiver(receiverRegister, new IntentFilter(ACTION_UPDATE_NOTIF));
        registerReceiver(receiverRegister, new IntentFilter(ACTION_CANCEL_NOTIF));
    }

    private void updateNotification() {
        Bitmap androidImage = BitmapFactory.decodeResource(getResources(), R.drawable.mascot);
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        notifyBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(androidImage)
        .setBigContentTitle("Notification Updated!"));
        notificationManager.notify(NOTIFICATION_ID, notifyBuilder.build());
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
        Intent updateIntent = new Intent(ACTION_UPDATE_NOTIF);
        Intent cancerIntent = new Intent(ACTION_CANCEL_NOTIF);
        PendingIntent updatePendingIntent = PendingIntent.getBroadcast(this,
                NOTIFICATION_ID, updateIntent, PendingIntent.FLAG_IMMUTABLE);
        PendingIntent cancerPendingIntent = PendingIntent.getBroadcast(this,
                NOTIFICATION_ID, cancerIntent, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        //Penambahan Teks Untuk Notif
        notifyBuilder.addAction(R.drawable.ic_copyright_black_24dp, "Update Notification", updatePendingIntent);
        notifyBuilder.addAction(R.drawable.ic_copyright_black_24dp, "Delete", cancerPendingIntent);

        //Supaya bisa mengirim, kita butuh yang namanya notification id
        notificationManager.notify(NOTIFICATION_ID, notifyBuilder.build());
    }
    //Pembuatan receiver untuk pencetan dari notification
    //Jangan lupa tambahkan (register) di receiver
    public class NotificationReceiver extends BroadcastReceiver{

        public NotificationReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_UPDATE_NOTIF)){
                updateNotification();
            }
            else if (action.equals(ACTION_CANCEL_NOTIF)){
                notificationManager.cancel(NOTIFICATION_ID);
            }
        }
    }

    //Jangan lupa unregister receiver onDestroy, untuk formalitas
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiverRegister);
    }
}
