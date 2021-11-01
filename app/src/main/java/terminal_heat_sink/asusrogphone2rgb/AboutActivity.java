package terminal_heat_sink.asusrogphone2rgb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class AboutActivity extends Fragment {
    private ScrollView scrollView;

    //check if magisk mode
    private final String magisk_mode_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.magiskmode";

    public AboutActivity() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_about, container, false);

        ImageView telegram_image_view = (ImageView) root.findViewById(R.id.imageViewTelegram);

        telegram_image_view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://t.me/Terminal_Heat_Sink_Group"));
                startActivity(intent);
            }
        });

        ImageView paypal_image_view = (ImageView) root.findViewById(R.id.imageViewPaypal);

        paypal_image_view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.paypal.me/artiomSudo"));
                startActivity(intent);
            }
        });

        ImageView patreon_image_view = (ImageView) root.findViewById(R.id.imageViewPatreon);

        patreon_image_view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.patreon.com/user?u=28429069"));
                startActivity(intent);
            }
        });

        ImageView github_image_view = (ImageView) root.findViewById(R.id.imageViewGithub);

        github_image_view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://github.com/ArtiomSu"));
                startActivity(intent);
            }
        });

        ImageView youtube_image_view = (ImageView) root.findViewById(R.id.imageViewYoutube);

        youtube_image_view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.youtube.com/c/TerminalHeatSink"));
                startActivity(intent);
            }
        });



        TextView text = (TextView) root.findViewById(R.id.textViewAbout);
        text.setTextColor(getResources().getColor(R.color.colorText));
        text.setText(R.string.about_text);


        scrollView = (ScrollView) root.findViewById(R.id.scrollViewabout);
        ImageView easterEgg = (ImageView) root.findViewById(R.id.eastereggabout);

        easterEgg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //scrollView.fullScroll(ScrollView.FOCUS_UP);
                scrollView.smoothScrollTo(0,0);
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.youtube.com/watch?v=yX8yrOAjfKM"));
                startActivity(intent);
            }
        });

        Button export_settings = (Button) root.findViewById(R.id.export_settings);
        export_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SystemWriter.save_shared_preferences(getActivity().getApplicationContext());
                Toast.makeText(getActivity().getApplicationContext(), "Settings Saved", Toast.LENGTH_LONG).show();
            }
        });

        Button import_settings = (Button) root.findViewById(R.id.import_settings);
        import_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SystemWriter.restore_shared_preferences(getActivity().getApplicationContext());
                Intent i = getActivity().getApplicationContext().getPackageManager().
                        getLaunchIntentForPackage(getActivity().getApplicationContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                System.exit(0);
            }
        });

        Context context = getContext();
        SharedPreferences prefs = context.getSharedPreferences(
                "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);

        boolean running_in_magisk_mode = prefs.getBoolean(magisk_mode_shared_preference_key, false);

        Button convert_to_magisk = (Button) root.findViewById(R.id.convert_to_magisk);
        convert_to_magisk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PackageManager pm = getContext().getPackageManager();
                String path = "";
                for (ApplicationInfo app : pm.getInstalledApplications(0)) {
                    if(app.packageName.equals("terminal_heat_sink.asusrogphone2rgb")){
                        path = app.sourceDir;
                    }
                }
                SystemWriter.create_magisk_module(getContext(),path);
            }
        });

        if(running_in_magisk_mode){
            convert_to_magisk.setVisibility(View.GONE);
        }

        scrollView.smoothScrollTo(0,0);

        return root;
    }
}

