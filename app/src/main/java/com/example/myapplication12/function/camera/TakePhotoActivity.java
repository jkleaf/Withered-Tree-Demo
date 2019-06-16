package com.example.myapplication12.function.camera;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication12.R;
import com.example.myapplication12.tool.DialogUtil;
import com.example.myapplication12.tool.FileUtil;
import com.example.myapplication12.tool.ImageUtil;
import com.example.myapplication12.tool.LocateUtil;
import com.example.myapplication12.tool.PermissonUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DecimalFormat;

import static com.example.myapplication12.tool.Content.*;

public class TakePhotoActivity extends AppCompatActivity implements View.OnClickListener {

    private final static int CAMERA_REQUEST_CODE = 0;

//    private final static int GALLERY_REQUEST_CODE = 1;

    private String photoPath;

    private Uri imageUri;

    private boolean imgDirFlag;

    private boolean isSaved;

    private ImageView photoImageView;

    private TextView longitudeTxtView;
    private TextView latitudeTxtView;
    private TextView photoPathTxtView;
    private Button saveBtn;
    private Button removeBtn;
    private Bitmap bitmap;

    private Dialog dialog;

    private String sysLongitude;
    private String sysLatitude;
    private String lngGetFromImg;
    private String latGetFromImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takephoto);
        initElements();
        initClickListener();
        if (FileUtil.fileExists(TREEIMG)) {
            imgDirFlag = true;
        }
        checkCameraPermission();
    }

    private void initElements() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        photoImageView = findViewById(R.id.photoImageView);
        photoPathTxtView = findViewById(R.id.photoPathTxtView);
        longitudeTxtView = findViewById(R.id.longitudeTxtView);
        latitudeTxtView = findViewById(R.id.latitudeTxtView);
        saveBtn = findViewById(R.id.saveBtn);
        removeBtn = findViewById(R.id.removeBtn);
    }

    private void initClickListener() {
        saveBtn.setOnClickListener(this);
        removeBtn.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(!isSaved)
                    FileUtil.deleteFile(photoPath);
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(!isSaved)
            FileUtil.deleteFile(photoPath);
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saveBtn:
                if (photoImageView != null)
                    saveBtnClicked(v);
                break;
            case R.id.removeBtn:
                if (photoImageView != null)
                    removeBtnClicked(v);
                break;
        }
    }

    private void checkCameraPermission() {
        if (!PermissonUtil.checkApplyPermisson(this, Manifest.permission.CAMERA)) {
            PermissonUtil.applyPermisson(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        } else {
            takePhoto();
        }
    }

    private void takePhoto() {
        Intent intentToTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (!imgDirFlag) {
            FileUtil.mkDir(TREEIMG);
            imgDirFlag = true;
        }
        photoPath = FileUtil.getPhotoPathName();
        imageUri = Uri.fromFile(new File(photoPath));///no use
        intentToTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intentToTakePhoto, CAMERA_REQUEST_CODE);
    }

