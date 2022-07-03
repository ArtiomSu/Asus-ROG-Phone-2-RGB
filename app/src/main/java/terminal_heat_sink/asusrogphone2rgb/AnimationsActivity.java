package terminal_heat_sink.asusrogphone2rgb;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class AnimationsActivity extends Fragment {
    public AnimationsActivity() {
        // Required empty public constructor
    }
    private int current_selected = 0;
    private String current_selected_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.current_selected";
    private String notifications_on_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.notifications_on";
    private String notifications_animation_on_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.notifications_animation";
    private String notifications_second_led_on_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.notifications_second_led";
    private String use_notifications_second_led_only_on_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.notifications_second_led_use_only";

    private String use_notifications_timeout_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.use_notifications_timeout_shared_preference_key";
    private String notifications_timeout_seconds_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.notifications_timeout_seconds_shared_preference_key";
    private String notifications_timeout_progress_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.notifications_timeout_progress_shared_preference_key";

    //notification snooze
    private String use_notifications_snooze_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.use_notifications_snooze_shared_preference_key";
    private String show_notifications_snooze_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.show_notifications_snooze_shared_preference_key";
    private String notifications_snooze_start_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.notifications_snooze_start_shared_preference_key";
    private String notifications_snooze_end_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.notifications_snooze_end_shared_preference_key";
    private String notifications_snooze_first_launch_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.notifications_snooze_first_launch_shared_preference_key";

    private AlarmManager alarmMgr;
    private AlarmManager alarmMgr_stop;
    private PendingIntent notification_snooze_start_intent;
    private PendingIntent notification_snooze_stop_intent;

    //battery charging preferences
    private final String battery_animate_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.battery_animate";
    private final String battery_use_second_led_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.battery_use_second_led";
    private final String battery_use_second_led_only_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.battery_use_second_led_only";
    private final String battery_animation_mode_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.battery_animation_mode";

    //visualiser charging preferences
//    private final String visualiser_on_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.visualiser_on";
//    private final String visualiser_use_second_led_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.visualiser_use_second_led";
//    private final String visualiser_use_second_led_only_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.visualiser_use_second_led_only";
//    private final String visualiser_animation_mode_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.visualiser_animation_mode";

    //miscellaneous preferences
    private final String shake_on_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.shake_on";
    private final String shake_on_first_launch_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.shake_on_first_launch";

    //check if phone is rog 3
    private final String isphone_rog3_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.isrog3";

    private final String is_root_mode_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.isrootmode";

    //help guide
    private final String hide_help_animations_preference_key = "terminal_hear_sink.asusrogphone2rgb.hide_help_on_animations";
    private boolean helpWebViewPageNotAvailable = false;
    private final String help_video_id = "WONmNu35GFM";

    private LinearLayout notification_settings_ll;
    private CheckBox switch_enable_notifications;
    private Spinner notificationAnimationSelector;
    private CheckBox switch_enable_second_led_notifications;
    private CheckBox switch_use_second_led_for_notifications_only;
    private Button button_select_apps;
    private SeekBar timeout_seekbar;
    private TextView timeout_text;
    private ScrollView scrollView;

    private LinearLayout notification_snooze_ll;
    private CheckBox use_notification_snooze;
    private LinearLayout notification_snooze_start_ll;
    private TextView start_text;
    private Button button_start;
    private LinearLayout notification_snooze_end_ll;
    private TextView end_text;
    private Button button_end;

    private LinearLayout battery_settings_ll;

    //private LinearLayout visualiser_settings_ll;

    private LinearLayout animations_mode_ll;

    private LinearLayout help_ll;
    private Button show_help_button;

    private boolean easter_egg_clicked = false;

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
            {"13","slow glitchy rainbow"}
    };

    private int check_box_states[][];
    private int check_box_colors[];

    private static final boolean testing  = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_animations, container, false);

        check_box_states = new int[][]{{android.R.attr.state_checked}, {}};
        check_box_colors = new int[]{getResources().getColor(R.color.colorON), getResources().getColor(R.color.colorDisabled)};

        LinearLayout animations_linear_layout = (LinearLayout) root.findViewById(R.id.animations_linear_layout);
        scrollView = (ScrollView) root.findViewById(R.id.animations_scroll_layout);

        ImageView easterEgg = (ImageView) root.findViewById(R.id.easteregg);

        easterEgg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                easter_egg_clicked = !easter_egg_clicked;
                easter_egg();
            }
        });

        SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences(
                "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);


        show_help_button = (Button) root.findViewById(R.id.show_help_button);
        final boolean hide_help = prefs.getBoolean(hide_help_animations_preference_key, false);
        if(!hide_help){
            show_help_button.setVisibility(View.GONE);
        }

        show_help_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs.edit().putBoolean(hide_help_animations_preference_key, false).apply();
                help_ll.setVisibility(View.VISIBLE);
                show_help_button.setVisibility(View.GONE);
                scrollView.smoothScrollTo(0,0);
            }
        });



        current_selected = prefs.getInt(current_selected_shared_preference_key,0);
        if(current_selected == 0){
            if(savedInstanceState != null){
                current_selected = savedInstanceState.getInt("current_selected");
            }else{
                current_selected = 0;
            }
        }

        if(testing){
            final TextView test_text_view = new TextView(getActivity().getApplicationContext());
            LinearLayout.LayoutParams custom_text_view_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
            custom_text_view_params.setMargins(0,0,0,20);
            test_text_view.setLayoutParams(custom_text_view_params);
            test_text_view.setTextColor(getResources().getColor(R.color.colorText));
            test_text_view.setText("Shake test");
            test_text_view.setTextSize(test_text_view.getTextSize()+1);
            test_text_view.setTypeface(null, Typeface.BOLD);
            test_text_view.setBackgroundColor(getResources().getColor(R.color.seperator));
            test_text_view.setGravity(Gravity.CENTER_HORIZONTAL);

            animations_linear_layout.addView(test_text_view);
        }

        create_help_view(animations_linear_layout, prefs);

        create_animation_switches(animations_linear_layout,root);

        create_notification_settings(animations_linear_layout, prefs);

        create_battery_settings(animations_linear_layout,prefs);

        //create_visualiser_settings(animations_linear_layout,prefs);

        //create_miscellaneous_settings(animations_linear_layout, prefs);

        scrollView.smoothScrollTo(0,0);

        return root;
    }

    private void create_animation_switches(LinearLayout animations_linear_layout, View root){

        animations_mode_ll = new LinearLayout(getActivity().getApplicationContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(0,10,0,0);
        animations_mode_ll.setLayoutParams(params);
        animations_mode_ll.setOrientation(LinearLayout.VERTICAL);
        animations_mode_ll.setGravity(Gravity.FILL_VERTICAL);
        animations_mode_ll.setBackgroundColor(getResources().getColor(R.color.seperator));
        animations_mode_ll.setPadding(0,0,0,0);
        animations_mode_ll.setBackground(getResources().getDrawable(R.drawable.linearlayoutborder));

        animations_linear_layout.addView(animations_mode_ll);

        final TextView custom_text_view = new TextView(getActivity().getApplicationContext());
        LinearLayout.LayoutParams custom_text_view_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        custom_text_view_params.setMargins(0,0,0,20);
        custom_text_view.setLayoutParams(custom_text_view_params);
        custom_text_view.setTextColor(getResources().getColor(R.color.colorText));
        custom_text_view.setText("Animation Modes");
        //custom_text_view.setTextSize(custom_text_view.getTextSize()+1);
        custom_text_view.setTypeface(null, Typeface.BOLD);
        custom_text_view.setBackgroundColor(getResources().getColor(R.color.seperator));
        custom_text_view.setGravity(Gravity.CENTER_HORIZONTAL);


        animations_mode_ll.addView(custom_text_view);

        final CheckBox[] switches = new CheckBox[animation_options.length];

        for (int i = 0; i < animation_options.length; i++) {


            CheckBox sw = new CheckBox(getActivity().getApplicationContext());
            sw.setButtonTintList(new ColorStateList(check_box_states,check_box_colors));
            sw.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            sw.setButtonDrawable(R.drawable.asus_rog_logo_scaled);
            sw.setPadding(0,0,0,25);


            //sw.setThumbDrawable(getResources().getDrawable(R.drawable.asus_rog_logo_scaled));
            sw.setId(i);
            final int id_ = sw.getId();
            if(id_ == current_selected){
                sw.setChecked(true);
                //sw.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorON)));
                //sw.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorThumbOn)));
                sw.setTextColor(getResources().getColor(R.color.colorON));
            }else{
                //sw.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOFF)));
                //sw.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOFF)));
                sw.setTextColor(getResources().getColor(R.color.colorOFF));
            }
            sw.setText(animation_options[i][1]);
            //timeout_seekbar.setThumb(getResources().getDrawable(R.drawable.asus_rog_logo_scaled));
            switches[i] = sw;
            animations_mode_ll.addView(sw);
            sw = ((CheckBox) root.findViewById(id_));
            sw.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    boolean all_off = true;
                    SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences(
                            "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);
                    for (int i = 0; i < switches.length; i++) {
                        if(i == id_){
                            if(switches[i].isChecked()){
                                current_selected = i;
                                prefs.edit().putInt(current_selected_shared_preference_key, current_selected).apply();
                                all_off = false;
                                //switches[i].setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorON)));
                                //switches[i].setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorThumbOn)));
                                switches[i].setTextColor(getResources().getColor(R.color.colorON));
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

                    if(all_off){
                        current_selected = 0;
                        switches[0].setChecked(true);
                        //switches[0].setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorON)));
                        //switches[0].setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorThumbOn)));
                        switches[0].setTextColor(getResources().getColor(R.color.colorON));
                        prefs.edit().putInt(current_selected_shared_preference_key, 0).apply();
                        SystemWriter.write_animation(0,getActivity().getApplicationContext());
                    }else{
                        //set effect
                        SystemWriter.write_animation(id_,getActivity().getApplicationContext());
                    }

                }
            });
        }

        //animations_linear_layout.addView(animations_mode_ll);
    }


    private void create_notification_settings(LinearLayout animations_linear_layout, SharedPreferences prefs){
        notification_settings_ll = new LinearLayout(getActivity().getApplicationContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(0,20,0,0);
        notification_settings_ll.setLayoutParams(params);
        notification_settings_ll.setOrientation(LinearLayout.VERTICAL);
        notification_settings_ll.setGravity(Gravity.FILL_VERTICAL);
        notification_settings_ll.setBackgroundColor(getResources().getColor(R.color.seperator));
        notification_settings_ll.setPadding(0,0,0,0);
        notification_settings_ll.setBackground(getResources().getDrawable(R.drawable.linearlayoutborder));


        final TextView custom_text_view = new TextView(getActivity().getApplicationContext());
        LinearLayout.LayoutParams custom_text_view_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        custom_text_view_params.setMargins(0,0,0,20);
        custom_text_view.setLayoutParams(custom_text_view_params);
        custom_text_view.setTextColor(getResources().getColor(R.color.colorText));
        custom_text_view.setText("Notification Settings");
        //custom_text_view.setTextSize(custom_text_view.getTextSize()+1);
        custom_text_view.setTypeface(null, Typeface.BOLD);
        custom_text_view.setBackgroundColor(getResources().getColor(R.color.seperator));
        custom_text_view.setGravity(Gravity.CENTER_HORIZONTAL);


        notification_settings_ll.addView(custom_text_view);

        // react to notifications
        switch_enable_notifications = new CheckBox(getActivity().getApplicationContext());
        switch_enable_notifications.setText("React To Notifications");
        //switch_enable_notifications.setThumbDrawable(getResources().getDrawable(R.drawable.asus_rog_logo_scaled));
        switch_enable_notifications.setButtonTintList(new ColorStateList(check_box_states,check_box_colors));
        switch_enable_notifications.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        switch_enable_notifications.setButtonDrawable(R.drawable.asus_rog_logo_scaled);
        switch_enable_notifications.setPadding(0,0,0,25);


        boolean notifications_enabled = prefs.getBoolean(notifications_on_shared_preference_key,false);
        boolean isRootMode = prefs.getBoolean(is_root_mode_shared_preference_key, false);

        if(!isRootMode){
            notifications_enabled = NotificationManagerCompat.getEnabledListenerPackages(getActivity().getApplicationContext()).contains("terminal_heat_sink.asusrogphone2rgb");
            prefs.edit().putBoolean(notifications_on_shared_preference_key, notifications_enabled).apply();
        }


        if(notifications_enabled){
            //switch_enable_notifications.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorON)));
            //switch_enable_notifications.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorThumbOn)));
            switch_enable_notifications.setTextColor(getResources().getColor(R.color.colorON));
            switch_enable_notifications.setChecked(true);
            if(isRootMode){
                SystemWriter.notification_access(true,getActivity().getApplicationContext());
            }else{
                if (!NotificationManagerCompat.getEnabledListenerPackages(getActivity().getApplicationContext()).contains("terminal_heat_sink.asusrogphone2rgb")) {        //ask for permission
                    Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                    startActivity(intent);
                }
            }

            String isRog3 = prefs.getString(isphone_rog3_shared_preference_key," ");
            if(!isRog3.equals(" ")){
                if(isRog3.charAt(0) == '3'){
                    Log.i("AsusRogPhone2RGBNotificationService", "Rog3 wakelock");
                    SystemWriter.rog_3_wakelock(getActivity().getApplicationContext());
                }
            }

        }else{
            switch_enable_notifications.setChecked(false);
            //switch_enable_notifications.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOFF)));
            //switch_enable_notifications.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOFF)));
            switch_enable_notifications.setTextColor(getResources().getColor(R.color.colorOFF));
            if(isRootMode){
                SystemWriter.notification_access(false,getActivity().getApplicationContext());
            }else{
                if (NotificationManagerCompat.getEnabledListenerPackages(getActivity().getApplicationContext()).contains("terminal_heat_sink.asusrogphone2rgb")) {        //ask for permission
                    Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                    startActivity(intent);
                }
            }

            String isRog3 = prefs.getString(isphone_rog3_shared_preference_key," ");
            if(!isRog3.equals(" ")) {
                if (isRog3.charAt(0) == '3'){
                    Log.i("AsusRogPhone2RGBNotificationService", "Rog3 release wakelock");
                    SystemWriter.rog_3_wakeunlock(getActivity().getApplicationContext());
                }
            }
        }

        switch_enable_notifications.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences(
                        "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);
                boolean notifications_enabled = prefs.getBoolean(notifications_on_shared_preference_key,false);
                boolean isRootMode = prefs.getBoolean(is_root_mode_shared_preference_key, false);
                CheckBox s = (CheckBox) view;

                if(notifications_enabled){
                    s.setChecked(false);
                    //s.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOFF)));
                    //s.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOFF)));
                    s.setTextColor(getResources().getColor(R.color.colorOFF));
                    prefs.edit().putBoolean(notifications_on_shared_preference_key, false).apply();
                    if(isRootMode) {
                        SystemWriter.notification_access(false, getActivity().getApplicationContext());
                    } else{
                        if (NotificationManagerCompat.getEnabledListenerPackages(getActivity().getApplicationContext()).contains("terminal_heat_sink.asusrogphone2rgb")) {        //ask for permission
                            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                            startActivity(intent);
                        }
                    }

                    Intent notification_intent = new Intent(getActivity().getApplicationContext(), NotificationService.class);
                    getActivity().getApplicationContext().stopService(notification_intent);

                }else{
                    s.setChecked(true);
                    //s.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorON)));
                    //s.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorThumbOn)));
                    s.setTextColor(getResources().getColor(R.color.colorON));
                    prefs.edit().putBoolean(notifications_on_shared_preference_key, true).apply();
                    if(isRootMode) {
                        SystemWriter.notification_access(true, getActivity().getApplicationContext());
                    } else{
                        if (!NotificationManagerCompat.getEnabledListenerPackages(getActivity().getApplicationContext()).contains("terminal_heat_sink.asusrogphone2rgb")) {        //ask for permission
                            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                            startActivity(intent);
                        }
                    }

                    Intent notification_intent = new Intent(getActivity().getApplicationContext(), NotificationService.class);
                    getActivity().getApplicationContext().startService(notification_intent);
                }

            }
        });

        notification_settings_ll.addView(switch_enable_notifications);

        // use second led also for notifications
        switch_enable_second_led_notifications = new CheckBox(getActivity().getApplicationContext());
        switch_enable_second_led_notifications.setText("Use second led for notifications also");
        //switch_enable_second_led_notifications.setThumbDrawable(getResources().getDrawable(R.drawable.asus_rog_logo_scaled));
        switch_enable_second_led_notifications.setButtonTintList(new ColorStateList(check_box_states,check_box_colors));
        switch_enable_second_led_notifications.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        switch_enable_second_led_notifications.setButtonDrawable(R.drawable.asus_rog_logo_scaled);
        switch_enable_second_led_notifications.setPadding(0,0,0,25);

        boolean notifications_second_led_enabled = prefs.getBoolean(notifications_second_led_on_shared_preference_key,false);

        if(notifications_second_led_enabled){
            //switch_enable_second_led_notifications.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorON)));
            //switch_enable_second_led_notifications.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorThumbOn)));
            switch_enable_second_led_notifications.setTextColor(getResources().getColor(R.color.colorON));
            switch_enable_second_led_notifications.setChecked(true);
        }else{
            switch_enable_second_led_notifications.setChecked(false);
            //switch_enable_second_led_notifications.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOFF)));
            //switch_enable_second_led_notifications.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOFF)));
            switch_enable_second_led_notifications.setTextColor(getResources().getColor(R.color.colorOFF));
        }

        switch_enable_second_led_notifications.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences(
                        "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);
                boolean notifications_second_led_enabled = prefs.getBoolean(notifications_second_led_on_shared_preference_key,false);

                CheckBox s = (CheckBox) view;
                if(notifications_second_led_enabled){
                    s.setChecked(false);
                    //s.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOFF)));
                    //s.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOFF)));
                    s.setTextColor(getResources().getColor(R.color.colorOFF));
                    prefs.edit().putBoolean(notifications_second_led_on_shared_preference_key, false).apply();
                }else{
                    s.setChecked(true);
                    //s.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorON)));
                    //s.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorThumbOn)));
                    s.setTextColor(getResources().getColor(R.color.colorON));
                    prefs.edit().putBoolean(notifications_second_led_on_shared_preference_key, true).apply();

                }

            }
        });

        notification_settings_ll.addView(switch_enable_second_led_notifications);


        // use second led for notifications only
        switch_use_second_led_for_notifications_only = new CheckBox(getActivity().getApplicationContext());
        switch_use_second_led_for_notifications_only.setText("Use Only the second led for notifications");
        //switch_use_second_led_for_notifications_only.setThumbDrawable(getResources().getDrawable(R.drawable.asus_rog_logo_scaled));
        switch_use_second_led_for_notifications_only.setButtonTintList(new ColorStateList(check_box_states,check_box_colors));
        switch_use_second_led_for_notifications_only.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        switch_use_second_led_for_notifications_only.setButtonDrawable(R.drawable.asus_rog_logo_scaled);
        switch_use_second_led_for_notifications_only.setPadding(0,0,0,25);

        boolean notifications_second_led_enabled_only = prefs.getBoolean(use_notifications_second_led_only_on_shared_preference_key,false);

        if(notifications_second_led_enabled_only){
            //switch_use_second_led_for_notifications_only.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorON)));
            //switch_use_second_led_for_notifications_only.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorThumbOn)));
            switch_use_second_led_for_notifications_only.setTextColor(getResources().getColor(R.color.colorON));
            switch_use_second_led_for_notifications_only.setChecked(true);
        }else{
            switch_use_second_led_for_notifications_only.setChecked(false);
            //switch_use_second_led_for_notifications_only.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOFF)));
            //switch_use_second_led_for_notifications_only.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOFF)));
            switch_use_second_led_for_notifications_only.setTextColor(getResources().getColor(R.color.colorOFF));
        }

        switch_use_second_led_for_notifications_only.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences(
                        "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);
                boolean notifications_second_led_enabled_only = prefs.getBoolean(use_notifications_second_led_only_on_shared_preference_key,false);
                CheckBox s = (CheckBox) view;
                if(notifications_second_led_enabled_only){
                    s.setChecked(false);
                    //s.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOFF)));
                    //s.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOFF)));
                    s.setTextColor(getResources().getColor(R.color.colorOFF));
                    prefs.edit().putBoolean(use_notifications_second_led_only_on_shared_preference_key, false).apply();
                }else{
                    s.setChecked(true);
                    //s.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorON)));
                    //s.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorThumbOn)));
                    s.setTextColor(getResources().getColor(R.color.colorON));
                    prefs.edit().putBoolean(use_notifications_second_led_only_on_shared_preference_key, true).apply();
                }

            }
        });

        notification_settings_ll.addView(switch_use_second_led_for_notifications_only);

        // select which apps to use for notifications
        button_select_apps = new Button(getActivity().getApplicationContext());
        button_select_apps.setText("Select which apps trigger notifications");
        button_select_apps.setTextColor(getResources().getColor(R.color.colorText));
        button_select_apps.setBackgroundColor(getResources().getColor(R.color.colorButton));

        button_select_apps.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent app_selector = new Intent(getActivity().getApplicationContext(), AppSelector.class);
                startActivity(app_selector);
            }});

        notification_settings_ll.addView(button_select_apps);

        // which animation to use for notifications
        notificationAnimationSelector = new Spinner(getActivity().getApplicationContext());
        String[] animation_items = getResources().getStringArray(R.array.notification_animations);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, animation_items);


        adapter.setDropDownViewResource(R.layout.spinner_text);

        notificationAnimationSelector.setAdapter(adapter);

        notificationAnimationSelector.setPrompt("Select Animation to use for notifications");
        //notificationAnimationSelector.setBackgroundColor(getResources().getColor(R.color.colorBG));

        int notifications_animation = prefs.getInt(notifications_animation_on_shared_preference_key,1);
        if(notifications_animation >=13){
            notificationAnimationSelector.setSelection((notifications_animation-20-1)+13,true);
        }else{
            notificationAnimationSelector.setSelection(notifications_animation-1,true);
        }


        notificationAnimationSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    int animation_mode = i+1;
                    if(animation_mode >= 13){
                        Log.i("Notification_animation_selector:","custom animation selected "+animation_mode);
                        animation_mode = 20 + (animation_mode-13);
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

        notification_settings_ll.addView(notificationAnimationSelector);


        CheckBox switch_timeout = new CheckBox(getActivity().getApplicationContext());
        switch_timeout.setText("Notification timeout");
        //switch_timeout.setThumbDrawable(getResources().getDrawable(R.drawable.asus_rog_logo_scaled));
        switch_timeout.setButtonTintList(new ColorStateList(check_box_states,check_box_colors));
        switch_timeout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        switch_timeout.setButtonDrawable(R.drawable.asus_rog_logo_scaled);

        boolean use_timeout = prefs.getBoolean(use_notifications_timeout_shared_preference_key,false);
        notification_settings_ll.addView(switch_timeout);
        if(use_timeout){
            //switch_timeout.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorON)));
            //switch_timeout.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorThumbOn)));
            switch_timeout.setTextColor(getResources().getColor(R.color.colorON));
            switch_timeout.setChecked(true);
            create_timeout_settings(prefs);
        }else{
            switch_timeout.setChecked(false);
            //switch_timeout.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOFF)));
            //switch_timeout.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOFF)));
            switch_timeout.setTextColor(getResources().getColor(R.color.colorOFF));
        }

        switch_timeout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences(
                        "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);
                boolean use_timeout = prefs.getBoolean(use_notifications_timeout_shared_preference_key,false);
                CheckBox s = (CheckBox) view;
                if(use_timeout){
                    s.setChecked(false);
                    //s.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOFF)));
                    //s.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOFF)));
                    s.setTextColor(getResources().getColor(R.color.colorOFF));
                    prefs.edit().putBoolean(use_notifications_timeout_shared_preference_key, false).apply();
                    notification_settings_ll.removeView(timeout_seekbar);
                    notification_settings_ll.removeView(timeout_text);

                }else{
                    s.setChecked(true);
                    //s.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorON)));
                    //s.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorThumbOn)));
                    s.setTextColor(getResources().getColor(R.color.colorON));
                    prefs.edit().putBoolean(use_notifications_timeout_shared_preference_key, true).apply();
                    create_timeout_settings(prefs);
                }

            }
        });

        animations_linear_layout.addView(notification_settings_ll);
        create_notification_snooze(notification_settings_ll,prefs);

    }

    private void create_notification_snooze(LinearLayout animations_linear_layout, SharedPreferences prefs){
        notification_snooze_ll = new LinearLayout(getActivity().getApplicationContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(0,20,0,20);
        notification_snooze_ll.setLayoutParams(params);
        notification_snooze_ll.setOrientation(LinearLayout.VERTICAL);
        notification_snooze_ll.setGravity(Gravity.FILL_VERTICAL);
        notification_snooze_ll.setBackgroundColor(getResources().getColor(R.color.seperator));
        notification_snooze_ll.setPadding(0,0,0,0);


        //Show snooze Options
        CheckBox show_notification_snooze  = new CheckBox(getActivity().getApplicationContext());
        notification_snooze_ll.addView(show_notification_snooze);
        show_notification_snooze.setText("Show Notification Snooze Settings");
        show_notification_snooze.setButtonTintList(new ColorStateList(check_box_states,check_box_colors));
        show_notification_snooze.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        show_notification_snooze.setButtonDrawable(R.drawable.asus_rog_logo_scaled);
        show_notification_snooze.setPadding(0,0,0,25);

        boolean show_notification_snooze_b = prefs.getBoolean(show_notifications_snooze_shared_preference_key,false);

        if(show_notification_snooze_b){
            show_notification_snooze.setTextColor(getResources().getColor(R.color.colorON));
            show_notification_snooze.setChecked(true);
            draw_notification_snooze_options(prefs);
        }else{
            show_notification_snooze.setChecked(false);
            show_notification_snooze.setTextColor(getResources().getColor(R.color.colorOFF));
        }

        show_notification_snooze.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences(
                        "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);
                boolean notifications_enabled = prefs.getBoolean(show_notifications_snooze_shared_preference_key,false);
                CheckBox s = (CheckBox) view;

                if(notifications_enabled){
                    s.setChecked(false);
                    s.setTextColor(getResources().getColor(R.color.colorOFF));
                    prefs.edit().putBoolean(show_notifications_snooze_shared_preference_key, false).apply();
                    notification_snooze_ll.removeView(use_notification_snooze);
                    notification_snooze_ll.removeView(notification_snooze_start_ll);
                    notification_snooze_ll.removeView(notification_snooze_end_ll);


                }else{
                    s.setChecked(true);
                    s.setTextColor(getResources().getColor(R.color.colorON));
                    prefs.edit().putBoolean(show_notifications_snooze_shared_preference_key, true).apply();
                    draw_notification_snooze_options(prefs);

                    boolean launched_first_time = prefs.getBoolean(notifications_snooze_first_launch_shared_preference_key,false);
                    if(!launched_first_time){
                        prefs.edit().putBoolean(notifications_snooze_first_launch_shared_preference_key, true).apply();
                        new AlertDialog.Builder(requireActivity())
                                .setTitle("How to use Notification Snooze")
                                .setMessage("Select \"Set start time\" this is when you want the notification service to stop. set a future time. so for example if right now its 20:45 set 20:46 since if you set 20:44 it will trigger tommorow.\n\nSelect \"set end time\" set this after your start time.\n\nselect \"enable snooze\" to start the thing.\n\nNOTE: if you change the start time or end time you will need to select \"enable snooze\" again it will be unselected anyway once you change the settings.\n\nNOTE on Delay: cause of how repeating alarm manager works the times you set will not be accurate.\n\nSo for example if you set start time for 20:10 it will probably stop the notification service at like 20:15 maybe earlier or even later.")
                                .setCancelable(false)
                                .setPositiveButton("I understand now", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .show();
                    }

                }

            }
        });


        animations_linear_layout.addView(notification_snooze_ll);
    }

    private void draw_notification_snooze_options(final SharedPreferences prefs){
        //Enable snooze
        use_notification_snooze  = new CheckBox(getActivity().getApplicationContext());
        use_notification_snooze.setText("Enable Snooze");
        use_notification_snooze.setButtonTintList(new ColorStateList(check_box_states,check_box_colors));
        use_notification_snooze.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        use_notification_snooze.setButtonDrawable(R.drawable.asus_rog_logo_scaled);
        use_notification_snooze.setPadding(0,0,0,25);


        boolean notifications_enabled = prefs.getBoolean(use_notifications_snooze_shared_preference_key,false);

        if(notifications_enabled){
            use_notification_snooze.setTextColor(getResources().getColor(R.color.colorON));
            use_notification_snooze.setChecked(true);
        }else{
            use_notification_snooze.setChecked(false);
            use_notification_snooze.setTextColor(getResources().getColor(R.color.colorOFF));
        }

        use_notification_snooze.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences(
                        "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);
                boolean notifications_enabled = prefs.getBoolean(use_notifications_snooze_shared_preference_key,false);
                CheckBox s = (CheckBox) view;

                if(notifications_enabled){
                    s.setChecked(false);
                    s.setTextColor(getResources().getColor(R.color.colorOFF));
                    prefs.edit().putBoolean(use_notifications_snooze_shared_preference_key, false).apply();

                    //cancel alarms
                    if (alarmMgr!= null) {
                        alarmMgr.cancel(notification_snooze_start_intent);
                    }
                    if (alarmMgr_stop!= null) {
                        alarmMgr_stop.cancel(notification_snooze_stop_intent);
                    }


                }else{
                    s.setChecked(true);
                    s.setTextColor(getResources().getColor(R.color.colorON));
                    prefs.edit().putBoolean(use_notifications_snooze_shared_preference_key, true).apply();

//                    //cancel alarms
                    if (alarmMgr!= null) {
                        alarmMgr.cancel(notification_snooze_start_intent);
                    }
                    if (alarmMgr_stop!= null) {
                        alarmMgr_stop.cancel(notification_snooze_stop_intent);
                    }

                    //apply alarm
                    String start_time = prefs.getString(notifications_snooze_start_shared_preference_key,"");
                    String end_time = prefs.getString(notifications_snooze_end_shared_preference_key,"");
                    if(start_time == "" || end_time == ""){
                        //invalid time
                        s.setChecked(false);
                        s.setTextColor(getResources().getColor(R.color.colorOFF));
                        prefs.edit().putBoolean(use_notifications_snooze_shared_preference_key, false).apply();
                        Toast.makeText(getActivity().getApplicationContext(), "Please set start and end times first", Toast.LENGTH_LONG).show();
                    }else {
                        //extract time
                        //start time
                        int start_hour = Integer.parseInt(start_time.split(":")[0]);
                        int start_minute = Integer.parseInt(start_time.split(":")[1]);

                        int end_hour = Integer.parseInt(end_time.split(":")[0]);
                        int end_minute = Integer.parseInt(end_time.split(":")[1]);

                        Log.i("NotificationSnooze",""+start_hour+" "+start_minute+" "+end_hour+" "+end_minute);

                        alarmMgr = (AlarmManager)getActivity().getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                        Intent intent = new Intent(getActivity().getApplicationContext(), NotificationSnoozeReceiver.class);
                        intent.putExtra("start",true);
                        notification_snooze_start_intent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), 192837, intent, PendingIntent.FLAG_MUTABLE);

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(System.currentTimeMillis());
                        calendar.set(Calendar.HOUR_OF_DAY, start_hour);
                        calendar.set(Calendar.MINUTE, start_minute);
                        calendar.set(Calendar.SECOND,0);
                        calendar.set(Calendar.MILLISECOND,0);

                        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                AlarmManager.INTERVAL_DAY, notification_snooze_start_intent);



                        alarmMgr_stop = (AlarmManager)getActivity().getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                        Intent intent_stop = new Intent(getActivity().getApplicationContext(), NotificationSnoozeReceiver.class);
                        intent.putExtra("start",false);
                        notification_snooze_stop_intent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), 192838, intent_stop, PendingIntent.FLAG_MUTABLE);


                        Calendar calendar_stop = Calendar.getInstance();
                        calendar_stop.setTimeInMillis(System.currentTimeMillis());
                        calendar_stop.set(Calendar.HOUR_OF_DAY, end_hour);
                        calendar_stop.set(Calendar.MINUTE, end_minute);
                        calendar.set(Calendar.SECOND,0);
                        calendar.set(Calendar.MILLISECOND,0);

                        alarmMgr_stop.setRepeating(AlarmManager.RTC_WAKEUP, calendar_stop.getTimeInMillis(),
                                AlarmManager.INTERVAL_DAY, notification_snooze_stop_intent);





                    }

                }

            }
        });

        notification_snooze_ll.addView(use_notification_snooze);

        //Start snooze layouts
        notification_snooze_start_ll = new LinearLayout(getActivity().getApplicationContext());
        LinearLayout.LayoutParams paramss = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        notification_snooze_start_ll.setLayoutParams(paramss);
        notification_snooze_start_ll.setOrientation(LinearLayout.HORIZONTAL);
        notification_snooze_start_ll.setGravity(Gravity.FILL_HORIZONTAL);

        String snooze_start_string = prefs.getString(notifications_snooze_start_shared_preference_key,"not set");

        start_text = new TextView(getActivity().getApplicationContext());
        LinearLayout.LayoutParams params_text = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        params_text.weight = 0.75f;
        start_text.setLayoutParams(params_text);
        start_text.setTextColor(getResources().getColor(R.color.colorText));
        start_text.setText("Starting Snooze at: "+snooze_start_string);

        notification_snooze_start_ll.addView(start_text);

        button_start = new Button(getActivity().getApplicationContext());
        LinearLayout.LayoutParams params_button = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        params_button.weight = 0.25f;
        button_start.setLayoutParams(params_button);
        button_start.setText("Set Start Time");
        button_start.setTextColor(getResources().getColor(R.color.colorText));
        button_start.setBackgroundColor(getResources().getColor(R.color.colorButton));

        button_start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final Calendar myCalender = Calendar.getInstance();
                int hour = myCalender.get(Calendar.HOUR_OF_DAY);
                int minute = myCalender.get(Calendar.MINUTE);


                TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (view.isShown()) {
                            myCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            myCalender.set(Calendar.MINUTE, minute);

                            String hour = "";
                            hour= (hourOfDay < 10) ? "0"+hourOfDay: ""+hourOfDay;
                            String minute_s = "";
                            minute_s= (minute < 10) ? "0"+minute: ""+minute;
                            start_text.setText("Starting Snooze at: "+hour+":"+minute_s);
                            prefs.edit().putString(notifications_snooze_start_shared_preference_key, hour+":"+minute_s).apply();
                            //turn off enable to reaply and cancel any pending alarms;
                            prefs.edit().putBoolean(use_notifications_snooze_shared_preference_key, false).apply();
                            use_notification_snooze.setChecked(false);
                            use_notification_snooze.setTextColor(getResources().getColor(R.color.colorOFF));
                            //cancel alarms
//                            if (alarmMgr!= null) {
//                                alarmMgr.cancel(notification_snooze_start_intent);
//                            }
//                            if (alarmMgr_stop!= null) {
//                                alarmMgr_stop.cancel(notification_snooze_stop_intent);
//                            }

                        }
                    }
                };
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), android.R.style.Theme_DeviceDefault_Dialog_MinWidth, myTimeListener, hour, minute, true);
                timePickerDialog.setTitle("Choose Snooze Start Time");
                timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                timePickerDialog.show();

            }});

        notification_snooze_start_ll.addView(button_start);


        //End snooze layouts
        notification_snooze_end_ll = new LinearLayout(getActivity().getApplicationContext());
        LinearLayout.LayoutParams paramsss = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        notification_snooze_end_ll.setLayoutParams(paramsss);
        notification_snooze_end_ll.setOrientation(LinearLayout.HORIZONTAL);
        notification_snooze_end_ll.setGravity(Gravity.FILL_HORIZONTAL);

        String snooze_end_string = prefs.getString(notifications_snooze_end_shared_preference_key,"not set");

        end_text = new TextView(getActivity().getApplicationContext());
        LinearLayout.LayoutParams params_text_end = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        params_text_end.weight = 0.75f;
        end_text.setLayoutParams(params_text_end);
        end_text.setTextColor(getResources().getColor(R.color.colorText));
        end_text.setText("Ending Snooze at: " +snooze_end_string);

        notification_snooze_end_ll.addView(end_text);

        button_end = new Button(getActivity().getApplicationContext());
        LinearLayout.LayoutParams params_button_end = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        params_button_end.weight = 0.25f;
        button_end.setLayoutParams(params_button_end);
        button_end.setText("Set End Time");
        button_end.setTextColor(getResources().getColor(R.color.colorText));
        button_end.setBackgroundColor(getResources().getColor(R.color.colorButton));

        button_end.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final Calendar myCalender = Calendar.getInstance();
                int hour = myCalender.get(Calendar.HOUR_OF_DAY);
                int minute = myCalender.get(Calendar.MINUTE);


                TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (view.isShown()) {
                            myCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            myCalender.set(Calendar.MINUTE, minute);

                            String hour = "";
                            hour= (hourOfDay < 10) ? "0"+hourOfDay: ""+hourOfDay;
                            String minute_s = "";
                            minute_s= (minute < 10) ? "0"+minute: ""+minute;
                            end_text.setText("Ending Snooze at: "+hour+":"+minute_s);
                            prefs.edit().putString(notifications_snooze_end_shared_preference_key, hour+":"+minute_s).apply();
                            //turn off enable to reaply and cancel any pending alarms;
                            prefs.edit().putBoolean(use_notifications_snooze_shared_preference_key, false).apply();
                            use_notification_snooze.setChecked(false);
                            use_notification_snooze.setTextColor(getResources().getColor(R.color.colorOFF));
                            //cancel alarms
//                            if (alarmMgr!= null) {
//                                alarmMgr.cancel(notification_snooze_start_intent);
//                            }
//                            if (alarmMgr_stop!= null) {
//                                alarmMgr_stop.cancel(notification_snooze_stop_intent);
//                            }

                        }
                    }
                };
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), android.R.style.Theme_DeviceDefault_Dialog_MinWidth, myTimeListener, hour, minute, true);
                timePickerDialog.setTitle("Choose Snooze End Time");
                timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                timePickerDialog.show();
            }});

        notification_snooze_end_ll.addView(button_end);

        notification_snooze_ll.addView(notification_snooze_start_ll);
        notification_snooze_ll.addView(notification_snooze_end_ll);
    }

    private void create_timeout_settings(SharedPreferences prefs){

        int timeout_seconds = prefs.getInt(notifications_timeout_seconds_shared_preference_key,60*30);// 30 mins
        int progress = prefs.getInt(notifications_timeout_progress_shared_preference_key,20);

        timeout_text = new TextView(getActivity().getApplicationContext());
        timeout_text.setTextColor(getResources().getColor(R.color.colorText));


        timeout_seekbar = new SeekBar(getActivity().getApplicationContext());
        timeout_seekbar.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorON)));

        timeout_seekbar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorON)));


        timeout_seekbar.setThumb(getResources().getDrawable(R.drawable.asus_rog_logo_scaled));
        timeout_seekbar.setMax(1000);
        timeout_seekbar.setMin(10);

        timeout_seekbar.setProgress(progress);
        timeout_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences(
                        "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);

                prefs.edit().putInt(notifications_timeout_progress_shared_preference_key,i).apply();

                int input = (int)Math.pow(1.0105,i)+19;

                prefs.edit().putInt(notifications_timeout_seconds_shared_preference_key,input).apply();
                timeout_text.setText(format_timeout(input));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        timeout_text.setText(format_timeout(timeout_seconds));
        notification_settings_ll.addView(timeout_text);
        notification_settings_ll.addView(timeout_seekbar);
    }

    private String format_timeout(int time) {
        String output = "Notifications will timeout after";
        int total_time = time;
        if (total_time >= 60 * 60) { // greater than an hour
            int hours = 0;
            while (total_time >= 60 * 60) {
                hours++;
                total_time = total_time - 60 * 60;
            }
            output += " " + hours + " hours ";
        }

        if (total_time >= 60) { // minutes
            int minutes = 0;
            while (total_time >= 60) {
                minutes++;
                total_time = total_time - 60;
            }
            output += " " + minutes + " minutes ";
        }

        //seconds remaning
        if (total_time > 0)
            output += " " + total_time + " seconds ";

        Log.i("AsusRogPhone2RGBTimeoutSelected","milli:"+time+" "+output);

        return output;
    }

    private void easter_egg(){
        scrollView.smoothScrollTo(0,0);

        int[] colors1 = {Color.parseColor("#000000"), Color.parseColor("#0f9b0f")};
        GradientDrawable frame1 = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,colors1);
        GradientDrawable frame11 = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,colors1);

        int[] colors2 = {Color.parseColor("#DA22FF"), Color.parseColor("#9733EE")};
        GradientDrawable frame2 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,colors2);

        int[] colors3 = {Color.parseColor("#7b4397"), Color.parseColor("#7b4397")};
        GradientDrawable frame3 = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT,colors3);

        int[] colors4 = {Color.parseColor("#a8ff78"), Color.parseColor("#78ffd6")};
        GradientDrawable frame4 = new GradientDrawable(GradientDrawable.Orientation.BL_TR,colors4);

        int[] colors5 = {Color.parseColor("#0F2027"), Color.parseColor("#2C5364")};
        GradientDrawable frame5 = new GradientDrawable(GradientDrawable.Orientation.TR_BL,colors5);

        int[] colors6 = {Color.parseColor("#e1eec3"), Color.parseColor("#f05053")};
        GradientDrawable frame6 = new GradientDrawable(GradientDrawable.Orientation.BR_TL,colors6);

        int[] colors7 = {Color.parseColor("#2980B9"), Color.parseColor("#FFFFFF")};
        GradientDrawable frame7 = new GradientDrawable(GradientDrawable.Orientation.TL_BR,colors7);
        GradientDrawable frame77 = new GradientDrawable(GradientDrawable.Orientation.TL_BR,colors7);


        int[] colors8 = {Color.parseColor("#8E0E00"), Color.parseColor("#1F1C18")};
        GradientDrawable frame8 = new GradientDrawable(GradientDrawable.Orientation.BL_TR,colors8);

        int frame_seconds = 3000;

        AnimationDrawable animationDrawable = new AnimationDrawable();
        animationDrawable.addFrame(frame1,frame_seconds);
        animationDrawable.addFrame(frame2,frame_seconds);
        animationDrawable.addFrame(frame3,frame_seconds);
        animationDrawable.addFrame(frame4,frame_seconds);
        animationDrawable.addFrame(frame5,frame_seconds);
        animationDrawable.addFrame(frame6,frame_seconds);
        animationDrawable.addFrame(frame7,frame_seconds);

        animationDrawable.setEnterFadeDuration(1500);
        animationDrawable.setExitFadeDuration(1500);

        scrollView.setBackground(animationDrawable);

        int frame_seconds1 = 3000;
        AnimationDrawable animationDrawable1 = new AnimationDrawable();
        animationDrawable1.addFrame(frame77,frame_seconds1);
        animationDrawable1.addFrame(frame6,frame_seconds1);
        animationDrawable1.addFrame(frame5,frame_seconds1);
        animationDrawable1.addFrame(frame8,frame_seconds1);
        animationDrawable1.addFrame(frame3,frame_seconds1);
        animationDrawable1.addFrame(frame2,frame_seconds1);
        animationDrawable1.addFrame(frame11,frame_seconds1);
        animationDrawable1.setEnterFadeDuration(1500);
        animationDrawable1.setExitFadeDuration(1500);

        notification_settings_ll.setBackground(animationDrawable1);
        if(easter_egg_clicked){
            animationDrawable.start();
            animationDrawable1.start();
        }else{
            animationDrawable.stop();
            animationDrawable1.stop();
        }


    }

    public void create_battery_settings(LinearLayout animations_linear_layout, SharedPreferences prefs){
        battery_settings_ll = new LinearLayout(getActivity().getApplicationContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(0,20,0,0);
        battery_settings_ll.setLayoutParams(params);
        battery_settings_ll.setOrientation(LinearLayout.VERTICAL);
        battery_settings_ll.setGravity(Gravity.FILL_VERTICAL);
        battery_settings_ll.setBackgroundColor(getResources().getColor(R.color.seperator));
        battery_settings_ll.setPadding(0,0,0,0);
        battery_settings_ll.setBackground(getResources().getDrawable(R.drawable.linearlayoutborder));


        final TextView custom_text_view = new TextView(getActivity().getApplicationContext());
        LinearLayout.LayoutParams custom_text_view_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        custom_text_view_params.setMargins(0,0,0,20);
        custom_text_view.setLayoutParams(custom_text_view_params);
        custom_text_view.setTextColor(getResources().getColor(R.color.colorText));
        custom_text_view.setText("Battery Charging Settings");
        //custom_text_view.setTextSize(custom_text_view.getTextSize()+1);
        custom_text_view.setTypeface(null, Typeface.BOLD);
        custom_text_view.setBackgroundColor(getResources().getColor(R.color.seperator));
        custom_text_view.setGravity(Gravity.CENTER_HORIZONTAL);


        battery_settings_ll.addView(custom_text_view);



        // react to notifications
        CheckBox enable_battery = new CheckBox(getActivity().getApplicationContext());
        enable_battery.setText("Animate Battery Charging");
        //switch_enable_notifications.setThumbDrawable(getResources().getDrawable(R.drawable.asus_rog_logo_scaled));
        enable_battery.setButtonTintList(new ColorStateList(check_box_states,check_box_colors));
        enable_battery.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        enable_battery.setButtonDrawable(R.drawable.asus_rog_logo_scaled);
        enable_battery.setPadding(0,0,0,25);


        boolean animate_battery = prefs.getBoolean(battery_animate_shared_preference_key,false);

        if(animate_battery){
            enable_battery.setTextColor(getResources().getColor(R.color.colorON));
            enable_battery.setChecked(true);
            Intent notification_intent = new Intent(getActivity().getApplicationContext(), BatteryService.class);
            getActivity().getApplicationContext().startService(notification_intent);

        }else{
            enable_battery.setChecked(false);
            enable_battery.setTextColor(getResources().getColor(R.color.colorOFF));
        }

        enable_battery.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences(
                        "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);
                boolean animate_battery = prefs.getBoolean(battery_animate_shared_preference_key,false);
                CheckBox s = (CheckBox) view;

                if(animate_battery){
                    s.setChecked(false);
                    s.setTextColor(getResources().getColor(R.color.colorOFF));
                    prefs.edit().putBoolean(battery_animate_shared_preference_key, false).apply();

                    Intent notification_intent = new Intent(getActivity().getApplicationContext(), BatteryService.class);
                    getActivity().getApplicationContext().stopService(notification_intent);

                }else{
                    s.setChecked(true);
                    s.setTextColor(getResources().getColor(R.color.colorON));
                    prefs.edit().putBoolean(battery_animate_shared_preference_key, true).apply();

                    Intent notification_intent = new Intent(getActivity().getApplicationContext(), BatteryService.class);
                    getActivity().getApplicationContext().startService(notification_intent);
                }

            }
        });

        battery_settings_ll.addView(enable_battery);


        // use second led also for battery
        CheckBox enable_second_led_battery = new CheckBox(getActivity().getApplicationContext());
        enable_second_led_battery.setText("Use second led for battery also");
        enable_second_led_battery.setButtonTintList(new ColorStateList(check_box_states,check_box_colors));
        enable_second_led_battery.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        enable_second_led_battery.setButtonDrawable(R.drawable.asus_rog_logo_scaled);
        enable_second_led_battery.setPadding(0,0,0,25);

        boolean battery_second_led_enabled = prefs.getBoolean(battery_use_second_led_shared_preference_key,false);

        if(battery_second_led_enabled){
            enable_second_led_battery.setTextColor(getResources().getColor(R.color.colorON));
            enable_second_led_battery.setChecked(true);
        }else{
            enable_second_led_battery.setChecked(false);
            enable_second_led_battery.setTextColor(getResources().getColor(R.color.colorOFF));
        }

        enable_second_led_battery.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences(
                        "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);
                boolean battery_second_led_enabled = prefs.getBoolean(battery_use_second_led_shared_preference_key,false);

                CheckBox s = (CheckBox) view;
                if(battery_second_led_enabled){
                    s.setChecked(false);
                    s.setTextColor(getResources().getColor(R.color.colorOFF));
                    prefs.edit().putBoolean(battery_use_second_led_shared_preference_key, false).apply();
                }else{
                    s.setChecked(true);
                    s.setTextColor(getResources().getColor(R.color.colorON));
                    prefs.edit().putBoolean(battery_use_second_led_shared_preference_key, true).apply();
                }
            }
        });

        battery_settings_ll.addView(enable_second_led_battery);


        // use second led for battery only
        CheckBox use_second_led_for_battery_only = new CheckBox(getActivity().getApplicationContext());
        use_second_led_for_battery_only.setText("Use Only the second led for battery");
        use_second_led_for_battery_only.setButtonTintList(new ColorStateList(check_box_states,check_box_colors));
        use_second_led_for_battery_only.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        use_second_led_for_battery_only.setButtonDrawable(R.drawable.asus_rog_logo_scaled);
        use_second_led_for_battery_only.setPadding(0,0,0,25);

        boolean battery_second_led_enabled_only = prefs.getBoolean(battery_use_second_led_only_shared_preference_key,false);

        if(battery_second_led_enabled_only){
            use_second_led_for_battery_only.setTextColor(getResources().getColor(R.color.colorON));
            use_second_led_for_battery_only.setChecked(true);
        }else{
            use_second_led_for_battery_only.setChecked(false);
            use_second_led_for_battery_only.setTextColor(getResources().getColor(R.color.colorOFF));
        }

        use_second_led_for_battery_only.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences(
                        "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);
                boolean battery_second_led_enabled_only = prefs.getBoolean(battery_use_second_led_only_shared_preference_key,false);
                CheckBox s = (CheckBox) view;
                if(battery_second_led_enabled_only){
                    s.setChecked(false);
                    s.setTextColor(getResources().getColor(R.color.colorOFF));
                    prefs.edit().putBoolean(battery_use_second_led_only_shared_preference_key, false).apply();
                }else{
                    s.setChecked(true);
                    s.setTextColor(getResources().getColor(R.color.colorON));
                    prefs.edit().putBoolean(battery_use_second_led_only_shared_preference_key, true).apply();
                }

            }
        });

        battery_settings_ll.addView(use_second_led_for_battery_only);


        // which animation to use for notifications
        Spinner batteryAnimationSelector = new Spinner(getActivity().getApplicationContext());
        String[] animation_items = {"solid one colour","breathing one colour"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, animation_items);


        adapter.setDropDownViewResource(R.layout.spinner_text);

        batteryAnimationSelector.setAdapter(adapter);

        batteryAnimationSelector.setPrompt("Select Animation to use for Notifications");

        int notifications_animation = prefs.getInt(battery_animation_mode_shared_preference_key,1);
        batteryAnimationSelector.setSelection(notifications_animation-1,true);


        batteryAnimationSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int animation_mode = i+1;

                TextView selected = ((TextView) adapterView.getChildAt(0));
                selected.setTextColor(getResources().getColor(R.color.colorText));
                selected.setText("selected animation: "+selected.getText());

                SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences(
                        "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);
                prefs.edit().putInt(battery_animation_mode_shared_preference_key, animation_mode).apply();


                //((TextView) adapterView.getChildAt(0)).setTextSize(5);

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        battery_settings_ll.addView(batteryAnimationSelector);


        animations_linear_layout.addView(battery_settings_ll);
    }

    public void create_help_view(LinearLayout animations_linear_layout, SharedPreferences prefs){

        help_ll = new LinearLayout(getActivity().getApplicationContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(0,10,0,10);
        help_ll.setLayoutParams(params);
        help_ll.setOrientation(LinearLayout.VERTICAL);
        help_ll.setGravity(Gravity.FILL_VERTICAL);
        help_ll.setBackgroundColor(getResources().getColor(R.color.seperator));
        help_ll.setPadding(0,0,0,0);
        help_ll.setBackground(getResources().getDrawable(R.drawable.linearlayoutborder));

        final TextView custom_text_view = new TextView(getActivity().getApplicationContext());
        LinearLayout.LayoutParams custom_text_view_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        custom_text_view_params.setMargins(0,0,0,20);
        custom_text_view.setLayoutParams(custom_text_view_params);
        custom_text_view.setTextColor(getResources().getColor(R.color.colorText));
        custom_text_view.setText("Tutorial");
        //custom_text_view.setTextSize(custom_text_view.getTextSize()+1);
        custom_text_view.setTypeface(null, Typeface.BOLD);
        custom_text_view.setBackgroundColor(getResources().getColor(R.color.seperator));
        custom_text_view.setGravity(Gravity.CENTER_HORIZONTAL);

        help_ll.addView(custom_text_view);

        final TextView line1 = new TextView(getActivity().getApplicationContext());
        line1.setLayoutParams(custom_text_view_params);
        line1.setTextColor(getResources().getColor(R.color.colorText));
        line1.setText("For a full guide on how to use this app please watch the video bellow");
        line1.setBackgroundColor(getResources().getColor(R.color.seperator));
        line1.setGravity(Gravity.CENTER_HORIZONTAL);

        help_ll.addView(line1);

        final TextView line2 = new TextView(getActivity().getApplicationContext());
        line2.setLayoutParams(custom_text_view_params);
        line2.setTextColor(getResources().getColor(R.color.colorText));
        line2.setText("And yes the LEDs can light up during a phone call you just need to select your phone app in the \"select which apps trigger notifications\" setting, its as simple as that.");
        line2.setBackgroundColor(getResources().getColor(R.color.seperator));
        line2.setGravity(Gravity.CENTER_HORIZONTAL);

        help_ll.addView(line2);

        Button hide_help_button = new Button(getActivity().getApplicationContext());
        hide_help_button.setText("I know what I am doing. Hide this section");
        hide_help_button.setTextColor(getResources().getColor(R.color.colorText));
        hide_help_button.setBackgroundColor(getResources().getColor(R.color.colorButton));

        hide_help_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences(
                        "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);
                prefs.edit().putBoolean(hide_help_animations_preference_key, true).apply();
                help_ll.setVisibility(View.GONE);
                show_help_button.setVisibility(View.VISIBLE);
                new AlertDialog.Builder(requireActivity())
                        .setTitle("Help Section is Hidden Now")
                        .setMessage("To show the help section again scroll down to the very end and click on show help button")
                        .setCancelable(false)
                        .setPositiveButton("Sounds good thanks", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            }});

        help_ll.addView(hide_help_button);

        TextView webviewText = new TextView(getActivity().getApplicationContext());
        webviewText.setLayoutParams(custom_text_view_params);
        webviewText.setTextColor(getResources().getColor(R.color.colorText));
        webviewText.setText("Loading Video 0%");
        webviewText.setBackgroundColor(getResources().getColor(R.color.seperator));
        webviewText.setGravity(Gravity.LEFT);

        help_ll.addView(webviewText);

        WebView webView = new WebView(getActivity().getApplicationContext());

        LinearLayout.LayoutParams webview_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        webview_params.setMargins(0,20,0,20);
        webView.setLayoutParams(webview_params);

        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.clearCache(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.getSettings().setForceDark(WebSettings.FORCE_DARK_ON);
        webView.setVisibility(View.GONE);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                Log.d("progress",""+progress);
                webviewText.setText("Loading Video "+progress+"%");
                if (progress == 100 && !helpWebViewPageNotAvailable) { //...page is fully loaded.
                    webView.setVisibility(View.VISIBLE);
                    webviewText.setVisibility(View.GONE);
                }else if(!helpWebViewPageNotAvailable){

                }

                if(helpWebViewPageNotAvailable){
                    webviewText.setText("Failed to load video. Check your internet");
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                Log.d("Title Change", title);
                if(title.equals("Web page not available")){
                    helpWebViewPageNotAvailable = true;
                    webviewText.setText("Failed to load video. Check your internet");
                }
            }
        });
        String videoStr = "<html style=\"padding:0px;margin:0px;\"><body style=\"padding:0px;margin:0px;\"><iframe width=\"100%\" height=\"240\" src=\"https://www.youtube.com/embed/"+help_video_id+"\" style=\"padding:0px;margin:0px;border:0\" allowfullscreen></iframe></body></html>";
        webView.loadData(videoStr, "text/html", "utf-8");

        help_ll.addView(webView);

        final boolean hide_this = prefs.getBoolean(hide_help_animations_preference_key, false);
        if(hide_this){
            help_ll.setVisibility(View.GONE);
        }

        animations_linear_layout.addView(help_ll);
    }
