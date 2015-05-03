package com.rorantes.muvruler;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Roger on 4/25/2015.
 */
public class HomeActivity extends ActionBarActivity {
    static final int NEW_ENTRY_RESULT = 1;
    static final String NEW_HISTORY_LOG = "com.rorantes.muvruler.history";
    ArrayList<LogEntry> logHistory = new ArrayList();
    ListView list;
    RowAdapter adapter;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        list = (ListView) findViewById(R.id.listView);
        adapter = new RowAdapter(this, logHistory);
        list.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_ENTRY_RESULT) {
            if(resultCode == RESULT_OK){
                if(data != null){
                    LogEntry currentLogEntry = data.getParcelableExtra(NEW_HISTORY_LOG);
                    logHistory.add(currentLogEntry);
                    adapter.notifyDataSetChanged();
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
            startActivityForResult(intent, NEW_ENTRY_RESULT);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class RowAdapter extends ArrayAdapter<LogEntry>{
        public RowAdapter(Context context, ArrayList<LogEntry> items){
            super(context, 0, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LogEntry currentLogEntry = getItem(position);
            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_layout, parent, false);
            }

            TextView tagname = (TextView) convertView.findViewById(R.id.tagname);
            TextView measurement = (TextView) convertView.findViewById(R.id.measurement);

            tagname.setText(currentLogEntry.getTagname());
            measurement.setText(Double.toString(Math.round(currentLogEntry.getMeasurement() * 100.0) / 100.0));

            return convertView;
        }
    }
}
