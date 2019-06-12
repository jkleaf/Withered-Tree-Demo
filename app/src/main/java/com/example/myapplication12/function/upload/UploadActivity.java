package com.example.myapplication12.function.upload;

import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication12.R;
import com.example.myapplication12.bean.TreeImage;
import com.example.myapplication12.controller.LoadingController;
import com.example.myapplication12.tool.FileUtil;
import com.example.myapplication12.tool.HttpStatus;
import com.example.myapplication12.tool.NetWorkUtil;
import com.example.myapplication12.tool.OkHttpUtil;
import com.zx.uploadlibrary.listener.ProgressListener;
import com.zx.uploadlibrary.listener.impl.UIProgressListener;
import com.zx.uploadlibrary.utils.OKHttpUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.myapplication12.tool.Content.SERVER_URL;
import static com.example.myapplication12.tool.Content.TREEIMG;

public class UploadActivity extends AppCompatActivity {

    private ListView filesListView;

    private List<String> filesList;

    private ArrayAdapter<String> filesArrayAdapter;

    public static final String PHOTO_DIR_PATH = FileUtil.getFilePath(TREEIMG).getPath();

    private ProgressBar uploadProgressBar;

    private TextView uploadTxtView;

    private static final String POST_FILE_URL = SERVER_URL + "MutilUpload";

    private HttpStatus status;

    private boolean canUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        filesList = getIntent().getStringArrayListExtra("filesList");
        initElements();
        showListView();
    }

    private void initElements() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        filesListView = findViewById(R.id.filesListView);
        uploadProgressBar = findViewById(R.id.upload_progress_bar);
        uploadTxtView = findViewById(R.id.upload_progress_textView);
        uploadProgressBar.setVisibility(View.INVISIBLE);
        uploadTxtView.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_upload, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (filesList != null) {///////////////////
                    filesList.clear();
                    filesArrayAdapter.clear();
                    filesListView.setAdapter(filesArrayAdapter);
                }
                this.finish();
                return true;
            case R.id.uploadItem:
                uploadItemSelected();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {///////////////////
        super.onBackPressed();
    }

    private void showListView() {
        if (filesList != null) {
            filesArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, filesList);
            filesListView.setAdapter(filesArrayAdapter);
        }
    }

    private void uploadItemSelected() {
        if (!NetWorkUtil.isNetworkConnected(this)) {
            Toast.makeText(this, "当前设备网络不可用~", Toast.LENGTH_LONG).show();
        } else {
//            new LoadingController(this).execute(Integer.valueOf(0));
            Thread checkStatusThread=new Thread(new Runnable() {
                @Override
                public void run() {
                    status = new OkHttpUtil(SERVER_URL).doGet();
                    Log.i("TAG-GetResponse_Answer:", status.getStatus() + " " + status.getResponseAns());
                    if (status.getStatus() == 200) {
                        canUpload = true;
                    } else {
                        canUpload = false;
                        Looper.prepare();//线程中使用Toast
                        Toast.makeText(UploadActivity.this, "无法上传~响应码：" + status.getStatus(), Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                }
            });
            checkStatusThread.start();
            try {
                checkStatusThread.join();//wait!!!!
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (canUpload) {
                upload();
            }
        }
    }

    private void showProgressLogs(long bytesWrite, long contentLength, boolean done) {
        Log.i("TAG", "bytesWrite:" + bytesWrite);
        Log.i("TAG", "contentLength" + contentLength);
        Log.i("TAG", (100 * bytesWrite) / contentLength + " % done ");
        Log.i("TAG", "done:" + done);
        Log.i("TAG", "================================");
    }

    private void upload() {
        final ProgressListener progressListener = new ProgressListener() {
            @Override
            public void onProgress(long bytesWrite, long contentLength, boolean done) {
                showProgressLogs(bytesWrite, contentLength, done);
            }
        };

        UIProgressListener uiProgressRequestListener = new UIProgressListener() {
            @Override
            public void onUIProgress(long bytesWrite, long contentLength, boolean done) {
                showProgressLogs(bytesWrite, contentLength, done);
                int progress = (int) ((100 * bytesWrite) / contentLength);
                uploadProgressBar.setProgress(progress);
                uploadTxtView.setText("上传进度值：" + progress + "%");
            }

            @Override
            public void onUIStart(long bytesWrite, long contentLength, boolean done) {
                super.onUIStart(bytesWrite, contentLength, done);
                uploadProgressBar.setVisibility(View.VISIBLE);
                uploadTxtView.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "开始上传", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUIFinish(long bytesWrite, long contentLength, boolean done) {
                super.onUIFinish(bytesWrite, contentLength, done);
//                uploadProgressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_SHORT).show();
                uploadProgressBar.setVisibility(View.INVISIBLE);
                uploadTxtView.setVisibility(View.INVISIBLE);
            }
        };

        //POST
        OKHttpUtils.doPostRequest(POST_FILE_URL, initUploadFile(), uiProgressRequestListener, new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                Log.i("TAG", "error------> " + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {////////////////////////////////
                        Toast.makeText(UploadActivity.this, "上传失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("TAG", "success---->" + response.body().string());
            }
        });
    }

    private List<String> initUploadFile() {
        List<String> filesPathNames = new ArrayList<>();
        for (String fileName : filesList) {
            filesPathNames.add(PHOTO_DIR_PATH + File.separator + fileName);
//            Log.i("---------initUploadFile----------", PHOTO_DIR_PATH + File.separator + fileName);
        }
        return filesPathNames;
    }

}
