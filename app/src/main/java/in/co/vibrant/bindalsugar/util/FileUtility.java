package in.co.vibrant.bindalsugar.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import com.analogics.impactprinter.AnalogicsImpactPrinter;

import java.text.DecimalFormat;

import in.co.vibrant.bindalsugar.R;


public class FileUtility {

    Context context;
    public void PrintAtBTPrinter(Context context,String PrintValue,String Btadd)
    {
        this.context=context;
        new PrintJob().execute(PrintValue,Btadd);
    }

    private class PrintJob extends AsyncTask<String, Void, Void> {
        String message,PrintValue,Btadd;
        private ProgressDialog dialog = new ProgressDialog(context);
        boolean printStatus=false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitle("Printing...");
            dialog.setIndeterminate(false);
            dialog.setMessage("initializing bluetooth printer...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setProgress(0);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                PrintValue=params[0];
                Btadd=params[1];
                AnalogicsImpactPrinter conn = new AnalogicsImpactPrinter();
                try {
                    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (mBluetoothAdapter == null) {
                        new AlertDialogManager().showToast((Activity) context,"Device does not support bluetooth");
                        printStatus=true;
                    } else if (!mBluetoothAdapter.isEnabled()) {
                        // Bluetooth is not enabled :)
                        mBluetoothAdapter.enable();
                        dialog.setMessage("Enabling bluetooth");
                        Thread.sleep(2000);
                    }
                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (mBluetoothAdapter.isEnabled())
                    {
                        if (conn.isConnected() == false)
                        {
                            dialog.setMessage("Connecting printer");
                            conn.openBT(Btadd);
                        }
                        else{
                            dialog.setMessage("Printer connected");
                        }
                        if (conn.isConnected() == true)
                        {
                            dialog.setMessage("Printer connected");
                            Thread.sleep(500);
                            conn.printData(PrintValue + "\n");
                            dialog.setMessage("Printing job");
                            Thread.sleep(3000);
                            conn.closeBT();
                            printStatus=true;
                        }
                    }

                }
                catch (Exception ex) {
                    dialog.dismiss();
                }
            } catch (SecurityException e) {
                Log.e("Exception", e.getMessage());
                message = e.getMessage();
                dialog.dismiss();
            }
            catch (Exception e) {
                Log.e("Exception", e.getMessage());
                message = e.getMessage();
                dialog.dismiss();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(dialog.isShowing())
                dialog.dismiss();
            try {
                if(!printStatus)
                {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                            context);
                    alertDialog.setTitle(context.getString(R.string.app_name));
                    alertDialog.setMessage("Sorry printer not connected please enable bluetooth and check printer switch on then click on retry button");
                    alertDialog.setPositiveButton("Retry Printing",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    PrintAtBTPrinter(context,PrintValue,Btadd);
                                }
                            });
                    alertDialog.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                }
            }
            catch (Exception e)
            {
                new AlertDialogManager().RedDialog(context,"Sorry we could not connect printer to mobile please contact admin");
            }
        }
    }

    public int getModValue(String data,int modValue) throws Exception
    {
        String tareValue=data;
        String[] arr=tareValue.split("\\.");
        int modWt=0;
        if(arr.length>1)
        {
            if(arr[1].length()==2)
                modWt=Integer.parseInt(arr[0]+arr[1]);
            else
                modWt=Integer.parseInt(arr[0]+arr[1]+"0");
        }
        else
        {
            modWt=Integer.parseInt(arr[0]+"00");
        }
        int modWeight= modWt%modValue;
        return modWeight;
    }

    public String getAmountValue(String amount) throws Exception
    {
        String tareValue=new DecimalFormat("0.0000").format(Double.parseDouble(amount));;
        String[] arr=tareValue.split("\\.");
        String decimalPart="";
        String retAmount="";
        if(arr.length>1)
        {
            if(arr[1].length()>2) {
                if(arr[1].length()>7)
                {
                    if(arr[1].endsWith("99999"))
                    {
                        long temp1=Long.parseLong(arr[1])+1;
                        arr[1]=""+temp1;
                    }
                }
                decimalPart=arr[1].substring(0,3);
                int lastValue=Integer.parseInt(decimalPart.substring(2));
                if(lastValue>=5)
                {
                    if(decimalPart.startsWith("0"))
                    {
                        if(decimalPart.startsWith("09"))
                        {
                            retAmount = arr[0] + ".10";
                        }
                        else{
                            retAmount = arr[0] + ".0" + (Integer.parseInt(arr[1].substring(0,2))+1);
                        }

                    }
                    else
                    {
                        retAmount = arr[0] + "." + (Integer.parseInt(arr[1].substring(0,2))+1);
                    }
                }
                else
                {
                    retAmount = arr[0] + "." + arr[1].substring(0,2);
                }
            }
            else if(arr[1].length()==2)
                retAmount=arr[0]+"."+arr[1];
            else
                retAmount=arr[0]+"."+arr[1]+"0";
        }
        else
        {
            retAmount=arr[0]+".00";
        }
        if(retAmount.endsWith(".100"))
        {
            String[] finalArr=retAmount.split("\\.");
            retAmount=(Integer.parseInt(finalArr[0])+1)+".00";
        }
        //int modWeight= modWt%modValue;
        return ""+retAmount;
    }

}