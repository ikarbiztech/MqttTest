package com.iker.mqtttest;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;



import com.iker.mqtttest.mqtt.ActivityConstants;
import com.iker.mqtttest.mqtt.MqttAccess;

import org.eclipse.paho.android.service.MqttService;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        startService(new Intent(getApplicationContext(), MqttService.class));
        Log.i("is my service running??: ",isMyServiceRunning(MqttService.class)?"Yes":"No");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
               /* MqttAndroidClient androidClient = new MqttAndroidClient(getApplicationContext(),"10.95.101.40","iker_client_id");
                Log.i("ERROR","Client Created");
                Connection con = new Connection("clientHandler","iker_client_id","10.95.101.40",1883,getApplicationContext(),androidClient,false);
                Log.i("ERROR","Connection Created");
                con.createConnection("clientHandler", "10.95.101.40", 1883, getApplicationContext(), false);
                Log.i("ERROR", "Connected");*/
                Bundle data = new Bundle();
                data.putString(ActivityConstants.password, "ldapuser01");
                data.putString(ActivityConstants.username, "ldapuser01");
                data.putString(ActivityConstants.clientId, "Android_1");
                data.putString(ActivityConstants.server, "10.95.101.40");
                data.putString(ActivityConstants.port, "1883");
                data.putBoolean(ActivityConstants.cleanSession, true);
                data.putBoolean(ActivityConstants.ssl, false);
                data.putString(ActivityConstants.message, "Hi there!!");
                data.putString(ActivityConstants.topic, "hello/world");
                data.putInt(ActivityConstants.qos, 1);
                data.putBoolean(ActivityConstants.retained, false);
                data.putInt(ActivityConstants.timeout, 30000);
                data.putInt(ActivityConstants.keepalive, 1800000);

                MqttAccess mqttAccess = new MqttAccess("10.95.101.40","1883","hello/world","Hi there!!","ldapuser01","ldapuser01","Android_1");
                mqttAccess.connectAction(getApplicationContext());
                try {
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                mqttAccess.subscribe(getApplicationContext());
//
//                try {
//                    Thread.sleep(20000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                mqttAccess.publish(getApplicationContext());
            }
        });
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


}
