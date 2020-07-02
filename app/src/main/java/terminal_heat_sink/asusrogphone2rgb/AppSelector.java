package terminal_heat_sink.asusrogphone2rgb;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AppSelector extends AppCompatActivity {

    private String TAG = "AsusRogPhone2RGBAppSelector";
    private String apps_selected_for_notifications_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.notifications_apps_selected";
    private Set<String> apps_to_notify;

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

        TextView header_text = new TextView(context);
        header_text.setText("Select Apps");
        header_text.setTextSize(header_text.getTextSize()+1);
        header_text.setTextColor(getResources().getColor(R.color.colorText));
        header_text.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        header_text.setPadding(10,10,10,10);
        header_text.setGravity(Gravity.CENTER);

        app_selector_ll.addView(header_text);

        final PackageManager pm = getPackageManager();
//get a list of installed apps.
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        //Map package_names = new HashMap();
        ArrayList<String> package_names = new ArrayList<>();

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

            LinearLayout enclosure = new LinearLayout(context);
            enclosure.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams encloseure_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
            encloseure_params.setMargins(10,20,10,0);

            enclosure.setLayoutParams(encloseure_params);




            enclosure.addView(icon_view);
            enclosure.addView(box);

            app_selector_ll.addView(enclosure);






            box = ((CheckBox) findViewById(id_));

            int states[][] = {{android.R.attr.state_checked}, {}};
            int colors[] = {getResources().getColor(R.color.colorON), getResources().getColor(R.color.colorOFF)};
            box.setButtonTintList(new ColorStateList(states,colors));
            box.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

            if(apps_to_notify.contains(package_names.get(i))){
                box.setChecked(true);
                box.setTextColor(getResources().getColor(R.color.colorON));
            }else{
                box.setChecked(false);
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
                        box.setTextColor(getResources().getColor(R.color.colorON));
                        if(!apps_to_notify.contains(box.getText().toString())){
                            apps_to_notify.add(box.getText().toString());
                            prefs.edit().putStringSet(apps_selected_for_notifications_shared_preference_key,apps_to_notify).apply();
                            Log.i(TAG, "adding to list "+box.getText().toString());
                        }
                    }else{
                        box.setChecked(false);
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
