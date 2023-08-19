package terminal_heat_sink.asusrogphone2rgb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Startup extends AppCompatActivity {

    private final String isphone_rog3_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.isrog3";
    private static String is_root_mode_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.isrootmode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        SharedPreferences prefs = getApplicationContext().getSharedPreferences(
                "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);

        Button rog2 = findViewById(R.id.button_select_rog2);

        prefs.edit().putBoolean(is_root_mode_shared_preference_key, true).apply();

        rog2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.putExtra("PHONE","2");
                prefs.edit().putString(isphone_rog3_shared_preference_key,"2").apply();
                //setResult(404,intent);
                //finish();
                exit_and_get_root(prefs);
            }
        });

        Button rog3 = findViewById(R.id.button_select_rog3);

        rog3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.putExtra("PHONE","3");
                prefs.edit().putString(isphone_rog3_shared_preference_key,"3").apply();
                //setResult(404,intent);
                //finish();
                exit_and_get_root(prefs);
            }
        });

        //Button rootMode = findViewById(R.id.button_select_root);
        //Button noRootMode = findViewById(R.id.button_select_noroot);

//        rootMode.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                prefs.edit().putBoolean(is_root_mode_shared_preference_key,true).apply();
//
//                rootMode.setBackground(getResources().getDrawable(R.drawable.button_green));
//                rootMode.setTextColor(getResources().getColor(R.color.colorON));
//
//                noRootMode.setBackground(getResources().getDrawable(R.drawable.button_red));
//                noRootMode.setTextColor(getResources().getColor(R.color.colorOFF));
//            }
//        });


//        noRootMode.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                prefs.edit().putBoolean(is_root_mode_shared_preference_key,false).apply();
//                noRootMode.setBackground(getResources().getDrawable(R.drawable.button_green));
//                noRootMode.setTextColor(getResources().getColor(R.color.colorON));
//
//                rootMode.setBackground(getResources().getDrawable(R.drawable.button_red));
//                rootMode.setTextColor(getResources().getColor(R.color.colorOFF));
//            }
//        });

    }

    private void exit_and_get_root(SharedPreferences prefs){
        if(prefs.getBoolean(is_root_mode_shared_preference_key, false)){
            SystemWriter.turn_off_magisk_notifications(getApplicationContext());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }


        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
        Intent i = getApplicationContext().getPackageManager().
                getLaunchIntentForPackage(getApplicationContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        System.exit(0);
    }
}