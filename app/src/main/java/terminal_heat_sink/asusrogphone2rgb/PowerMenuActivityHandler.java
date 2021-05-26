package terminal_heat_sink.asusrogphone2rgb;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class PowerMenuActivityHandler extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals("logo")){
            Log.e("onStartCommand","found logo");
        }
        return super.onStartCommand(intent, flags, startId);
    }
}