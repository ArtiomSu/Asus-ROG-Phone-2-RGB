package terminal_heat_sink.asusrogphone2rgb;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

public class NotificationSnoozeReceiver extends BroadcastReceiver {

    private String notifications_on_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.notifications_on";
    private String use_notifications_snooze_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.use_notifications_snooze_shared_preference_key";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            SharedPreferences prefs = context.getSharedPreferences(
                    "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);
            boolean notification_service_on = prefs.getBoolean(notifications_on_shared_preference_key,false);

            boolean snooze_on = prefs.getBoolean(use_notifications_snooze_shared_preference_key,false);

            boolean start = extras.getBoolean("start");
            Log.i("reciever","start: "+start+" notif on?: "+notification_service_on+" snooze_on?: "+snooze_on);
            if(start && notification_service_on && snooze_on){
                //stop the notification service
                Log.i("reciever","stopping notification service");
                prefs.edit().putBoolean(notifications_on_shared_preference_key, false).apply();
                SystemWriter.notification_access(false,context);

                Intent notification_intent = new Intent(context, NotificationService.class);
                context.stopService(notification_intent);

                SystemWriter.turn_on(false,context);


            }else if(snooze_on && !notification_service_on) {
                Log.i("reciever","starting notification service");
                SystemWriter.notification_access(true,context);

                prefs.edit().putBoolean(notifications_on_shared_preference_key, true).apply();
                SystemWriter.notification_access(true,context);

                Intent notification_intent = new Intent(context, NotificationService.class);
                context.getApplicationContext().startService(notification_intent);

            }else{
                Log.e("reciever","doesnt know what to do");
            }
        }else{
            Log.e("reciever","didnt get extras");
        }

    }
}
