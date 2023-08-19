package terminal_heat_sink.asusrogphone2rgb;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
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

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AboutActivity extends Fragment {
    private ScrollView scrollView;

    //check if magisk mode
    private final String magisk_mode_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.magiskmode";
    private static String is_root_mode_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.isrootmode";
    private boolean pageNotAvailable = false;
    private String mainTextString;
    private static final int PERM_REQUEST = 112;
    private static final int PERM_REQUEST_READ = 113;
    public AboutActivity() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_about, container, false);

        Context context = getContext();
        SharedPreferences prefs = context.getSharedPreferences(
                "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);

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
                new AlertDialog.Builder(context)
                        .setTitle("Export Settings")
                        .setMessage("Root Mode is the original way for exporting (recommended)\nIf you are using this app without root then it will save it to downloads.")
                        .setCancelable(true)
                        .setPositiveButton("Export to Downloads", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if(request_perms(context,PERM_REQUEST)){
                                    savePrefs(prefs);
                                }
                            }
                        })
                        .setNegativeButton("Root Mode", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                SystemWriter.save_shared_preferences(getActivity().getApplicationContext());
                                Toast.makeText(getActivity().getApplicationContext(), "Settings Saved", Toast.LENGTH_LONG).show();
                            }
                        })
                        .show();
            }
        });

        Button import_settings = (Button) root.findViewById(R.id.import_settings);
        import_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Import Settings")
                        .setMessage("If you have used export settings in the past before this dialog existed or you exported with \"Root Mode\" use \"Root Mode\"")
                        .setCancelable(true)
                        .setPositiveButton("Import From Downloads", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if(request_perms(context,PERM_REQUEST_READ)){
                                    importPrefs(prefs);
                                }
                            }
                        })
                        .setNegativeButton("Root Mode", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                SystemWriter.restore_shared_preferences(getActivity().getApplicationContext());
                                Intent i = getActivity().getApplicationContext().getPackageManager().
                                getLaunchIntentForPackage(getActivity().getApplicationContext().getPackageName());
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                System.exit(0);
                            }
                        })
                        .show();
            }
        });

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

        Button change_root_mode = (Button) root.findViewById(R.id.change_root_mode);
        boolean isRootMode = prefs.getBoolean(is_root_mode_shared_preference_key, false);
        if(isRootMode){
            change_root_mode.setText("Toggle Mode (Current Mode: Root)");
        }else{
            change_root_mode.setText("Toggle Mode (Current Mode: Non Root)");
        }

        change_root_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isRootMode = prefs.getBoolean(is_root_mode_shared_preference_key, false);
                Log.d("isroot mode", String.valueOf(isRootMode));
                isRootMode = !isRootMode;
                Log.d("isroot mode", String.valueOf(isRootMode));
                prefs.edit().putBoolean(is_root_mode_shared_preference_key, isRootMode).apply();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent i = getActivity().getApplicationContext().getPackageManager().
                        getLaunchIntentForPackage(getActivity().getApplicationContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                System.exit(0);
            }
        });


        WebView webView = root.findViewById(R.id.webview);

        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERM_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Context context = getContext();
                    SharedPreferences prefs = context.getSharedPreferences(
                            "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);
                    savePrefs(prefs);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "The app was not allowed to write in your storage", Toast.LENGTH_LONG).show();
                }
                break;
            case PERM_REQUEST_READ:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Context context = getContext();
                    SharedPreferences prefs = context.getSharedPreferences(
                            "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);
                    importPrefs(prefs);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "The app was not allowed to write in your storage", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private boolean request_perms(Context context, int requestId){
        String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

        for (String permission : PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, PERMISSIONS, requestId );
                return false;
            }
        }

        return true;
    }

    private void savePrefs(SharedPreferences prefs){
        File myPath = new File(Environment.getExternalStorageDirectory().toString());
        File myFile = new File(myPath,"/Download/.terminal_heat_sink.asusrogphone2rgb.xml.txt");

        try
        {
            FileWriter fw = new FileWriter(myFile);
            PrintWriter pw = new PrintWriter(fw);

            Map<String,?> prefsMap = prefs.getAll();

            for(Map.Entry<String,?> entry : prefsMap.entrySet())
            {
                pw.println(entry.getKey() + ":mysplit:" + entry.getValue().getClass().getSimpleName() + ":mysplit:" + entry.getValue().toString());
            }

            pw.close();
            fw.close();
            Toast.makeText(getActivity().getApplicationContext(), "Saved to Download/.terminal_heat_sink.asusrogphone2rgb.xml.txt", Toast.LENGTH_LONG).show();
        }
        catch (Exception e)
        {
            Log.e("Saving Shared Prefs Failed", e.toString());
            Toast.makeText(getActivity().getApplicationContext(), "Failed to save", Toast.LENGTH_LONG).show();
        }
    }

    private void importPrefs(SharedPreferences prefs){
        File myPath = new File(Environment.getExternalStorageDirectory().toString());
        File myFile = new File(myPath,"/Download/.terminal_heat_sink.asusrogphone2rgb.xml.txt");
        try
        {
            FileReader fr = new FileReader(myFile);

            ArrayList<String> input = new ArrayList<String>();
            StringBuilder temp = new StringBuilder();

            int i;
            while ((i = fr.read()) != -1) {
                if((char)i == '\n'){
                    input.add(temp.toString());
                    temp = new StringBuilder();
                }
                temp.append((char) i);
                //Log.e("SystemWriter", String.valueOf((char)i));
            }

            for( String item : input){
                String parts[] = item.split(":mysplit:");
                String key = parts[0];
                String type = parts[1];
                String value = parts[2];

                switch (type){
                    case "String":
                        prefs.edit().putString(key, value).apply();
                        Log.d("SystemWriter", "adding string");
                        break;
                    case "Boolean":
                        prefs.edit().putBoolean(key, value.equals("true")).apply();
                        Log.d("SystemWriter", "adding boolean");
                        break;
                    case "Integer":
                        prefs.edit().putInt(key, Integer.parseInt(value)).apply();
                        Log.d("SystemWriter", "adding integer");
                        break;
                    case "HashSet":
                        String joinedMinusBrackets = value.substring( 1, value.length() - 1);
                        String[] resplit = joinedMinusBrackets.split( ", ");
                        HashSet<String> myset = new HashSet<String>();

                        myset.addAll(Arrays.asList(resplit));

                        prefs.edit().putStringSet(key, myset).apply();
                        Log.d("SystemWriter", "adding hashset");
                        Log.d("SystemWriter", myset.toString());
                        break;
                }

                Log.d("SystemWriter", key + " " + type + " " + value);
            }

            fr.close();
            Toast.makeText(getActivity().getApplicationContext(), "imported successfully", Toast.LENGTH_LONG).show();

            Intent intent = getActivity().getApplicationContext().getPackageManager().
                        getLaunchIntentForPackage(getActivity().getApplicationContext().getPackageName());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            System.exit(0);
        }
        catch (Exception e)
        {
            Log.e("Saving Shared Prefs Failed", e.toString());
            Toast.makeText(getActivity().getApplicationContext(), "Failed to import", Toast.LENGTH_LONG).show();
        }
    }
}

