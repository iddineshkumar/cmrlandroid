package com.cm.cmrl.fcm;

/**
 * Created by Iddinesh.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import com.cm.cmrl.R;
import com.cm.cmrl.session.SessionManager;
import com.cm.cmrl.view.StationActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static androidx.core.app.NotificationCompat.*;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String type = "", messsgae = "";

    SessionManager sessionManager;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        sessionManager = new SessionManager(this);
        type = remoteMessage.getData().get("type");
        sendNotification(messsgae);
        messsgae = remoteMessage.getData().get("message");
    }
    // [END receive_message]
    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody) {

        Intent intent = new Intent(this, StationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Builder notificationBuilder = new Builder(this);

        int icon = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ?
                R.drawable.ic_touch_app_white_24dp : R.drawable.ic_touch_app_white_24dp;

        notificationBuilder.setSmallIcon(icon);
        notificationBuilder.setContentTitle("Reliefz");
        notificationBuilder.setContentText(messageBody);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setSound(defaultSoundUri);
        notificationBuilder.setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationBuilder.setColor(COLOR_DEFAULT);

        BigTextStyle style =
                new BigTextStyle(notificationBuilder);
        style.bigText(messageBody
        );

        assert notificationManager != null;
        notificationManager.notify(0 /* ID of notification */, style.build());
    }
}