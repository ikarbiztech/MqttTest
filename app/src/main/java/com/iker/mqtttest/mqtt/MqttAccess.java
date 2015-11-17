package com.iker.mqttpahotest.mqtt;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by CONSULTANT on 13/11/2015.
 */
public class MqttAccess  {

    Bundle data = new Bundle();
    String clientHandle = new String();
    Connection connection = null;

//  TODO Check how to create a thread to launch the client instead of waiting from the Android main thread, droidnesis has threads development!!!
    public MqttAccess(String serverIP,String serverPort,String topic, String message, String username, String password, String clientId)
    {
    //        TODO   Pick this configuration from a XML or system file, property files... Droidnesis does this from a file, check that configuration
        //default data configuration
        data.putBoolean(ActivityConstants.cleanSession, true);
        data.putBoolean(ActivityConstants.ssl, false);
        data.putInt(ActivityConstants.qos, 1);
        data.putBoolean(ActivityConstants.retained, false);
        data.putInt(ActivityConstants.timeout, 30000);
        data.putInt(ActivityConstants.keepalive,1800000);
        data.putString(ActivityConstants.password,"ldapuser01");
        data.putString(ActivityConstants.username,"ldapuser01");
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

        if(serverIP!=null && !serverIP.isEmpty())
            data.putString(ActivityConstants.server,serverIP);
        if(serverPort!=null && !serverIP.isEmpty())
            data.putString(ActivityConstants.port,serverPort);
        if(topic!=null && !topic.isEmpty())
            data.putString(ActivityConstants.topic, topic);
        if(message!=null && !message.isEmpty())
            data.putString(ActivityConstants.message, message);
        if(username!=null && !username.isEmpty())
            data.putString(ActivityConstants.username, username);
        if(password!=null && !password.isEmpty())
            data.putString(ActivityConstants.password, password);
        if(clientId!=null && !clientId.isEmpty())
            data.putString(ActivityConstants.clientId,clientId);
    }


    public boolean connectAction(Context context){
        return connectAction(data, context);
    }
    public boolean connectAction(Bundle data, Context context) {

        MqttConnectOptions conOpt = new MqttConnectOptions();


        // The basic client information
        String server = (String) data.get(ActivityConstants.server);
        String clientId = (String) data.get(ActivityConstants.clientId);
        int port = Integer.parseInt((String) data.get(ActivityConstants.port));
        boolean cleanSession = (Boolean) data.get(ActivityConstants.cleanSession);
        boolean ssl = (Boolean) data.get(ActivityConstants.ssl);
        String ssl_key = (String) data.get(ActivityConstants.ssl_key);
        String uri = null;
        if (ssl) {
            Log.e("SSLConnection", "Doing an SSL Connect");
            uri = "ssl://";
        }
        else {
            uri = "tcp://";
        }

        uri = uri + server + ":" + port;

        MqttAndroidClient client;
        client = Connections.getInstance(context).createClient(context, uri, clientId);

        if (ssl){
            try {
                if(ssl_key != null && !ssl_key.equalsIgnoreCase(""))
                {
                    FileInputStream key = new FileInputStream(ssl_key);
                    conOpt.setSocketFactory(client.getSSLSocketFactory(key,
                            "mqtttest"));
                }

            } catch (MqttSecurityException e) {
                Log.e(this.getClass().getCanonicalName(),
                        "MqttException Occured: ", e);
            } catch (FileNotFoundException e) {
                Log.e(this.getClass().getCanonicalName(),
                        "MqttException Occured: SSL Key file not found", e);
            }
        }

        // create a client handle
        clientHandle = uri + clientId;

        // last will message
        String message = (String) data.get(ActivityConstants.message);
        String topic = (String) data.get(ActivityConstants.topic);
        Integer qos = (Integer) data.get(ActivityConstants.qos);
        Boolean retained = (Boolean) data.get(ActivityConstants.retained);

        // connection options

        String username = (String) data.get(ActivityConstants.username);

        String password = (String) data.get(ActivityConstants.password);

        int timeout = (Integer) data.get(ActivityConstants.timeout);
        int keepalive = (Integer) data.get(ActivityConstants.keepalive);

        connection = new Connection(clientHandle, clientId, server, port, context, client, ssl);

        String[] actionArgs = new String[1];
        actionArgs[0] = clientId;
        connection.changeConnectionStatus(Connection.ConnectionStatus.CONNECTING);

        conOpt.setCleanSession(cleanSession);
        conOpt.setConnectionTimeout(timeout);
        conOpt.setKeepAliveInterval(keepalive);
        if (!username.equals(ActivityConstants.empty)) {
            conOpt.setUserName(username);
        }
        if (!password.equals(ActivityConstants.empty)) {
            conOpt.setPassword(password.toCharArray());
        }

        final ActionListener callback = new ActionListener(context, ActionListener.Action.CONNECT, clientHandle, actionArgs);

        boolean doConnect = true;

        if ((!message.equals(ActivityConstants.empty))
                || (!topic.equals(ActivityConstants.empty))) {
            // need to make a message since last will is set
            try {
                conOpt.setWill(topic, message.getBytes(), qos.intValue(),
                        retained.booleanValue());
            }
            catch (Exception e) {
                Log.e(this.getClass().getCanonicalName(), "Exception Occured", e);
                doConnect = false;
                callback.onFailure(null, e);
            }
        }
        client.setCallback(new MqttCallbackHandler(context, clientHandle));


        //set traceCallback
        client.setTraceCallback(new MqttTraceCallback());

        connection.addConnectionOptions(conOpt);
        Connections.getInstance(context).addConnection(connection);
        if (doConnect) {
            try {
                client.connect(conOpt, callback
                );
               /* String[] topics = new String[1];
                topics[0] = topic;
                ActionListener action1 = new ActionListener(context, ActionListener.Action.SUBSCRIBE, clientHandle, topics);
                client.subscribe(topic, data.getInt(ActivityConstants.qos), null, action1);*/

            }
            catch (MqttException e) {
                Log.e(this.getClass().getCanonicalName(),
                        "MqttException Occured", e);
            }
        }
        return doConnect;
    }


