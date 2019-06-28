package com.example.myapplication12.function.display;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.myapplication12.R;
import com.example.myapplication12.bean.TreeImage;
import com.example.myapplication12.function.map.route.WalkRouteActivity;
import com.example.myapplication12.tool.IntentUtil;

public class DisplayPhotoActivity extends AppCompatActivity {

    private ImageView imageView;

    private TreeImage treeImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_photo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        String url=bundle.getString("url");
        treeImage= (TreeImage) bundle.getSerializable("treeImage");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "拍摄日期："+treeImage.getRecord_date()+"  拍摄者："+treeImage.getU_account()+
                                "\n经度："+treeImage.getLongitude()+"  纬度："+treeImage.getLatitude()
                        , Snackbar.LENGTH_LONG).show();
            }
        });
        imageView=findViewById(R.id.imageView2);
        Glide.with(this).load(url).thumbnail(Glide.with(this).load(R.drawable.loading)).fitCenter().into(imageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_createroute,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.createrouteItem:
            {
                Bundle bundle=new Bundle();
//                bundle.putString("endlocationLongtitude",treeImage.getLongitude().toString());
//                bundle.putString("endlocationLatitude",treeImage.getLatitude().toString());
                bundle.putSerializable("treeImage",treeImage);
                IntentUtil.sendIntent(DisplayPhotoActivity.this,WalkRouteActivity.class,bundle);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
