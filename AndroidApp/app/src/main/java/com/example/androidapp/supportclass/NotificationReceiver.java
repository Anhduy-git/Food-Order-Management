package com.example.androidapp.supportclass;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.media.RingtoneManager;
import android.net.Uri;


import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.androidapp.R;
import com.example.androidapp.activity_fragment.activity.MainActivity;
import com.example.androidapp.activity_fragment.activity.SplashActivity;


public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int numOrderTomorrow = intent.getIntExtra("numOrderTomorrow", -1);
        if(numOrderTomorrow > 0) {

            Intent repeatIntent = new Intent(context, SplashActivity.class);
            //Open Upcoming fragment as default
            repeatIntent.putExtra("fragmentSelect", 1);
            repeatIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //Get logo
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.logo);
            //Get sound
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


            PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, repeatIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MainActivity.CHANNEL_ID)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.logo_small)
                    .setLargeIcon(bitmap)
                    .setSound(uri)
                    .setContentTitle("Thông báo")
                    .setContentText("Ngày mai bạn có" + numOrderTomorrow + " đơn hàng")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.notify(100, builder.build());
            }

        }
    }
}
