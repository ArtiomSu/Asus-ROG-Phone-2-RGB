package terminal_heat_sink.asusrogphone2rgb;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.service.controls.Control;
import android.service.controls.ControlsProviderService;
import android.service.controls.DeviceTypes;
import android.service.controls.actions.BooleanAction;
import android.service.controls.actions.ControlAction;
import android.service.controls.templates.ControlButton;
import android.service.controls.templates.ControlTemplate;
import android.service.controls.templates.ToggleTemplate;

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

    private final String fab_on_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.fab_on";
    private final String use_second_led_on_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.use_second_led";
    private final String notifications_on_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.notifications_on";
    private final String use_notifications_timeout_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.use_notifications_timeout_shared_preference_key";
    private final String battery_animate_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.battery_animate";

    private ReplayProcessor updatePublisher;
    private final String[] controlIds = {
            "mainLogo",
            "secondLogo",
            "notification",
            "notificationTimeout",
            "battery"
    };

    private final String[] controlIdsTitle = {
            "Logo LED",
            "Second LED",
            "React to Notification",
            "Notification Timeout",
            "Battery Charging"
    };

    private final String[] controlPrefs = {
            fab_on_shared_preference_key,
            use_second_led_on_shared_preference_key,
            notifications_on_shared_preference_key,
            use_notifications_timeout_shared_preference_key,
            battery_animate_shared_preference_key
    };

    private Icon getIcon(int id){
        Icon[] icons = {
                Icon.createWithResource(this, R.drawable.asus_rog_logo),
                Icon.createWithResource(this, R.drawable.second_led),
                Icon.createWithResource(this, R.drawable.ic_baseline_notifications_24),
                Icon.createWithResource(this, R.drawable.ic_baseline_timer_24),
                Icon.createWithResource(this, R.drawable.ic_baseline_battery_full_24),
        };
        return icons[id];
    }

    private boolean getState(int id){
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);
        return prefs.getBoolean(controlPrefs[id], false);
    }

    private void updateState(int id, boolean on){
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);
        prefs.edit().putBoolean(controlPrefs[id], on).apply();
    }

    private ControlTemplate make_button_template(boolean on){
        ControlButton btn = new ControlButton(on,"button");
        return new ToggleTemplate("button", btn);
    }

    private Control makeControl(int id, boolean on){
        Intent i = new Intent(PowerMenuButtonsService.this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 1, i, PendingIntent.FLAG_MUTABLE);
        String subtitle = !on ? "turn on" : "turn off";
        return new Control.StatefulBuilder(controlIds[id], pi)
                .setTitle(controlIdsTitle[id])
                .setSubtitle(subtitle)
                .setStructure("Asus Rog Phone RGB")
                .setDeviceType(DeviceTypes.TYPE_LIGHT)
                .setStatus(Control.STATUS_OK)
                .setCustomColor(new ColorStateList(new int[][] { new int[] { android.R.attr.state_enabled} },  new int[] { Color.rgb(58,112, 59) }))
                .setControlTemplate(make_button_template(on))
                .setCustomIcon(getIcon(id))
                .build();
    }

    private Control makeControlInitial(int id){
        Intent i = new Intent(PowerMenuButtonsService.this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 1, i, PendingIntent.FLAG_IMMUTABLE);

        return new Control.StatelessBuilder(controlIds[id], pi)
                .setTitle(controlIdsTitle[id])
                .setSubtitle("turn on")
                .setStructure("Asus Rog Phone RGB")
                .setDeviceType(DeviceTypes.TYPE_LIGHT)
                .setCustomColor(new ColorStateList(new int[][] { new int[] { android.R.attr.state_enabled} },  new int[] { Color.rgb(58,112, 59) }))
                .setCustomIcon(getIcon(id))
                .build();
    }

    @Override
    public Flow.Publisher createPublisherForAllAvailable() {
        ArrayList<Control> controls = new ArrayList<>();
        controls.add(makeControlInitial(0));
        controls.add(makeControlInitial(1));
        controls.add(makeControlInitial(2));
        controls.add(makeControlInitial(3));
        controls.add(makeControlInitial(4));
        return FlowAdapters.toFlowPublisher(Flowable.fromIterable(controls));
    }

    @Override
    public Flow.Publisher createPublisherFor(List list) {
        updatePublisher = ReplayProcessor.create();
        for(int i = 0; i< list.size(); i++) {
            if(list.get(i).equals(controlIds[0])){
                updatePublisher.onNext(makeControl(0, getState(0)));
            }else if(list.get(i).equals(controlIds[1])){
                updatePublisher.onNext(makeControl(1, getState(1)));
            }else if(list.get(i).equals(controlIds[2])){
                updatePublisher.onNext(makeControl(2, getState(2)));
            }else if(list.get(i).equals(controlIds[3])){
                updatePublisher.onNext(makeControl(3, getState(3)));
            }else if(list.get(i).equals(controlIds[4])){
                updatePublisher.onNext(makeControl(4, getState(4)));
            }
        }
        return FlowAdapters.toFlowPublisher(updatePublisher);
    }

    @Override
    public void performControlAction(String controlId, ControlAction actionControl,
                                     Consumer consumer) {
        if(actionControl instanceof BooleanAction){
            boolean state = ((BooleanAction) actionControl).getNewState();
            if(controlIds[0].equals(controlId)){
                SystemWriter.turn_on(state,getApplicationContext());
                updateState(0, state);
                updatePublisher.onNext(makeControl(0, state));
            }else if(controlIds[1].equals(controlId)){
                SystemWriter.turn_on_second_led(state,getApplicationContext());
                updateState(1, state);
                updatePublisher.onNext(makeControl(1, state));
            }else if(controlIds[2].equals(controlId)){
                SystemWriter.notification_access(state,getApplicationContext());
                updateState(2, state);
                updatePublisher.onNext(makeControl(2, state));
            }else if(controlIds[3].equals(controlId)){
                updateState(3, state);
                updatePublisher.onNext(makeControl(3, state));
            }else if(controlIds[4].equals(controlId)){
                Intent notification_intent = new Intent(getApplicationContext(), BatteryService.class);
                if(!state){
                    getApplicationContext().stopService(notification_intent);
                }else{
                    getApplicationContext().startService(notification_intent);
                }
                updateState(4, state);
                updatePublisher.onNext(makeControl(4, state));
            }
            consumer.accept(ControlAction.RESPONSE_OK);
        }

    }


}
