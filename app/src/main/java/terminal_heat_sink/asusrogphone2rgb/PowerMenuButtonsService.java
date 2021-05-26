package terminal_heat_sink.asusrogphone2rgb;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.service.controls.Control;
import android.service.controls.ControlsProviderService;
import android.service.controls.DeviceTypes;
import android.service.controls.actions.BooleanAction;
import android.service.controls.actions.ControlAction;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.reactivestreams.FlowAdapters;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Flow;
import java.util.function.Consumer;

import io.reactivex.Flowable;
import io.reactivex.processors.ReplayProcessor;

@RequiresApi(api = Build.VERSION_CODES.R)
public class PowerMenuButtonsService extends ControlsProviderService {

    private ReplayProcessor updatePublisher;
    private int on = Control.STATUS_OK;
    private int off = Control.STATUS_DISABLED;

    private int[][] states = new int[][] {
            new int[] { android.R.attr.state_enabled}, // enabled
            new int[] {-android.R.attr.state_enabled}, // disabled
            new int[] {-android.R.attr.state_checked}, // unchecked
            new int[] { android.R.attr.state_pressed}  // pressed
    };

    private int[] colors = new int[] {
            Color.argb(128, 0,250,0),
            Color.argb(128, 0,0,0),
            Color.argb(128, 250,0,0),
            Color.argb(128, 0,0,250)
    };

    ColorStateList myList = new ColorStateList(states, colors);

    @Override
    public Flow.Publisher createPublisherForAllAvailable() {
        Log.e("createPublisherForAllAvailable", "ok");
        Context context = getBaseContext();
        Intent i = new Intent();
        PendingIntent pi = PendingIntent.getActivity(context, 1, i, PendingIntent.FLAG_UPDATE_CURRENT);
        List controls = new ArrayList<>();
        Control control = new Control.StatelessBuilder("main_led", pi)
                    // Required: The name of the control
                    .setTitle("Toggle Logo LED")
                    // Required: Usually the room where the control is located
                    .setSubtitle("Toggle the logo led on the back of the phone")
                    // Optional: Structure where the control is located, an example would be a house
                    //.setStructure(MY-CONTROL-STRUCTURE)
                    // Required: Type of device, i.e., thermostat, light, switch
                    .setDeviceType(DeviceTypes.TYPE_SWITCH) // For example, DeviceTypes.TYPE_THERMOSTAT
                    .setCustomColor(myList)
                    .setCustomIcon(Icon.createWithResource(context,R.drawable.asus_rog_logo_scaled))
                    .build();

        controls.add(control);

        Control control_second_led = new Control.StatelessBuilder("second_led", pi)
                // Required: The name of the control
                .setTitle("Toggle Second LED")
                // Required: Usually the room where the control is located
                .setSubtitle("Toggle the second led on the back of the phone")
                // Optional: Structure where the control is located, an example would be a house
                //.setStructure(MY-CONTROL-STRUCTURE)
                // Required: Type of device, i.e., thermostat, light, switch
                .setDeviceType(DeviceTypes.TYPE_SWITCH) // For example, DeviceTypes.TYPE_THERMOSTAT
                .setCustomColor(myList)
                .setCustomIcon(Icon.createWithResource(context,R.drawable.second_led))
                .build();

        controls.add(control_second_led);
        // Create more controls here if needed and add it to the ArrayList



        // Uses the RxJava 2 library
        return FlowAdapters.toFlowPublisher(Flowable.fromIterable(controls));
    }

