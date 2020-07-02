package terminal_heat_sink.asusrogphone2rgb;

import android.content.Context;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;

public class SystemWriter {

    private static void write_to_sys(String command, Context context){
        Process p;
        try {
            // Preform su to get root privledges
            p = Runtime.getRuntime().exec("su");

            // Attempt to write a file to a root-only
            DataOutputStream os = new DataOutputStream(p.getOutputStream());
            //Log.e("write_to_sys",command);
            os.writeBytes(command);

            // Close the terminal
            os.writeBytes("exit\n");
            os.flush();
            try {
                p.waitFor();
                if (p.exitValue() != 255) {
                    // TODO Code to run on success
                    //Toast toast = Toast.makeText(context, "success", Toast.LENGTH_SHORT);
                    //toast.show();

                }
                else {
                    // TODO Code to run on unsuccessful
                    Toast toast = Toast.makeText(context, "root required please root your phone", Toast.LENGTH_SHORT);
                    toast.show();
                }
            } catch (InterruptedException e) {
                // TODO Code to run in interrupted exception
                Toast toast = Toast.makeText(context, "root required please root your phone", Toast.LENGTH_SHORT);
                toast.show();
            }
        } catch (IOException e) {
            // TODO Code to run in input/output exception
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
}
