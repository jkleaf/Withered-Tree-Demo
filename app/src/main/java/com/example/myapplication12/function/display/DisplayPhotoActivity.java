package com.example.myapplication12.function.display;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.myapplication12.R;
import com.example.myapplication12.bean.TreeImage;

public class DisplayPhotoActivity extends AppCompatActivity {

    private WebView webView;

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
        webView=findViewById(R.id.Windows);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        webView.loadUrl(url);
    }

}