//
//    public void create_visualiser_settings(LinearLayout animations_linear_layout, SharedPreferences prefs){
//        visualiser_settings_ll = new LinearLayout(getActivity().getApplicationContext());
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
//        params.setMargins(0,20,0,20);
//        visualiser_settings_ll.setLayoutParams(params);
//        visualiser_settings_ll.setOrientation(LinearLayout.VERTICAL);
//        visualiser_settings_ll.setGravity(Gravity.FILL_VERTICAL);
//        visualiser_settings_ll.setBackgroundColor(getResources().getColor(R.color.seperator));
//        visualiser_settings_ll.setPadding(0,20,0,20);
//
//
//        TextView custom_text_view = new TextView(getActivity().getApplicationContext());
//        LinearLayout.LayoutParams custom_text_view_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
//        custom_text_view_params.setMargins(0,0,0,20);
//        custom_text_view.setLayoutParams(custom_text_view_params);
//        custom_text_view.setTextColor(getResources().getColor(R.color.colorText));
//        custom_text_view.setText("Audio Visualiser Settings");
//        //custom_text_view.setTextSize(custom_text_view.getTextSize()+1);
//        custom_text_view.setTypeface(null, Typeface.BOLD);
//        custom_text_view.setBackgroundColor(getResources().getColor(R.color.seperator));
//        custom_text_view.setGravity(Gravity.CENTER_HORIZONTAL);
//
//
//        visualiser_settings_ll.addView(custom_text_view);
//
//
//        CheckBox enable_visualiser = new CheckBox(getActivity().getApplicationContext());
//        enable_visualiser.setText("Enable Visualiser");
//        enable_visualiser.setButtonTintList(new ColorStateList(check_box_states,check_box_colors));
//        enable_visualiser.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
//        enable_visualiser.setButtonDrawable(R.drawable.asus_rog_logo_scaled);
//        enable_visualiser.setPadding(0,0,0,25);
//
//
//        boolean enable_visualiser_test = prefs.getBoolean(visualiser_on_shared_preference_key,false);
//
//        if(enable_visualiser_test){
//            enable_visualiser.setTextColor(getResources().getColor(R.color.colorON));
//            enable_visualiser.setChecked(true);
//            Intent notification_intent = new Intent(getActivity().getApplicationContext(), VisualiserService.class);
//            getActivity().getApplicationContext().startService(notification_intent);
//
//        }else{
//            enable_visualiser.setChecked(false);
//            enable_visualiser.setTextColor(getResources().getColor(R.color.colorOFF));
//            Intent notification_intent = new Intent(getActivity().getApplicationContext(), VisualiserService.class);
//            getActivity().getApplicationContext().stopService(notification_intent);
//        }
//
//        enable_visualiser.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences(
//                        "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);
//                boolean animate_battery = prefs.getBoolean(visualiser_on_shared_preference_key,false);
//                CheckBox s = (CheckBox) view;
//
//                if(animate_battery){
//                    s.setChecked(false);
//                    s.setTextColor(getResources().getColor(R.color.colorOFF));
//                    prefs.edit().putBoolean(visualiser_on_shared_preference_key, false).apply();
//                    Intent notification_intent = new Intent(getActivity().getApplicationContext(), VisualiserService.class);
//                    getActivity().getApplicationContext().stopService(notification_intent);
//
//                }else{
//                    s.setChecked(true);
//                    s.setTextColor(getResources().getColor(R.color.colorON));
//                    prefs.edit().putBoolean(visualiser_on_shared_preference_key, true).apply();
//
//                    Intent notification_intent = new Intent(getActivity().getApplicationContext(), VisualiserService.class);
//                    getActivity().getApplicationContext().startService(notification_intent);
//                }
//
//            }
//        });
//
//        visualiser_settings_ll.addView(enable_visualiser);
//
//
//        // use second led also for battery
//        CheckBox enable_second_led_visualiser = new CheckBox(getActivity().getApplicationContext());
//        enable_second_led_visualiser.setText("Use second led for visualiser also");
//        enable_second_led_visualiser.setButtonTintList(new ColorStateList(check_box_states,check_box_colors));
//        enable_second_led_visualiser.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
//        enable_second_led_visualiser.setButtonDrawable(R.drawable.asus_rog_logo_scaled);
//        enable_second_led_visualiser.setPadding(0,0,0,25);
//
//        boolean second_led_enabled = prefs.getBoolean(visualiser_use_second_led_shared_preference_key,false);
//
//        if(second_led_enabled){
//            enable_second_led_visualiser.setTextColor(getResources().getColor(R.color.colorON));
//            enable_second_led_visualiser.setChecked(true);
//        }else{
//            enable_second_led_visualiser.setChecked(false);
//            enable_second_led_visualiser.setTextColor(getResources().getColor(R.color.colorOFF));
//        }
//
//        enable_second_led_visualiser.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences(
//                        "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);
//                boolean second_led_enabled = prefs.getBoolean(visualiser_use_second_led_shared_preference_key,false);
//
//                CheckBox s = (CheckBox) view;
//                if(second_led_enabled){
//                    s.setChecked(false);
//                    s.setTextColor(getResources().getColor(R.color.colorOFF));
//                    prefs.edit().putBoolean(visualiser_use_second_led_shared_preference_key, false).apply();
//                }else{
//                    s.setChecked(true);
//                    s.setTextColor(getResources().getColor(R.color.colorON));
//                    prefs.edit().putBoolean(visualiser_use_second_led_shared_preference_key, true).apply();
//                }
//            }
//        });
//
//        visualiser_settings_ll.addView(enable_second_led_visualiser);
//
//
//        // use second led for battery only
//        CheckBox use_second_led_for_visualiser_only = new CheckBox(getActivity().getApplicationContext());
//        use_second_led_for_visualiser_only.setText("Use Only the second led for the visualiser");
//        use_second_led_for_visualiser_only.setButtonTintList(new ColorStateList(check_box_states,check_box_colors));
//        use_second_led_for_visualiser_only.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
//        use_second_led_for_visualiser_only.setButtonDrawable(R.drawable.asus_rog_logo_scaled);
//        use_second_led_for_visualiser_only.setPadding(0,0,0,25);
//
//        boolean second_led_enabled_only = prefs.getBoolean(visualiser_use_second_led_only_shared_preference_key,false);
//
//        if(second_led_enabled_only){
//            use_second_led_for_visualiser_only.setTextColor(getResources().getColor(R.color.colorON));
//            use_second_led_for_visualiser_only.setChecked(true);
//        }else{
//            use_second_led_for_visualiser_only.setChecked(false);
//            use_second_led_for_visualiser_only.setTextColor(getResources().getColor(R.color.colorOFF));
//        }
//
//        use_second_led_for_visualiser_only.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences(
//                        "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);
//                boolean second_led_enabled_only = prefs.getBoolean(visualiser_use_second_led_only_shared_preference_key,false);
//                CheckBox s = (CheckBox) view;
//                if(second_led_enabled_only){
//                    s.setChecked(false);
//                    s.setTextColor(getResources().getColor(R.color.colorOFF));
//                    prefs.edit().putBoolean(visualiser_use_second_led_only_shared_preference_key, false).apply();
//                }else{
//                    s.setChecked(true);
//                    s.setTextColor(getResources().getColor(R.color.colorON));
//                    prefs.edit().putBoolean(visualiser_use_second_led_only_shared_preference_key, true).apply();
//                }
//
//            }
//        });
//
//        visualiser_settings_ll.addView(use_second_led_for_visualiser_only);
//
//
//        // which animation to use for notifications
//        Spinner VisualiserModeSelector = new Spinner(getActivity().getApplicationContext());
//        String[] animation_items = {"wave to hue","wave to lightness (pick colour from colour wheel tab)"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, animation_items);
//
//
//        adapter.setDropDownViewResource(R.layout.spinner_text);
//
//        VisualiserModeSelector.setAdapter(adapter);
//
//        VisualiserModeSelector.setPrompt("Select Animation to use for Audio");
//
//        int notifications_animation = prefs.getInt(visualiser_animation_mode_shared_preference_key,1);
//        VisualiserModeSelector.setSelection(notifications_animation-1,true);
//
//
//        VisualiserModeSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                int animation_mode = i+1;
//
//                TextView selected = ((TextView) adapterView.getChildAt(0));
//                selected.setTextColor(getResources().getColor(R.color.colorText));
//                selected.setText("selected animation: "+selected.getText());
//
//                SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences(
//                        "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);
//                prefs.edit().putInt(visualiser_animation_mode_shared_preference_key, animation_mode).apply();
//
//
//                //((TextView) adapterView.getChildAt(0)).setTextSize(5);
//
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//
//        visualiser_settings_ll.addView(VisualiserModeSelector);
//
//
//        animations_linear_layout.addView(visualiser_settings_ll);
//    }
//




