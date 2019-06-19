package com.example.myapplication12.function.login;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication12.R;
import com.example.myapplication12.bean.User;
import com.example.myapplication12.main.MainActivity;
import com.example.myapplication12.tool.Content;
import com.example.myapplication12.tool.DialogUtil;
import com.example.myapplication12.tool.HttpStatus;
import com.example.myapplication12.tool.IntentUtil;
import com.example.myapplication12.tool.NetWorkUtil;
import com.example.myapplication12.tool.OkHttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.example.myapplication12.tool.Content.SERVER_URL;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private String postJson;

    private String returnJson;

    private static final String POST_LOGIN_PARAMS_URL = SERVER_URL + "Login";

    private HttpStatus status;

    public static boolean isLogin;

    private boolean canReachable;

    private Dialog loginMsgDialog;

    private User user;

    private Button login_btn;

    private EditText account_editText;

    private EditText pwd_editText;

    private ImageView pwd_see_imageView;

//    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initElements();
        initClickListener();

    }

    private void initElements() {
        login_btn=findViewById(R.id.btn_login);
        account_editText=findViewById(R.id.et_account);
        pwd_editText=findViewById(R.id.et_password);
        pwd_see_imageView=findViewById(R.id.iv_see_password);
    }

    private void initClickListener(){
        login_btn.setOnClickListener(this);
        pwd_see_imageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                login();
                break;
            case R.id.iv_see_password:
                setPasswordVisibility();
                break;
        }
    }

    private String getAccount() {
        return account_editText.getText().toString().trim();
    }

    private String getPassword() {
        return pwd_editText.getText().toString().trim();
    }

    private void setPasswordVisibility(){
        if(pwd_see_imageView.isSelected()){
            pwd_see_imageView.setSelected(false);
            pwd_editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }else{
            pwd_see_imageView.setSelected(true);
            pwd_editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
    }

    private void login() {
        if(!NetWorkUtil.isNetworkConnected(this)){
            Toast.makeText(this,"设备的网络不可用~",Toast.LENGTH_SHORT).show();
            return;
        }
        if(getAccount().isEmpty()){
            Toast.makeText(this,"输入的账号不能为空!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(getPassword().isEmpty()){
            Toast.makeText(this,"输入的密码不能为空!",Toast.LENGTH_SHORT).show();
            return;
        }
        Thread checkStatusThread=new Thread(new Runnable() {
            @Override
            public void run() {
                postJson=createLoginJSON(getAccount(),getPassword());
                RequestBody body = RequestBody.create(Content.JSON_HEADER, postJson);

                status = new OkHttpUtil(POST_LOGIN_PARAMS_URL).doPost(body);
                returnJson=status.getResponseAns();
                Log.i("TAG-login",status.getStatus()+" "+returnJson);
                if (status.getStatus() == 200) {
                    canReachable=true;
                } else {
                    canReachable=false;
                }
            }
        });
        checkStatusThread.start();
        try {
            checkStatusThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(canReachable){
            parseLoginJson(returnJson);
        }else{
            Toast.makeText(LoginActivity.this, "服务器异常暂时无法访问！", Toast.LENGTH_SHORT).show();//
        }
    }

    private void parseLoginJson(String json){
        try {
            JSONObject jsonObject=new JSONObject(json);
//            JSONObject dialogMsgJson=jsonObject.getJSONObject("message");
            JSONObject userInfoJson=jsonObject.getJSONObject("user");
//            String dialogMsg= (String) dialogMsgJson.get("message");
//            Log.i("TAG-login-dialogMsg",dialogMsg);
            if(!userInfoJson.toString().contains("{}")){
                isLogin=true;
                Log.i("TAG-login-userInfo",userInfoJson.toString());
                String account=userInfoJson.getString("account");
                String username=userInfoJson.getString("username");
                String password=userInfoJson.getString("password");
                String gender=userInfoJson.getString("gender");
                String email=userInfoJson.getString("email");
                String telephone=userInfoJson.getString("telephone");
                user=new User(account,username,password,gender,email,telephone);
                showMsgDialog("登陆成功!");
            }else{
                isLogin=false;
                showMsgDialog("登录失败!用户名或密码错误!");
//                Log.i("TAG-login",dialogMsg);
                //
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String createLoginJSON(String account, String password) {
        return "{\"account\":\"" + account + "\",\"" + "password\":\"" + password + "\"}";
    }

    private void showMsgDialog(final String message){
        loginMsgDialog= DialogUtil.showNormalDialog(this, "登录响应", message, "确定"
                , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(message.contains("成功")){
                            Bundle bundle=new Bundle();
                            bundle.putSerializable("user",user);
                            IntentUtil.sendIntent(LoginActivity.this, MainActivity.class,bundle);
                        }else if(message.contains("失败")){
                        }
                    }
                }
        );
        loginMsgDialog.setCancelable(false);
        loginMsgDialog.show();
    }

}
