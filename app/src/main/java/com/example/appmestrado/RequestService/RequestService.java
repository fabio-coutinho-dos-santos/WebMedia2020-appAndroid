package com.example.appmestrado.RequestService;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RequestService {

    final String url = "http://coutmasters.ddns.net:3000/api/teste";
    private JSONObject jsonObject;
    private Context context;


    public RequestService(JSONObject jsonObject, Context context){
        this.jsonObject = jsonObject;
        this.context = context;
        postJson();
    }

    public void postJson() {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (!response.equals(null)) {
                            Log.i("Your Array Response", response.toString());
                            Toast.makeText(context,"Mensagem enviada com Sucesso!!",Toast.LENGTH_LONG).show();
                        } else {
                            Log.i("Your Array Response", "Data Null");
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error is ", "" + error);
                        Toast.makeText(context,"ERRO ao enviar mensagem!!",Toast.LENGTH_LONG).show();

                    }
                }) {

            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                return params;
            }

        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsObjRequest);
    }

}
