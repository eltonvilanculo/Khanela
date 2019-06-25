package com.jeyme.khanela;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationService extends FirebaseMessagingService {
    private static final String CHANNEL_ID = "High";
    String TAG = getClass().getSimpleName();
    NotificationManager notificationManager;



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        final Map<String, String> data = remoteMessage.getData();


        //performNotification(remoteMessage);
        createNotification("Message",getApplicationContext(),data);




    }
    public void createNotification(String aMessage, Context context,Map<String, String> data) {
        final int NOTIFY_ID = 0; // ID of notification
        String id = CHANNEL_ID ;// default_channel_id
        String title = "Mensagem"; // Default Channel
        String msg = data.get("text");
        String senderId = data.get("senderId");

        Intent intent;
        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;
        if (notificationManager == null) {
            notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notificationManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, title, importance);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notificationManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(context, id);
            intent = new Intent(context, MainActivity.class); //intent para lr
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            builder.setContentTitle(aMessage)                            // required
                    .setSmallIcon(android.R.drawable.ic_popup_reminder)   // required
                    .setContentText(msg) // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker("Ola")
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        }
        else {
            builder = new NotificationCompat.Builder(getApplicationContext(), id);
            intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            builder.setContentTitle("Mensagem")                            // required
                    .setSmallIcon(android.R.drawable.ic_popup_reminder)   // required
                    .setContentText(msg) // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker("Ola")
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setPriority(Notification.PRIORITY_HIGH);
        }
        Notification notification = builder.build();
        if (senderId.equalsIgnoreCase(FirebaseAuth.getInstance().getUid())){
//            Toast.makeText(context, "Sent", Toast.LENGTH_SHORT).show();
        }else{
            notificationManager.notify(NOTIFY_ID, notification);
        }

    }

    private void performNotification(RemoteMessage remoteMessage) {
        final Map<String, String> data = remoteMessage.getData();

        Log.d(TAG, data.toString());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("High","Calling", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Calling");

        }else{
            createNotificationChannel();
        }

        if (data.isEmpty()){
            return;
        }

        long date = Long.parseLong(data.get("date").trim());

//        SimpleDateFormat format = new SimpleDateFormat("YYYY/MM/DD HH:mm");

        //  String dataInStrimg = format.format(new Date(date));

        // Log.d(TAG, dataInStrimg);

        Notification notification = new NotificationCompat.Builder(this,"High")
                .setSmallIcon(R.drawable.gender_icon)
                .setContentText(data.get("text"))
                .setContentText(data.get("text"))
                .setVibrate(new long [] {
                        100, 200, 300, 400, 500, 400, 300, 200, 400
                })
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        NotificationManagerCompat.from(this).notify(1,notification);
    }


    private void createNotificationChannel() {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = getString(R.string.channel_name);
                String description = getString(R.string.channel_description);
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                channel.setDescription(description);
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }

    }
}
