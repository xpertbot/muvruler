package com.rorantes.muvruler;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ToggleButton;

import java.util.ArrayList;


public class RulerActivity extends ActionBarActivity implements RulerFragment.OnSavingMeasurement, LocationListener{
    static final String NEW_HISTORY_LOG = "com.rorantes.muvruler.history";

    LogEntry currentLogEntry;
    float  measuredDistance;
    boolean on = false;

    ArrayList<Location> coordList = new ArrayList();
    Location currentLocation;
    private int provider = 0;
    private float distance = 0;

    private LocationManager locationManager;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruler);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);

        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.container);
        if(fragment == null){
            fm.beginTransaction()
                .add(R.id.container, new RulerFragment())
                .commit();
        }
    }

    public void onToggleClicked(View view){
        on = ((ToggleButton) view).isChecked();
        if(on){

        }
        RulerFragment fragment = (RulerFragment) getFragmentManager().findFragmentById(R.id.container);
        fragment.setGPSFlag();
    }

    public boolean onGPSAction(){
        return on;
    }

    public void onSaveOption(float measuredDistance){
        this.measuredDistance = measuredDistance;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add("Save Measurement").setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                final EditText input = new EditText(RulerActivity.this);
                input.setHint("Tagname for current Measurement");
                new AlertDialog.Builder(RulerActivity.this)
                        .setTitle("Save Measurement")
                        .setMessage("Would you like to save the Measurement?")
                        .setView(input)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if(input.getText() != null){
                                    currentLogEntry = new LogEntry(input.getText().toString(), measuredDistance);
                                   // Log.d("MyDebug", currentLogEntry.getTagname());
                                   // Log.d("MyDebug", Float.toString(currentLogEntry.getMeasurement()));
                                    Intent returnIntent = new Intent();
                                    returnIntent.putExtra(NEW_HISTORY_LOG, currentLogEntry);
                                    //LogEntry testingLogEntry = (LogEntry) returnIntent.getParcelableExtra(NEW_HISTORY_LOG);
                                   // Log.d("MyDebug", testingLogEntry.getTagname());
                                   // Log.d("MyDebug", Float.toString(testingLogEntry.getMeasurement()));
                                    RulerActivity.this.setResult(RESULT_OK, returnIntent);
                                    RulerActivity.this.finish();
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(R.drawable.ic_action_important)
                        .show();
                return true;
            }
        });
        return true;
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        on = false;
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = new Location("location"+Integer.toString(provider));
        currentLocation.setLatitude(location.getLatitude());
        currentLocation.setLongitude(location.getLongitude());
        coordList.add(currentLocation);
        if(coordList.size() > 1){
            distance += coordList.get(provider - 1).distanceTo(currentLocation);
        }
        Log.d("MyDebug", Float.toString(distance));
        provider++;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
}
