package terminal_heat_sink.asusrogphone2rgb;

import android.content.Context;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;

public class SystemWriter {
    public static boolean write(String path, String value, Context context){
        Process p;
        try {
            // Preform su to get root privledges
            p = Runtime.getRuntime().exec("su");

            // Attempt to write a file to a root-only
            DataOutputStream os = new DataOutputStream(p.getOutputStream());
            String command = "echo " + value + " >" + path + "\n";
            os.writeBytes(command);

            // Close the terminal
            os.writeBytes("exit\n");
            os.flush();
            try {
                p.waitFor();
                if (p.exitValue() != 255) {
                    // TODO Code to run on success
                    Toast toast = Toast.makeText(context, "success", Toast.LENGTH_SHORT);
                    toast.show();

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
        return true;
    }
}
