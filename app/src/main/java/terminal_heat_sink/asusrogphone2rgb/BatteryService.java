package terminal_heat_sink.asusrogphone2rgb;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.IBinder;
import android.util.Log;
import androidx.core.graphics.ColorUtils;

public class BatteryService extends Service {

    private String battery_charging_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.battery_charging";
    private String battery_animate_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.battery_animate";
    private String battery_use_second_led_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.battery_use_second_led";
    private String battery_use_second_led_only_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.battery_use_second_led_only";
    private String battery_animation_mode_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.battery_animation_mode";

    private String notification_animation_running_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.notification_animation_running_shared_preference_key";

    //restoring prefs
    private String fab_on_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.fab_on";
    private String current_selected_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.current_selected";
    private String use_second_led_on_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.use_second_led";
    private String SAVED_PREFS_KEY_COLOR = "terminal_heat_sink.asusrogphone2rgb.saved_prefs_key_color";


    private BroadcastReceiver mBatteryStateReceiver = new BroadcastReceiver() {

        private int hue(int color, int hueFactor) {
            float[] hsl= new float[3];
            ColorUtils.colorToHSL(color,hsl);
            hsl[0]=hueFactor;
            return ColorUtils.HSLToColor(hsl);
        }
        @Override
        public void onReceive(Context context, Intent intent) {

            SharedPreferences prefs = context.getSharedPreferences(
                    "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);
            boolean notification_running = prefs.getBoolean(notification_animation_running_shared_preference_key,false);

            switch (intent.getAction()){
                case Intent.ACTION_POWER_CONNECTED:
                    prefs.edit().putBoolean(battery_charging_shared_preference_key,true).apply();
                    Log.i("BatteryReceiver", "battery is charging");
                    break;
                case Intent.ACTION_POWER_DISCONNECTED:
                    prefs.edit().putBoolean(battery_charging_shared_preference_key,false).apply();
                    if(!notification_running){
                        Log.i("BatteryReceiver", "battery not charging restoring leds");

                        //turn off and restore
                        boolean on = prefs.getBoolean(fab_on_shared_preference_key,false);
                        int animation = prefs.getInt(current_selected_shared_preference_key,0);
                        boolean use_second_led = prefs.getBoolean(use_second_led_on_shared_preference_key,false);

                        int color = prefs.getInt(SAVED_PREFS_KEY_COLOR,-1031);
                        SystemWriter.notification_stop(!on,animation,true,Color.red(color),Color.green(color),Color.blue(color),context,use_second_led);

                    }else{
                        Log.i("BatteryReceiver", "battery not charging notifications are running so nothing will happen");
                    }
                    break;
                case Intent.ACTION_BATTERY_CHANGED:
                    boolean animate_charge = prefs.getBoolean(battery_animate_shared_preference_key,false);
                    if(animate_charge){
                        boolean battery_charging = prefs.getBoolean(battery_charging_shared_preference_key,false);
                        if(battery_charging){
                            if(!notification_running){
                                boolean use_second_led = prefs.getBoolean(battery_use_second_led_shared_preference_key,false);
                                boolean use_second_led_only = prefs.getBoolean(battery_use_second_led_only_shared_preference_key,false);
                                int animation_mode = prefs.getInt(battery_animation_mode_shared_preference_key,1);
                                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);
                                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE,-1);
                                float batteryp = level * 100 / (float)scale;
                                int color = Color.rgb(255,0,0);
                                // 0% battery = 0 hue
                                // 100% battery = 130 hue
                                batteryp = batteryp*1.5f-20f;
                                if(batteryp<0){
                                    batteryp = 0;
                                }
                                color = hue(color,(int)batteryp);
                                SystemWriter.notification_start(animation_mode,true,Color.red(color),Color.green(color),Color.blue(color),context,use_second_led,use_second_led_only);

                                Log.i("BatteryReceiver", "battery changed"+batteryp+" level "+ level+ " scale"+scale+ " color r"+Color.red(color)+"g"+Color.green(color)+"b"+Color.blue(color));
                            }

                        }

                    }

                    break;
                default:
                    break;
            }

        }
    };


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        IntentFilter ifilter = new IntentFilter();
        ifilter.addAction(Intent.ACTION_POWER_CONNECTED);
        ifilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        ifilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(mBatteryStateReceiver, ifilter);
        Log.i("BatteryService","started");

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Log.i("LocalService", "Received start id " + startId + ": " + intent);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mBatteryStateReceiver);
        Log.i("BatteryService","stopped");
        //turn off and restore
        SharedPreferences prefs = getApplication().getApplicationContext().getSharedPreferences(
                "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);
        boolean on = prefs.getBoolean(fab_on_shared_preference_key,false);
        int animation = prefs.getInt(current_selected_shared_preference_key,0);
        boolean use_second_led = prefs.getBoolean(use_second_led_on_shared_preference_key,false);

        int color = prefs.getInt(SAVED_PREFS_KEY_COLOR,-1031);
        SystemWriter.notification_stop(!on,animation,true,Color.red(color),Color.green(color),Color.blue(color),getApplication().getApplicationContext(),use_second_led);
    }



}
