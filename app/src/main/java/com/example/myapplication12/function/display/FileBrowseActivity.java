package com.example.myapplication12.function.display;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.myapplication12.R;
import com.example.myapplication12.bean.TreeImage;
import com.example.myapplication12.tool.Content;
import com.example.myapplication12.tool.HttpStatus;
import com.example.myapplication12.tool.IntentUtil;
import com.example.myapplication12.tool.NetWorkUtil;
import com.example.myapplication12.tool.OkHttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.RequestBody;

import static com.example.myapplication12.tool.Content.SERVER_URL;

public class FileBrowseActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_startTime;
    private EditText et_endTime;

    private Date startTime = new Date();
    private Date endTime = new Date();

    private TimePickerView pvTime;

    private Button searchBtn;

    private String postJson;

    private String returnJson;

    private boolean canReachable;

    private HttpStatus status;

    private String POST_DATE_URL = SERVER_URL + "QueryTrees";

    private List<TreeImage> treeImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_file_browse);
        initTimePicker();
        et_startTime = findViewById(R.id.et_startTime);
        et_endTime = findViewById(R.id.et_endTime);
        et_startTime.setOnClickListener(this);
        et_endTime.setOnClickListener(this);
        searchBtn = findViewById(R.id.search_btn);
    }

    private void initTimePicker() {

        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                //如果是开始时间的EditText
                if (v.getId() == R.id.et_startTime) {
                    startTime = date;
                } else {
                    endTime = date;
                }
                EditText editText = (EditText) v;
                editText.setText(getTime(date));
            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {

                    }
                })
                .setType(new boolean[]{true, true, true, false, false, false})
                .isDialog(true)
                .build();


        Dialog mDialog = pvTime.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            pvTime.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
            }
        }
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        Log.i("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_startTime:
                if (pvTime != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(startTime);
                    pvTime.setDate(calendar);
                    pvTime.show(view);
                }
                break;
            case R.id.et_endTime:
                if (pvTime != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(endTime);
                    pvTime.setDate(calendar);
                    pvTime.show(view);
                }
                break;
            case R.id.search_btn:
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date1 = dateFormat.parse(et_startTime.getText().toString());
                    Date date2 = dateFormat.parse(et_endTime.getText().toString());

                    if (date1.getTime() <= date2.getTime() && (date1.getTime() <= new Date().getTime() && date2.getTime() <= new Date().getTime())) {
                        search(et_startTime.getText().toString(), et_endTime.getText().toString());
                    } else {
                        Toast.makeText(getApplicationContext(), "请输入正确的日期范围(请检查选择日期是否超过当前日期或日期上限与日期下限不合逻辑)", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    private void search(final String startDate, final String endDate) {
        if (!NetWorkUtil.isNetworkConnected(this)) {
            Toast.makeText(this, "设备的网络不可用~", Toast.LENGTH_SHORT).show();
            return;
        }
        Thread checkStatusThread = new Thread(new Runnable() {
            @Override
            public void run() {
                postJson = createDateJson(handleDateString(startDate), handleDateString(endDate));
                RequestBody body = RequestBody.create(Content.JSON_HEADER, postJson);

                status = new OkHttpUtil(POST_DATE_URL).doPost(body);
                returnJson = status.getResponseAns();
                Log.i("TAG-search", status.getStatus() + " " + returnJson);
                if (status.getStatus() == 200) {
                    canReachable = true;
                } else {
                    canReachable = false;
                }
            }
        });
        checkStatusThread.start();
        try {
            checkStatusThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (canReachable) {
            parseTreeImagesJson(returnJson);
        } else {
            Toast.makeText(FileBrowseActivity.this, "服务器异常暂时无法访问！", Toast.LENGTH_SHORT).show();//
        }
    }

    private void parseTreeImagesJson(String json) {
        try {
            JSONObject treeImagesJson = new JSONObject(json);//jsonObject
            JSONArray jsonArray = treeImagesJson.getJSONArray("treeImages");
            if (!jsonArray.toString().contains("{}")) {//
                treeImages = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String id = jsonObject.getString("id");
                    String name = jsonObject.getString("name");
                    Double longitude = jsonObject.getDouble("longitude");
                    Double latitude = jsonObject.getDouble("latitude");
                    String u_account = jsonObject.getString("u_account");
                    String record_date = jsonObject.getString("record_date");
                    TreeImage treeImage = new TreeImage(id, name, longitude, latitude, u_account, record_date);
                    treeImages.add(treeImage);
                }
//                IntentUtil.sendIntent(FileBrowseActivity.this,QueryListActivity.class);
                Intent intent = new Intent(FileBrowseActivity.this, QueryListActivity.class);
                intent.putExtra("treeImages", (Serializable) treeImages);
                startActivity(intent);
            } else {
                Toast.makeText(FileBrowseActivity.this, "查询没有结果~", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String handleDateString(String date) {
        return date.substring(0, 4) + date.substring(5, 7) + date.substring(8, 10);
    }

    private String createDateJson(String startDate, String endDate) {
        return "{\"startDate\":\"" + startDate + "\",\"" + "endDate\":\"" + endDate + "\"}";
    }

}
