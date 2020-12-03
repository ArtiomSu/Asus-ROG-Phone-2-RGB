package terminal_heat_sink.asusrogphone2rgb;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.Date;


public class ShakeService extends Service implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity
    private boolean light_on = false;
    private long previous_shake = 0L;
    private long last_time_on = 0L; // so that the light wont flicker on and off
    private boolean first_shake_passed = false;
    private long threshold = 300; //300ms

    private String current_selected_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.current_selected";
    private String SAVED_PREFS_KEY_COLOR = "terminal_heat_sink.asusrogphone2rgb.saved_prefs_key_color";
    private String fab_on_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.fab_on";



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer,
                SensorManager.SENSOR_DELAY_UI, new Handler());
        return START_STICKY;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        mAccelLast = mAccelCurrent;
        mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
        float delta = mAccelCurrent - mAccelLast;
        mAccel = mAccel * 0.9f + delta; // perform low-cut filter

        if (mAccel > 11) {

            if(previous_shake == 0L){
                previous_shake = new Date().getTime();
            }else{
                //we already have a previous date so lets get new date
                long current = new Date().getTime();
                //Log.e("shake_service","previous date: "+previous_shake+ " current date: "+ current+ " time passed: "+ (current-previous_shake));


                if((current-previous_shake) < threshold){
                        if(first_shake_passed){
                            toggle_leds(current);
                            first_shake_passed = false;

                        }else{
                            first_shake_passed = true;
                        }

                }else{
                    first_shake_passed = false;
                }



                previous_shake = current;

            }


        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("shake_service","shutting down "+"light on:"+light_on);
        SystemWriter.turn_on_second_led(false,getApplicationContext());
    }

    private void toggle_leds(long current){
        if((current - last_time_on) > 1000){
            light_on = !light_on;
            last_time_on = current;
            Log.i("shake_service","detected shake "+"light on:"+light_on+" previous_shake:"+previous_shake+" current_shake: "+current);

            SharedPreferences prefs = getApplicationContext().getSharedPreferences(
                    "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);

            if(light_on){
                int mode = prefs.getInt(current_selected_shared_preference_key,4);
                int color = prefs.getInt(SAVED_PREFS_KEY_COLOR, 0xFFFF8000);
                SystemWriter.write_animation(mode,getApplicationContext());
                SystemWriter.write_colour(Color.red(color),Color.green(color),Color.blue(color),getApplicationContext());
            }
            boolean main_logo = prefs.getBoolean(fab_on_shared_preference_key,false);
            SystemWriter.turn_on_second_led(light_on,getApplicationContext());
            SystemWriter.turn_on(main_logo,getApplicationContext());
        }
    }
}