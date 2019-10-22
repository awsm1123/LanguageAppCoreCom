package awsm.awsmizng.u.alanguageapp.helper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import java.net.ContentHandler;

import awsm.awsmizng.u.alanguageapp.R;
import awsm.awsmizng.u.alanguageapp.activities.Crossway;
import awsm.awsmizng.u.alanguageapp.activities.MainActivity;
import awsm.awsmizng.u.alanguageapp.activities.SplashScreen;

public class NotificationBuilder {
    private static final String UPLOAD_ARTICLES_CHANNEL = "upload_articles_channel";
    private static final int UPLOAD_ARTICLE_ID = 69690;
    private static final int PENDIN_ID = 7345;

    public static void uploadArticleNotification(Context context, String filename, String resultValue){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(
                    UPLOAD_ARTICLES_CHANNEL,
                    "Upload Articles",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, UPLOAD_ARTICLES_CHANNEL)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_parrot_svg)
                .setContentTitle("Uploading Article")
                .setContentText(filename)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(resultValue))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .setAutoCancel(true)
                ;

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN &&
        Build.VERSION.SDK_INT<Build.VERSION_CODES.O){
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }

        notificationManager.notify(UPLOAD_ARTICLE_ID, builder.build());
    }

    public static void uploadArticleNotificationProgress(Context context, String filename, String resultValue, int progress){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(
                    UPLOAD_ARTICLES_CHANNEL,
                    "Upload Articles",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, UPLOAD_ARTICLES_CHANNEL)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.settings_icon)
                .setContentTitle("Uploading Article")
                .setContentText(filename)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(resultValue))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .setAutoCancel(true)
                .setProgress(100, progress, false);
        ;

        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.O){
            builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        notificationManager.notify(UPLOAD_ARTICLE_ID, builder.build());
    }

    private static PendingIntent contentIntent(Context context){
        Intent intent = new Intent(context, Crossway.class);
        return PendingIntent.getActivity(
                context,
                PENDIN_ID,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
    }
}
