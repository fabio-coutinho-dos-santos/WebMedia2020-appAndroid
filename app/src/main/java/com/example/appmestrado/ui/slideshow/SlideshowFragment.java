package com.example.appmestrado.ui.slideshow;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.appmestrado.MainActivity;
import com.example.appmestrado.R;
import com.example.appmestrado.RequestService.RequestService;
import com.example.appmestrado.Untils.Mqtt;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class SlideshowFragment extends Fragment {

    private Mqtt mqtt = MainActivity.mqtt;
    private static final String TOPICO_ENVIA = "RecebeLocalizacaoFront3";
    private static final String TOPICO_SUBSCRIBE = "EnviaLocationAndroid";
    private RequestService requestService;
    private FusedLocationProviderClient client;

    // create your json here
    private JSONObject jsonObject = new JSONObject();
    private TextView textLocation;
    private TextView textLatitude;
    private TextView textLongitude;
    private CardView cardAguarde;
    private CardView cardLocalizacao;
    private Button btnLocation;
    private Long tsLong;
    private static Long tempoInicial;
    private static Long tempoFinal;
    private static Long tempoTotal;
    private Double latitude;
    private Double longitude;


    private SlideshowViewModel slideshowViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);

        slideshowViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });

        requestPermission();

        textLocation = (TextView) root.findViewById(R.id.textLocationId);
        textLatitude = (TextView) root.findViewById(R.id.textLatitudeId);
        textLongitude = (TextView) root.findViewById(R.id.textLongitudeId);
        cardAguarde = (CardView) root.findViewById(R.id.cardAguardandoChamadaId);
        cardLocalizacao = (CardView) root.findViewById(R.id.cardLocalizacaoChamadaId);

        cardAguarde.setVisibility(View.VISIBLE);
        cardLocalizacao.setVisibility(View.INVISIBLE);


        mqtt.subscribe(TOPICO_SUBSCRIBE, 1);


        mqtt.getClient().setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Toast.makeText(getActivity(), "Conexão Perdida", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception
            {
                textLatitude.setText("");
                textLongitude.setText("");
                String mensagem = new String(message.getPayload());
                if(mensagem.equals("1")) {
                    //Toast.makeText(getActivity(), mensagem.toString(), Toast.LENGTH_SHORT).show();
                    tempoInicial = System.currentTimeMillis();
                    getLocation();
                }

            }


            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });


        return root;
    }

    public void getLocation() {
        client = LocationServices.getFusedLocationProviderClient(getActivity());

        if (ActivityCompat.checkSelfPermission(getActivity(), ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {

            return;
        }
        client.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if(location!=null)
                {
                    tempoFinal = System.currentTimeMillis();
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    textLatitude.setText(textLatitude.getText() + " "+latitude);
                    textLongitude.setText(textLongitude.getText() + " "+longitude);
                    cardAguarde.setVisibility(View.INVISIBLE);
                    cardLocalizacao.setVisibility(View.VISIBLE);

                    tempoTotal = tempoFinal-tempoInicial;

                    try {
                        jsonObject.put("app", "Android");
                        jsonObject.put("mensagem", latitude.toString() + "#" + longitude.toString());
                        jsonObject.put("tempoinicial", tempoInicial);
                        jsonObject.put("tempofinal", tempoFinal);
                        jsonObject.put("tempototal", tempoTotal);
                        //Toast.makeText(getActivity(),""+(tempoFinal-tempoInicial),Toast.LENGTH_LONG).show();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    requestService = new RequestService(jsonObject, getActivity());


//                    textLocation.setText(location.getLatitude() + " / " + location.getLongitude() + " - "+
//                            tempoInicial.toString() + " / " + tempoFinal.toString());

                    mqtt.publish(TOPICO_ENVIA,latitude.toString() + "#" + longitude.toString(),1,false);
                }

            }
        });

    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(getActivity(), new String[]{ACCESS_FINE_LOCATION},1);
    }





}