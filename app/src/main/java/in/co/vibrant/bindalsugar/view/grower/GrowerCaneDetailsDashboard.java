package in.co.vibrant.bindalsugar.view.grower;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import in.co.vibrant.bindalsugar.R;


public class GrowerCaneDetailsDashboard extends AppCompatActivity  {

    SQLiteDatabase db;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grower_cane_details_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context= GrowerCaneDetailsDashboard.this;
        setTitle(getString(R.string.MENU_CANE_DETAILS));
        toolbar.setTitle(getString(R.string.MENU_CANE_DETAILS));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }





}
