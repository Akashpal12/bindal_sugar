/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package in.co.vibrant.bindalsugar.BluetoothPrint;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Movie;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.co.vibrant.bindalsugar.R;


/**
 * This is the main Activity that displays the current chat session.
 */
public class BluetoothChat extends AppCompatActivity
{
    // Debugging
    private static final String TAG                    = "BluetoothChat";
    private static final boolean   D                      = true;

    // Message types sent from the BluetoothChatService Handler
    public static final int        MESSAGE_STATE_CHANGE   = 1;
    public static final int        MESSAGE_READ           = 2;
    public static final int        MESSAGE_WRITE          = 3;
    public static final int        MESSAGE_DEVICE_NAME    = 4;
    public static final int        MESSAGE_TOAST          = 5;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME            = "device_name";
    public static final String TOAST                  = "toast";

    // Intent request codes
    private static final int       REQUEST_CONNECT_DEVICE = 2;
    private static final int       REQUEST_ENABLE_BT      = 3;

    // Layout Views
    private Button mSendButton,button_cancel,button_scan;

    // Name of the connected device
    private String mConnectedDeviceName   = null;
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter      = null;
    // Member object for the chat services
    private BluetoothChatService    mChatService           = null;
    String receivedmsg            = "";
    boolean                        ifbattery              = false;
    byte                           cmdforcharge;
    String prevCmd                = "";
    String readData               = "";
    String printmsg               = "", printadd = "", printcharge = "";
    TextView txtprintername,print_text;
    static Movie movie;
    LinearLayout btnLayout,btnLayoutScan;


    public static int PRINTERTYPE_DOT=0;
    public static int PRINTERTYPE_THERMAL=1;


    byte[] bytearr,printcmd;

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        if ( D )
            Log.e( TAG, "+++ ON CREATE +++" );

        // Set up the window layout
        setContentView( R.layout.bluetooth_chat_activity );
        // setContentView( new GifView(this) );
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Print");
        toolbar.setTitle("Print");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /*final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled( true );
        actionBar.setIcon( new ColorDrawable( getResources().getColor( android.R.color.transparent ) ) );
        actionBar.setTitle( "PRINT INVOICE" );*/

        btnLayout=findViewById( R.id.btnLayout );
        btnLayoutScan=findViewById( R.id.btnLayoutScan );
        txtprintername=findViewById( R.id.txtprintername);
        print_text=findViewById( R.id.print_text);
        button_cancel=findViewById( R.id.button_cancel);
        button_scan=findViewById( R.id.button_scan);
        printmsg = getIntent().getStringExtra( Variables.PrintString );
        print_text.setText(getIntent().getStringExtra( "printData" ));
        Log.e("hhhh", "onCreate: "+printmsg );
        
        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


        printadd = Variables.getPreferenceString(getApplicationContext(), Variables.BluetoothDevice);
        if ( printadd.length() > 0 )
        {
          //  printcharge = my_Preferences.getString( Variables.PRINTERCHARGE, "" );
           // setStatus( "Charge "+printcharge );
        }

        if ( mBluetoothAdapter == null )
        {
            txtprintername.setText("Bluetooth is not available");
            Toast.makeText( this, "Bluetooth is not available", Toast.LENGTH_LONG ).show();
            // finish();
            return;
        }
        
        
        
        button_cancel.setOnClickListener( new OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BluetoothChat.this);
                alertDialogBuilder.setMessage("Do You Want To Exit?");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                                finish();
                            }
                        });

                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                
            }
        } );

        button_scan.setOnClickListener( new OnClickListener()
        {

            @Override
            public void onClick( View v )
            {
                try {
                    Intent serverIntent = new Intent(BluetoothChat.this, DeviceListActivity.class);
                    startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                }
                catch(Exception e)
                {
                    Log.e( TAG, "++ ON START ++"+e.toString() );
                }
            }
        } );

    }

