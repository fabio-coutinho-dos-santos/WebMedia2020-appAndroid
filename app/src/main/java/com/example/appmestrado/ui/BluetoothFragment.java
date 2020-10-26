package com.example.appmestrado.ui;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.appmestrado.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static com.google.android.gms.internal.zzagy.runOnUiThread;

public class BluetoothFragment extends Fragment {

    private ListView listBluetoothDevices;
    private FloatingActionButton floatingBtnBluetooth;
    private ArrayList<String> arrayListDevicesNames = new ArrayList<>();
    private ArrayList<BluetoothDevice> arrayListDevices = new ArrayList<>();
    private ArrayAdapter arrayAdapter;
    private BluetoothAdapter bluetoothAdapter;
    private boolean mScanning;
    private Handler handler= new Handler();
    private static final long SCAN_PERIOD = 10000;
    private BluetoothGatt bluetoothGatt;
    private int connectionState = STATE_DISCONNECTED;

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";



    // Stops scanning after 10 seconds.

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BluetoothFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BluetothFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BluetoothFragment newInstance(String param1, String param2) {
        BluetoothFragment fragment = new BluetoothFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bluetooth, container, false);
        // Inflate the layout for this fragment

        listBluetoothDevices = (ListView) view.findViewById(R.id.listBluetoothId);
        floatingBtnBluetooth = (FloatingActionButton) view.findViewById(R.id.floattingBtnId);

        floatingBtnBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(getActivity(),"Clicou",Toast.LENGTH_LONG).show();

                final BluetoothManager bluetoothManager =
                        (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
                bluetoothAdapter = bluetoothManager.getAdapter();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mScanning = false;
                        bluetoothAdapter.stopLeScan(scanCallback);
                    }
                }, SCAN_PERIOD);

                mScanning = true;

                bluetoothAdapter.startLeScan(scanCallback);

            }


        });

        listBluetoothDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getActivity(),"Clicou - " + arrayListDevices.get(i).getName(),Toast.LENGTH_LONG).show();
                bluetoothGatt = arrayListDevices.get(i).connectGatt(getActivity(), false, gattCallback);

            }
        });


        return view;
    }

    private final BluetoothGattCallback gattCallback =
            new BluetoothGattCallback() {

                @Override
                public void onConnectionStateChange(final BluetoothGatt gatt, final int status, final int newState) {
                    // this will get called when a device connects or disconnects
                    System.out.println(newState);
                    //Toast.makeText(getActivity(),""+newState,Toast.LENGTH_SHORT).show();
                    switch (newState) {
                        case 0:
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(getActivity(),"Desconectado do Dispositivo.",Toast.LENGTH_LONG).show();
//                                    peripheralTextView.append("device disconnected\n");
//                                    connectToDevice.setVisibility(View.VISIBLE);
//                                    disconnectDevice.setVisibility(View.INVISIBLE);
                                }
                            });
                            break;
                        case 2:
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(getActivity(),"Conectado ao Dispositivo.",Toast.LENGTH_LONG).show();

//                                    peripheralTextView.append("device connected\n");
//                                    connectToDevice.setVisibility(View.INVISIBLE);
//                                    disconnectDevice.setVisibility(View.VISIBLE);

                                }
                            });

                            // discover services and characteristics for this device
                            bluetoothGatt.discoverServices();

                            break;
                        default:
                           getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(getActivity(),"Sei lá to GATT server.",Toast.LENGTH_LONG).show();
                                }
                            });
                            break;
                    }
                }

//                @Override
//                public void onServicesDiscovered(final BluetoothGatt gatt, final int status) {
//                    // this will get called after the client initiates a 			BluetoothGatt.discoverServices() call
//                    getActivity().runOnUiThread(new Runnable() {
//                        public void run() {
//                            Toast.makeText(getActivity(),"Serviços encontrados",Toast.LENGTH_LONG).show();
//                        }
//                    });
//                    //displayGattServices(bluetoothGatt.getServices());
//                }

//                @Override
//                public void onConnectionStateChange(BluetoothGatt gatt, int status,
//                                                    int newState) {
//                    String intentAction;
//                    if (newState == BluetoothProfile.STATE_CONNECTED) {
//                        intentAction = ACTION_GATT_CONNECTED;
//                        connectionState = STATE_CONNECTED;
//                       // broadcastUpdate(intentAction);
//                        Toast.makeText(getActivity(),"Connected to GATT server.",Toast.LENGTH_LONG).show();
//                        bluetoothGatt.discoverServices();
//                        //Log.i(TAG, "Connected to GATT server.");
//                        //Log.i(TAG, "Attempting to start service discovery:" +
//
//
//                    } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
//                        intentAction = ACTION_GATT_DISCONNECTED;
//                        connectionState = STATE_DISCONNECTED;
//                        Toast.makeText(getActivity(),"Disconnected from GATT server.",Toast.LENGTH_LONG).show();
//
//                        //Log.i(TAG, "Disconnected from GATT server.");
//                        //broadcastUpdate(intentAction);
//                    }
//                }
//
//                @Override
//                // New services discovered
//                public void onServicesDiscovered(BluetoothGatt gatt, int status) {
//                    if (status == BluetoothGatt.GATT_SUCCESS) {
//                        Toast.makeText(getActivity(),"Connected to GATT server.",Toast.LENGTH_LONG).show();
//                    } else {
//                        Toast.makeText(getActivity(),"onServicesDiscovered received: ",Toast.LENGTH_LONG).show();
//                    }
//                }
//
//                public void onCharacteristicRead(BluetoothGatt gatt,
//                                                 BluetoothGattCharacteristic characteristic,
//                                                 int status) {
//                    if (status == BluetoothGatt.GATT_SUCCESS) {
//                        Toast.makeText(getActivity(),"Connected to GATT server.",Toast.LENGTH_LONG).show();
//                    }
//                }


            };

    // Stops scanning after 10 seconds.
    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    bluetoothAdapter.stopLeScan(scanCallback);
                }
            }, SCAN_PERIOD);

            mScanning = true;

            bluetoothAdapter.startLeScan(scanCallback);
        } else {
            mScanning = false;
            bluetoothAdapter.stopLeScan(scanCallback);
        }


    }

    private BluetoothAdapter.LeScanCallback scanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi,
                                     byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if(!arrayListDevicesNames.contains(device.getName())) {
                                arrayListDevicesNames.add(device.getName());
                                arrayListDevices.add(device);
                                arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, arrayListDevicesNames);
                                listBluetoothDevices.setAdapter(arrayAdapter);
                            }
                        }
                    });
                }
            };

}