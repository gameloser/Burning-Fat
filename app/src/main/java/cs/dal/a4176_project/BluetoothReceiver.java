package cs.dal.a4176_project;

/**
 * Created by Roxanne on 2017/11/26 0026.
 */

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BluetoothReceiver extends BroadcastReceiver {
    String strPsw = "0000";//default connection key
    final String ACTION_PAIRING_REQUEST = "android.bluetooth.device.action.PAIRING_REQUEST";
    static BluetoothDevice remoteDevice = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_PAIRING_REQUEST)) {

            BluetoothDevice device = intent
                    .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);//create a new activity and get the device status
            if (device.getBondState() != BluetoothDevice.BOND_BONDED) {//check if the device is bonded or not
                try {
                    ClsUtils.setPin(device.getClass(), device, strPsw);//set password (connection key)
                    ClsUtils.cancelPairingUserInput(device.getClass(), device);
                    Toast.makeText(context, "Pair Information   " + device.getName(), Toast.LENGTH_LONG).show();
                    //send a toast message if receiver success connected with the server.
                } catch (Exception e) {
                    Toast.makeText(context, "Connection fails.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