//    public void create_miscellaneous_settings(LinearLayout animations_linear_layout, SharedPreferences prefs){
//        LinearLayout miscellaneous_settings_ll = new LinearLayout(getActivity().getApplicationContext());
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
//        params.setMargins(0,20,0,20);
//        miscellaneous_settings_ll.setLayoutParams(params);
//        miscellaneous_settings_ll.setOrientation(LinearLayout.VERTICAL);
//        miscellaneous_settings_ll.setGravity(Gravity.FILL_VERTICAL);
//        miscellaneous_settings_ll.setBackgroundColor(getResources().getColor(R.color.seperator));
//        miscellaneous_settings_ll.setPadding(0,20,0,20);
//
//
//        TextView custom_text_view = new TextView(getActivity().getApplicationContext());
//        LinearLayout.LayoutParams custom_text_view_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
//        custom_text_view_params.setMargins(0,0,0,20);
//        custom_text_view.setLayoutParams(custom_text_view_params);
//        custom_text_view.setTextColor(getResources().getColor(R.color.colorText));
//        custom_text_view.setText("Miscellaneous Settings");
//        //custom_text_view.setTextSize(custom_text_view.getTextSize()+1);
//        custom_text_view.setTypeface(null, Typeface.BOLD);
//        custom_text_view.setBackgroundColor(getResources().getColor(R.color.seperator));
//        custom_text_view.setGravity(Gravity.CENTER_HORIZONTAL);
//
//
//        miscellaneous_settings_ll.addView(custom_text_view);
//
//
//        CheckBox enable_shake = new CheckBox(getActivity().getApplicationContext());
//        enable_shake.setText("Enable Triple Shake");
//        enable_shake.setButtonTintList(new ColorStateList(check_box_states,check_box_colors));
//        enable_shake.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
//        enable_shake.setButtonDrawable(R.drawable.asus_rog_logo_scaled);
//        enable_shake.setPadding(0,0,0,25);
//
//
//        boolean enable_shake_on = prefs.getBoolean(shake_on_shared_preference_key,false);
//
//        if(enable_shake_on){
//            enable_shake.setTextColor(getResources().getColor(R.color.colorON));
//            enable_shake.setChecked(true);
//
//            Intent shake_intent = new Intent(getActivity().getApplicationContext(), ShakeService.class);
//            getActivity().getApplicationContext().startService(shake_intent);
//
//        }else{
//            enable_shake.setChecked(false);
//            enable_shake.setTextColor(getResources().getColor(R.color.colorOFF));
//            Intent shake_intent = new Intent(getActivity().getApplicationContext(), ShakeService.class);
//            getActivity().getApplicationContext().stopService(shake_intent);
//        }
//
//        enable_shake.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences(
//                        "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);
//                boolean animate_battery = prefs.getBoolean(shake_on_shared_preference_key,false);
//                CheckBox s = (CheckBox) view;
//
//                if(animate_battery){
//                    s.setChecked(false);
//                    s.setTextColor(getResources().getColor(R.color.colorOFF));
//                    prefs.edit().putBoolean(shake_on_shared_preference_key, false).apply();
//                    Intent shake_intent = new Intent(getActivity().getApplicationContext(), ShakeService.class);
//                    getActivity().getApplicationContext().stopService(shake_intent);
//
//                }else{
//                    s.setChecked(true);
//                    s.setTextColor(getResources().getColor(R.color.colorON));
//                    prefs.edit().putBoolean(shake_on_shared_preference_key, true).apply();
//
//                    Intent shake_intent = new Intent(getActivity().getApplicationContext(), ShakeService.class);
//                    getActivity().getApplicationContext().startService(shake_intent);
//
//                    boolean launched_first_time = prefs.getBoolean(shake_on_first_launch_shared_preference_key,false);
//                    if(!launched_first_time){
//                        prefs.edit().putBoolean(shake_on_first_launch_shared_preference_key, true).apply();
//                        new AlertDialog.Builder(requireActivity())
//                                .setTitle("What is Triple Shake Feature?")
//                                .setMessage("Triple Shake allows you to quickly shake your phone 3 times to toggle the second led.\n\nThe second led will use whatever mode you selected above at the very top of this page.\n\nIt will also use the colour you selected in the colour wheel for the appropriate modes.\n\nI made this because I use the second led as a flashlight sometimes so this is a quick shortcut lol")
//                                .setCancelable(false)
//                                .setPositiveButton("I understand now", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                    }
//                                })
//                                .show();
//                    }
//
//                }
//
//            }
//        });
//
//        miscellaneous_settings_ll.addView(enable_shake);
//
//        animations_linear_layout.addView(miscellaneous_settings_ll);
//    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putInt("current_selected", current_selected);
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}