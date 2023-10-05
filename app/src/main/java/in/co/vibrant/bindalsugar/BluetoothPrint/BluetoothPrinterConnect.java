package in.co.vibrant.bindalsugar.BluetoothPrint;

import static in.co.vibrant.bindalsugar.BluetoothPrint.BluetoothChat.TOAST;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Set;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.OfflineControlModel;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;


/**
 * This Activity appears as a dialog. It lists any paired devices and devices
 * detected in the area after discovery. When a device is chosen by the user,
 * the MAC address of the device is sent back to the parent Activity in the
 * result Intent.
 */
public class BluetoothPrinterConnect extends AppCompatActivity {
    // Debugging
    private static final String TAG = "DeviceListActivity";
    private static final boolean D = true;

    // Return Intent extra
    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    private static final int       REQUEST_CONNECT_DEVICE = 2;
    private static final int       REQUEST_ENABLE_BT      = 3;

    public static final int        MESSAGE_STATE_CHANGE   = 1;
    public static final int        MESSAGE_READ           = 2;
    public static final int        MESSAGE_WRITE          = 3;
    public static final int        MESSAGE_DEVICE_NAME    = 4;
    public static final int        MESSAGE_TOAST          = 5;

    private BluetoothChatService mChatService           = null;

    // Member fields
    private BluetoothAdapter mBtAdapter;
    private BluetoothAdapter mBluetoothAdapter      = null;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
    private ArrayAdapter<String> mNewDevicesArrayAdapter;

    ListView paireddev,founddev;
    Button scanButton ;

    ImageView scanview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup the window
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.bluetooth_scan_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Find Devices");
        toolbar.setTitle("Find Devices");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if ( mBluetoothAdapter == null )
        {
            //txtprintername.setText("Bluetooth is not available");
            Toast.makeText( this, "Bluetooth is not available", Toast.LENGTH_LONG ).show();
            // finish();
            return;
        }
		/*final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);*/
        // Drawable d = getResources().getDrawable( R.drawable.ab_background );
        // actionBar.setBackgroundDrawable( d );

        // scanview = (ImageView)findViewById(R.id.scanning_anim_view);
        //scanview.setBackgroundResource(R.drawable.wifi_animation);
        //	AnimationDrawable frameanimation = (AnimationDrawable)scanview.getBackground();
        //	frameanimation.start();

        // Set result CANCELED in case the user backs out
        setResult(Activity.RESULT_CANCELED);

        // Get the local Bluetooth adapter
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        // Initialize the button to perform device discovery
        scanButton = (Button) findViewById(R.id.button_scan);

