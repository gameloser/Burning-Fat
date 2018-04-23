package cs.dal.a4176_project;


/**
 * Created by Roxanne on 2017/11/26 0026.
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

public class BluetoothActivity extends Activity {

    Button btnSearch, btnDis;//button to scan other device and set own device visible
    Switch tbtnSwitch;//on off switch
    ListView lvBTDevices;//list of all visible bluetooth device
    ArrayAdapter<String> adtDevices;
    List<String> lstDevices = new ArrayList<String>();
    BluetoothAdapter btAdapt;
    public static BluetoothSocket btSocket;
    Button back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);



        btnSearch = (Button) this.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new ClickEvent());
        btnDis = (Button) this.findViewById(R.id.btnDis);
        btnDis.setOnClickListener(new ClickEvent());
        //back to the main page
        back = findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        //Set up switch for bluetooth
        tbtnSwitch = (Switch) this.findViewById(R.id.tbtnSwitch);
        tbtnSwitch.setOnClickListener(new ClickEvent());
        //Add scanned device to array to show in the listview
        lvBTDevices = (ListView) this.findViewById(R.id.lvDevices);
        //add new device to the list
        adtDevices = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, lstDevices);
        lvBTDevices.setAdapter(adtDevices);
        lvBTDevices.setOnItemClickListener(new ItemClickEvent());

        btAdapt = BluetoothAdapter.getDefaultAdapter();
        //See if the bluetooth is on or off, if on change to off, if off change it to on.
        if (btAdapt.getState() == BluetoothAdapter.STATE_OFF)
            tbtnSwitch.setChecked(true);
        else if (btAdapt.getState() == BluetoothAdapter.STATE_ON) tbtnSwitch.setChecked(true);

        if (btAdapt.isEnabled()) {
            tbtnSwitch.setChecked(true);
        } else {
            tbtnSwitch.setChecked(false);
        }

        //register receiver
        IntentFilter intent = new IntentFilter();//start another activity
        intent.addAction(BluetoothDevice.ACTION_FOUND);
        intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(searchDevices, intent);
    }
    //search other bluetooth device function
    private final BroadcastReceiver searchDevices = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            Bundle b = intent.getExtras();
            Object[] lstName = b.keySet().toArray();//store the name of all device

            //show all devices
            for (int i = 0; i < lstName.length; i++) {
                String keyName = lstName.toString();
                Log.e(keyName, String.valueOf(b.get(keyName)));
            }
            BluetoothDevice device = null;

            //get their mac address
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() == BluetoothDevice.BOND_NONE) {//get whether the bluetooth is connected or not
                    String str = "           Not Paired|" + device.getName() + "|"
                            + device.getAddress();
                    if (lstDevices.indexOf(str) == -1)// prevent redundancy
                        lstDevices.add(str); //get name and MAC address
                    adtDevices.notifyDataSetChanged();
                }
            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //show message and change bonding.
                switch (device.getBondState()) {
                    case BluetoothDevice.BOND_BONDING:
                        Log.d("BlueToothTestActivity", "Pairing");
                        break;
                    case BluetoothDevice.BOND_BONDED:
                        Log.d("BlueToothTestActivity", "Pair Completed");

                        break;
                    case BluetoothDevice.BOND_NONE:
                        Log.d("BlueToothTestActivity", "Cancel Pair");
                    default:
                        break;
                }
            }

        }
    };

    @Override
    //destroy connection
    protected void onDestroy() {
        this.unregisterReceiver(searchDevices);
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    class ItemClickEvent implements AdapterView.OnItemClickListener {

        @Override
        //click on a device and start connection
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            if (btAdapt.isDiscovering()) btAdapt.cancelDiscovery();
            String str = lstDevices.get(arg2);
            String[] values = str.split("\\|");
            String address = values[2];
            Log.e("address", values[2]);
            BluetoothDevice btDev = btAdapt.getRemoteDevice(address);
            try {
                Boolean returnValue = false;
                //check the bond of targeted device
                if (btDev.getBondState() == BluetoothDevice.BOND_NONE) {
                    Toast.makeText(BluetoothActivity.this, "Send paring request", Toast.LENGTH_LONG).show();
                    ClsUtils.createBond(btDev.getClass(), btDev);
                } else if (btDev.getBondState() == BluetoothDevice.BOND_BONDED) {
                    Toast.makeText(BluetoothActivity.this, btDev.getBondState() + "...connected successfully", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class ClickEvent implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v == btnSearch) {
                //search bluetooth device
                if (btAdapt.getState() == BluetoothAdapter.STATE_OFF) {
                    // if it is off
                    Toast.makeText(BluetoothActivity.this, "Please turn on the bluetooth", Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                if (btAdapt.isDiscovering())
                    btAdapt.cancelDiscovery();
                lstDevices.clear();
                Object[] lstDevice = btAdapt.getBondedDevices().toArray();
                for (int i = 0; i < lstDevice.length; i++) {
                    BluetoothDevice device = (BluetoothDevice) lstDevice[i];
                    String str = "    Paired|" + device.getName() + "|" + device.getAddress();
                    lstDevices.add(str);
                    adtDevices.notifyDataSetChanged();
                }
                setTitle("This Device:" + btAdapt.getAddress());
                btAdapt.startDiscovery();
            } else if (v == tbtnSwitch) {
                if (tbtnSwitch.isChecked() == true)
                    btAdapt.enable();

                else if (tbtnSwitch.isChecked() == false)
                    btAdapt.disable();

            } else if (v == btnDis)//enable to be searched
            {
                Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                startActivity(discoverableIntent);
            }
        }
    }

}