package terminal_heat_sink.asusrogphone2rgb;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import top.defaults.colorpicker.ColorObserver;
import top.defaults.colorpicker.ColorPickerView;

public class ColourWheelActivity extends Fragment {

    private static final String SAVED_STATE_KEY_COLOR = "saved_state_key_color";
    private static final String SAVED_PREFS_KEY_COLOR = "saved_prefs_key_color";

    ColorPickerView colorPickerView;

    public ColourWheelActivity() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_colour_wheel, container, false);

        LinearLayout ll = (LinearLayout) root.findViewById(R.id.colorPickerLinear);

        colorPickerView = new ColorPickerView(getActivity().getApplicationContext());

        ll.addView(colorPickerView);

        colorPickerView.setEnabledAlpha(false);
        colorPickerView.setEnabledBrightness(false);

        colorPickerView.subscribe(new ColorObserver() {
            @Override
            public void onColor(int color, boolean fromUser, boolean shouldPropagate) {
                SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences(
                        "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);

                //check if the same colour is already applied
                int previous_color = prefs.getInt(SAVED_PREFS_KEY_COLOR,0);
                if(previous_color != color || previous_color == 0){
                    prefs.edit().putInt(SAVED_PREFS_KEY_COLOR, color).apply();
                    //Color c = Color.valueOf(color);
                    //Log.e("color"," color picked "+color + " RGB is "+ Color.red(color)+ " " + Color.green(color)+ " "+ Color.blue(color));

                    SystemWriter.write_colour(Color.red(color),Color.green(color),Color.blue(color),getActivity().getApplicationContext());
                }

            }
        });

        int color = 0;
        SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences(
                "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);

        color = prefs.getInt(SAVED_PREFS_KEY_COLOR,0);
        if(color == 0){
            if (savedInstanceState != null) {
                color = savedInstanceState.getInt(SAVED_STATE_KEY_COLOR, 0xFFFF8000);
            }else{
                color = -1031;
            }
        }

        colorPickerView.setInitialColor(color);

        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_STATE_KEY_COLOR, colorPickerView.getColor());
    }
}

