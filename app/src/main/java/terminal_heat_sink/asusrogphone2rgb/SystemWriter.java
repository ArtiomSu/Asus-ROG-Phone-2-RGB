package terminal_heat_sink.asusrogphone2rgb;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SystemWriter {

    private static String read_from_sys(String command, Context context){
        Process p;
        String result = "";
        try {
            // Preform su to get root privledges
            p = Runtime.getRuntime().exec("su");

            DataOutputStream os = new DataOutputStream(p.getOutputStream());
            DataInputStream in = new DataInputStream(p.getInputStream());
            os.writeBytes(command);
            // Close the terminal
            os.writeBytes("exit\n");
            os.flush();
            try {
                p.waitFor();
                if (p.exitValue() != 255) {

                    if(p.exitValue() == 0){
                        Log.i("SystemWriter","read successfully");
                        int i;
                        String output = "";
                        char c;
                        while((i = in.read())!=-1) {
                            c = (char)i;
                            output +=c;
                        }
                        result = output.toString();
                    }else{
                        Log.i("SystemWriter","failed to read");
                        Toast toast = Toast.makeText(context, "Could not read files please allow AsusRogPhone2RGB root access in magisk", Toast.LENGTH_LONG);
                        toast.show();
                    }

                }
                else {
                    Log.i("SystemWriter","not rooted 1");
                    Toast toast = Toast.makeText(context, "root required please root your phone", Toast.LENGTH_SHORT);
                    toast.show();
                }
            } catch (InterruptedException e) {
                Log.i("SystemWriter","not rooted 2");
                Toast toast = Toast.makeText(context, "root required please root your phone", Toast.LENGTH_SHORT);
                toast.show();
            }
        } catch (IOException e) {
            Log.i("SystemWriter","not rooted 3");
            Toast toast = Toast.makeText(context, "root required please root your phone", Toast.LENGTH_SHORT);
            toast.show();
        }
        return result;
    }

    private static void write_to_sys(String command, Context context){
        Process p;
        try {
            // Preform su to get root privledges
            p = Runtime.getRuntime().exec("su");

            DataOutputStream os = new DataOutputStream(p.getOutputStream());
            os.writeBytes(command);
            // Close the terminal
            os.writeBytes("exit\n");
            os.flush();
            try {
                p.waitFor();
                if (p.exitValue() != 255) {

                    if(p.exitValue() == 0){
                        Log.i("SystemWriter","wrote successfully");

                    }else{
                        Log.i("SystemWriter","failed to write");
                        Toast toast = Toast.makeText(context, "Could not write please allow AsusRogPhone2RGB root access in magisk", Toast.LENGTH_LONG);
                        toast.show();
                    }



                }
                else {
                    Log.i("SystemWriter","not rooted 1");
                    Toast toast = Toast.makeText(context, "root required please root your phone", Toast.LENGTH_SHORT);
                    toast.show();
                }
            } catch (InterruptedException e) {
                Log.i("SystemWriter","not rooted 2");
                Toast toast = Toast.makeText(context, "root required please root your phone", Toast.LENGTH_SHORT);
                toast.show();
            }
        } catch (IOException e) {
            Log.i("SystemWriter","not rooted 3");
            Toast toast = Toast.makeText(context, "root required please root your phone", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public static void write_colour(int red, int green, int blue, Context context){
        String command =
                "echo " +red+ " > "+ "/sys/class/leds/aura_sync/red_pwm" + " && " +
                "echo " +green+ " > "+ "/sys/class/leds/aura_sync/green_pwm" + " && " +
                "echo " +blue+ " > "+ "/sys/class/leds/aura_sync/blue_pwm" + " && " +
                "echo 1 > "+ "/sys/class/leds/aura_sync/apply" + " \n ";

        write_to_sys(command,context);
    }

    public static void write_animation(int animation, Context context){
        String command =
                "echo " +animation+ " > "+ "/sys/class/leds/aura_sync/mode" + " && " +
                        "echo 1 > "+ "/sys/class/leds/aura_sync/apply" + " \n ";

        write_to_sys(command,context);
    }

    public static void turn_on(boolean on, Context context){
        if(on){
            write_to_sys("echo 1 > /sys/class/leds/aura_sync/led_on \n",context);
        }else{
            write_to_sys("echo 0 > /sys/class/leds/aura_sync/led_on \n",context);
        }
    }

    public static void turn_on_second_led(boolean on, Context context){
        if(on){
            write_to_sys("echo 1 > /sys/class/leds/aura_sync/CSCmode && echo 1 > /sys/class/leds/aura_sync/bumper_enable \n",context);
        }else{
            write_to_sys("echo 0 > /sys/class/leds/aura_sync/bumper_enable && echo 0 > /sys/class/leds/aura_sync/CSCmode \n",context);
        }
    }

    public static void notification_start(int mode, boolean use_color, int red, int green, int blue, Context context, boolean use_second_led, boolean use_second_led_only){

        String command = "";

        if(use_second_led_only){
            command += "echo 0 > /sys/class/leds/aura_sync/led_on && ";
        }else{
            command += "echo 1 > /sys/class/leds/aura_sync/led_on && ";
        }

        if(use_second_led || use_second_led_only){
            command += "echo 1 > /sys/class/leds/aura_sync/CSCmode && echo 1 > /sys/class/leds/aura_sync/bumper_enable && ";
        }else{
            command += "echo 0 > /sys/class/leds/aura_sync/bumper_enable && echo 0 > /sys/class/leds/aura_sync/CSCmode && ";
        }

        if(use_color){
            command += "echo " + red + " > " + "/sys/class/leds/aura_sync/red_pwm" + " && " +
                    "echo " + green + " > " + "/sys/class/leds/aura_sync/green_pwm" + " && " +
                    "echo " + blue + " > " + "/sys/class/leds/aura_sync/blue_pwm" + " && ";
        }
        command += "echo " + mode + " > "+ "/sys/class/leds/aura_sync/mode" + " && " +
                "echo 1 > "+ "/sys/class/leds/aura_sync/apply" + " \n ";

        write_to_sys(command,context);
    }

    public static void notification_stop(boolean turn_off, int mode, boolean use_color, int red, int green, int blue, Context context, boolean use_second_led){
        String command = "";

        if(use_second_led){
            command += "echo 1 > /sys/class/leds/aura_sync/CSCmode && echo 1 > /sys/class/leds/aura_sync/bumper_enable && ";
        }else{
            command += "echo 0 > /sys/class/leds/aura_sync/bumper_enable && echo 0 > /sys/class/leds/aura_sync/CSCmode && ";
        }

        if(turn_off){
            command += "echo 0 > /sys/class/leds/aura_sync/led_on && ";
        }else{
            command += "echo 1 > /sys/class/leds/aura_sync/led_on && ";
        }

        if(use_color){
            command += "echo " + red + " > " + "/sys/class/leds/aura_sync/red_pwm" + " && " +
                    "echo " + green + " > " + "/sys/class/leds/aura_sync/green_pwm" + " && " +
                    "echo " + blue + " > " + "/sys/class/leds/aura_sync/blue_pwm" + " && ";
        }
        command += "echo " + mode + " > "+ "/sys/class/leds/aura_sync/mode" + " && " +
                "echo 1 > "+ "/sys/class/leds/aura_sync/apply" + " \n ";

        write_to_sys(command,context);
    }

    public static void save_shared_preferences(Context context){
        write_to_sys("cat /data/data/terminal_heat_sink.asusrogphone2rgb/shared_prefs/terminal_heat_sink.asusrogphone2rgb.xml > /sdcard/.terminal_heat_sink.asusrogphone2rgb.xml \n",context);
    }

    public static void restore_shared_preferences(Context context){
        write_to_sys("cat /sdcard/.terminal_heat_sink.asusrogphone2rgb.xml > /data/data/terminal_heat_sink.asusrogphone2rgb/shared_prefs/terminal_heat_sink.asusrogphone2rgb.xml \n",context);
    }

    public static void notification_access(boolean allow,Context context){
        if(allow){
            write_to_sys("cmd notification allow_listener terminal_heat_sink.asusrogphone2rgb/terminal_heat_sink.asusrogphone2rgb.NotificationService \n",context);
        }else{
            write_to_sys("cmd notification disallow_listener terminal_heat_sink.asusrogphone2rgb/terminal_heat_sink.asusrogphone2rgb.NotificationService \n",context);
        }
    }

}
