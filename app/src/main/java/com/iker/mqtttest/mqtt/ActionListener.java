
package com.iker.mqttpahotest.mqtt;


import com.iker.mqttpahotest.R;
import com.iker.mqttpahotest.mqtt.Connection.*;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

import android.content.Context;
import android.util.Log;


/**
 * This Class handles receiving information from the
 * {link MqttAndroidClient} and updating the {link Connection} associated with 
 * the action
 */
public class ActionListener implements IMqttActionListener {

  /**
   * Actions that can be performed Asynchronously <strong>and</strong> associated with a
   * {link ActionListener} object
   *
   */

  public enum Action {
    /** Connect Action **/
    CONNECT,
    /** Disconnect Action **/
    DISCONNECT,
    /** Subscribe Action **/
    SUBSCRIBE,
    /** Publish Action **/
    PUBLISH
  }

  /**
   * The {link Action} that is associated with this instance of
   * <code>ActionListener</code>
   **/
  private Action action;
  /** The arguments passed to be used for formatting strings**/
  private String[] additionalArgs;
  /** Handle of the {link Connection} this action was being executed on **/
  private String clientHandle;
  /** {link Context} for performing various operations **/
  private Context context;

  /**
   * Creates a generic action listener for actions performed form any activity
   *
   * @param context
   *            The application context
   * @param action
   *            The action that is being performed
   * @param clientHandle
   *            The handle for the client which the action is being performed
   *            on
   * @param additionalArgs
   *            Used for as arguments for string formating
   */
  public ActionListener(Context context, Action action,
      String clientHandle, String... additionalArgs) {
    this.context = context;
    this.action = action;
    this.clientHandle = clientHandle;
    this.additionalArgs = additionalArgs;
  }

  /**
   * The action associated with this listener has been successful.
   *
   * @param asyncActionToken
   *            This argument is not used
   */
  @Override
  public void onSuccess(IMqttToken asyncActionToken) {
    switch (action) {
      case CONNECT :

        break;
      case DISCONNECT :

        break;
      case SUBSCRIBE :

        break;
      case PUBLISH :

        break;
    }

  }


  /**
   * The action associated with the object was a failure
   *
   * @param token
   *            This argument is not used
   * @param exception
   *            The exception which indicates why the action failed
   */
  @Override
  public void onFailure(IMqttToken token, Throwable exception) {
    switch (action) {
      case CONNECT :

        break;
      case DISCONNECT :

        break;
      case SUBSCRIBE :

        break;
      case PUBLISH :

        break;
    }

  }


}