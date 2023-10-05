package in.co.vibrant.bindalsugar.view.supervisor;

import static com.github.vipulasri.timelineview.TimelineView.TAG;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.PlantingModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.DownloadMasterData;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;

public class CaneDownloadMaster extends AppCompatActivity {
    DBHelper dbh;
    SQLiteDatabase db;
    List<UserDetailsModel> userDetailsModels;
    String VillageCode="";
    RelativeLayout download_master_data_rl,download_planting_data_rl;

    androidx.appcompat.app.AlertDialog dialog;
    TextView download_cane_masterdata,download_planting_data;
    int tempInc=0;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cane_download_master);
        context= CaneDownloadMaster.this;
        dbh = new DBHelper(this);
        db = new DBHelper(this).getWritableDatabase();
        userDetailsModels=new ArrayList<>();
        userDetailsModels=dbh.getUserDetailsModel();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.MENU_IMPORT_MASTER_DATA));
        toolbar. setTitle(getString(R.string.MENU_IMPORT_MASTER_DATA));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        download_master_data_rl= findViewById(R.id.download_master_data_rl);
        download_planting_data_rl=findViewById(R.id.download_planting_data_rl);


        download_cane_masterdata=findViewById(R.id.download_cane_masterdata);
        download_planting_data=findViewById(R.id.download_planting_data);


        download_cane_masterdata.setText(dbh.getMasterDropDown("").size() + " Data");
        download_planting_data.setText(dbh.getPlantingModel("","","","","").size() + " Data");


        download_master_data_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    new DownloadMasterData().DownloadMaster(context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        download_planting_data_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    new DownloadMasterData().GetDownloadPlanting(context);
                    //new DownloadMasterData().downloadPlantingData(context,userDetailsModels.get(0).getDivision(), userDetailsModels.get(0).getCode());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


  /*public void OpenCaneMasterData(View v) throws Exception {
      downloadDropDownMasterDataCd(context,dbh,userDetailsModels.get(0).getDivision(),userDetailsModels.get(0).getCode());

    }*/





  /*public void downloadDropDownMasterDataCd(Context ctx,DBHelper dbhelper,String division,String code)
    {
        context=ctx;
        dbh=dbhelper;
        db = dbh.getWritableDatabase();
        new DownloadCaneMaster().execute(division,code);
    }*/
    public void downloadPlantingData(View v) throws Exception {
        new DownloadMasterData().GetDownloadPlanting(context);
        //new DownloadMasterData().downloadPlantingData(context, userDetailsModels.get(0).getDivision(), userDetailsModels.get(0).getCode());
    }
    /*public class DownloadCaneMaster extends AsyncTask<String, Integer, Void> {
        String Content = null;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
          *//*dialog = ProgressDialog.show(Login.this, getString(R.string.app_name),
                    "Please wait while we verify your details", true);*//*
            dialog.setTitle("Please wait ...");
            dialog.setIndeterminate(false);
            dialog.setMessage("Downloading data...");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();

        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                GetDeviceImei getDeviceImei=new GetDeviceImei(context);
                String url=APIUrl.BASE_URL_CANE_DEVELOPMENT+"/GETMASTER";
                //String url = "http://demo.asginnovations.in/teafarmservice.asmx/GETMASTER";
                //int i = Integer.parseInt(params[0]);
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                //MultipartEntity entity = new MultipartEntity();
                entity.add(new BasicNameValuePair("Factory",params[0]));
                entity.add(new BasicNameValuePair("USERID",params[1]));
                entity.add(new BasicNameValuePair("imei",getDeviceImei.GetDeviceImeiNumber()));
                String paramString = URLEncodedUtils.format(entity, "utf-8");
                HttpGet httpGet = new HttpGet(url+"?"+paramString);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                Content = httpClient.execute(httpGet, responseHandler);


                int totalData=0;
                JSONArray jsonArray=new JSONArray(Content);
                if(jsonArray.length()>0)
                {
                    db.execSQL("DROP TABLE IF EXISTS " + MasterDropDown.TABLE_NAME);
                    db.execSQL("DROP TABLE IF EXISTS " + VillageModal.TABLE_NAME);
                    db.execSQL("DROP TABLE IF EXISTS " + GrowerModel.TABLE_NAME);
                    db.execSQL("DROP TABLE IF EXISTS " + ControlModel.TABLE_NAME);
                    db.execSQL("DROP TABLE IF EXISTS " + MasterSubDropDown.TABLE_NAME);
                    db.execSQL(MasterDropDown.CREATE_TABLE);
                    db.execSQL(VillageModal.CREATE_TABLE);
                    db.execSQL(GrowerModel.CREATE_TABLE);
                    db.execSQL(ControlModel.CREATE_TABLE);
                    db.execSQL(MasterSubDropDown.CREATE_TABLE);
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    final JSONArray IRRIGATIONLIST=jsonObject.getJSONArray("IRRIGATIONLIST");
                    final JSONArray SUPPLYMODELIST=jsonObject.getJSONArray("SUPPLYMODELIST");
                    final JSONArray HARVESTINGLIST=jsonObject.getJSONArray("HARVESTINGLIST");
                    final JSONArray EQUIMENTLIST=jsonObject.getJSONArray("EQUIMENTLIST");
                    final JSONArray LANDTYPELIST=jsonObject.getJSONArray("LANDTYPELIST");
                    final JSONArray SEEDTYPELIST=jsonObject.getJSONArray("SEEDTYPELIST");
                    final JSONArray BASALDOSELIST=jsonObject.getJSONArray("BASALDOSELIST");
                    final JSONArray SEEDTREATMENTLIST=jsonObject.getJSONArray("SEEDTREATMENTLIST");
                    final JSONArray METHODLIST=jsonObject.getJSONArray("METHODLIST");
                    final JSONArray SPRAYITEMLIST=jsonObject.getJSONArray("SPRAYITEMLIST");
                    final JSONArray SPRAYTYPELIST=jsonObject.getJSONArray("SPRAYTYPELIST");
                    final JSONArray PLOUGHINGTYPELIST=jsonObject.getJSONArray("PLOUGHINGTYPELIST");
                    final JSONArray VARIETYLIST=jsonObject.getJSONArray("VARIETYLIST");
                    final JSONArray PLANTINGTYPELIST=jsonObject.getJSONArray("PLANTINGTYPELIST");
                    final JSONArray PLANTATIONLIST=jsonObject.getJSONArray("PLANTATIONLIST");
                    final JSONArray CROPLIST=jsonObject.getJSONArray("CROPLIST");
                    final JSONArray GROWERDATALIST=jsonObject.getJSONArray("GROWERDATALIST");
                    final JSONArray VILLAGEDATALIST=jsonObject.getJSONArray("VILLAGEDATALIST");
                    final JSONArray FILDTRUEFALSELIST=jsonObject.getJSONArray("FILDTRUEFALSELIST");
                    final JSONArray SEEDTTYPELIST=jsonObject.getJSONArray("SEEDTTYPELIST");
                    final JSONArray SEEDSETTYPELIST=jsonObject.getJSONArray("SEEDSETTYPELIST");
                    final JSONArray SOILTREATMENTLIST=jsonObject.getJSONArray("SOILTREATMENTLIST");
                    final JSONArray FILDPREPRATIONLIST=jsonObject.getJSONArray("FILDPREPRATIONLIST");
                    final JSONArray ROWTOROWLIST=jsonObject.getJSONArray("ROWTOROWLIST");
                    final JSONArray TARGETTYPELIST=jsonObject.getJSONArray("TARGETTYPE");
                    final JSONArray PLOTACTIVITYMST=jsonObject.getJSONArray("PLOTACTIVITYMST");
                    final JSONArray PLOTACTIVITYMETHODMST=jsonObject.getJSONArray("PLOTACTIVITYMETHODMST");
                    final JSONArray SUPERVOISERLIST=jsonObject.getJSONArray("SUPERVOISER");

                    totalData +=IRRIGATIONLIST.length();
                    totalData +=SUPPLYMODELIST.length();
                    totalData +=HARVESTINGLIST.length();
                    totalData +=LANDTYPELIST.length();
                    totalData +=SEEDTYPELIST.length();
                    totalData +=BASALDOSELIST.length();
                    totalData +=SEEDTREATMENTLIST.length();
                    totalData +=METHODLIST.length();
                    totalData +=SPRAYITEMLIST.length();
                    totalData +=SPRAYTYPELIST.length();
                    totalData +=PLOUGHINGTYPELIST.length();
                    totalData +=VARIETYLIST.length();
                    totalData +=PLANTINGTYPELIST.length();
                    totalData +=PLANTATIONLIST.length();
                    totalData +=CROPLIST.length();
                    totalData +=GROWERDATALIST.length();
                    totalData +=VILLAGEDATALIST.length();
                    totalData +=FILDTRUEFALSELIST.length();
                    totalData +=SEEDTTYPELIST.length();
                    totalData +=SEEDSETTYPELIST.length();
                    totalData +=SOILTREATMENTLIST.length();
                    totalData +=FILDPREPRATIONLIST.length();
                    totalData +=ROWTOROWLIST.length();
                    totalData +=TARGETTYPELIST.length();
                    totalData +=PLOTACTIVITYMST.length();
                    totalData +=PLOTACTIVITYMETHODMST.length();
                    totalData +=SUPERVOISERLIST.length();


                   *//* 1-IRRIGATIONLIST
                    2-SUPPLYMODELIST
                    3-HARVESTINGLIST
                    4-EQUIMENTLIST
                    5-LANDTYPELIST
                    6-SEEDTYPELIST
                    7-BASALDOSELIST
                    8-METHODLIST
                    9-SPRAYITEMLIST
                    10-SPRAYTYPELIST
                    11-PLOUGHINGTYPELIST
                    12-VARIETYLIST
                    13-PLANTINGTYPELIST
                    14-PLANTATIONLIST
                    15-CROPLIST
                    16-SEEDTREATMENTLIST
                    17-SEEDTTYPELIST
                    18-SEEDSETTYPELIST
                    19-SOILTREATMENTLIST
                    20-FILDPREPRATIONLIST
                    21-ROWTOROWLIST
                    22-TARGETTYPELIST
                    23-SUPERVOISERLIST
                    24-PLOTACTIVITYMST*//*


                    dialog.setMax(totalData);
                    dialog.setProgress(0);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setCancelable(false);

                    if(IRRIGATIONLIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating irrigation data...");
                            }
                        });

                        for (int i=0;i<IRRIGATIONLIST.length();i++)
                        {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = IRRIGATIONLIST.getJSONObject(i);
                            MasterDropDown masterDropDown = new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("1");
                            dbh.insertMasterData(masterDropDown);

                        }
                    }

                    if(SUPPLYMODELIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating supply mode data...");

                            }
                        });

                        for (int i=0;i<SUPPLYMODELIST.length();i++)
                        {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object=SUPPLYMODELIST.getJSONObject(i);
                            MasterDropDown masterDropDown=new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("2");
                            dbh.insertMasterData(masterDropDown);
                        }


                    }

                    if(HARVESTINGLIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating harvesting data...");

                            }
                        });

                        for (int i=0;i<HARVESTINGLIST.length();i++)
                        {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = HARVESTINGLIST.getJSONObject(i);
                            MasterDropDown masterDropDown = new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("3");
                            dbh.insertMasterData(masterDropDown);
                        }
                    }

                    if(EQUIMENTLIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating equipment data...");

                            }
                        });

                        for (int i=0;i<EQUIMENTLIST.length();i++)
                        {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = EQUIMENTLIST.getJSONObject(i);
                            MasterDropDown masterDropDown = new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("4");
                            dbh.insertMasterData(masterDropDown);
                        }
                    }

                    if(LANDTYPELIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating land type data...");
                            }
                        });

                        for (int i=0;i<LANDTYPELIST.length();i++)
                        {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = LANDTYPELIST.getJSONObject(i);
                            MasterDropDown masterDropDown = new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("5");
                            dbh.insertMasterData(masterDropDown);
                        }
                    }

                    if(SEEDTYPELIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating seed type data...");
                            }
                        });

                        for (int i=0;i<SEEDTYPELIST.length();i++)
                        {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = SEEDTYPELIST.getJSONObject(i);
                            MasterDropDown masterDropDown = new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("6");
                            dbh.insertMasterData(masterDropDown);
                        }
                    }

                    if(BASALDOSELIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating basaldose data...");
                            }
                        });

                        for (int i=0;i<BASALDOSELIST.length();i++)
                        {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = BASALDOSELIST.getJSONObject(i);
                            MasterDropDown masterDropDown = new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("7");
                            dbh.insertMasterData(masterDropDown);
                        }
                    }

                    if(METHODLIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating method data...");
                            }
                        });

                        for (int i=0;i<METHODLIST.length();i++)
                        {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = METHODLIST.getJSONObject(i);
                            MasterDropDown masterDropDown = new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("8");
                            dbh.insertMasterData(masterDropDown);
                        }
                    }

                    if(SPRAYITEMLIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating spray item data...");
                            }
                        });

                        for (int i=0;i<SPRAYITEMLIST.length();i++)
                        {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = SPRAYITEMLIST.getJSONObject(i);
                            MasterDropDown masterDropDown = new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("9");
                            dbh.insertMasterData(masterDropDown);
                        }
                    }

                    if(SPRAYTYPELIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating spray type data...");
                            }
                        });

                        for (int i=0;i<SPRAYTYPELIST.length();i++)
                        {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = SPRAYTYPELIST.getJSONObject(i);
                            MasterDropDown masterDropDown = new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("10");
                            dbh.insertMasterData(masterDropDown);
                        }
                    }

                    if(PLOUGHINGTYPELIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating ploughing data...");
                            }
                        });

                        for (int i=0;i<PLOUGHINGTYPELIST.length();i++)
                        {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = PLOUGHINGTYPELIST.getJSONObject(i);
                            MasterDropDown masterDropDown = new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("11");
                            dbh.insertMasterData(masterDropDown);
                        }
                    }

                    if(VARIETYLIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating variety list data...");
                            }
                        });

                        for (int i=0;i<VARIETYLIST.length();i++)
                        {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = VARIETYLIST.getJSONObject(i);
                            MasterDropDown masterDropDown = new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("12");
                            dbh.insertMasterData(masterDropDown);
                        }
                    }

                    if(PLANTINGTYPELIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating planting type data...");
                            }
                        });

                        for (int i=0;i<PLANTINGTYPELIST.length();i++)
                        {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = PLANTINGTYPELIST.getJSONObject(i);
                            MasterDropDown masterDropDown = new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("13");
                            dbh.insertMasterData(masterDropDown);
                        }
                    }

                    if(PLANTATIONLIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating plantation data...");
                            }
                        });

                        for (int i=0;i<PLANTATIONLIST.length();i++)
                        {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = PLANTATIONLIST.getJSONObject(i);
                            MasterDropDown masterDropDown = new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("14");
                            dbh.insertMasterData(masterDropDown);
                        }
                    }

                    if(CROPLIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating crop list data...");
                            }
                        });

                        for (int i=0;i<CROPLIST.length();i++)
                        {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = CROPLIST.getJSONObject(i);
                            MasterDropDown masterDropDown = new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("15");
                            dbh.insertMasterData(masterDropDown);
                        }
                    }

                    if(VILLAGEDATALIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating village data...");
                            }
                        });

                        for (int i=0;i<VILLAGEDATALIST.length();i++)
                        {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = VILLAGEDATALIST.getJSONObject(i);
                            VillageModal villageModal = new VillageModal();
                            villageModal.setCode(object.getString("VCODE"));
                            villageModal.setName(object.getString("VNAME"));
                            villageModal.setTarget(object.getInt("TARGET"));//0 not set 1 set
                            villageModal.setMaxIndent(object.getString("VINDSERIALNO"));
                            villageModal.setMaxPlant(object.getString("VPLNSERIALNO"));
                            dbh.insertVillageModal(villageModal);
                        }
                    }

                    if(GROWERDATALIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating grower data...");
                            }
                        });

                        for (int i=0;i<GROWERDATALIST.length();i++) {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = GROWERDATALIST.getJSONObject(i);
                            GrowerModel growerModel = new GrowerModel();
                            growerModel.setVillageCode(object.getString("VCODE"));
                            growerModel.setGrowerCode(object.getString("GCODE"));
                            growerModel.setGrowerName(object.getString("GNAME"));
                            growerModel.setGrowerFather(object.getString("FATHER"));
                            dbh.insertGrowerModel(growerModel);
                        }
                    }

                    if(FILDTRUEFALSELIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating control data...");
                            }
                        });

                        for (int i=0;i<FILDTRUEFALSELIST.length();i++) {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = FILDTRUEFALSELIST.getJSONObject(i);
                            ControlModel controlSurveyModel = new ControlModel();
                            controlSurveyModel.setName(object.getString("Fild"));
                            controlSurveyModel.setValue(object.getString("Value"));
                            controlSurveyModel.setFormName(object.getString("FormName"));
                            dbh.insertControlModel(controlSurveyModel);
                        }
                    }

                    if(SEEDTREATMENTLIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating seed treatment data...");
                            }
                        });

                        for (int i=0;i<SEEDTREATMENTLIST.length();i++) {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = SEEDTREATMENTLIST.getJSONObject(i);
                            MasterDropDown masterDropDown = new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("16");
                            dbh.insertMasterData(masterDropDown);
                        }
                    }

                    if(SEEDTTYPELIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating seed type data...");
                            }
                        });

                        for (int i=0;i<SEEDTTYPELIST.length();i++) {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = SEEDTTYPELIST.getJSONObject(i);
                            MasterDropDown masterDropDown = new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("17");
                            dbh.insertMasterData(masterDropDown);
                        }
                    }

                    if(SEEDSETTYPELIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating seed set type data...");
                            }
                        });

                        for (int i=0;i<SEEDSETTYPELIST.length();i++) {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = SEEDSETTYPELIST.getJSONObject(i);
                            MasterDropDown masterDropDown = new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("18");
                            dbh.insertMasterData(masterDropDown);
                        }
                    }

                    if(SOILTREATMENTLIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating soil treatment data...");
                            }
                        });

                        for (int i=0;i<SOILTREATMENTLIST.length();i++) {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = SOILTREATMENTLIST.getJSONObject(i);
                            MasterDropDown masterDropDown = new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("19");
                            dbh.insertMasterData(masterDropDown);
                        }
                    }

                    if(FILDPREPRATIONLIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating field prepration data...");
                            }
                        });

                        for (int i=0;i<FILDPREPRATIONLIST.length();i++) {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = FILDPREPRATIONLIST.getJSONObject(i);
                            MasterDropDown masterDropDown = new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("20");
                            dbh.insertMasterData(masterDropDown);
                        }
                    }

                    if(ROWTOROWLIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating row to row data...");
                            }
                        });

                        for (int i=0;i<ROWTOROWLIST.length();i++) {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = ROWTOROWLIST.getJSONObject(i);
                            MasterDropDown masterDropDown = new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("21");
                            dbh.insertMasterData(masterDropDown);
                        }
                    }

                    if(TARGETTYPELIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating target data...");
                            }
                        });

                        for (int i=0;i<TARGETTYPELIST.length();i++) {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = TARGETTYPELIST.getJSONObject(i);
                            MasterDropDown masterDropDown = new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("22");
                            dbh.insertMasterData(masterDropDown);
                        }
                    }


                    if(SUPERVOISERLIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating supervisor data...");
                            }
                        });

                        for (int i=0;i<SUPERVOISERLIST.length();i++) {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = SUPERVOISERLIST.getJSONObject(i);
                            MasterDropDown masterDropDown = new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("23");
                            dbh.insertMasterData(masterDropDown);
                        }
                    }

                    if(TARGETTYPELIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating target data...");
                            }
                        });

                        for (int i=0;i<TARGETTYPELIST.length();i++) {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = TARGETTYPELIST.getJSONObject(i);
                            MasterDropDown masterDropDown = new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("24");
                            dbh.insertMasterData(masterDropDown);
                        }
                    }

                    if(PLOTACTIVITYMST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating plot activity data...");
                            }
                        });

                        for (int i=0;i<PLOTACTIVITYMST.length();i++) {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = PLOTACTIVITYMST.getJSONObject(i);
                            MasterSubDropDown masterDropDown = new MasterSubDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setMasterCode(object.getString("PRBCODE"));
                            masterDropDown.setType("25");
                            dbh.insertMasterSubDropDown(masterDropDown);
                        }
                    }

                    if(PLOTACTIVITYMETHODMST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating plot activity method data...");
                            }
                        });

                        for (int i=0;i<PLOTACTIVITYMETHODMST.length();i++) {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = PLOTACTIVITYMETHODMST.getJSONObject(i);
                            MasterSubDropDown masterDropDown = new MasterSubDropDown();
                            masterDropDown.setCode(object.getString("ATMCODE"));
                            masterDropDown.setName(object.getString("ATMNAME"));
                            masterDropDown.setMasterCode(object.getString("ATMACTCODE"));
                            masterDropDown.setExtraField(object.getInt("ITEMFLG"));
                            masterDropDown.setType("22");
                            dbh.insertMasterSubDropDown(masterDropDown);
                        }
                    }
                }

            } catch (SecurityException e) {
                Log.e("SecurityException", e.getMessage());
                new AlertDialogManager().RedDialog(context,"Error : "+e.toString());
                Content = e.getMessage();
            }catch (Exception e) {
                Log.e("Exception", e.getMessage());
                Content = e.getMessage();
                new AlertDialogManager().RedDialog(context,"Error : "+e.toString());
                //dbh.onUpgrade(db,1,1);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            dialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            try{
                dialog.dismiss();
                //download_dropdown_data.setText(dbh.getMasterDropDown("").size()+" Data");

               // new AlertDialogManager().GreenDialog(context,"Data successfully downloaded");
                download_cane_masterdata.setText(""+dbh.getMasterSubDropDown("","").size());
                //AlertPopUp(dbh.getMasterDropDown("").size() + " successfully imported");
                new UpdateMaster().execute();
            }
            catch (Exception e)
            {
                new AlertDialogManager().RedDialog(context,"Error : "+e.toString());
            }
        }
    }

    private class UpdateMaster extends AsyncTask<String, Void, Void> {
        String Content = null;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            *//*dialog = ProgressDialog.show(Login.this, getString(R.string.app_name),
                    "Please wait while we verify your details", true);*//*
            dialog.setTitle("Please wait ...");
            dialog.setIndeterminate(false);
            dialog.setMessage("Updating data...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();

        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                GetDeviceImei getDeviceImei=new GetDeviceImei(context);
                String url=APIUrl.BASE_URL_CANE_DEVELOPMENT+"/UPDATEMASTERDATETINE";
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                //MultipartEntity entity = new MultipartEntity();
                entity.add(new BasicNameValuePair("DIVN",userDetailsModels.get(0).getDivision()));
                entity.add(new BasicNameValuePair("USERCODE",userDetailsModels.get(0).getCode()));
                entity.add(new BasicNameValuePair("IMINO",getDeviceImei.GetDeviceImeiNumber()));
                String paramString = URLEncodedUtils.format(entity, "utf-8");
                HttpGet httpGet = new HttpGet(url+"?"+paramString);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                Content = httpClient.execute(httpGet, responseHandler);
            } catch (SecurityException e) {
                Log.e("SecurityException", e.getMessage());
                // new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
                Content = e.getMessage();
            }catch (Exception e) {
                Log.e("Exception", e.getMessage());
                Content = e.getMessage();
                // new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
                //dbh.onUpgrade(db,1,1);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub


            super.onPostExecute(result);
            Log.d(TAG, "onPostExecute: " + result);
            if (Content != null) {
                try{
                    Log.d(TAG, Content);
                    int update=Integer.parseInt(Content);
                    dbh.updateMasterDownload(""+update);
                    new AlertDialogManager().GreenDialog(context, dbh.getMasterDropDown("").size() + " successfully imported");
                   *//* int update=Integer.parseInt(Content);
                    dbh.updateMasterDownload("successfully imported"+update);*//*
                }
                catch (Exception e)
                {
                    new AlertDialogManager().GreenDialog(context, e.toString());
                }
                if (dialog.isShowing())
                    dialog.dismiss();
            } else {
                new AlertDialogManager().GreenDialog(context, context.getString(R.string.oops_connect_your_internet));
            }


        }
    }*/


    private class GetDownloadPlanting extends AsyncTask<String , Integer, Void> {
        String message;
        int tempData=0;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitle("Please wait ...");
            dialog.setIndeterminate(false);
            dialog.setMessage("Downloading data...");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                String imei=new GetDeviceImei((context)).GetDeviceImeiNumber();
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_DownloadPlanting);
                //request1.addProperty("EMEI", "864547042872734");
                //request1.addProperty("imeiNo", "355844090708282");
                request1.addProperty("DIVN", params[0]);
                request1.addProperty("MachineID", imei);
                request1.addProperty("OPR", params[1]);

                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL_CANE_DEVELOPMENT, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_DownloadPlanting, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("DownloadPlantingResult").toString();

                    try{
                        JSONObject jsonObject1=new JSONObject(message);
                        JSONArray jsonArray=jsonObject1.getJSONArray("PLANTINGDATA");
                        int totalData=jsonArray.length();
                        dialog.setMax(totalData);
                        //JSONArray jsonArrayVillage=jsonObject1.getJSONArray("VILLAGE");
                        if(jsonArray.length()>0)
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.setMessage("inserting planting data...");
                                }
                            });
                            dbh.truncatePlanting();
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                tempData++;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        publishProgress(tempData);
                                    }
                                });
                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                PlantingModel plantingModel=new PlantingModel();
                                plantingModel.setVillage(jsonObject.getString("PL_VILLAGE").replace(".0",""));
                                plantingModel.setGrower(jsonObject.getString("PL_GROW").replace(".0",""));
                                //plantingModel.set(jsonObject.getString("PL_GPLNO").replace(".0",""));
                                plantingModel.setPLOTVillage(jsonObject.getString("PL_PLVILL").replace(".0",""));
                                plantingModel.setPlotSerialNumber(jsonObject.getString("PL_VPLOTNO").replace(".0",""));
                                plantingModel.setLAT1(jsonObject.getString("PL_PLAT1"));
                                plantingModel.setLON1(jsonObject.getString("PL_PLON1"));
                                plantingModel.setLAT2(jsonObject.getString("PL_PLAT2"));
                                plantingModel.setLON2(jsonObject.getString("PL_PLON2"));
                                plantingModel.setLAT3(jsonObject.getString("PL_PLAT3"));
                                plantingModel.setLON3(jsonObject.getString("PL_PLON3"));
                                plantingModel.setLAT4(jsonObject.getString("PL_PLAT4"));
                                plantingModel.setLON4(jsonObject.getString("PL_PLON4"));
                                plantingModel.setDim1(jsonObject.getString("PL_DIM_1"));
                                plantingModel.setDim2(jsonObject.getString("PL_DIM_2"));
                                plantingModel.setDim3(jsonObject.getString("PL_DIM_3"));
                                plantingModel.setDim4(jsonObject.getString("PL_DIM_4"));
                                plantingModel.setArea(jsonObject.getString("PL_AREA"));
                                plantingModel.setSeedSource(jsonObject.getString("PL_SEEDSOURCE").replace(".0",""));
                                plantingModel.setMethod(jsonObject.getString("PL_SMETHOD").replace(".0",""));
                                plantingModel.setSmMethod(jsonObject.getString("PL_SMETHOD").replace(".0",""));
                                plantingModel.setCurrentDate(jsonObject.getString("PL_PDATE").replace("T"," "));
                                plantingModel.setSuperviserCode(jsonObject.getString("PL_PLSUP").replace(".0",""));
                                plantingModel.setManualArea(jsonObject.getString("PL_PMAREA"));
                                plantingModel.setMobileNumber(jsonObject.getString("PL_MOBNO").replace(".0",""));
                                plantingModel.setmDate(jsonObject.getString("PL_MDATE").replace("T"," "));
                                plantingModel.setVARIETY(jsonObject.getString("PL_VARIETY").replace(".0",""));
                                plantingModel.setPlantingType(jsonObject.getString("PL_PLATYPE").replace(".0",""));
                                plantingModel.setSeedType(jsonObject.getString("PL_SEEDTYPE").replace(".0",""));
                                plantingModel.setSeedSetType(jsonObject.getString("PL_SEEDSET").replace(".0",""));
                                plantingModel.setSoilTreatment(jsonObject.getString("PL_SOILTR").replace(".0",""));
                                //plantingModel.setPLOTVillage(jsonObject.getString("PL_INDPLVILL").replace(".0",""));
                                //plantingModel.setPLOTVillage(jsonObject.getString("PL_INDVPLOTNO").replace(".0",""));
                                plantingModel.setIsIdeal(jsonObject.getString("PL_ISIDEAL").replace(".0",""));
                                plantingModel.setIsNursery(jsonObject.getString("PL_ISNARSARI").replace(".0",""));
                                plantingModel.setSeedBagQty(jsonObject.getString("PL_SEEDBAGQTY"));
                                plantingModel.setServerStatus("DONE");
                                plantingModel.setRemark("");

                                dbh.saveDownloadedPlantingModel(plantingModel);
                            }
                            //download_old_survey_data.setText(""+dbh.getFarmerModel("").size());

                        }
                    }
                    catch (Exception e)
                    {

                    }
                }
            } catch (SecurityException e) {
                Log.e("Exception", e.getMessage());
                message = e.getMessage();
                if (dialog.isShowing())
                    dialog.dismiss();
            }
            catch (Exception e) {
                Log.e("Exception", e.getMessage());
                message = e.getMessage();
                if (dialog.isShowing())
                    dialog.dismiss();
            }
            return null;
        }

      @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            dialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Log.d(TAG, "onPostExecute: " + result);
            if (message != null) {
                try{
                    Log.d(TAG, message);

                    download_planting_data.setText(dbh.getPlantingModel("","","","","").size() + " Data");
                    AlertPopUp(dbh.getPlantingModel("","","","","").size() + " successfully imported");

                }
                catch (Exception e)
                {
                    AlertPopUp(e.toString());
                }
                if (dialog.isShowing())
                    dialog.dismiss();
            } else {
                AlertPopUp(getString(R.string.technical_error));
            }
        }
    }

    private void AlertPopUp(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                context);
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

    private void AlertPopUpWithFinish(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                context);
        alertDialog.setTitle(getString(R.string.app_name));
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton(getString(R.string.BTN_OK),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
        alertDialog.show();
    }



}