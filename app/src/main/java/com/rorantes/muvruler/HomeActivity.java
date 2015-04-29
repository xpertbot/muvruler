package com.rorantes.muvruler;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Roger on 4/25/2015.
 */
public class HomeActivity extends ActionBarActivity {
    static final int NEW_ENTRY_RESULT = 1;
    ArrayList<LogEntry> logHistory = new ArrayList();
    LogEntry currentLogEntry;
    ListView list;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        list = (ListView) findViewById(R.id.listView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == NEW_ENTRY_RESULT){
            Bundle bundle = data.getExtras();
            if(bundle != null){
                if(bundle.containsKey("history")){
                    currentLogEntry = getIntent().getExtras().getParcelable("history");
                    logHistory.add(currentLogEntry);
                    Log.d("MyDebug", Integer.toString(logHistory.size()));
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_new_measurement) {
            Intent intent = new Intent(this ,RulerActivity.class);
            startActivityForResult(intent, 1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