        scanButton.setText("SCAN");
        scanButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (scanButton.getText().toString().equals("SCAN")) {
                    scanButton.setText("STOP SCANNING");
                    doDiscovery();
					/*gifwebView.setVisibility(View.VISIBLE);
					gifwebView.resumeTimers();*/
                } else {
                    if (mBtAdapter.isDiscovering()) {
                        mBtAdapter.cancelDiscovery();
                    }
					/*gifwebView.pauseTimers();
					gifwebView.setVisibility(View.INVISIBLE);*/
                }
            }
        });

        // Initialize array adapters. One for already paired devices and
        // one for newly discovered devices
        mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this,R.layout.list_item);
        mNewDevicesArrayAdapter = new ArrayAdapter<String>(this,R.layout.list_item);

        // Find and set up the ListView for paired devices
        paireddev = (ListView) findViewById(R.id.pairedList);


        // Find and set up the ListView for newly discovered devices
        founddev = (ListView) findViewById(R.id.newList);
		/*founddev.setAdapter(mNewDevicesArrayAdapter);
		founddev.setOnItemClickListener(mDeviceClickListener);*/

        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);



        // Get a set of currently paired devices
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                mPairedDevicesArrayAdapter.add(device.getName() + "\n"
                        + device.getAddress());
                paireddev.setAdapter(mPairedDevicesArrayAdapter);
                setListViewHeightBasedOnChildren(paireddev);
            }
        } else {
			/*String noDevices = "No New Device found" ;
			mPairedDevicesArrayAdapter.add(noDevices);*/
        }
        paireddev.setAdapter(mPairedDevicesArrayAdapter);
        setListViewHeightBasedOnChildren(paireddev);
        paireddev.setOnItemClickListener(mDeviceClickListener);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        if ( D )
            Log.e( TAG, "++ ON START ++" );

        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if ( !mBluetoothAdapter.isEnabled() )
        {
            Intent enableIntent = new Intent( BluetoothAdapter.ACTION_REQUEST_ENABLE );
            startActivityForResult( enableIntent, REQUEST_ENABLE_BT );
            // Otherwise, setup the chat session
        }
        if ( mChatService == null )
        {
            setupChat();
        }
    }

    private void setupChat()
    {
        Log.d( TAG, "setupChat()" );
        // Initialize the send button with a listener that for click events
        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothChatService( this, mHandler );
        String printadd = Variables.getPreferenceString(getApplicationContext(), Variables.BluetoothDevice);
        try
        {
            connectDevice( printadd );
        }
        catch ( Exception e )
        {
            Log.e( TAG, "- Exception in connect device -" );
        }

    }

    private void connectDevice( String data )
    {
        // Get the device MAC address
        /*
         * String address = data.getExtras().getString(
         * DeviceListActivity.EXTRA_DEVICE_ADDRESS);
         */
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice( data );
        // Attempt to connect to the device
        mChatService.connect( device );
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Make sure we're not doing discovery anymore
        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
        }

        // Unregister broadcast listeners
        this.unregisterReceiver(mReceiver);
    }

    /**
     * Start device discover with the BluetoothAdapter
     */
    private void doDiscovery() {
        if (D)
            Log.d(TAG, "doDiscovery()");

        // Indicate scanning in the title
        setProgressBarIndeterminateVisibility(true);
        //setTitle(R.string.scanning);

        // Turn on sub-title for new devices
        //findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);
        mNewDevicesArrayAdapter.clear();
        founddev.setAdapter(mNewDevicesArrayAdapter);
        setListViewHeightBasedOnChildren(founddev);
        founddev.setOnItemClickListener(mDeviceClickListener);

        // If we're already discovering, stop it
        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }
        // Request discover from BluetoothAdapter
        mBtAdapter.startDiscovery();
    }

    // The on-click listener for all devices in the ListViews
    private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // Cancel discovery because it's costly and we're about to connect
            mBtAdapter.cancelDiscovery();

            // Get the device MAC address, which is the last 17 chars in the
            // View
            String info = ((TextView) v).getText().toString();
            String address = null;
            try{
                address = info.substring(info.length() - 17);
                BluetoothDevice device = mBluetoothAdapter.getRemoteDevice( address );
                int isPair=device.getBondState();
                if(isPair==12)
                {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                            BluetoothPrinterConnect.this);
                    alertDialog.setTitle(getString(R.string.app_name));
                    alertDialog.setMessage("Would you like to set this device as Default Device");
                    final String finalAddress = address;
                    alertDialog.setPositiveButton(getString(R.string.BTN_OK),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    DBHelper dbh=new DBHelper(BluetoothPrinterConnect.this);
                                    dbh.deleteOfflineControlModel();
                                    OfflineControlModel offlineControlModel=new OfflineControlModel();
                                    offlineControlModel.setPrintAddress(finalAddress);
                                    dbh.insertOfflineControlModel(offlineControlModel);
                                    //dbh.updatePrinter(finalAddress);
                                    new AlertDialogManager().GreenDialog(BluetoothPrinterConnect.this,"Default printer successfully updated..");
                                }
                            });
                    alertDialog.setNegativeButton(getString(R.string.BTN_LATER),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                }else
                {
                    connectDevice(address);
                }

            }
            catch(Exception e)
            {

            }
            // Create the result Intent and include the MAC address
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
            Variables.setPreference(getApplicationContext(), Variables.BluetoothDevice, address );
            // Set result and finish this Activity
            setResult(Activity.RESULT_OK, intent);
            //finish();
        }
    };

    // The BroadcastReceiver that listens for discovered devices and
    // changes the title when discovery is finished
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed
                // already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mNewDevicesArrayAdapter.add(device.getName() + "\n"
                            + device.getAddress());
                    founddev.setAdapter(mNewDevicesArrayAdapter);
                    mNewDevicesArrayAdapter.notifyDataSetChanged();
                    setListViewHeightBasedOnChildren(founddev);
                }
                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
                    .equals(action)) {
                setProgressBarIndeterminateVisibility(false);

                scanButton.setText("SCAN");
                //	gifwebView.pauseTimers();
                //	gifwebView.setVisibility(View.INVISIBLE);
                if (mNewDevicesArrayAdapter.getCount() == 0) {
					/*String noDevices = getResources().getText(
							R.string.none_found).toString();
					mNewDevicesArrayAdapter.add(noDevices);*/
                    String noDevices = "No New Device found";
                    mNewDevicesArrayAdapter.add(noDevices);
                }
            }
        }
    };


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private final Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage( Message msg )
        {
            switch ( msg.what )
            {
                case MESSAGE_STATE_CHANGE:
                    if ( D )
                        Log.i( TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1 );
                    switch ( msg.arg1 )
                    {
                        case BluetoothChatService.STATE_CONNECTED:
                            //setStatus( "Charge " + printcharge );
                            //txtprintername.setText( "Connected to " + mConnectedDeviceName );
                            // mSendButton.setVisibility( View.VISIBLE );
                            //btnLayout.setVisibility( View.VISIBLE );
                            //btnLayoutScan.setVisibility( View.GONE );
                            //sendMessageTahi1(printmsg);
                            new AlertDialogManager().GreenDialog(BluetoothPrinterConnect.this,"Default printer successfully updated..");
                            //finish();
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            // setStatus( R.string.title_connecting );
                            //txtprintername.setText( "Connecting... " );
                            // mSendButton.setVisibility( View.INVISIBLE );
                            //btnLayout.setVisibility( View.GONE );
                            //btnLayout.setVisibility( View.GONE );
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            //PrintUtil.setSelectedPrinterType("");
                            //  setStatus( R.string.title_not_connected );
                            //txtprintername.setText( "Not Connected... " );
                            // mSendButton.setVisibility( View.VISIBLE );
                            //btnLayout.setVisibility( View.GONE );
                            //btnLayoutScan.setVisibility( View.VISIBLE );
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    try
                    {
                        byte[] writeBuf = ( byte[] ) msg.obj;
                        String writeMessage = new String( writeBuf );
                        // mConversationArrayAdapter.add("Me:  "
                        // + writeMessage);
                    }
                    catch ( Exception e )
                    {
                        // TODO: handle exception
                    }
                    // construct a string from the
                    // buffer

                    break;
                case MESSAGE_READ:
                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's
                    // name
                    //mConnectedDeviceName = msg.getData().getString( DEVICE_NAME );
                    //txtprintername.setText( "Connected to " + mConnectedDeviceName );
                    //Toast.makeText( getApplicationContext(),"Connected to " + mConnectedDeviceName, Toast.LENGTH_SHORT ).show();
                    //btnLayout.setVisibility( View.VISIBLE );
                    //btnLayoutScan.setVisibility( View.GONE );
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText( getApplicationContext(),
                            msg.getData().getString( TOAST ), Toast.LENGTH_SHORT ).show();
                    break;
            }
        }
    };

}