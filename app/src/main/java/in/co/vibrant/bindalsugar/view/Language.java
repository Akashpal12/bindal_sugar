package in.co.vibrant.bindalsugar.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import java.util.Locale;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;


/**
 * Created by Shobhit on 11-08-2017.
 */

public class Language extends AppCompatActivity {
    Toolbar toolbar;
    Context context;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.language);
        toolbar=findViewById(R.id.toolbar);
        context=Language.this;
        setTitle(getString(R.string.MSG_SELECT_LANGUAGE));
        toolbar.setTitle(getString(R.string.MSG_SELECT_LANGUAGE));
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }

    private void redirect()
    {
        Intent intent=new Intent(context,Home.class);
        startActivity(intent);
        finish();
    }


    public void setEnglish(View v)
    {
        try {
            Locale locale = new Locale("en");
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("language", "en");
            editor.commit();
            Resources resources = getResources();
            Configuration configuration = resources.getConfiguration();
            DisplayMetrics displayMetrics = resources.getDisplayMetrics();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                configuration.setLocale(locale);
            } else {
                configuration.locale = locale;
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                getApplicationContext().createConfigurationContext(configuration);
            } else {
                resources.updateConfiguration(configuration, displayMetrics);
            }
            redirect();
        }
        catch (Exception e)
        {
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }
    }

    public void setHindi(View v)
    {
        try {
            Locale locale = new Locale("hi");
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("language", "hi");
            editor.commit();
            Resources resources = getResources();
            Configuration configuration = resources.getConfiguration();
            DisplayMetrics displayMetrics = resources.getDisplayMetrics();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                configuration.setLocale(locale);
            } else {
                configuration.locale = locale;
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                getApplicationContext().createConfigurationContext(configuration);
            } else {
                resources.updateConfiguration(configuration, displayMetrics);
            }
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = locale;
            res.updateConfiguration(conf, dm);
            redirect();
        }
        catch (Exception e)
        {
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }
    }
}
