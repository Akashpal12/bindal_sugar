package in.co.vibrant.bindalsugar.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;

import java.util.ArrayList;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.view.supervisor.StaffMainActivity;
import in.co.vibrant.bindalsugar.view.grower.GrowerMainActivity;
import in.co.vibrant.bindalsugar.view.admin.AdminMainActivity;


public class Login extends AppCompatActivity {

    EditText Username,Password;
    SQLiteDatabase db;
    Context context;
    Spinner login_factory;
    TextView hinttxt1,hinttxt2;
    FusedLocationProviderClient fusedLocationClient;
    boolean inside=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        context=Login.this;

        login_factory=findViewById(R.id.login_factory);
        hinttxt1=findViewById(R.id.username_hint);
        hinttxt2=findViewById(R.id.login_password_hint);
        Username=findViewById(R.id.login_user_name);
        Password=findViewById(R.id.login_password);
        //Username.setText("G001");
        //Password.setText("1234");
        ArrayList<String> data=new ArrayList<String>();
        data.add(getString(R.string.LBL_USER_TYPE));
        data.add(getString(R.string.LBL_GROWER));
        data.add(getString(R.string.LBL_STAFF));
        data.add(getString(R.string.LBL_ADMIN));
        ArrayAdapter<String> adaptersupply = new ArrayAdapter<String>(context,
                R.layout.list_item, data);
        login_factory.setAdapter(adaptersupply);
        login_factory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(login_factory.getSelectedItemPosition()==1)
                {
                    Username.setHint(getString(R.string.LBL_VILLAGE_CODE));
                    Password.setHint(getString(R.string.LBL_GROWER_CODE));
                    Username.setInputType(InputType.TYPE_CLASS_NUMBER);
                    Password.setInputType(InputType.TYPE_CLASS_NUMBER);
                    Username.setText("");
                    Password.setText("");
                    hinttxt1.setText("");
                    hinttxt2.setText("");
                }
                else if(login_factory.getSelectedItemPosition()==2)
                {
                    Username.setHint(getString(R.string.LBL_USER_NAME));
                    Password.setHint(getString(R.string.LBL_PASSWORD));
                    Username.setInputType(InputType.TYPE_CLASS_TEXT);
                    Password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    Username.setText("");
                    Password.setText("");
                    hinttxt1.setText("");
                    hinttxt2.setText("");
                }
                else if(login_factory.getSelectedItemPosition()==3)
                {
                    Username.setHint(getString(R.string.LBL_USER_NAME));
                    Password.setHint(getString(R.string.LBL_PASSWORD));
                    Username.setInputType(InputType.TYPE_CLASS_TEXT);
                    Password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    Username.setText("");
                    Password.setText("");
                    hinttxt1.setText("");
                    hinttxt2.setText("");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(login_factory.getSelectedItemPosition()==1)
                {
                    if(Username.getText().toString().equalsIgnoreCase("10101"))
                    {
                        hinttxt1.setText("Dhanoura");
                    }
                    else {
                        hinttxt1.setText("Invalid village code");
                    }
                }
                else
                {
                    hinttxt1.setText("");
                }
            }
        });
        Password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(login_factory.getSelectedItemPosition()==1)
                {
                    if(Password.getText().toString().equalsIgnoreCase("101"))
                    {
                        hinttxt2.setText("Prabhujot Singh");
                    }
                    else {
                        hinttxt2.setText("Invalid grower code");
                    }
                }
                else
                {
                    hinttxt2.setText("");
                }
            }
        });
    }



    public void login(View v)
    {
        if(login_factory.getSelectedItemPosition()==0)
        {
            new AlertDialogManager().RedDialog(context,getString(R.string.MSG_PLEASE_ENTER)+" "+getString(R.string.LBL_USER_TYPE));
        }
        else if(Username.getText().toString().length()==0)
        {
            new AlertDialogManager().RedDialog(context,getString(R.string.MSG_PLEASE_ENTER)+" "+getString(R.string.LBL_USER_NAME));
        }
        else if(Password.getText().toString().length()==0)
        {
            new AlertDialogManager().RedDialog(context,getString(R.string.MSG_PLEASE_ENTER)+" "+getString(R.string.LBL_PASSWORD));
        }
        else
        {
            if(login_factory.getSelectedItemPosition()==1)
            {
                if(Username.getText().toString().equalsIgnoreCase("10101") && Password.getText().toString().equalsIgnoreCase("101"))
                {
                    Intent intent=new Intent(Login.this, GrowerMainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    new AlertDialogManager().RedDialog(context,"Invalid username or password");
                }
            }
            else if(login_factory.getSelectedItemPosition()==2)
            {
                if(Username.getText().toString().equals("EMP0001") && Password.getText().toString().equalsIgnoreCase("123"))
                {
                    Intent intent=new Intent(Login.this, StaffMainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    new AlertDialogManager().RedDialog(context,"Invalid username or password");
                }
            }
            else if(login_factory.getSelectedItemPosition()==3)
            {
                if(Username.getText().toString().equals("ADMIN") && Password.getText().toString().equalsIgnoreCase("123"))
                {
                    Intent intent=new Intent(Login.this, AdminMainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    new AlertDialogManager().RedDialog(context,"Invalid username or password");
                }
            }
            else
            {
                new AlertDialogManager().RedDialog(context,"Invalid username or password");
            }

        }

    }

    private void AlertPopUp(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                Login.this);
        alertDialog.setTitle(getString(R.string.app_name));
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton(getString(R.string.BTN_OK),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

}
