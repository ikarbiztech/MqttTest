package com.iker.mqtttest.mqtt;

import android.os.Binder;

import org.eclipse.paho.android.service.MqttService;

/**
 * Created by CONSULTANT on 12/11/2015.
 */
public class ServiceBinder extends Binder {

    private MqttService mqttService;
    private String activityToken;

    ServiceBinder(MqttService mqttService) {
        this.mqttService = mqttService;
    }

    /**
     * @return a reference to the Service
     */
    public MqttService getService() {
        return mqttService;
    }

    void setActivityToken(String activityToken) {
        this.activityToken = activityToken;
    }

    /**
     * @return the activityToken provided when the Service was started
     */
    public String getActivityToken() {
        return activityToken;
    }



}
