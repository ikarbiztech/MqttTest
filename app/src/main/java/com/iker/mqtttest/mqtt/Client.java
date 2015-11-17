package com.iker.mqttpahotest.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * Created by CONSULTANT on 11/11/2015.
 */
public class Client extends MqttClient{


    public Client(String serverURI, String clientId, MqttClientPersistence persistence) throws MqttException {
        super(serverURI, clientId, persistence);
    }

    public Client(String serverURI, String clientId) throws MqttException {
        super(serverURI, clientId);
    }
}
