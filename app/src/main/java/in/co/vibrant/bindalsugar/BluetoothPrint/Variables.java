package in.co.vibrant.bindalsugar.BluetoothPrint;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by VISHNU on 01-06-2016.
 * constant values required in services and APK build
 */
public class Variables {

    //    enum projectCodeEnum
//    {
//        RMS,
//        RMSDEV,
//        RMSBETA,
//        RMSQA,
//        RMSQALIVE,
//        RMSDEMO,
//        QARMSSWITCH;
//    }
    @interface projectCode
    {
        int RMS=0;
        int RMSDEV=1;
        int RMSBETA=2;
        int RMSQA=3;
        int RMSQALIVE=4;
        int RMSDEMO=5;
        int QARMSSWITCH=6;
    }
    //rms,rmsbeta/rmsqalive
    public static String ProjectCode                    = new String[]{"RMS","RMSDEV","RMSBETA","RMSQA","RMSQALIVE","RMSDEMO",
            "QARMSSWITCH"}[projectCode.RMSDEV];


    public static String ADDRESS                    = "/";
    public static String OperatorRegistration                       = ADDRESS + "OperatorRegistration?composite=";
    public static String GetAllCategorySubCategoryAndStocks         = ADDRESS + "GetAllCategorySubCategoryAndStocks?composite=";
    public static String GetLocationsAndCustomers                   = ADDRESS + "GetLocationsAndCustomers?composite=";
    public static String GetPaymentModes                            = ADDRESS + "GetPaymentModes";
    public static String GetAllCountryAndState                      = ADDRESS + "GetAllCountryAndState";
    public static String GetDeviceSettings                          = ADDRESS + "GetDeviceSettings?composite=";
    public static String GetVolumetricDetails                       = ADDRESS + "GetVolumetricDetails?Composite=";
    public static String GetAllCreditors                            = ADDRESS + "GetAllCreditors?Composite=";
    public static String DeviceMapping                              = ADDRESS + "DeviceMapping?Composite=";
    public static String OperatorLogOut                             = ADDRESS + "OperatorLogOut?composite=";
    public static String GetOrdersByBranch                          = ADDRESS + "GetOrdersByBranch?composite=";
    public static String APKUpdateCheck                             = ADDRESS + "APKUpdateCheck?composite=";
    public static String CheckDeviceRegistration                    = ADDRESS + "CheckDeviceRegistration?composite=";
    public static String GetServiceUrl                              = ADDRESS + "GetServiceUrl?composite=";
    public static String GetSecondaryServiceUrl                     = ADDRESS + "GetSecondaryServiceUrl?composite=";
    public static String GetCustomer                                = ADDRESS + "GetCustomer?composite=";
    public static String GetAllCheckList                                = ADDRESS + "GetAllCheckList?composite=";
    public static String GetLastUploadedTime                        = ADDRESS + "GetLastUploadedTime?composite=";


    public static String SaveOrderDetails                   = ADDRESS + "SaveOrderDetails";

    public static String GetRecentOrders                   = ADDRESS + "GetRecentOrders?composite=";
    public static String GetBillReprintDetails                   = ADDRESS + "GetBillReprintDetails?composite=";

    public static String SaveGPSCoordinates                 = ADDRESS + "SaveGPSCoordinates";
    public static String GetOrdersByOperator                = ADDRESS + "GetOrdersByOperator?composite=";
    public static String GiveInvoice                        = ADDRESS + "GiveInvoice";
    public static String SaveDepositDetails                 = ADDRESS + "SaveDepositDetails?composite=";
    public static String SaveExpenseDetails                 = ADDRESS + "SaveExpenseDetails?composite=";
    public static String SaveOtherExpenseDetails            = ADDRESS + "SaveOtherExpenseDetails?composite=";
    public static String CheckRequestApproval               = ADDRESS + "CheckRequestApproval?composite=";
    public static String SaveTestVehicleDetails             = ADDRESS + "SaveTestVehicleDetails";
    public static String DismissTestVehicleDetails          = ADDRESS + "DismissTestVehicleDetails?composite=";


    public static String Exitorder                          = ADDRESS + "ExitOrder";
    public static String Saveimage                          = ADDRESS + "SaveImageSharing";
    public static String ReplaceOrder                       = ADDRESS + "ReplaceOrder";
    public static String EditOrder                          = ADDRESS + "EditOrder";
    public static String SaveRequest                        = ADDRESS +"SaveRequest";
    public static String SubmitExit                         = ADDRESS + "SubmitExitOrder";
    public static String SaveCheckList                         = ADDRESS + "SaveCheckList";



    public static String IPAddress                      = "IP";
    public static String IPStatus                       = "IPStatus";
    public static String LoginStatus                    = "LoginStatus";
    public static String OrderNo                        = "OrderNo";
    public static String TransactionNo                  = "TransactioNo";
    public static String LastDateChange                 = "LastDateChange";
    public static String LastDateLogo                   = "LastDateLogo";
    public static String TestNo                         = "TestNo";
    public static String IsTestVehicle                  = "IsTestVehicle";

