package com.example.myapplication12.main;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.myapplication12.R;
import com.example.myapplication12.controller.LoadingController;
import com.example.myapplication12.function.camera.TakePhotoActivity;
import com.example.myapplication12.function.display.FilePickerActivity;
import com.example.myapplication12.tool.FileUtil;
import com.example.myapplication12.tool.IntentUtil;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, View.OnLongClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ///////////////////////////////////////////////////////
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
//        builder.detectFileUriExposure();
//        TakePhotoActivity.setRootPath(this);
//        takePhoto=new TakePhotoActivity(MainActivity.this);
        FileUtil.setRootPath(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            navCameraSelected();
        } else if (id == R.id.nav_gallery) {
            navGallerySelected();
        } else if (id == R.id.nav_slideshow) {
            Toast.makeText(this, "此功能尚未开放~", Toast.LENGTH_SHORT).show();//
        } else if (id == R.id.nav_manage) {
            Toast.makeText(this, "此功能尚未开放~", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_share) {
            Toast.makeText(this, "此功能尚未开放~", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_send) {
//            Toast.makeText(this, "此功能尚未开放~", Toast.LENGTH_SHORT).show();
            LoadingController loadingView = new LoadingController(MainActivity.this);//this
            loadingView.execute(Integer.valueOf(0));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    private void navCameraSelected(){
//        Bundle bundle=new Bundle();
//        bundle.putString("pageTitle",CAMERATITLE);
        IntentUtil.sendIntent(MainActivity.this,TakePhotoActivity.class);
    }

    private void navGallerySelected(){
        IntentUtil.sendIntent(MainActivity.this, FilePickerActivity.class);
    }

}
