package terminal_heat_sink.asusrogphone2rgb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AppSelector extends AppCompatActivity {

    private String TAG = "AsusRogPhone2RGBAppSelector";
    private String apps_selected_for_notifications_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.notifications_apps_selected";
    private Set<String> apps_to_notify;

    private ArrayList<String> package_names;
    private ArrayList<LinearLayout> packages_enclosure_ll = new ArrayList<>();

    private Bitmap getBitmapFromDrawable( Drawable drawable) {
        final Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bmp);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bmp;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_selector);

        Context context = getApplicationContext();

        SharedPreferences prefs = context.getSharedPreferences(
                "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);

        apps_to_notify = prefs.getStringSet(apps_selected_for_notifications_shared_preference_key,null);
        if(apps_to_notify == null){
            Log.i(TAG, "Didn't find any apps in notification list so creating new one ");
            apps_to_notify = new HashSet<String>();
        }


        LinearLayout app_selector_ll = (LinearLayout) findViewById(R.id.app_selector_ll);
        app_selector_ll.setBackgroundColor(getResources().getColor(R.color.colorBG));
        //app_selector_ll.setPadding(10,10,10,10);

        TextView header_text = findViewById(R.id.text_view_app_selector);
        header_text.setText("Select Apps\nClick on App Icon to set custom animations");
        header_text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25f);
        header_text.setTextColor(getResources().getColor(R.color.colorText));
        header_text.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        header_text.setPadding(10,10,10,10);
        header_text.setGravity(Gravity.CENTER);


        TextInputEditText filter_text = findViewById(R.id.text_input_app_selector);
        filter_text.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        filter_text.setTextColor(getResources().getColor(R.color.colorText));
        filter_text.setHintTextColor(getResources().getColor(R.color.colorText));
        filter_text.setLines(1);
        filter_text.setCursorVisible(false);
        filter_text.setSingleLine(true);




        filter_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i("AppSelector",charSequence.toString());

                for(int package_num=0; package_num<package_names.size(); package_num++){
                    if(package_names.get(package_num).contains(charSequence.toString().toLowerCase())){
                        //show packages
                        packages_enclosure_ll.get(package_num).setVisibility(View.VISIBLE);
                    }else{
                        //hide packages
                        packages_enclosure_ll.get(package_num).setVisibility(View.GONE);
                        //Log.i("AppSelector","hiding package "+package_names.get(package_num));
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        final PackageManager pm = getPackageManager();
        //get a list of installed apps.
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        //Map package_names = new HashMap();
        package_names = new ArrayList<>();

        for (ApplicationInfo packageInfo : packages) {
            //Log.d(TAG, "Installed package :" + packageInfo.packageName);
            package_names.add(packageInfo.packageName);


        }


        final CheckBox[] boxes = new CheckBox[package_names.size()];

        for (int i = 0; i < package_names.size(); i++) {

            CheckBox box = new CheckBox(context);
            box.setId(i);
            final int id_ = box.getId();

            box.setText(package_names.get(i));
            LinearLayout.LayoutParams box_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
            box_params.setMargins(20,0,0,0);
            box.setLayoutParams(box_params);


            boxes[i] = box;


            ImageView icon_view = new ImageView(context);
            try
            {
                Drawable icon = context.getPackageManager().getApplicationIcon(box.getText().toString());

                Bitmap b = getBitmapFromDrawable(icon);
                Bitmap bitmapResized = Bitmap.createScaledBitmap(b, 100, 100, false);
                icon = new BitmapDrawable(getResources(), bitmapResized);

                icon_view.setImageDrawable(icon);



            }
            catch (PackageManager.NameNotFoundException e)
            {
            }
            icon_view.setId(1000+i);

            icon_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageView img = (ImageView) view;
                    int id = (img.getId()) - 1000;
                    //Log.i("Appselector","image click on "+ package_names.get(id));
                    Intent intent = new Intent(getApplicationContext(), PerAppCustomisations.class);
                    intent.putExtra("package_name",package_names.get(id));
                    startActivity(intent);
                }
            });


            LinearLayout enclosure = new LinearLayout(context);
            enclosure.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams encloseure_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
            encloseure_params.setMargins(10,20,10,0);

            enclosure.setLayoutParams(encloseure_params);




            enclosure.addView(icon_view);
            enclosure.addView(box);
                packages_enclosure_ll.add(enclosure);

            app_selector_ll.addView(enclosure);


            box = ((CheckBox) findViewById(id_));

            int states[][] = {{android.R.attr.state_checked}, {}};
            int colors[] = {getResources().getColor(R.color.colorON), getResources().getColor(R.color.colorDisabled)};
            box.setButtonTintList(new ColorStateList(states,colors));
            box.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            box.setButtonDrawable(R.drawable.asus_rog_logo_scaled);

            if(apps_to_notify.contains(package_names.get(i))){
                box.setChecked(true);

                box.setTextColor(getResources().getColor(R.color.colorON));
            }else{
                box.setChecked(false);
                //box.setButtonDrawable(R.drawable.empty_check_box);
                box.setTextColor(getResources().getColor(R.color.colorOFF));
            }

            box.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    CheckBox box = (CheckBox) view;

                    SharedPreferences prefs = getApplicationContext().getSharedPreferences(
                            "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);

                    apps_to_notify = prefs.getStringSet(apps_selected_for_notifications_shared_preference_key,null);
                    if(apps_to_notify == null){
                        Log.i(TAG, "Didn't find any apps in notification list so creating new one ");
                        apps_to_notify = new HashSet<String>();
                    }

                    if(box.isChecked()){
                        box.setChecked(true);
                        //box.setButtonDrawable(R.drawable.asus_rog_logo_scaled);
                        box.setTextColor(getResources().getColor(R.color.colorON));
                        if(!apps_to_notify.contains(box.getText().toString())){
                            apps_to_notify.add(box.getText().toString());
                            prefs.edit().putStringSet(apps_selected_for_notifications_shared_preference_key,apps_to_notify).apply();
                            Log.i(TAG, "adding to list "+box.getText().toString());
                        }
                    }else{
                        box.setChecked(false);
                        //box.setButtonDrawable(R.drawable.empty_check_box);
                        box.setTextColor(getResources().getColor(R.color.colorOFF));
                        if(apps_to_notify.contains(box.getText().toString())){
                            apps_to_notify.remove(box.getText().toString());
                            prefs.edit().putStringSet(apps_selected_for_notifications_shared_preference_key,apps_to_notify).apply();
                            Log.i(TAG, "removing from list "+box.getText().toString());
                        }
                    }


                    Log.d(TAG, "clicked on :" + box.getText() + " ticked:" + box.isChecked());


                }
            });
        }



    }

}
