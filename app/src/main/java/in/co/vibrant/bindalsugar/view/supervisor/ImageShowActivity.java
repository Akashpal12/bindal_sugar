package in.co.vibrant.bindalsugar.view.supervisor;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import in.co.vibrant.bindalsugar.R;


public class ImageShowActivity extends AppCompatActivity  {

    Context context;
    String ImageUrl;
    TextView txt;
    String Text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_view_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context= ImageShowActivity.this;
        setTitle(getString(R.string.MENU_IMAGE));
        toolbar.setTitle(getString(R.string.MENU_IMAGE));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
       ImageView image_view = findViewById(R.id.image_view);
        txt = findViewById(R.id.txt);
        ImageUrl=getIntent().getExtras().getString("ImageUrl");
        Text=getIntent().getExtras().getString("body");
        //Picasso.with(context).load(ImageUrl).into(image_view);
        Glide.with(this.context)
                .load(ImageUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(image_view);

    }

}