    @Override
    public Flow.Publisher createPublisherFor(List controlIds) {
        Log.e("createPublisherFor", "ok");
        Context context = getBaseContext();
        /* Fill in details for the activity related to this device. On long press,
         * this Intent will be launched in a bottomsheet. Please design the activity
         * accordingly to fit a more limited space (about 2/3 screen height).
         */
        Intent i = new Intent(context, PowerMenuActivityHandler.class);
        i.setAction("logo");
        PendingIntent pi = PendingIntent.getService(context, 1, i, PendingIntent.FLAG_UPDATE_CURRENT);

        //Intent i = new Intent();
        //PendingIntent pi = PendingIntent.getActivity(context, 2, i, PendingIntent.FLAG_UPDATE_CURRENT);


        updatePublisher = ReplayProcessor.create();

        // For each controlId in controlIds


        if (controlIds.contains("main_led")) {

                Control control = new Control.StatefulBuilder("main_led",pi)
                        // Required: The name of the control
                        .setTitle("Turn on Logo LED")
                        // Required: Usually the room where the control is located
                        .setSubtitle("Toggle the logo led on the back of the phone")
                        // Optional: Structure where the control is located, an example would be a house
                        //.setStructure(MY-CONTROL-STRUCTURE)
                        // Required: Type of device, i.e., thermostat, light, switch
                        .setDeviceType(DeviceTypes.TYPE_SWITCH) // For example, DeviceTypes.TYPE_THERMOSTAT
                        // Required: Current status of the device
                        .setStatus(Control.STATUS_OK) // For example, Control.STATUS_OK
                        .setCustomColor(myList)
                        .setCustomIcon(Icon.createWithResource(context,R.drawable.asus_rog_logo_scaled))
                        .build();


                updatePublisher.onNext(control);

        }

        if (controlIds.contains("second_led")) {

            Control control = new Control.StatefulBuilder("second_led",pi)
                    // Required: The name of the control
                    .setTitle("Turn on Second LED")
                    // Required: Usually the room where the control is located
                    .setSubtitle("Toggle the second led on the back of the phone")
                    // Optional: Structure where the control is located, an example would be a house
                    //.setStructure(MY-CONTROL-STRUCTURE)
                    // Required: Type of device, i.e., thermostat, light, switch
                    .setDeviceType(DeviceTypes.TYPE_SWITCH) // For example, DeviceTypes.TYPE_THERMOSTAT
                    // Required: Current status of the device
                    .setStatus(Control.STATUS_OK) // For example, Control.STATUS_OK
                    .setCustomColor(myList)
                    .setCustomIcon(Icon.createWithResource(context,R.drawable.second_led))
                    .build();


            updatePublisher.onNext(control);

        }

        // Uses the Reactive Streams API
        return FlowAdapters.toFlowPublisher(updatePublisher);
    }

    @Override
    public void performControlAction(String controlId, ControlAction action,
                                     Consumer consumer) {
        Log.e("performControlAction", "ok");
        /* First, locate the control identified by the controlId. Once it is located, you can
         * interpret the action appropriately for that specific device. For instance, the following
         * assumes that the controlId is associated with a light, and the light can be turned on
         * or off.
         */
        Context context = getBaseContext();
        Intent i = new Intent();
        PendingIntent pi = PendingIntent.getActivity(context, 1, i, PendingIntent.FLAG_UPDATE_CURRENT);

            if (action instanceof BooleanAction) {

                // Inform SystemUI that the action has been received and is being processed
                consumer.accept(ControlAction.RESPONSE_OK);

                BooleanAction actiont = (BooleanAction) action;
                int state = off;
                if(actiont.getNewState()){
                    state = on;
                }
                // In this example, action.getNewState() will have the requested action: true for “On”,
                // false for “Off”.

                /* This is where application logic/network requests would be invoked to update the state of
                 * the device.
                 * After updating, the application should use the publisher to update SystemUI with the new
                 * state.
                 */
                Control control = new Control.StatefulBuilder("main_led", pi)
                        // Required: The name of the control
                        .setTitle("Toggle Logo LED")
                        // Required: Usually the room where the control is located
                        .setSubtitle("Toggle the logo led on the back of the phone")
                        // Optional: Structure where the control is located, an example would be a house
                        //.setStructure(MY-CONTROL-STRUCTURE)
                        // Required: Type of device, i.e., thermostat, light, switch
                        .setDeviceType(DeviceTypes.TYPE_SWITCH) // For example, DeviceTypes.TYPE_THERMOSTAT
                        // Required: Current status of the device
                        .setStatus(off) // For example, Control.STATUS_OK
                        .setCustomColor(myList)
                        .setCustomIcon(Icon.createWithResource(context,R.drawable.asus_rog_logo_scaled))
                        .build();

                // This is the publisher the application created during the call to createPublisherFor()
                updatePublisher.onNext(control);
            }

    }


}
