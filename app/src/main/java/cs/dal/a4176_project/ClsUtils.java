package cs.dal.a4176_project;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Roxanne on 2017/11/26 0026.
 */

public class ClsUtils {
    public static BluetoothDevice remoteDevice = null;

    @SuppressWarnings("unchecked")
    //create bond
    static public boolean createBond(@SuppressWarnings("rawtypes") Class btClass, BluetoothDevice btDevice)
            throws Exception {
        Method createBondMethod = btClass.getMethod("createBond");
        Boolean returnValue = (Boolean) createBondMethod.invoke(btDevice);
        return returnValue.booleanValue();
    }

    @SuppressWarnings("unchecked")
    //remove bond
    static public boolean removeBond(Class btClass, BluetoothDevice btDevice)
            throws Exception {
        Method removeBondMethod = btClass.getMethod("removeBond");
        Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice);
        return returnValue.booleanValue();
    }

    @SuppressWarnings("unchecked")
    //set a pin for server to enter
    static public boolean setPin(Class btClass, BluetoothDevice btDevice,
                                 String str) throws Exception {
        try {
            Method removeBondMethod = btClass.getDeclaredMethod("setPin", new Class[]{byte[].class});
            Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice,
                    new Object[]{str.getBytes()});
            Log.d("returnValue", "setPin is success " + btDevice.getAddress() + returnValue.booleanValue());
            //return message that the pin is set successfully
        } catch (SecurityException e) {
            // throw new RuntimeException(e.getMessage());
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // throw new RuntimeException(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;

    }

    //remove user's input
    @SuppressWarnings("unchecked")
    static public boolean cancelPairingUserInput(Class btClass,
                                                 BluetoothDevice device)

            throws Exception {
        Method createBondMethod = btClass.getMethod("cancelPairingUserInput");

        Boolean returnValue = (Boolean) createBondMethod.invoke(device);
        Log.d("returnValue", "cancelPairingUserInput is success " + returnValue.booleanValue());
        return returnValue.booleanValue();
    }

    @SuppressWarnings("unchecked")
    //cancel bonding process
    static public boolean cancelBondProcess(Class btClass,
                                            BluetoothDevice device)

            throws Exception {
        Method createBondMethod = btClass.getMethod("cancelBondProcess");
        Boolean returnValue = (Boolean) createBondMethod.invoke(device);
        return returnValue.booleanValue();
    }


    @SuppressWarnings("unchecked")
    //show the information of bluetooth device
    static public void printAllInform(Class clsShow) {
        try {
            Method[] hideMethod = clsShow.getMethods();
            int i = 0;
            for (; i < hideMethod.length; i++) {
                //Log.e("method name", hideMethod.getName() + ";and the i is:"
                //      + i);
            }
            Field[] allFields = clsShow.getFields();
            for (i = 0; i < allFields.length; i++) {
                //Log.e("Field name", allFields.getName());
            }
        } catch (SecurityException e) {
            // throw new RuntimeException(e.getMessage());
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // throw new RuntimeException(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
