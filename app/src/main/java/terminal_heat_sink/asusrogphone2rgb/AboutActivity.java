package terminal_heat_sink.asusrogphone2rgb;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AboutActivity extends Fragment {
    public AboutActivity() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_about, container, false);

        LinearLayout ll = (LinearLayout) root.findViewById(R.id.about_linear);


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


        TextView text = (TextView) root.findViewById(R.id.textViewAbout);
        text.setTextColor(getResources().getColor(R.color.colorText));
        text.setText(R.string.about_text);

        return root;

        //return inflater.inflate(R.layout.activity_about, container, false);
    }
}