//    private void choosePhoto(){
//        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
//        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/jpeg");//
//        startActivityForResult(intentToPickPic, GALLERY_REQUEST_CODE);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == TakePhotoActivity.RESULT_OK) {//
            Log.i("TakePhotoActivity_onActivityResult", "--------------------------Enter Here--------------------------");
            switch (requestCode) {
                case CAMERA_REQUEST_CODE:
                    try {
                        Log.i("TakePhotoActivity_onActivityResult", "true");
//                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        bitmap= ImageUtil.getCompressBitmap(this,new File(photoPath));
//                        Location location = LocateUtil.exif2Loc(photoPath);
//                        Location location=LocateUtil.getLocFromSys(this);
                        LocateUtil.getLocFromSys(this);
//                        Log.i("TakePhotoActivity_onActivityResult",bitmap.toString());
//                        saveBitmapToDir(bitmap);
//                        bitmap=drawCircleView(bitmap);
                        if (bitmap != null) {
//                            bitmap = ImageUtil.getTranslateImage(bitmap, "119.23586273", "26.086814880");
                            photoImageView.setImageBitmap(bitmap);
                            photoPathTxtView.setText(photoPath);
                            sysLongitude = String.valueOf(LocateUtil.getLongitude());
                            sysLatitude = String.valueOf(LocateUtil.getLatitude());//String.valueOf返回字符串"null"
                            if ("null" != sysLongitude && "null" != sysLatitude) {
                                Log.i("----------TAG----------",sysLongitude+" "+sysLatitude);
                                bitmap=ImageHandler.getTranslateImage(bitmap,sysLongitude,sysLatitude);
//                                bitmap = ImageHandler.getTranslateImage(bitmap, "151.45153", "89.4545");
                                lngGetFromImg = ImageHandler.getLng(bitmap);
                                latGetFromImg = ImageHandler.getLat(bitmap);
                                longitudeTxtView.setText("经度: " + sysLongitude);
                                latitudeTxtView.setText("纬度: " + sysLatitude);
                            } else {
                                Toast.makeText(this, "无法获取经纬度~", Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
//                case GALLERY_REQUEST_CODE:
//                    imageUri = data.getData();
//                    if (imageUri != null) {
//                        try {
//                            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
//                            saveBitmapToDir(bitmap);
////                            bitmap=drawCircleView(bitmap);
////                            if(bitmap!=null) {
////                                imageView.setImageBitmap(bitmap);
////                            }
//                        } catch (FileNotFoundException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);//
    }

//    private Bitmap drawCircleView(Bitmap bitmap) {
//        int width=bitmap.getWidth();
//        int height=bitmap.getHeight();
//        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
//        Bitmap bm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bm);
//        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        Shader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
//        paint.setShader(shader);
//        canvas.drawCircle(width/2, height/2, width/2, paint);
//        return bm;
//    }

    private void saveImageInfoToDir() {//Image Info to Textadb

    }

    private void saveBtnClicked(View v) {
        if (lngGetFromImg != null && latGetFromImg != null) {
            Log.i("----------------------------------lngGetFromImg:----------------------------------", lngGetFromImg);
            Log.i("----------------------------------latGetFromImg:----------------------------------", latGetFromImg);
            if (!lngGetFromImg.equals(zeroize(sysLongitude)) || !latGetFromImg.equals(zeroize(sysLatitude))) {//////////////
                Snackbar.make(v, "图像处理失败~", Snackbar.LENGTH_SHORT).show();
//                FileUtil.deleteFile(photoPath);//
            } else {
                saveBitmapToDir();//
                Snackbar.make(v, "图像处理成功!已保存~", Snackbar.LENGTH_SHORT).show();
                /*******/
//                try {
//                    Bitmap bitmapTmp=BitmapFactory.decodeStream(new FileInputStream(photoPath));
//                    if(bitmapTmp!=null) {
//                        longitudeTxtView.setText(ImageHandler.getLng(bitmapTmp));
//                        latitudeTxtView.setText(ImageHandler.getLat(bitmapTmp));
//                    }
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
                /******/
                isSaved=true;
            }
        } else {
            Toast.makeText(this, "无法获取经纬度~", Toast.LENGTH_LONG).show();
        }
    }

    private String zeroize(String doubleStr){
        return new DecimalFormat("0.000000").format(Double.parseDouble(doubleStr));
    }

    private void removeBtnClicked(View v) {
        showConfirmDialog(v);
    }

    private void removeImage(View v) {
        if (photoPath != null) {
            FileUtil.deleteFile(photoPath);
            Snackbar.make(v, "移除成功!", Snackbar.LENGTH_SHORT).show();
            photoImageView.setImageBitmap(null);
            photoPathTxtView.setText("");
            latitudeTxtView.setText("");
            longitudeTxtView.setText("");
        }
    }

    private void saveBitmapToDir() {
        File file = new File(photoPath);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showConfirmDialog(final View v) {
        dialog = DialogUtil.showNormalDialog(this, "移除图片", "确定要移除该图片吗？", "确定", "取消"
                , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeImage(v);
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
                        return;
                    }
                });
        dialog.show();
    }

}
