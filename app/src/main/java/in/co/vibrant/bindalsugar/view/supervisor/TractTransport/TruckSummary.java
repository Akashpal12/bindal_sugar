package in.co.vibrant.bindalsugar.view.supervisor.TractTransport;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;

public class TruckSummary extends AppCompatActivity {
     Context context;
    DBHelper dbh;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truck_summary);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        context= TruckSummary.this;
        dbh = new DBHelper(this);
        db = new DBHelper(this).getWritableDatabase();
        //transportDetailsList=dbh.getTransportDetails();
        setTitle(getString(R.string.MENU_TRUCK_SuMMERY));
        toolbar.setTitle(getString(R.string.MENU_TRUCK_SuMMERY));
    }
}