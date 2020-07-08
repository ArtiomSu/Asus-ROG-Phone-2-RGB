package terminal_heat_sink.asusrogphone2rgb;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.PopupMenu;


import terminal_heat_sink.asusrogphone2rgb.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private boolean fab_on = false;
    private boolean second_led_on = false;
    private String fab_on_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.fab_on";
    private String use_second_led_on_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.use_second_led";


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

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.i("MainActivity","android build model: "+ Build.MODEL+ " android build manufacturer: "+ Build.MANUFACTURER+" android build brand: "
                +Build.BRAND+" android build VERSION.RELEASE: "+Build.VERSION.RELEASE+" android build VERSION.SDK_INT: "+Build.VERSION.SDK_INT);

        if(!Build.MANUFACTURER.equals("asus")){
            Log.i("MainActivity","phone is not asus");
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Can't run this app")
                    .setMessage("Phone is not Asus")
                    .setCancelable(false)
                    .setPositiveButton("ok I'll buy an asus phone", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finishAndRemoveTask();
                        }
                    })
                    .show();

        }
        if(Build.VERSION.SDK_INT != 29){
            Log.i("MainActivity","phone is not running android 10");
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Can't run this app")
                    .setMessage("Android 10 is required to run this app")
                    .setCancelable(false)
                    .setPositiveButton("ok I'll update my phone sorry", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finishAndRemoveTask();
                        }
                    })
                    .show();

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("fab_on", fab_on);
    }
}