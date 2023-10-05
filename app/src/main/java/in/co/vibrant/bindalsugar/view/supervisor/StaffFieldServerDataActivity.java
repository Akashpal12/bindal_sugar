package in.co.vibrant.bindalsugar.view.supervisor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;

public class StaffFieldServerDataActivity extends AppCompatActivity {

    DBHelper dbh;
    SQLiteDatabase db;
    Button btn, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9;
    AlertDialog dialogPopup;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.server_menu_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.MENU_Server_DATA));
        toolbar.setTitle(getString(R.string.MENU_Server_DATA));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        context = StaffFieldServerDataActivity.this;


        try {
            dbh = new DBHelper(this);
            db = new DBHelper(this).getWritableDatabase();
            btn = findViewById(R.id.btn);
            btn.setText("PENDING INDENTING ON SERVER\n" + dbh.getIndentModel("No", "", "", "", "").size());
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, StaffServerPendingIndentReport.class);
                    startActivity(intent);
                    //ShowMaterialList();
                }
            });
            btn1 = findViewById(R.id.btn1);
            btn1.setText("FAILED INDENTING ON SERVER\n" + dbh.getIndentModel("Failed", "", "", "", "").size());
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, StaffServerFailedIndentReport.class);
                    startActivity(intent);
                    //ShowMaterialList();
                }
            });
            btn2 = findViewById(R.id.btn2);
            btn2.setText("UPLOADED INDENTING ON SERVER\n" + dbh.getIndentModel("Done", "", "", "", "").size());
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, StaffServerUploadedIndentReport.class);
                    startActivity(intent);
                    //ShowMaterialList();
                }
            });
        /*btn3 = findViewById(R.id.btn3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    new GenerateLogFile(context).generateIndentData();
                }
                catch (Exception e)
                {
                    AlertPopUp("Error : "+e.toString());
                }
                //ShowMaterialList();
            }
        });
        btn4 = findViewById(R.id.btn4);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
                    View mView = getLayoutInflater().inflate(R.layout.dialogue_verify_password, null);
                    dialogbilder.setView(mView);
                    final EditText password=mView.findViewById(R.id.password);

                    dialogPopup = dialogbilder.create();
                    dialogPopup.show();
                    dialogPopup.setCancelable(true);
                    dialogPopup.setCanceledOnTouchOutside(true);
                    Button btn_ok=mView.findViewById(R.id.btn_ok);
                    btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                if(password.getText().toString().length()==0)
                                {
                                    AlertPopUp("Please enter password");
                                }
                                else
                                {
                                    if(password.getText().toString().equalsIgnoreCase("1234321"))
                                    {
                                        new GenerateLogFile(context).importIndentData();
                                    }
                                    else
                                    {
                                        AlertPopUp("Please enter password valid password");
                                    }

                                }
                            }
                            catch(Exception e)
                            {

                            }
                            dialogPopup.dismiss();
                        }
                    });
                }
                catch(SecurityException e)
                {

                }
                //ShowMaterialList();
            }
        });*/

            btn6 = findViewById(R.id.btn6);
            btn6.setText("PENDING PLANTING ON SERVER\n" + dbh.getPlantingModel("No", "", "", "", "").size());
            btn6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent intent = new Intent(context, StaffServerPendingPlantingReport.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        AlertPopUp("Error : " + e.toString());
                    }
                    //ShowMaterialList();
                }
            });
            btn7 = findViewById(R.id.btn7);
            btn7.setText("FAILED PLANTING ON SERVER\n" + dbh.getPlantingModel("Failed", "", "", "", "").size());
            btn7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent intent = new Intent(context, StaffServerFailedPlantingReport.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        AlertPopUp("Error : " + e.toString());
                    }
                    //ShowMaterialList();
                }
            });
            btn8 = findViewById(R.id.btn8);
            btn8.setText("UPLOADED PLANTING ON SERVER\n" + dbh.getPlantingModel("DONE", "", "", "", "").size());
            btn8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent intent = new Intent(context, StaffServerUploadedPlantingReport.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        AlertPopUp("Error : " + e.toString());
                    }
                    //ShowMaterialList();
                }
            });

            btn5 = findViewById(R.id.btn5);
            btn5.setVisibility(View.GONE);
            btn5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent intent = new Intent(context, StaffServerUploadedPlantingReport.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        AlertPopUp("Error : " + e.toString());
                    }
                    //ShowMaterialList();
                }
            });
            btn9 = findViewById(R.id.btn9);
            btn9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        //new downloadIndenting().execute();
                        Intent intent = new Intent(context, StaffCaneReportServerDataListActivity.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        AlertPopUp("Error : " + e.toString());
                    }
                    //ShowMaterialList();
                }
            });

        } catch (Exception e) {
            AlertPopUp("Error:" + e.toString());
        }


    }


    private void AlertPopUp(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                context);
        alertDialog.setTitle(getString(R.string.app_name));
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }
}
