package terminal_heat_sink.asusrogphone2rgb;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.audiofx.Visualizer;
import android.os.IBinder;
import android.util.Log;

import androidx.core.graphics.ColorUtils;


public class VisualiserService extends Service {

    private Visualizer visualizer;
    private String visualiser_on_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.visualiser_on";
    private String visualiser_use_second_led_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.visualiser_use_second_led";
    private String visualiser_use_second_led_only_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.visualiser_use_second_led_only";
    private String visualiser_animation_mode_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.visualiser_animation_mode";

    //restoring prefs
    private String fab_on_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.fab_on";
    private String current_selected_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.current_selected";
    private String use_second_led_on_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.use_second_led";
    private String SAVED_PREFS_KEY_COLOR = "terminal_heat_sink.asusrogphone2rgb.saved_prefs_key_color";

    private String notification_animation_running_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.notification_animation_running_shared_preference_key";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i("VisualiserService","started");

        visualizer = new Visualizer(0);
        int[] captureSizeRange = Visualizer.getCaptureSizeRange();
        int captureSize = Math.max(128, captureSizeRange[0]);
        captureSize = Math.min(captureSize, captureSizeRange[1]);
        visualizer.setCaptureSize(captureSize);
        //Log.e("Visualiser","capture size "+captureSize);
        visualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
            @Override
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes, int i) {
                SharedPreferences prefs = getApplication().getApplicationContext().getSharedPreferences(
                        "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);

                boolean notification_running = prefs.getBoolean(notification_animation_running_shared_preference_key,false);
                if(!notification_running){
                    int color;

                    boolean use_second_led = prefs.getBoolean(visualiser_use_second_led_shared_preference_key,false);
                    boolean use_second_led_only = prefs.getBoolean(visualiser_use_second_led_only_shared_preference_key,false);
                    int animation_mode = prefs.getInt(visualiser_animation_mode_shared_preference_key,1);

                    if(animation_mode == 1){
                        color = Color.rgb(255,0,0);
                        color = color_algorithm1(color,bytes);
                    }
                    else {
                        color = prefs.getInt(SAVED_PREFS_KEY_COLOR,-1031);
                        color = color_algorithm2(color,bytes);
                    }

                    //Log.e("Visualiser","colors r"+Color.red(color)+"g"+Color.green(color)+"b"+Color.blue(color));
                    SystemWriter.notification_start(1,true,Color.red(color),Color.green(color),Color.blue(color),getApplication().getApplicationContext(),use_second_led,use_second_led_only);
                }


            }

            @Override
            public void onFftDataCapture(Visualizer visualizer, byte[] bytes, int i) {

            }
        },4096,true,false);
        visualizer.setEnabled(true);

    }

    private int color_algorithm1(int color, byte[] bytes){
        // -128 = 360 hue
        // 128 = 0 hue
        int first_byte = bytes[0];
        int hue = 0;
        if(first_byte < 0){
            hue = (int) (180 + (Math.abs(first_byte)*1.4));
        }else{
            hue = (int) (first_byte*1.4);
        }

        //Log.e("Visualiser","lightness "+hue+" byte "+first_byte);
        color = hue(color,hue);
        return color;
    };

    private int color_algorithm2(int color, byte[] bytes){
        // -128 = 360 hue
        // 128 = 0 hue
        int first_byte = bytes[0];
        int hue = 0;
        if(first_byte < 0){
            hue = 25 - (int) (Math.abs(first_byte)/5.12);
        }else{
            hue = 25 + (int) (first_byte/5.12);
        }

        //Log.e("Visualiser","lightness "+hue+" byte "+first_byte);
        color = lightness(color,hue);
        return color;
    };

    private int hue(int color, int hueFactor) {
        float[] hsl= new float[3];
        ColorUtils.colorToHSL(color,hsl);
        hsl[0]=hueFactor;
        //Log.e("Visualiser","hue "+hsl[0]);
        return ColorUtils.HSLToColor(hsl);
    }

    private int lightness(int color, int hueFactor) {
        float[] hsl= new float[3];
        ColorUtils.colorToHSL(color,hsl);
        hsl[2]=hueFactor/100f;
        //Log.e("Visualiser","lightness "+hsl[2]);
        return ColorUtils.HSLToColor(hsl);
    }

    @Override
    public void onDestroy() {

        Log.i("Visualiser Service","stopped");
        //turn off and restore
        visualizer.release();

        SharedPreferences prefs = getApplication().getApplicationContext().getSharedPreferences(
                "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);
        boolean on = prefs.getBoolean(fab_on_shared_preference_key,false);
        int animation = prefs.getInt(current_selected_shared_preference_key,0);
        boolean use_second_led = prefs.getBoolean(use_second_led_on_shared_preference_key,false);

        int color = prefs.getInt(SAVED_PREFS_KEY_COLOR,-1031);
        SystemWriter.notification_stop(!on,animation,true,Color.red(color),Color.green(color),Color.blue(color),getApplication().getApplicationContext(),use_second_led);
        stopSelf();
    }
}