    public static String BluetoothDevice                = "BluetoothDevice";
    public static String PrintString                    = "PrintString";

    public static String DayFlag                        = "DayFlag";
    public static String CaptureImage                   = "CaptureImage";
    public static String OrderNoFormat                  = "OrderNoFormat";
    public static String TestNoFormat                   = "TestNoFormat";
    public static String ItemID                         = "ItemID";
    public static String PrinterModel                   = "PrinterModel";
    public static String UploadStatus                   = "UploadStatus";
    public static String TestVehicle                    = "TestVehicle";
    public static String TestMode                       = "TestMode";
    public static String RentVehicle                    = "RentVehicle";
    public static String LastUploadedTime               = "LastUploadedTime";
    public static String DateTimeFormat                 = "DateTimeFormat";
    public static String DateFormat                     = "DateFormat";


    public static int SUCCESS                           = 0;
    public static int NETWORK_ERROR                     = -1;
    public static int SERVICE_ERROR                     = 1;

    public static int ORDERCLICKFLAG                    = 1;  // deactivates order click when synch is in progress

    public static int PRIMARYSERVER                     = 0;
    public static int SECONDARYSERVER                   = -1;


    public static String START_ORDER_SERVICE            = "com.softland.bms_byky.START_ORDER_SERVICE";
    public static String START_RECIEVE_SERVICE          = "com.softland.bms_byky.START_RECIEVE_SERVICE";
    public static String START_REPLACE_SERVICE          = "com.softland.bms_byky.START_REPLACE_SERVICE";
    public static String START_DIRECT_RENT_SERIVCE      = "com.softland.bms_byky.START_DIRECT_RENT_SERIVCE";
    public static String START_REQUEST_SERVICE          = "com.softland.bms_byky.START_REQUEST_SERVICE";
    public static String START_APPROVAL_SERVICE         = "com.softland.bms_byky.START_APPROVAL_SERVICE";
    public static String START_SUBMIT_SERVICE           = "com.softland.bms_byky.START_SUBMIT_SERVICE";
    public static String START_IMAGESHARE_SERVICE       = "com.softland.bms_byky.START_IMAGESHARE_SERVICE";
    public static String START_TEST_SERVICE             = "com.softland.bms_byky.START_TEST_SERVICE";
    public static String START_DISMISS_TEST_SERVICE     = "com.softland.bms_byky.START_DISMISS_TEST_SERVICE";
    public static String START_SERVERSWITCH_SERVICE     = "com.softland.bms_byky.START_SERVERSWITCH_SERVICE";


    public static String DAY_CHANGE_ACTION              = "com.softland.bms_byky.DAY_CHANGE_ACTION";

    public static String START_DAY_SCHEDULE_SERVICE     = "com.softland.paymentcollection.START_DAY_SCHEDULE_SERVICE";
    public static String START_SHOPINOUT_SERVICE        = "com.softland.paymentcollection.START_SHOPINOUT_SERVICE";
    public static String START_DEPOSIT_SERVICE          = "com.softland.paymentcollection.START_DEPOSIT_SERVICE";
    public static String START_EXPENSE_SERVICE          = "com.softland.paymentcollection.START_EXPENSE_SERVICE";

    // public static String URL_HTTP="http://";
    public static String URL_HTTP="http://";

    /**
     * saves data to shared preference
     * @param context
     * @param prefName
     * @param value
     */
    public static void setPreference(Context context, String prefName, Object value){

        SharedPreferences preferences = context.getSharedPreferences("Preference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        if (value instanceof Integer){
            editor.putInt(prefName,((Integer) value).intValue());
        }
        else if (value instanceof String) {
            editor.putString(prefName, value.toString());
        }
        else if (value instanceof Boolean) {
            editor.putBoolean(prefName, ((Boolean) value).booleanValue());
        }
        editor.commit();
    }

    /**
     * retrieves  data from shared preference of string
     * @param context
     * @param prefName
     * @return
     */
    public static String getPreferenceString(Context context, String prefName){

        SharedPreferences preferences = context.getSharedPreferences("Preference", Context.MODE_PRIVATE);
        return preferences.getString( prefName, "");
    }

    /**
     *  retrieves  data from shared preference of int
     * @param context
     * @param prefName
     * @return
     */
    public static int getPreferenceInt(Context context, String prefName){

        SharedPreferences preferences = context.getSharedPreferences("Preference", Context.MODE_PRIVATE);
        return preferences.getInt( prefName, 0);
    }

    /**
     * retrieves  data from shared preference of boolean
     * @param context
     * @param prefName
     * @return
     */
    public static Boolean getPreferenceBoolean(Context context, String prefName){

        SharedPreferences preferences = context.getSharedPreferences("Preference", Context.MODE_PRIVATE);
        return preferences.getBoolean( prefName, false);
    }
}
