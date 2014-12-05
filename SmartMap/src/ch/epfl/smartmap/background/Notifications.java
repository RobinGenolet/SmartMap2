package ch.epfl.smartmap.background;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import ch.epfl.smartmap.R;
import ch.epfl.smartmap.activities.EventInformationActivity;
import ch.epfl.smartmap.activities.FriendsPagerActivity;
import ch.epfl.smartmap.activities.UserInformationActivity;
import ch.epfl.smartmap.cache.Cache;
import ch.epfl.smartmap.cache.Event;
import ch.epfl.smartmap.cache.ImmutableUser;
import ch.epfl.smartmap.cache.User;

/**
 * This class creates different sort of notifications
 * 
 * @author agpmilli
 */
public class Notifications {

    private static final int VIBRATE_NOTIFICATION_TIME = 500;

    private static final int SILENT_NOTIFICATION_TIME = 100;
    private final static long[] PATTERN = {0, VIBRATE_NOTIFICATION_TIME, SILENT_NOTIFICATION_TIME,
        VIBRATE_NOTIFICATION_TIME};
    private static long notificationID = 0;

    /**
     * Create an accepted friend invitation notification and notify it
     * 
     * @param view
     *            The current view
     * @param context
     *            The current activity
     * @param user
     *            The invited
     */
    public static void acceptedFriendNotification(Context context, ImmutableUser user) {

        // Get ID of notifications
        notificationID++;

        // Prepare intent that redirects the user to FriendActivity
        Intent friendsInfoIntent = new Intent(context, UserInformationActivity.class);
        friendsInfoIntent.putExtra("NOTIFICATION", true);
        friendsInfoIntent.putExtra("USER", user.getId());
        PendingIntent pFriendIntent =
            PendingIntent.getActivity(context, 0, friendsInfoIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Add Big View Specific Configuration
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        String[] events = new String[2];
        events[0] =
            new String(user.getName() + " " + context.getString(R.string.notification_invitation_accepted));
        events[1] = context.getString(R.string.notification_open_friend_list);

        // Sets a title for the Inbox style big view
        inboxStyle.setBigContentTitle(context.getString(R.string.notification_acceptedfriend_title));
        // Moves events into the big view
        for (int i = 0; i < events.length; i++) {
            inboxStyle.addLine(events[i]);
        }

        // Build notification
        NotificationCompat.Builder noti =
            new NotificationCompat.Builder(context)
                // Sets all notification's specifications in the builder
                .setStyle(inboxStyle)
                .setAutoCancel(true)
                .setContentTitle(context.getString(R.string.notification_acceptedfriend_title))
                .setContentText(
                    user.getName() + " " + context.getString(R.string.notification_invitation_accepted)
                        + "\n" + context.getString(R.string.notification_open_friend_list))
                .setSmallIcon(R.drawable.ic_launcher)
                .setTicker(
                    user.getName() + " " + context.getString(R.string.notification_invitation_accepted))
                .setContentIntent(pFriendIntent);

        Log.d("DEBUG-NOTIFICATION", "notificationVibrate : "
            + SettingsManager.getInstance().notificationsVibrate());
        if (SettingsManager.getInstance().notificationsVibrate()) {
            noti.setVibrate(PATTERN);
        } else {
            noti.setVibrate(null);
        }

        displayNotification(context, noti.build(), notificationID);

    }

    /**
     * Create an event invitation notification and notify it
     * 
     * @param view
     *            The current View
     * @param activity
     *            The current activity
     * @param event
     *            the Event
     */
    public static void newEventNotification(Context context, Event event) {

        // Get ID and the number of ongoing Event notifications
        notificationID++;

        // Prepare intent that redirect the user to EventActivity
        Intent showEventIntent = new Intent(context, EventInformationActivity.class);
        showEventIntent.putExtra("NOTIFICATION", true);
        showEventIntent.putExtra("EVENT", event.getId());
        // TODO mettre l'id de l'event
        PendingIntent pEventIntent =
            PendingIntent.getActivity(context, 0, showEventIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Add Big View Specific Configuration
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        String[] events = new String[2];
        events[0] =
            new String(Cache.getInstance().getFriendById(event.getCreatorId()).getName() + " "
                + context.getString(R.string.notification_event_invitation) + " " + event.getName());
        events[1] = context.getString(R.string.notification_open_event_list);

        // Sets a title for the Inbox style big view
        inboxStyle.setBigContentTitle(context.getString(R.string.notification_inviteevent_title));
        // Moves events into the big view
        for (int i = 0; i < events.length; i++) {
            inboxStyle.addLine(events[i]);
        }

        // Build notification
        NotificationCompat.Builder noti =
            new NotificationCompat.Builder(context)
                // Sets all notification's specifications in the builder
                .setStyle(inboxStyle)
                .setAutoCancel(true)
                .setContentTitle(context.getString(R.string.notification_inviteevent_title))
                .setContentText(
                    Cache.getInstance().getFriendById(event.getCreatorId()).getName() + " "
                        + context.getString(R.string.notification_event_invitation) + event.getName() + "\n"
                        + context.getString(R.string.notification_open_event_list))
                .setSmallIcon(R.drawable.ic_launcher)
                .setTicker(
                    Cache.getInstance().getFriendById(event.getCreatorId()).getName() + " "
                        + context.getString(R.string.notification_event_invitation) + event.getName())
                .setContentIntent(pEventIntent);
        if (SettingsManager.getInstance().notificationsVibrate()) {
            noti.setVibrate(PATTERN);
        } else {
            noti.setVibrate(null);
        }

        displayNotification(context, noti.build(), notificationID);
    }

    /**
     * Create a friend invitation notification and notify it
     * 
     * @param view
     *            The current view
     * @param context
     *            The current activity
     * @param user
     *            The inviter
     */
    public static void newFriendNotification(Context context, User user) {

        // Get ID and the number of ongoing Friend notifications
        notificationID++;

        // Prepare intent that redirects the user to FriendActivity
        Intent showFriendIntent = new Intent(context, FriendsPagerActivity.class);
        showFriendIntent.putExtra("INVITATION", true);
        showFriendIntent.putExtra("NOTIFICATION", true);
        PendingIntent pFriendIntent =
            PendingIntent.getActivity(context, 0, showFriendIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Add Big View Specific Configuration
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        String[] events = new String[2];
        events[0] = user.getName() + " " + context.getString(R.string.notification_friend_invitation);
        events[1] = context.getString(R.string.notification_open_friend_list);

        // Sets a title for the Inbox style big view
        inboxStyle.setBigContentTitle(context.getString(R.string.notification_invitefriend_title));
        // Moves events into the big view
        for (int i = 0; i < events.length; i++) {
            inboxStyle.addLine(events[i]);
        }

        // Build notification
        NotificationCompat.Builder noti =
            new NotificationCompat.Builder(context)
                // Add all notification's specifications in the builder
                .setStyle(inboxStyle)
                .setAutoCancel(true)
                .setContentIntent(pFriendIntent)
                .setSmallIcon(R.drawable.ic_launcher)
                .setTicker(user.getName() + " " + context.getString(R.string.notification_friend_invitation))
                .setContentTitle(context.getString(R.string.notification_invitefriend_title))
                .setContentText(
                    user.getName() + " " + context.getString(R.string.notification_friend_invitation) + "\n"
                        + context.getString(R.string.notification_open_friend_list));

        // TODO A FIX
        Log.d("DEBUG-NOTIFICATION", "notificationVibrate : "
            + SettingsManager.getInstance().notificationsVibrate());
        if (SettingsManager.getInstance().notificationsVibrate()) {
            noti.setVibrate(PATTERN);
        } else {
            noti.setVibrate(null);
        }

        displayNotification(context, noti.build(), notificationID);

    }

    /**
     * Build the notification and notify it with notification manager.
     * 
     * @param activity
     *            current activity
     * @param notification
     *            notification to notify
     * @param notificationId
     *            id of current notification
     */
    private static void displayNotification(Context context, Notification notification, long notificationId) {
        NotificationManager notificationManager =
            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) notificationId, notification);
    }
}