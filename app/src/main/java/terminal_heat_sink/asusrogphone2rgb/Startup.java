package terminal_heat_sink.asusrogphone2rgb;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Startup extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

            Button rog2 = findViewById(R.id.button_select_rog2);

            rog2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent();
                    intent.putExtra("PHONE","2");
                    setResult(404,intent);
                    finish();
                }
            });

            Button rog3 = findViewById(R.id.button_select_rog3);

            rog3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent();
                    intent.putExtra("PHONE","3");
                    setResult(404,intent);
                    finish();
                }
            });





    }
}