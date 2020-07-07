package terminal_heat_sink.asusrogphone2rgb;

import android.content.Context;
import android.content.SharedPreferences;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

public class QuicktileLogo extends TileService {

    private String fab_on_shared_preference_key = "terminal_heat_sink.asusrogphone2rgb.fab_on";


    @Override
    public void onClick() {
        super.onClick();

        SharedPreferences prefs = getApplicationContext().getSharedPreferences(
                "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);
        boolean enabled = prefs.getBoolean(fab_on_shared_preference_key,false);

        Tile tile = getQsTile();

        if(enabled){
            SystemWriter.turn_on(false,getApplicationContext());
            tile.setState(Tile.STATE_INACTIVE);
            tile.setLabel("Turn ON Logo LED");
            enabled = false;
        }else{
            SystemWriter.turn_on(true,getApplicationContext());
            tile.setState(Tile.STATE_ACTIVE);
            tile.setLabel("Turn Off Logo LED");
            enabled = true;
        }
        prefs.edit().putBoolean(fab_on_shared_preference_key, enabled).apply();
        tile.updateTile();
    }

    @Override
    public void onTileRemoved() {
        super.onTileRemoved();
    }

    @Override
    public void onTileAdded() {
        super.onTileAdded();
        update_tile();
    }

    @Override
    public void onStartListening() {
        super.onStartListening();

        update_tile();

    }

    private void update_tile(){
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(
                "terminal_heat_sink.asusrogphone2rgb", Context.MODE_PRIVATE);
        boolean enabled = prefs.getBoolean(fab_on_shared_preference_key,false);

        Tile tile = getQsTile();
        //tile.setLabel("Toggle Logo");

        if(enabled){
            tile.setState(Tile.STATE_ACTIVE);
            tile.setLabel("Turn Off Logo LED");
        }else{
            tile.setState(Tile.STATE_INACTIVE);
            tile.setLabel("Turn On Logo LED");
        }
        tile.updateTile();
    }

    @Override
    public void onStopListening() {
        super.onStopListening();
    }
}
