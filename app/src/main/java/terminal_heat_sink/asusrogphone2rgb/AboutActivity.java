package terminal_heat_sink.asusrogphone2rgb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class AboutActivity extends Fragment {
    private ScrollView scrollView;

    //check if magisk mode
    private final String magisk_mode_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.magiskmode";
    private boolean pageNotAvailable = false;
    private String mainTextString;

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

        ImageView donate_app_view = (ImageView) root.findViewById(R.id.imageViewDonateAppGoogle);

        donate_app_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(
                        "https://play.google.com/store/apps/details?id=terminal_heat_sink.donateterminalheatsink"));
                intent.setPackage("com.android.vending");
                startActivity(intent);
            }
        });

        ImageView donate_google_view = (ImageView) root.findViewById(R.id.imageViewDonateGoogle);

        donate_google_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(
                        "https://play.google.com/store/apps/details?id=terminal_heat_sink.donateterminalheatsink"));
                intent.setPackage("com.android.vending");
                startActivity(intent);
            }
        });

        mainTextString = getString(R.string.about_text);

        TextView text = (TextView) root.findViewById(R.id.textViewAbout);
        text.setTextColor(getResources().getColor(R.color.colorText));
        text.setText(mainTextString + " ( 0% )");


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

        WebView webView = root.findViewById(R.id.webview);

        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.clearCache(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.getSettings().setForceDark(WebSettings.FORCE_DARK_ON);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                Log.d("progress",""+progress);
                text.setText(mainTextString + " ( "+progress+"% )");
                if (progress == 100 && !pageNotAvailable) { //...page is fully loaded.
                    webView.setVisibility(View.VISIBLE);
                    scrollView.smoothScrollTo(0, 1100);
                    text.setVisibility(View.GONE);
                }else if(!pageNotAvailable){

                }

                if(pageNotAvailable){
                    text.setText(mainTextString + " ( Oops failed to load page. Check your internet )");
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                Log.d("Title Change", title);
                if(title.equals("Web page not available")){
                    pageNotAvailable = true;
                    text.setText(mainTextString + " ( Oops failed to load page. Check your internet )");
                }
            }
        });
        webView.loadUrl("https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB");


        scrollView.smoothScrollTo(0,0);

        return root;
    }
}

