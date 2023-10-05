package in.co.vibrant.bindalsugar.view.grower;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;


public class GrowerDevelopmentDashboard extends AppCompatActivity {

    SQLiteDatabase db;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.development_dashboard);
        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
            setSupportActionBar(toolbar);
            context = GrowerDevelopmentDashboard.this;
            setTitle(getString(R.string.MENU_DEVELOPMENT));
            toolbar.setTitle(getString(R.string.MENU_DEVELOPMENT));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        catch (Exception e)
        {
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }
    }

    public void openCaneDevelopment(View v)
    {

    }

}
