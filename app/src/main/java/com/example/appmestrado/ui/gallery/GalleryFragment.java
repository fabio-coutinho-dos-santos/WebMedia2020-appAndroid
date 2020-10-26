package com.example.appmestrado.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.appmestrado.MainActivity;
import com.example.appmestrado.R;
import com.example.appmestrado.Untils.Mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.math.RoundingMode;
import java.text.NumberFormat;

public class GalleryFragment extends Fragment {

    private static final String topicoMedia = "EnviaMediaMedidorCorrente";
    private static final String topicoMedicao = "EnviaMedicaoMedidorCorrente";
    private GalleryViewModel galleryViewModel;
    private TextView textMedicoes;
    private TextView textMedia;
    private Mqtt mqtt = MainActivity.mqtt;
    private float previsãoMensal=0;
    private NumberFormat format = NumberFormat.getInstance();







    private String teste = "teste";
    byte[] encodedPayload = new byte[0];

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {



        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
//        final TextView textView = root.findViewById(R.id.text_gallery);
        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
//                textView.setText(s);
            }
        });

        textMedicoes = (TextView) root.findViewById(R.id.textMedicaoNumeroId);
        textMedia = (TextView) root.findViewById(R.id.textMediaNumeroId);

        mqtt.subscribe(topicoMedia,1);
        mqtt.subscribe(topicoMedicao,1);

        mqtt.getClient().setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Toast.makeText(getActivity(), "Conexão Perdida", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception
            {
                String mensagem = new String(message.getPayload());

                if(topic.equals(topicoMedicao))
                    textMedicoes.setText(mensagem);

                if(topic.equals(topicoMedia)) {
                    format.setMaximumFractionDigits(2);
                    format.setMinimumFractionDigits(2);
                    format.setMaximumIntegerDigits(2);
                    format.setRoundingMode(RoundingMode.HALF_UP);
                    previsãoMensal = (Float.parseFloat(mensagem) * 127/1000*24);
                    textMedia.setText(""+format.format(previsãoMensal));
                }

            }


            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

//        mqtt2.getClient().setCallback(new MqttCallback() {
//            @Override
//            public void connectionLost(Throwable cause) {
//                Toast.makeText(getActivity(), "Conexão Perdida", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void messageArrived(String topic, MqttMessage message) throws Exception
//            {
//                String mensagem = new String(message.getPayload());
//
//                //Toast.makeText(getActivity(), mensagem + " / " + topic , Toast.LENGTH_SHORT).show();
//
//
////                if(topic == topicoMedicao)
////                    textMedicoes.setText(mensagem);
////
////                if(topic == topicoMedia)
////                    textMedia.setText(mensagem);
//
//            }
//
//
//            @Override
//            public void deliveryComplete(IMqttDeliveryToken token) {
//
//            }
//        });




        return root;
    }
}