/*    static class GifView extends View
    {

        GifView( Context context )
        {
            super( context );
            movie = Movie.decodeStream( context.getResources().openRawResource( R.drawable.gif ) );
        }

        @Override
        protected void onDraw( Canvas canvas )
        {
            if ( movie != null )
            {
                movie.setTime( ( int ) SystemClock.uptimeMillis() % movie.duration() );
                int width=this.getWidth();
                int height=this.getHeight();
                movie.draw( canvas, width/4, height/3 );
                invalidate();
            }
        }

    }*/

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
        else
        {
            if ( mChatService == null )
            {
                setupChat();
            }
        }
    }

    @Override
    public synchronized void onResume()
    {
        super.onResume();
        if ( D )
            Log.e( TAG, "+ ON RESUME +" );

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity
        // returns.
        if ( mChatService != null )
        {
            // Only if the state is STATE_NONE, do we know that we haven't
            // started already
            if ( mChatService.getState() == BluetoothChatService.STATE_NONE )
            {
                // Start the Bluetooth chat services
                mChatService.start();
            }
        }
    }

    private void setupChat()
    {
        Log.d( TAG, "setupChat()" );
        // Initialize the send button with a listener that for click events
        mSendButton = (Button) findViewById( R.id.button_send );
        mSendButton.setTag(1);
        mSendButton.setOnClickListener( new OnClickListener()
        {
            public void onClick( View v )
            {
                //sendMessage( printmsg );
                sendMessageTahi1(printmsg);
            }
        } );

        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothChatService( this, mHandler );

        try
        {
            connectDevice( printadd );
        }
        catch ( Exception e )
        {
            Log.e( TAG, "- Exception in connect device -" );
        }

    }

    @Override
    public synchronized void onPause()
    {
        super.onPause();
        if ( D )
            Log.e( TAG, "- ON PAUSE -" );
    }

    @Override
    public void onStop()
    {
        super.onStop();
        if ( D )
            Log.e( TAG, "-- ON STOP --" );
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        // Stop the Bluetooth chat services
        if ( mChatService != null )
            mChatService.stop();
        if ( D )
            Log.e( TAG, "--- ON DESTROY ---" );
    }

    private void ensureDiscoverable()
    {
        if ( D )
            Log.d( TAG, "ensure discoverable" );
        if ( mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE )
        {
            Intent discoverableIntent = new Intent( BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE );
            discoverableIntent.putExtra( BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300 );
            startActivity( discoverableIntent );
        }
    }

    public void sendMessageTahi1( String message ) {
        receivedmsg = message;

        /*if ( !ifbattery)
        {
            checkbattery();
        }else {*/
            Log.e("connectedddddddddd", "connectedddddddddd" + mChatService.getState());
            Log.e("connectedddddddddd", "connectedddddddddd" + BluetoothChatService.STATE_CONNECTED);


    /*    if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Log.e("connectedddddddddd","connecteddddddddddentereddddddddddd"+mChatService.getState());
            Toast.makeText(this, "Not Connected... ", Toast.LENGTH_SHORT)
                    .show();
            txtprintername.setText("Not Connected... ");
            return;
        }
*/


            if (message.length() > 0) {
                String str = message;
                // MyToast.toastMessage("Please wait Printing is in Process ",
                // pContext);
                //Toast.makeText( BluetoothChat.this, "  Please wait Printing is in Progress ", 1 ).show();
                String[] strArray = str.split("<");

                String hex = "", commandStr = "";
                try {

                    for (int i = 0; i < strArray.length; i++) {
                        String mstr = "<" + strArray[i];
                        Pattern pattern = Pattern.compile("<(.*?)>");
                        Matcher matcher = pattern.matcher(mstr);
                        Log.e("", " inside try catch  inside for malliiiiiiiiii");
                        // Log.i("", ""+matcher);
                        byte cmd = (byte) 0x10;
                        String strPrintArray = "...........";
                        // strPrintArray =
                        // mstr.replace("<"+matcher.group(1)+">","");
                        if (matcher.find()) {
                            strPrintArray = mstr.replace("<" + matcher.group(1) + ">", "");
                            // System.out.println(strPrintArray+"  1 "+matcher.group(1)+"  "+matcher.groupCount());
                            commandStr = matcher.group(1);
                            Log.e("dfsdf", "lengthis" + commandStr);

                            if (!commandStr.equals("0xPG")) {
                                try {
                                    hex = matcher.group(1).replace("0x", "");
                                    mChatService.write((byte) Integer.parseInt(hex, 16));
                                    mChatService.write(strPrintArray.getBytes());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {

                                //InputStream stream = (InputStream) getAssets().open("robot.bmp");

                                final String imageStr = "";//ParkingDetails.imgstring;

                                Log.e("dfsdf", "lengthis" + bytearr);
                                if (bytearr!=null && bytearr.length > 0) {


                                    mChatService.write(printcmd);
                                    mChatService.write(bytearr);


                                }
                            }

                        }


                        Log.e("", "remaining string: " + strPrintArray);


                    }

                    try {
                        // Thread.sleep(1500);
                        // ( ( Activity ) BluetoothChat.this ).finish();

                        mSendButton.setText("Reprint");




                    } catch (Exception e) {
                        // TODO: handle exception
                    }

                    //}
                    // }
                } catch (Exception e) {

                }


            }
      //  }
    }


    public void sendMessage( String message )
    {
        receivedmsg = message;

        if ( !ifbattery)
        {
            checkbattery();
        }

        else
        {

            try
            {
                Thread.sleep( 1050 );
            }
            catch ( InterruptedException e )
            {
                e.printStackTrace();
            }

            if ( message.length() > 0 )
            {
                String str = message;
                // MyToast.toastMessage("Please wait Printing is in Process ",
                // pContext);
                Toast.makeText( BluetoothChat.this, "  Please wait Printing is in Progress ", Toast.LENGTH_LONG).show();
                String[] strArray = str.split( "<" );

                try
                {

                    for ( int i = 0; i < strArray.length; i++ )
                    {
                        String mstr = "<" + strArray[ i ];
                        Pattern pattern = Pattern.compile( "<(.*?)>" );
                        Matcher matcher = pattern.matcher( mstr );
                        byte cmd = ( byte ) 0x10;
                        String strPrintArray = "...........";
                        if ( matcher.find() )
                        {
                            strPrintArray = mstr.replace( "<" + matcher.group( 1 ) + ">", "" );
                            try {

                                if (matcher.group(1).equals("0x09")) {

                                    Log.e("inside 09 match: ", "asdad");
                                    // Normal
                                    // cmd = ( byte ) 0x09;
                                    byte[] m = new byte[2];
                                    byte[] m2 = new byte[3];
                                    /*
                                     * m[0] = (byte)0x1b; m[1] = (byte)0x74;
                                     * m[2] = (byte)0x00;
                                     */
                                    m[0] = (byte) 0x1b;
                                    m[1] = (byte) 0x40;

                                    mChatService.write(m);
                                    // Thread.sleep(1050);
                                    m2[0] = (byte) 0x1b;
                                    m2[1] = (byte) 0x21;
                                    m2[2] = (byte) 0x00;

                                    // Thread.sleep(1050);
                                    /*
                                     * if ( i == 1 ) { Thread.sleep( 1050 ); }
                                     */
                                    // Log.e( "MAINCHAT", "<0x09>" );
                                    mChatService.write(m2);
                                    /*
                                     * for(int j = 0;j < m.length;j++) {
                                     * mChatService.write(m[j]);
                                     *
                                     * }
                                     */

                                    // mChatService.write( cmd );

                                    // Thread.sleep(1050);
                                } else if (matcher.group(1).equals("0x11")) {
                                    // Double Width
                                    cmd = (byte) 0x11;
                                    // Thread.sleep(1050);
                                    if (i == 1) {
                                        Thread.sleep(1050);
                                    }
                                    //Log.e( "MAINCHAT", "<0x11>" );

                                    mChatService.write(cmd);

                                    // Thread.sleep(1050);
                                } else if (matcher.group(1).equals("0x10")) {
                                    // Double Height
                                    cmd = (byte) 0x10;
                                    //   Log.e( "MAINCHAT", "<0x10>" );
                                    if (i == 1) {
                                        Thread.sleep(1050);
                                    }
                                    // Thread.sleep(1050);
                                    mChatService.write(cmd);

                                    // Thread.sleep(1050);
                                } else if (matcher.group(1).equals("0x12")) {
                                    // Double
                                    cmd = (byte) 0x12;
                                    // Thread.sleep(1050);
                                    // Log.e( "MAINCHAT", "<0x12>" );
                                    if (i == 1) {
                                        Thread.sleep(1050);
                                    }
                                    mChatService.write(cmd);

                                    // Thread.sleep(1050);
                                } else if (matcher.group(1).equals("0x13")) {
                                    // LandScape
                                    cmd = (byte) 0x13;
                                    // Thread.sleep(1050);
                                    // Log.e( "MAINCHAT", "<0x13>" );
                                    if (i == 1) {
                                        Thread.sleep(1050);
                                    }
                                    mChatService.write(cmd);

                                    // Thread.sleep(1050);
                                } else if (matcher.group(1).equals("0x0B")) {
                                    // BarCode
                                    cmd = (byte) 0x0B;
                                    // Thread.sleep(1050);
                                    if (i == 1) {
                                        Thread.sleep(1050);
                                    }
                                    mChatService.write(cmd);

                                    // Thread.sleep(1050);
                                } else if (matcher.group(1).equals("0x0D")) {
                                    // paper feed
                                    cmd = (byte) 0x0D;
                                    // Thread.sleep(1050);
                                    if (i == 1) {
                                        Thread.sleep(1050);
                                    }
                                    mChatService.write(cmd);

                                    // Thread.sleep(1050);
                                } else if (matcher.group(1).equals("0x81")) {
                                    // Graphics
                                    cmd = (byte) 0x81;
                                    // Thread.sleep(1050);
                                    if (i == 1) {
                                        Thread.sleep(1050);
                                    }
                                    mChatService.write(cmd);

                                    // Thread.sleep(1050);
                                } else if (matcher.group(1).equals("0x0A")) {
                                    // New Line
                                    cmd = (byte) 0x0A;
                                    // Thread.sleep(1050);
                                    if (i == 1) {
                                        Thread.sleep(700);
                                    }
                                    mChatService.write(cmd);
                                } else if (matcher.group(1).equals("0x05")) {
                                    // Marati Normal
                                    cmd = (byte) 0x05;
                                    cmdforcharge = (byte) 0x05;
                                    if (i == 1) {
                                        Thread.sleep(700);
                                    }
                                    // Thread.sleep(1050);

                                    mChatService.write(cmd);
                                    // Thread.sleep(1050);

                                } else if (matcher.group(1).equals("0x0E")) {
                                    // New Line
                                    cmd = (byte) 0x0E;
                                    if (i == 1) {
                                        Thread.sleep(700);
                                    }
                                    // Thread.sleep(1050);

                                    mChatService.write(cmd);
                                    // Thread.sleep(1050);
                                } else if (matcher.group(1).equals("0x80")) {
                                    // Graphics
                                    cmd = (byte) 0x80;
                                    // Log.e( "simpleimage", "<0x80>" );
                                    // Thread.sleep(1050);
                                    if (i == 1) {
                                        Thread.sleep(1050);
                                    }
                                    mChatService.write(cmd);
                                } else if (matcher.group(1).equals("0x82")) {
                                    // New Line
                                    cmd = (byte) 0x82;

                                    byte[] m = new byte[2];
                                    m[0] = cmd;
                                    m[1] = cmd;
                                    if (i == 1) {
                                        Thread.sleep(1050);
                                    }
                                    System.out.println("if cmd == 82 ");

                                    mChatService.write(cmd);
                                }

                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                           /* if ( i == 1 )
                            {
                                Thread.sleep( 1050 );
                            }*/
                            // Thread.sleep(1050);
                            byte[] byteStr = strPrintArray.getBytes();

                            mChatService.write(byteStr);

                        }

                        // Thread.sleep(1050);
                        /*
                         * byte[] byteStr = strPrintArray.getBytes();
                         * mChatService.write(byteStr);
                         */

                    }
                    // mChatService.stop();
                    // Log.e("mChatService",
                    // "STOPPEDDDDDDDDDDDDDDDDDDDDDDDDDD");
                    // deviceDisConnect();
                }
                catch ( Exception e )
                {
                    // TODO: handle exception
                    e.printStackTrace();
                }
                try
                {
                    // Thread.sleep(1500);
                    // ( ( Activity ) BluetoothChat.this ).finish();

                    mSendButton.setText( "Reprint" );
                    ifbattery = false;


                }
                catch ( Exception e )
                {
                    // TODO: handle exception
                }

                /*
                 * try { Thread.sleep(1050); } catch (InterruptedException e) {
                 * // TODO Auto-generated catch block e.printStackTrace(); }
                 * byte[] byteStr = message.getBytes();
                 * mChatService.write(byteStr);
                 */
            }
        }
        // }
    }

    private final void setStatus( int resId )
    {
        /*final ActionBar actionBar = getSupportActionBar();
        actionBar.setSubtitle( resId );*/
    }

    private final void setStatus( CharSequence subTitle )
    {
        /*final ActionBar actionBar = getSupportActionBar();
        actionBar.setSubtitle( subTitle );*/
    }

    // The Handler that gets information back from the BluetoothChatService
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
                                                   setStatus( "Charge " + printcharge );
                                                   txtprintername.setText( "Connected to " + mConnectedDeviceName );
                                                  // mSendButton.setVisibility( View.VISIBLE );
                                                   btnLayout.setVisibility( View.VISIBLE );
                                                   btnLayoutScan.setVisibility( View.GONE );
                                                   break;
                                               case BluetoothChatService.STATE_CONNECTING:
                                                  // setStatus( R.string.title_connecting );
                                                   txtprintername.setText( "Connecting... " );
                                                  // mSendButton.setVisibility( View.INVISIBLE );
                                                   btnLayout.setVisibility( View.GONE );
                                                   btnLayout.setVisibility( View.GONE );
                                                   break;
                                               case BluetoothChatService.STATE_LISTEN:
                                               case BluetoothChatService.STATE_NONE:
                                                   //PrintUtil.setSelectedPrinterType("");
                                                 //  setStatus( R.string.title_not_connected );
                                                   txtprintername.setText( "Not Connected... " );
                                                  // mSendButton.setVisibility( View.VISIBLE );
                                                   btnLayout.setVisibility( View.GONE );
                                                   btnLayoutScan.setVisibility( View.VISIBLE );
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
                                               byte[] readBuf = ( byte[] ) msg.obj;
                                               // construct a string from the
                                               // valid bytes in the buffer
                                               String readMessage = new String( readBuf, 0, msg.arg1 );
                                             /*  mConversationArrayAdapter.add( mConnectedDeviceName + ":  "
                                                       + readMessage );*/
                                               Log.e( "", "READ MESSEGE OUT :" + readMessage );
                                               System.out.println("dcd myread "+readMessage);

                                               if ( readMessage.length() >= 5 )
                                               {
                                                   
                                                   //ifbattery = true;
                                                   readMessage = readMessage.trim().replace( "BL=", "" );
                                                   int charge=0;
                                                   Log.e( "", "READ MESSEGE :" + readMessage );
                                                  /* try{
                                                       charge=Integer.parseInt( readMessage );
                                                   }catch (Exception e) {
                                                    // TODO: handle exception
                                                }*/
                                                  
                                                   if( readMessage.equals( "0" ) )
                                                   {
                                                       txtprintername.setText( "No Charge in Printer " );
                                                     
                                                   }else {
                                                   
                                                       ifbattery = true;
                                                       System.out.println("dcd receivedmsg");
                                                       BluetoothChat.this.sendMessageTahi1( printmsg );

                                                   }
                                                   setStatus("Charge " + readMessage );
                                                   //SharedPreferences.Editor editor = my_Preferences.edit();
                                                  // editor.putString( "Variables.PRINTERCHARGE", readMessage );
                                                   //editor.commit();
                                                   
                                                 //  ifbattery = true;
                                                  // BluetoothChat.this.sendMessageTahi( receivedmsg );
                                              
                                                /*   readMessage = readMessage.trim().replace( "BL=", "" );
                                                   printcharge = readMessage;
                                               //    Log.e( "", "READ MESSEGE :" + readMessage );

                                                   SharedPreferences.Editor editor = my_Preferences.edit();
                                                   editor.putString( Variables.PRINTERCHARGE, readMessage );
                                                   editor.commit();

                                                   if (  readMessage.contains( "0" ) )
                                                   {
                                                       txtprintername.setText( "No Charge in Printer " );
                                                     
                                                   }else {
                                                   
                                                       ifbattery = true;

                                                       BluetoothChat.this.sendMessageTahi( receivedmsg );

                                                   }*/
                                               }
                                               
                                              
                                               // mSendButton.setEnabled( false
                                               // );
                                               break;
                                           case MESSAGE_DEVICE_NAME:
                                               // save the connected device's
                                               // name
                                               mConnectedDeviceName = msg.getData().getString( DEVICE_NAME );
                                               txtprintername.setText( "Connected to " + mConnectedDeviceName );
                                               Toast.makeText( getApplicationContext(),
                                                       "Connected to " + mConnectedDeviceName, Toast.LENGTH_SHORT )
                                                       .show();
                                               btnLayout.setVisibility( View.VISIBLE );
                                               btnLayoutScan.setVisibility( View.GONE );
                                               break;
                                           case MESSAGE_TOAST:
                                               Toast.makeText( getApplicationContext(),
                                                       msg.getData().getString( TOAST ), Toast.LENGTH_SHORT ).show();
                                               break;
                                           }
                                       }
                                   };

    private void checkbattery()
    {
        byte[] m = new byte[ 3 ];

        m[ 0 ] = ( byte ) 0x1c;
        m[ 1 ] = ( byte ) 0x62;
        m[ 2 ] = ( byte ) 0x00;

        mChatService.write( m );

    }
    
    public static int hex2decimal(String s) {
        String digits = "0123456789ABCDEF";
        s = s.toUpperCase();
        int val = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int d = digits.indexOf(c);
            val = 16*val + d;
        }
        return val;
    }
    
    

    public void onActivityResult( int requestCode, int resultCode, Intent data ) {
        super.onActivityResult(requestCode, resultCode, data);
        if (D)
            Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    connectDevice(address);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupChat();
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(this, "Bluetooth not enabled", Toast.LENGTH_SHORT).show();
                    // finish();
                }
        }
    }

    @Override
    public void onBackPressed()
    {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BluetoothChat.this);
        alertDialogBuilder.setMessage("Do You Want To Exit?");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        finish();
                    }
                });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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
    public boolean onCreateOptionsMenu( Menu menu )
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.menu_main, (Menu) menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
        Intent serverIntent = null;
        switch ( item.getItemId() )
        {
        case R.id.action_settings:
            // Launch the DeviceListActivity to see devices and do scan
            serverIntent = new Intent( this, DeviceListActivity.class );
            startActivityForResult( serverIntent, REQUEST_CONNECT_DEVICE );
            return true;
        case android.R.id.home:

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BluetoothChat.this);
            alertDialogBuilder.setMessage("Do You Want To Exit?");
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton("yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            finish();
                        }
                    });

            alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
        return false;
    }

    public void deviceDisConnect()
    {
        // TODO Auto-generated method stub
        mChatService.stop();

        BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
        moveTaskToBack( true );
    }


}