    public boolean subscribe(Context context){
        return subscribe(data,context);
    }
    public boolean subscribe(Bundle data,Context context) {
        String topic =  data.getString(ActivityConstants.topic);
        try {
            String[] topics = new String[1];
            topics[0] = topic;
            ActionListener action1 = new ActionListener(context, ActionListener.Action.SUBSCRIBE, clientHandle, topics);
            Log.e("ClentID:",data.getString(ActivityConstants.clientId));
            Log.e("Client Handler:",clientHandle);
            Log.e("Topic", topic);
            for(int i=0; i<topics.length;i++)
            {
                Log.e("Topic"+i,topics[i]);
            }
            Log.e("QOS", String.valueOf(data.getInt(ActivityConstants.qos)));
//            connection = Connections.getInstance(context).getConnection(clientHandle);
            MqttAndroidClient client = connection.getClient();
            client.subscribe(topic,data.getInt(ActivityConstants.qos),null,action1);

//            client.subscribe(topic, data.getInt(ActivityConstants.qos),context,action1);

//            connection.getClient().subscribe(topic, data.getInt(ActivityConstants.qos), context, action1);
        }
        catch(NullPointerException e){
            Log.e(this.getClass().getCanonicalName(), "Something is null!!!",e);
        }
        catch (MqttSecurityException e) {
            Log.e(this.getClass().getCanonicalName(), "Failed to subscribe to" + topic + " the client with the handle " + ActivityConstants.clientId, e);
        }
        catch (MqttException e) {
            Log.e(this.getClass().getCanonicalName(), "Failed to subscribe to" + topic + " the client with the handle " + ActivityConstants.clientId, e);
        }
        return false;
    }


    public boolean publish(Context context){
        return publish(data,context);
    }
    public boolean publish(Bundle data, Context context) {

        String topic =  data.getString(ActivityConstants.topic);
        String message = data.getString(ActivityConstants.message);
        String[] args = new String[2];
        args[0] = topic;
        args[1] = message;
        try {
            Connections.getInstance(context).getConnection(data.getString(ActivityConstants.clientId)).getClient()
                    .publish(topic, message.getBytes(), data.getInt(ActivityConstants.qos), data.getBoolean(ActivityConstants.retained), context, new ActionListener(context, ActionListener.Action.PUBLISH, ActivityConstants.clientId, args));
        }
        catch (MqttSecurityException e) {
            Log.e(this.getClass().getCanonicalName(), "Failed to publish a messged from the client with the handle " + ActivityConstants.clientId, e);
        }
        catch (MqttException e) {
            Log.e(this.getClass().getCanonicalName(), "Failed to publish a messged from the client with the handle " + ActivityConstants.clientId, e);
        }

        return false;
    }
}
