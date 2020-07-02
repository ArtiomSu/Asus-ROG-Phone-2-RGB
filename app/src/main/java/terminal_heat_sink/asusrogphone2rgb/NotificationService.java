package terminal_heat_sink.asusrogphone2rgb;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.Set;

public class NotificationService extends NotificationListenerService {
    private String notifications_on_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.notifications_on";
    private String notifications_animation_on_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.notifications_animation";
    private String fab_on_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.fab_on";
    private String current_selected_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.current_selected";
    private String notifications_second_led_on_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.notifications_second_led";
    private String use_second_led_on_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.use_second_led";
    private String use_notifications_second_led_only_on_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.notifications_second_led_use_only";
    private String apps_selected_for_notifications_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.notifications_apps_selected";


    private String latest_notification = "";

    Context context ;


    @Override
    public void onCreate () {



        super.onCreate() ;
        context = getApplicationContext() ;

        SharedPreferences prefs = context.getSharedPreferences(
                "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);
        boolean test = prefs.getBoolean(notifications_on_shared_preference_key,false);
        if(test){
            String NOTIFICATION_CHANNEL_ID = "terminal_heat_sink.asusrogphone2rgb";
            String channelName = "Notification Service";
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
            chan.setLightColor(Color.RED);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);

            Intent notificationIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(context, 0, notificationIntent, 0);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
            Notification notification = notificationBuilder.setOngoing(true)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("Notification service running")
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationManager.IMPORTANCE_MIN)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .build();

            Log.i( "AsusRogPhone2RGBNotificationService" , "Creating Service Notification");


            startForeground(2, notification);
        }else {
            stopForeground(true);
            stopSelf();
        }

    }
    @Override
    public void onNotificationPosted (StatusBarNotification sbn) {
        handle_notification(true, sbn.getPackageName());
    }
    @Override
    public void onNotificationRemoved (StatusBarNotification sbn) {
        handle_notification(false, sbn.getPackageName());
    }

    private void handle_notification(boolean added, String package_name){

        SharedPreferences prefs = context.getSharedPreferences(
                "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);
        boolean test = prefs.getBoolean(notifications_on_shared_preference_key,false);
        int animation = prefs.getInt(notifications_animation_on_shared_preference_key, 1);

        Log. i ( "AsusRogPhone2RGBNotificationService" , "adding notification:"+added+" notifications_enabled:"+test+" package name:"+package_name);

        if(!test){
            stopForeground(true);
            stopSelf();
        }else{

            Set<String> apps_to_notify = prefs.getStringSet(apps_selected_for_notifications_shared_preference_key,null);
            if(apps_to_notify != null){
                if(apps_to_notify.contains(package_name)){
                    send_notification(package_name,added,animation);
                }
            }

        }


    }

    private void send_notification(String name, boolean added, int mode){

        SharedPreferences prefs = context.getSharedPreferences(
                "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);


        if(added && (!latest_notification.equals(name))){ //if different notification is added.
            latest_notification = name;
            boolean use_second_led_for_notification = prefs.getBoolean(notifications_second_led_on_shared_preference_key,false);
            boolean use_second_led_only = prefs.getBoolean(use_notifications_second_led_only_on_shared_preference_key,false);
            Log. i ( "AsusRogPhone2RGBNotificationService" , "stating to notify for "+latest_notification+" use second led:"+use_second_led_for_notification+" use second led only:"+use_second_led_only);


            if(mode >= 20){ //using custom animations
                mode = 8; // thunder rainbow
            }else{

            }
            SystemWriter.notification_start(mode,false,0,0,0,context,use_second_led_for_notification,use_second_led_only);


        }else if( !added && latest_notification.equals(name)){ // restore to previous config
            Log. i ( "AsusRogPhone2RGBNotificationService" , "stopping notifications because this is cleared "+latest_notification);


            boolean on = prefs.getBoolean(fab_on_shared_preference_key,false);
            int animation = prefs.getInt(current_selected_shared_preference_key,0);
            boolean use_second_led = prefs.getBoolean(use_second_led_on_shared_preference_key,false);



            if(mode >= 20){ //using custom animations so might need to restore colour
                SystemWriter.notification_stop(!on,animation,false,0,0,0,context,use_second_led);
            }else{
                SystemWriter.notification_stop(!on,animation,false,0,0,0,context,use_second_led);
            }

            latest_notification = "";
        }


    }

    //gets called when the notification access is removed from settings
    @Override
    public void onDestroy() {
        Log.i("AsusRogPhone2RGBNotificationService", "onCreate() , service stopped...");
        stopForeground(true);
        stopSelf();
        super.onDestroy();

    }


}