package terminal_heat_sink.asusrogphone2rgb;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;

public class AnimationsActivity extends Fragment {
    public AnimationsActivity() {
        // Required empty public constructor
    }
    private int current_selected = 0;
    private String current_selected_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.current_selected";

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

        String[][] options = {
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


        final Switch[] switches = new Switch[options.length];

        for (int i = 0; i < options.length; i++) {

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
            sw.setText(options[i][1]);

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

        return root;
    }



    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putInt("current_selected", current_selected);
    }

}