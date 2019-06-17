package com.example.myapplication12.main;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication12.R;
import com.example.myapplication12.bean.User;
import com.example.myapplication12.controller.LoadingController;
import com.example.myapplication12.function.camera.TakePhotoActivity;
import com.example.myapplication12.function.display.FilePickerActivity;
import com.example.myapplication12.tool.DialogUtil;
import com.example.myapplication12.tool.FileUtil;
import com.example.myapplication12.tool.IntentUtil;

import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, View.OnLongClickListener {

    private View nav_header;

    private NavigationView navigationView;

    private TextView usernameTextView;

    private TextView accountTextView;

    private TextView loginDurationTextView;

    private TextView lastLoginTextView;

    private TextView takenPhotosTextView;

    private TextView usernameNavTextView;

    private TextView emailTextView;

    private Button logoutBtn;

    private Dialog logoutDialog;

    private User user;

    private Timer timer;

    private TimerTask timerTask;

    private static int secondsCount = 0;

    private SimpleDateFormat sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = (User) getIntent().getSerializableExtra("user");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initElements();
        initClickListener();
        showUserInfo();
        runTimer();
        ///////////////////////////////////////////////////////
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
//        builder.detectFileUriExposure();
//        TakePhotoActivity.setRootPath(this);
//        takePhoto=new TakePhotoActivity(MainActivity.this);
        FileUtil.setRootPath(this);
    }

    private void initElements() {
        usernameTextView = findViewById(R.id.username_textview);
        accountTextView = findViewById(R.id.account_textview);
        loginDurationTextView = findViewById(R.id.login_duration_textview);
        lastLoginTextView = findViewById(R.id.last_login_textview);
        takenPhotosTextView = findViewById(R.id.taken_photos_amounts_textview);
        if(navigationView.getHeaderCount()>0){//防止重复
            nav_header=navigationView.getHeaderView(0);
            usernameNavTextView=nav_header.findViewById(R.id.username_nav_textview);
            emailTextView = nav_header.findViewById(R.id.email_nav_textview);
        }
        logoutBtn = findViewById(R.id.logout_btn);
        sdf = new SimpleDateFormat("HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));//时区
    }

    private void initClickListener() {
        logoutBtn.setOnClickListener(this);
    }

    private void showUserInfo() {
        usernameTextView.setText(user.getUsername());
        accountTextView.setText(user.getAccount());
        usernameNavTextView.setText(user.getUsername());
        emailTextView.setText(user.getEmail());
    }

    private String getTime() {
        return sdf.format(1000 * (secondsCount++));
    }

    private void runTimer() {
        if (timer == null && timerTask == null) {
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loginDurationTextView.setText(getTime());
                        }
                    });
                }
            };
            timer.schedule(timerTask, 0, 1000);
        }
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
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

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout_btn:
                logoutBtnClicked();
                break;
        }
    }

    private void logoutBtnClicked() {
        showLogoutDialog();
    }

    private void showLogoutDialog() {//invalidate session or exit(0)
        logoutDialog = DialogUtil.showNormalDialog(this, "退出", "确定要退出吗？", "确定", "取消"
                , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);//
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        logoutDialog.show();
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    private void navCameraSelected() {
//        Bundle bundle=new Bundle();
//        bundle.putString("pageTitle",CAMERATITLE);
        IntentUtil.sendIntent(MainActivity.this, TakePhotoActivity.class);
    }

    private void navGallerySelected() {
        IntentUtil.sendIntent(MainActivity.this, FilePickerActivity.class);
    }

}
