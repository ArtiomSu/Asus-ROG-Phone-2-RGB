package terminal_heat_sink.asusrogphone2rgb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

public class AnimationsActivity extends Fragment {
    public AnimationsActivity() {
        // Required empty public constructor
    }
    private int current_selected = 0;
    private String current_selected_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.current_selected";
    private String notifications_settings_on_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.notifications_settings";
    private String notifications_on_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.notifications_on";
    private String notifications_animation_on_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.notifications_animation";

    private LinearLayout custom_stuff;
    private Button open_settings;
    private Switch switch_enable_notifications;
    private Spinner notificationAnimationSelector;

    private String[][] animation_options = {
            {"0","off"},
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
            {"13","slow glitchy rainbow"},
            {"14","yellow light? rofl"},
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_animations, container, false);


        LinearLayout animations_linear_layout = (LinearLayout) root.findViewById(R.id.animations_linear_layout);

        SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences(
                "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);





        current_selected = prefs.getInt(current_selected_shared_preference_key,0);
        if(current_selected == 0){
            if(savedInstanceState != null){
                current_selected = savedInstanceState.getInt("current_selected");
            }else{
                current_selected = 0;
            }
        }




        final Switch[] switches = new Switch[animation_options.length];

        for (int i = 0; i < animation_options.length; i++) {

            Switch sw = new Switch(getActivity().getApplicationContext());
            sw.setId(i);
            final int id_ = sw.getId();
            if(id_ == current_selected){
                sw.setChecked(true);
                sw.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorON)));
                sw.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorON)));
                sw.setTextColor(getResources().getColor(R.color.colorON));
            }else{
                sw.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOFF)));
                sw.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOFF)));
                sw.setTextColor(getResources().getColor(R.color.colorText));
            }
            sw.setText(animation_options[i][1]);

            switches[i] = sw;
            animations_linear_layout.addView(sw);
            sw = ((Switch) root.findViewById(id_));
            sw.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                        boolean all_off = true;
                        for (int i = 0; i < switches.length; i++) {
                            if(i == id_){
                                if(switches[i].isChecked()){
                                    current_selected = i;
                                    SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences(
                                            "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);
                                    prefs.edit().putInt(current_selected_shared_preference_key, current_selected).apply();
                                    all_off = false;
                                    switches[i].setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorON)));
                                    switches[i].setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorON)));
                                    switches[i].setTextColor(getResources().getColor(R.color.colorON));
                                }else{
                                    switches[i].setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOFF)));
                                    switches[i].setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOFF)));
                                    switches[i].setTextColor(getResources().getColor(R.color.colorOFF));
                                }
                            }else{
                                switches[i].setChecked(false);
                                switches[i].setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOFF)));
                                switches[i].setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOFF)));
                                switches[i].setTextColor(getResources().getColor(R.color.colorOFF));
                            }
                        }

                        if(all_off){
                            current_selected = 0;
                            switches[0].setChecked(true);
                            switches[0].setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorON)));
                            switches[0].setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorON)));
                            switches[0].setTextColor(getResources().getColor(R.color.colorON));
                            SystemWriter.write_animation(0,getActivity().getApplicationContext());
                        }else{
                            //set effect
                            SystemWriter.write_animation(id_,getActivity().getApplicationContext());
                        }

                }
            });
        }

        custom_stuff = new LinearLayout(getActivity().getApplicationContext());
        custom_stuff.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        custom_stuff.setOrientation(LinearLayout.VERTICAL);
        custom_stuff.setGravity(Gravity.FILL_VERTICAL);

        final TextView custom_text_view = new TextView(getActivity().getApplicationContext());
        custom_text_view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
        custom_text_view.setTextColor(getResources().getColor(R.color.colorText));
        custom_text_view.setText("Custom Options");
        custom_text_view.setTextSize(custom_text_view.getTextSize()+1);


        custom_stuff.addView(custom_text_view);

        Switch switch_notifications_settings = new Switch(getActivity().getApplicationContext());
        switch_notifications_settings.setText("Notification settings");
        custom_stuff.addView(switch_notifications_settings);

        boolean notifications_enabled = prefs.getBoolean(notifications_settings_on_shared_preference_key,false);

        if(notifications_enabled){
            switch_notifications_settings.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorON)));
            switch_notifications_settings.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorON)));
            switch_notifications_settings.setTextColor(getResources().getColor(R.color.colorText));
            switch_notifications_settings.setChecked(true);

            create_notification_settings();



        }else{
            switch_notifications_settings.setChecked(false);
            switch_notifications_settings.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOFF)));
            switch_notifications_settings.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOFF)));
            switch_notifications_settings.setTextColor(getResources().getColor(R.color.colorText));
        }

        switch_notifications_settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences(
                        "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);
                boolean notifications_enabled = prefs.getBoolean(notifications_settings_on_shared_preference_key,false);
                    Switch s = (Switch) view;
                if(notifications_enabled){
                    s.setChecked(false);
                    s.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOFF)));
                    s.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOFF)));
                    s.setTextColor(getResources().getColor(R.color.colorText));
                    prefs.edit().putBoolean(notifications_settings_on_shared_preference_key, false).apply();

                    custom_stuff.removeView(open_settings);
                    custom_stuff.removeView(switch_enable_notifications);
                    custom_stuff.removeView(notificationAnimationSelector);


                }else{
                    s.setChecked(true);
                    s.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorON)));
                    s.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorON)));
                    s.setTextColor(getResources().getColor(R.color.colorText));
                    prefs.edit().putBoolean(notifications_settings_on_shared_preference_key, true).apply();


                    create_notification_settings();


                }

            }
        });

        animations_linear_layout.addView(custom_stuff);

        return root;
    }

    private void create_notification_settings(){
        switch_enable_notifications = new Switch(getActivity().getApplicationContext());
        switch_enable_notifications.setText("React To Notifications");

        SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences(
                "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);

        boolean notifications_enabled = prefs.getBoolean(notifications_on_shared_preference_key,false);

        if(notifications_enabled){
            switch_enable_notifications.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorON)));
            switch_enable_notifications.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorON)));
            switch_enable_notifications.setTextColor(getResources().getColor(R.color.colorON));
            switch_enable_notifications.setChecked(true);
        }else{
            switch_enable_notifications.setChecked(false);
            switch_enable_notifications.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOFF)));
            switch_enable_notifications.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOFF)));
            switch_enable_notifications.setTextColor(getResources().getColor(R.color.colorOFF));
        }

        switch_enable_notifications.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences(
                        "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);
                boolean notifications_enabled = prefs.getBoolean(notifications_on_shared_preference_key,false);
                Switch s = (Switch) view;
                if(notifications_enabled){
                    s.setChecked(false);
                    s.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOFF)));
                    s.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOFF)));
                    s.setTextColor(getResources().getColor(R.color.colorOFF));
                    prefs.edit().putBoolean(notifications_on_shared_preference_key, false).apply();
                }else{
                    s.setChecked(true);
                    s.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorON)));
                    s.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorON)));
                    s.setTextColor(getResources().getColor(R.color.colorON));
                    prefs.edit().putBoolean(notifications_on_shared_preference_key, true).apply();

                    Intent notification_intent = new Intent(getActivity().getApplicationContext(), NotificationService.class);
                    getActivity().getApplicationContext().startService(notification_intent);
                }

            }
        });

        custom_stuff.addView(switch_enable_notifications);



        open_settings = new Button(getActivity().getApplicationContext());
        open_settings.setText("Click to enable App to read notifications in settings");
        open_settings.setTextColor(getResources().getColor(R.color.colorText));
        open_settings.setBackgroundColor(getResources().getColor(R.color.colorButton));


        open_settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS" ) ;
                startActivity(intent);
            }});

        custom_stuff.addView(open_settings);


        notificationAnimationSelector = new Spinner(getActivity().getApplicationContext());
        String[] animation_items = getResources().getStringArray(R.array.notification_animations);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, animation_items);


        adapter.setDropDownViewResource(R.layout.spinner_text);

        notificationAnimationSelector.setAdapter(adapter);

        notificationAnimationSelector.setPrompt("Select Animation to use for notifications");
        notificationAnimationSelector.setBackgroundColor(getResources().getColor(R.color.colorBG));

        int notifications_animation = prefs.getInt(notifications_animation_on_shared_preference_key,1);
        notificationAnimationSelector.setSelection(notifications_animation-1,true);



        notificationAnimationSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    int animation_mode = i+1;
                    if(animation_mode >= 14){
                        Log.i("Notification_animation_selector:","custom animation selected "+animation_mode);
                        animation_mode = 20 + (animation_mode-14);
                    }else{
                        Log.i("Notification_animation_selector:","animation selected "+animation_options[animation_mode][1]);
                    }
                    TextView selected = ((TextView) adapterView.getChildAt(0));
                    selected.setTextColor(getResources().getColor(R.color.colorText));
                    selected.setText("selected animation: "+selected.getText());

                    SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences(
                        "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);
                    prefs.edit().putInt(notifications_animation_on_shared_preference_key, animation_mode).apply();


                //((TextView) adapterView.getChildAt(0)).setTextSize(5);

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        custom_stuff.addView(notificationAnimationSelector);

    }



    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putInt("current_selected", current_selected);
    }

}