package terminal_heat_sink.asusrogphone2rgb;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import terminal_heat_sink.asusrogphone2rgb.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private boolean fab_on = false;
    private boolean second_led_on = false;
    private final String fab_on_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.fab_on";
    private final String use_second_led_on_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.use_second_led";
    private final String isphone_rog3_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.isrog3";

    //notification animation running?.
    private final String notification_animation_running_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.notification_animation_running_shared_preference_key";

    //check if magisk mode
    private final String magisk_mode_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.magiskmode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);
        FloatingActionButton fab_second_led = findViewById(R.id.fab_second_led);

        final Context context = getApplicationContext();




        SharedPreferences prefs = context.getSharedPreferences(
                "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);

        fab_on = prefs.getBoolean(fab_on_shared_preference_key,false);
        if(!fab_on){
            if(savedInstanceState != null){
                fab_on = savedInstanceState.getBoolean("fab_on");
            }
        }


        if(fab_on){
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorON) ));
        }else{
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorBG) ));
//            if(! prefs.getBoolean(notification_animation_running_shared_preference_key,false) && prefs.getString(isphone_rog3_shared_preference_key," ").charAt(0) == '3'){
//                Log.i("startup","resetting driver for rog 3");
//                SystemWriter.rog_3_crap(context);
//            }
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
                if(fab_on){
                    fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorBG) ));
                    SystemWriter.turn_on(false,getApplicationContext());
                    fab_on = false;
                }else{
                    fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorON) ));
                    SystemWriter.turn_on(true,getApplicationContext());
                    fab_on = true;
                }
                SharedPreferences prefs = context.getSharedPreferences(
                        "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);
                prefs.edit().putBoolean(fab_on_shared_preference_key, fab_on).apply();

            }
        });


        second_led_on = prefs.getBoolean(use_second_led_on_shared_preference_key,false);
        if(second_led_on){
            fab_second_led.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorON) ));
        }else{
            fab_second_led.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorBG) ));
        }

        fab_second_led.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_second_led);
                if(second_led_on){
                    fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorBG) ));
                    SystemWriter.turn_on_second_led(false,getApplicationContext());
                    second_led_on = false;
                }else{
                    fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorON) ));
                    SystemWriter.turn_on_second_led(true,getApplicationContext());
                    second_led_on = true;
                }
                SharedPreferences prefs = context.getSharedPreferences(
                        "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);
                prefs.edit().putBoolean(use_second_led_on_shared_preference_key, second_led_on).apply();
            }
        });

    }

    boolean checkSdk(Context context, SharedPreferences prefs){
        if(Build.VERSION.SDK_INT < 29){
            Log.i("MainActivity","phone is not running android 10+");
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Warning")
                    .setMessage("Android 10 or higher is required to run this app. App will not function properly on android 9")
                    .setCancelable(false)
                    .setPositiveButton("I accept the risk", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            launchPhonePicker(context, prefs);
                        }
                    })
                    .setNegativeButton("Exit App", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //finishAndRemoveTask();
                            SystemWriter.uninstall_self(context);
                        }
                    })
                    .show();
            return false;
        }else {
            return true;
        }
    }

    private void launchPhonePicker(Context context, SharedPreferences prefs){
        String phone = prefs.getString(isphone_rog3_shared_preference_key," ");
        if(phone.equals(" ")){
            Intent app_selector = new Intent(context, Startup.class);
            startActivityForResult(app_selector, 404);
        }

        //check if magisk mode is present
        if(SystemWriter.check_if_system_app(context).equals("terminal_heat_sink.asusrogphone2rgb\n")){
            Log.i("MainActivity", " running in magisk mode");
            prefs.edit().putBoolean(magisk_mode_shared_preference_key, true).apply();
        }else{
            prefs.edit().putBoolean(magisk_mode_shared_preference_key, false).apply();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        final Context context = getApplicationContext();

        SharedPreferences prefs = context.getSharedPreferences(
                "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);

        Log.i("MainActivity","android build model: "+ Build.MODEL+ " android build manufacturer: "+ Build.MANUFACTURER+" android build brand: "
                +Build.BRAND+" android build VERSION.RELEASE: "+Build.VERSION.RELEASE+" android build VERSION.SDK_INT: "+Build.VERSION.SDK_INT+ " android build device: "+ Build.DEVICE
                +" android build product: "+Build.PRODUCT+" android build hardware: "+Build.HARDWARE);

        if(!Build.MANUFACTURER.equals("asus")){
            Log.i("MainActivity","phone is not asus");
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Warning")
                    .setMessage("This App is made to work only with Asus Rog phone 2 and 3. Usage on other devices may damage the device. You have been warned.")
                    .setCancelable(false)
                    .setPositiveButton("I accept the risk", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            checkSdk(context, prefs);


                        }
                    })
                    .setNegativeButton("Exit App", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //finishAndRemoveTask();
                            SystemWriter.uninstall_self(context);
                        }
                    })
                    .show();

        }else if(!checkSdk(context,prefs)){

        }else{
            launchPhonePicker(context, prefs);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("fab_on", fab_on);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==404)
        {
            SharedPreferences prefs = getApplicationContext().getSharedPreferences(
                    "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);
            String message=data.getStringExtra("PHONE");
            Log.i("startup","selected rog "+message+"");
            prefs.edit().putString(isphone_rog3_shared_preference_key,message).apply();
            if(message.charAt(0) == '3'){
                Log.i("startup","preparing driver for rog 3");
                SystemWriter.rog_3_crap(getApplicationContext());
            }
            SystemWriter.permissions(getApplicationContext());
            SystemWriter.turn_off_magisk_notifications(getApplicationContext());
        }
    }
}