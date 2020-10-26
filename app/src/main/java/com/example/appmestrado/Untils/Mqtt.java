package com.example.appmestrado.Untils;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

public class Mqtt {
    private Context mContext;
    private static String TAG = "Mqtt";
    static String MQTTSERVER = "tcp://coutmasters.ddns.net:1883";
    private MqttAndroidClient client;
    private ProgressDialog progressDialog;
    private static int id_notificacao = 0;


    public Mqtt(Context mContext){
        this.mContext = mContext;

        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(mContext.getApplicationContext(), MQTTSERVER,
                clientId);
    }


    public void connect(){

        try {

            IMqttToken token = client.connect();

            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken)
                {
                    Log.d("TAG", "onSuccess");
                    //Toast.makeText(mContext, "Sucesso", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception)
                {
                    if(!client.isConnected())
                    {
                        Toast.makeText(mContext, "Erro ao conectar", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailure");
                    }
                    // Something went wrong e.g. connection timeout or firewall problems
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void disconnect(){
        try {
            IMqttToken disconToken = client.disconnect();

            disconToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken)
                {
                    Log.d(TAG,"Desconectado com sucesso!");
                    Toast.makeText(mContext, "Desconectado", Toast.LENGTH_SHORT).show();
                    connect();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    Log.e(TAG,"Falha ao desconectar do broker!");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publish(String topic, String payload, int qos, boolean retained){
        try {
            client.publish(topic, payload.getBytes(), qos, retained);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void subscribe(String topic, int qos){
        try{
            client.subscribe(topic, qos);
            //Toast.makeText(mContext, "Inscrito", Toast.LENGTH_SHORT).show();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public MqttAndroidClient getClient(){
        return client;
    }
}

