package terminal_heat_sink.asusrogphone2rgb;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;

import top.defaults.colorpicker.ColorObserver;
import top.defaults.colorpicker.ColorPickerView;

public class PerAppCustomisations extends AppCompatActivity {

    private String[][] animation_options = {
            {"0","dafault"},
            {"1","solid one colour"},
            {"2","breathing one colour"},
            {"3","blink"},
            {"4","rainbow"},
            {"5","another rainbow?"},
            {"6","rainbow breathe"},
            {"7","somekind of thunder"},
            {"8","thunder rainbow"},
            {"9","quick two flashes"},
            {"10","quick two flashes rainbow"},
            {"11","breathe rainbow"},
            {"12","some strange breathe rainbow"},
            {"13","slow glitchy rainbow"}
    };

    private int[] show_colour = {1, 2, 3, 7, 9};

    private int current_selected = 0;
    private ColorPickerView colorPickerView;
    private String package_name = null;
    private String package_color_preference_pretext = "sharedPreferencePerAppColor";
    private String package_animation_preference_pretext = "sharedPreferencePerAppAnimationMode";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.per_app_customisation_activity);

        TextView header = (TextView) findViewById(R.id.text_view_per_app_customisations);
        header.setTextColor(getResources().getColor(R.color.colorText));
        header.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25f);
        header.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        header.setPadding(10,10,10,10);
        header.setGravity(Gravity.CENTER);
        package_name = getIntent().getStringExtra("package_name");
        if(package_name != null){
            header.setText("custom notification options for \n"+package_name);
        }else{
            Toast toast = Toast.makeText(getApplicationContext(), "package name was not passed to activity for some reason call the police", Toast.LENGTH_LONG);
            toast.show();
            finish();
        }

        LinearLayout ll = (LinearLayout) findViewById(R.id.per_app_customisations_ll);
        ll.setPadding(10,10,10,10);

        LinearLayout ll_colour = (LinearLayout) findViewById(R.id.per_app_customisations_color_ll);
        ll_colour.setPadding(10,10,10,10);
        create_colour_picker(ll_colour);
        colorPickerView.setVisibility(View.GONE);
        create_animation_switches(ll);




    }

    private void create_colour_picker(LinearLayout ll){
        colorPickerView = new ColorPickerView(getApplicationContext());

        ll.addView(colorPickerView);

        colorPickerView.setEnabledAlpha(false);
        colorPickerView.setEnabledBrightness(false);

        colorPickerView.subscribe(new ColorObserver() {
            @Override
            public void onColor(int color, boolean fromUser, boolean shouldPropagate) {
                SharedPreferences prefs = getApplicationContext().getSharedPreferences(
                        "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);

                //check if the same colour is already applied
                int previous_color = prefs.getInt(package_color_preference_pretext+package_name,0);
                if(previous_color != color || previous_color == 0){
                    prefs.edit().putInt(package_color_preference_pretext+package_name, color).apply();
                    //Log.i("PerAppCustomisations",package_name+": color picked "+color + " RGB is "+ Color.red(color)+ " " + Color.green(color)+ " "+ Color.blue(color));
                }

            }
        });

        SharedPreferences prefs = getApplicationContext().getSharedPreferences(
                "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);

        int color = prefs.getInt(package_color_preference_pretext+package_name,0);
        if(color == 0){
            color = -1031;
        }

        colorPickerView.setInitialColor(color);
    }

    private void create_animation_switches(LinearLayout animations_linear_layout){

        final CheckBox[] switches = new CheckBox[animation_options.length];
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(
                "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);
        current_selected = prefs.getInt(package_animation_preference_pretext+package_name,0);

        int states[][] = {{android.R.attr.state_checked}, {}};
        int colors[] = {getResources().getColor(R.color.colorON), getResources().getColor(R.color.colorDisabled)};


        for (int i = 0; i < animation_options.length; i++) {

            CheckBox sw = new CheckBox(getApplicationContext());
            //sw.setThumbDrawable(getResources().getDrawable(R.drawable.asus_rog_logo_scaled));
            sw.setButtonTintList(new ColorStateList(states,colors));
            sw.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            sw.setButtonDrawable(R.drawable.asus_rog_logo_scaled);
            sw.setPadding(0,0,0,10);
            sw.setId(i);
            final int id_ = sw.getId();
            if(id_ == current_selected){
                sw.setChecked(true);
                //sw.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorON)));
                //sw.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorThumbOn)));
                sw.setTextColor(getResources().getColor(R.color.colorON));

                for(int b=0;b<show_colour.length;b++){
                    if(show_colour[b] == id_){
                        colorPickerView.setVisibility(View.VISIBLE);
                        Log.i("test","it should be visible");
                        break;
                    }

                }


            }else{
                //sw.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOFF)));
                //sw.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOFF)));
                sw.setTextColor(getResources().getColor(R.color.colorOFF));
            }
            sw.setText(animation_options[i][1]);
            //timeout_seekbar.setThumb(getResources().getDrawable(R.drawable.asus_rog_logo_scaled));
            switches[i] = sw;
            animations_linear_layout.addView(sw);
            sw = ((CheckBox) findViewById(id_));
            sw.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    boolean all_off = true;
                    SharedPreferences prefs = getApplicationContext().getSharedPreferences(
                            "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);
                    colorPickerView.setVisibility(View.GONE);
                    for (int i = 0; i < switches.length; i++) {
                        if(i == id_){
                            if(switches[i].isChecked()){
                                current_selected = i;

                                prefs.edit().putInt(package_animation_preference_pretext+package_name, current_selected).apply();
                                all_off = false;
                                //switches[i].setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorON)));
                                //switches[i].setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorThumbOn)));
                                switches[i].setTextColor(getResources().getColor(R.color.colorON));
                                for(int b=0;b<show_colour.length;b++){
                                    if(show_colour[b] == id_){
                                        colorPickerView.setVisibility(View.VISIBLE);
                                        break;
                                    }

                                }
                            }else{
                                //switches[i].setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOFF)));
                                //switches[i].setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOFF)));
                                switches[i].setTextColor(getResources().getColor(R.color.colorOFF));
                            }
                        }else{
                            switches[i].setChecked(false);
                            //switches[i].setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOFF)));
                            //switches[i].setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOFF)));
                            switches[i].setTextColor(getResources().getColor(R.color.colorOFF));
                        }
                    }

                    if(all_off) {
                        current_selected = 0;
                        switches[0].setChecked(true);
                        //switches[0].setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorON)));
                        //switches[0].setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorThumbOn)));
                        switches[0].setTextColor(getResources().getColor(R.color.colorON));
                        prefs.edit().putInt(package_animation_preference_pretext + package_name, 0).apply();
                        colorPickerView.setVisibility(View.GONE);
                    }

                }
            });
        }
    }
}
