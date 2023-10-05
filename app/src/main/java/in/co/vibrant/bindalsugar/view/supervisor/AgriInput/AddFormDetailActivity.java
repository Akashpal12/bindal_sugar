package in.co.vibrant.bindalsugar.view.supervisor.AgriInput;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.AgriInputFormModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.UtilFile;


public class AddFormDetailActivity extends AppCompatActivity {
    private static final String TAG = "AddFormDetailActivity";
    TextView  tv_VillagCode, tv_villageName, tv_growerCode, tv_Name, tv_growerFatherName;
    ImageView iv_photo_1, iv_photo_2, iv_signature,iv_thumb;
    private List<AgriInputFormModel> inputFormList = new ArrayList<>();
    private List<UserDetailsModel> userDetailsModels = new ArrayList<>();
    private DBHelper db;
    Button btn_next;
    Toolbar toolbar;
   Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_form_detail);

        context=AddFormDetailActivity.this;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.MENU_AGRI_INPUT));
        toolbar. setTitle(getString(R.string.MENU_AGRI_INPUT));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        db = new DBHelper(this);
        init();
    }

    private void init() {

        tv_VillagCode = findViewById(R.id.tv_VillagCode);
        tv_villageName = findViewById(R.id.tv_villageName);
        tv_growerCode = findViewById(R.id.tv_growerCode);
        tv_Name = findViewById(R.id.tv_Name);
        tv_growerFatherName = findViewById(R.id.tv_growerFatherName);

        iv_photo_1 = findViewById(R.id.iv_photo_1);
        iv_photo_2 = findViewById(R.id.iv_photo_2);
        iv_signature = findViewById(R.id.iv_signature);
        iv_thumb = findViewById(R.id.iv_thumb);
        btn_next = findViewById(R.id.btn_next);

        try {
            if (db != null) {
                inputFormList.clear();
                inputFormList.addAll(db.getAllAgriInputData(UtilFile.villageCode, UtilFile.growerCode));
                Log.d(TAG, "init: " + "><><><><" + inputFormList.size());
                for (int i = 0; i < inputFormList.size(); i++) {
                    String village = inputFormList.get(i).getAgriVillageName().toLowerCase();
                    String mVill = village.substring(0, 1).toUpperCase() + village.substring(1);
                    tv_Name.setText(inputFormList.get(i).getAgriGrowerName());

                    String gFather = inputFormList.get(i).getAgriGrowerFather().toLowerCase();
                    String mGFather = gFather.substring(0, 1).toUpperCase() + gFather.substring(1);
                    tv_growerFatherName.setText("Father name : "+mGFather);
                    tv_growerCode.setText("Grower code : " + inputFormList.get(i).getAgriGrowerCode());
                    tv_VillagCode.setText("Village code : "+inputFormList.get(i).getAgriVillageCode());
                    tv_villageName.setText("Village : "+mVill);

                    iv_photo_1.setImageBitmap(db.getRetrivePhoto1(UtilFile.villageCode, UtilFile.growerCode));
                    iv_photo_2.setImageBitmap(db.getRetrivePhoto2(UtilFile.villageCode, UtilFile.growerCode));
                    iv_signature.setImageBitmap(db.getRetriveSignature(UtilFile.villageCode, UtilFile.growerCode));
                    iv_thumb.setImageBitmap(db.getRetriveThumb(UtilFile.villageCode, UtilFile.growerCode));
                    Log.d(TAG, "init: " + inputFormList.size());
                }

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }


        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddFormDetailActivity.this, AddMaterialDataActivity.class);
                inputFormList=db.getAllAgriInputData();
                String agriInputId="";
                agriInputId = "" + inputFormList.get(0).getAgri_input_Id();
                intent.putExtra("agriInputId",agriInputId);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                finish();

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }




}
