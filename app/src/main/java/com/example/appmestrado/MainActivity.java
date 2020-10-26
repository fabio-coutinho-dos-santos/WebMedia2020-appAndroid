package com.example.appmestrado;

import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.appmestrado.Untils.Mqtt;
import com.example.appmestrado.ui.gallery.GalleryFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public static Mqtt mqtt;


    //RequestQueue queue = Volley.newRequestQueue(this);
    //final String url = "http://coutmasters.ddns.net:3000/api/teste";


    // create your json here
    //JSONObject jsonObject = new JSONObject();



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mqtt = new Mqtt(getApplicationContext());

        if(!mqtt.getClient().isConnected())
            mqtt.connect();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });



        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_bluetooth)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

//        try {
//            jsonObject.put("app", "Android");
//            jsonObject.put("mensagem", "teste");
//            jsonObject.put("tempoinicial", "1");
//            jsonObject.put("tempofinal", "2");
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

//        JsonObjectRequest jsObjRequest = new JsonObjectRequest
//                (Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                if (!response.equals(null)) {
//                    Log.i("Your Array Response", response.toString());
//                    resposta = response.toString();
//                } else {
//                    Log.i("Your Array Response", "Data Null");
//                }
//            }
//
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("error is ", "" + error);
//            }
//        }) {
//
//            //This is for Headers If You Needed
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Content-Type", "application/json; charset=UTF-8");
//                return params;
//            }
//
//        };
//        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
//        queue.add(jsObjRequest);
//
//        Toast.makeText(this,resposta,Toast.LENGTH_LONG).show();

//        JsonObjectRequest getRequest = new JsonObjectRequest(
//                Request.Method.GET,
//                url,
//                null,
//                new Response.Listener<JSONObject>()
//        {
//            @Override
//            public void onResponse(JSONObject response) {
//                // mostra a resposta
//                Log.d("Response", response.toString());
//            }
//        },
//                new Response.ErrorListener()
//                {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.d("Error.Response", error.toString());
//                    }
//                });
//
//        public Map<String, String> getHeaders() throws AuthFailureError {
//            Map<String, String>  params = new HashMap<String, String>();
//            params.put("User-Agent", "Nintendo Gameboy");
//            params.put("Accept-Language", "fr");
//
//            return params;
//        }
//        };
//
//
//
//// Adiciona a Fila de requisicoes
//        queue.add(getRequest);


//        try {
//            String response = example.post(jsonObject);
//            Log.d("responsegot",response);
//            Toast.makeText(this,response, Toast.LENGTH_LONG).show();;
//        } catch (Exception exception){
//            exception.printStackTrace();
//            Toast.makeText(this,exception.toString(), Toast.LENGTH_LONG).show();;
//        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void showGalleryFragment()
    {
        GalleryFragment fragment = new GalleryFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
        fragmentTransaction.commit();
    }


}