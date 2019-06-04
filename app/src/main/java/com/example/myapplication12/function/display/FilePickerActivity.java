package com.example.myapplication12.function.display;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.myapplication12.R;
import com.example.myapplication12.function.upload.UploadActivity;
import com.example.myapplication12.tool.FileUtil;
import com.example.myapplication12.tool.IntentUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ru.bartwell.exfilepicker.ExFilePicker;
import ru.bartwell.exfilepicker.data.ExFilePickerResult;

import static com.example.myapplication12.tool.Content.*;

public class FilePickerActivity extends AppCompatActivity implements View.OnClickListener {

    private Button fileChooseBtn;

    private Button fileManagerBtn;

    private final int EX_FILE_PICKER_RESULT = 0x01;

    private final int FILE_MANAGER_RESULT = 0x02;

    private String startDir;

    private ArrayList<String> filesList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_choose);
        initElements();
        initClickListener();
    }

    private void initElements() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        fileChooseBtn = findViewById(R.id.fileChooseBtn);
        fileManagerBtn = findViewById(R.id.fileManagerBtn);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initClickListener() {
        fileChooseBtn.setOnClickListener(this);
        fileManagerBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fileChooseBtn:
                openFilePicker();
                break;
            case R.id.fileManagerBtn:
                openFileManager();
                break;
        }
    }

    private void openFilePicker() {
        ExFilePicker exFilePicker = new ExFilePicker();
//        exFilePicker.setCanChooseOnlyOneItem(false);
        exFilePicker.setQuitButtonEnabled(true);
        if (TextUtils.isEmpty(startDir)) {
            exFilePicker.setStartDirectory(FileUtil.getFilePath(TREEIMG).getPath());
        } else {
            exFilePicker.setStartDirectory(startDir);
        }
        exFilePicker.setChoiceType(ExFilePicker.ChoiceType.FILES);
        exFilePicker.start(this, EX_FILE_PICKER_RESULT);
    }

    private void openFileManager() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setDataAndType(Uri.fromFile(FileUtil.getFilePath(TREEIMG)), "image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, FILE_MANAGER_RESULT);
//        startActivityForResult(Intent.createChooser(intent,"选择文件"),FILE_SELECT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {//requestCode
                case EX_FILE_PICKER_RESULT:
                    Log.i("FilePicker_onActivityResult", "--------------------------Enter Here--------------------------");
                    ExFilePickerResult result = ExFilePickerResult.getFromIntent(data);
                    if (result != null && result.getCount() > 0) {
                        String path = result.getPath();
                        List<String> names = result.getNames();
                        for (int i = 0; i < names.size(); i++) {
                            File f = new File(path, names.get(i));
                            try {
//                            Uri uri = Uri.fromFile(f);
//                            Toast.makeText(this, "选择文件:" + uri.getPath(), Toast.LENGTH_SHORT)
//                                    .show();
                                if (filesList == null) {
                                    filesList = new ArrayList<>();
                                }
                                filesList.add(names.get(i));
                                startDir = path;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        Bundle bundle = new Bundle();
                        bundle.putStringArrayList("filesList", filesList);
                        IntentUtil.sendIntent(this, UploadActivity.class, bundle);
                    }
                    break;
                case FILE_MANAGER_RESULT:
//                    Uri uri=data.getData();
//                    Toast.makeText(this, "文件路径："+ uri.getPath(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
//            super.onActivityResult(requestCode, resultCode, data);
    }
}
/***
 *
 *   █████▒█    ██  ▄████▄   ██ ▄█▀       ██████╗ ██╗   ██╗ ██████╗
 * ▓██   ▒ ██  ▓██▒▒██▀ ▀█   ██▄█▒        ██╔══██╗██║   ██║██╔════╝
 * ▒████ ░▓██  ▒██░▒▓█    ▄ ▓███▄░        ██████╔╝██║   ██║██║  ███╗
 * ░▓█▒  ░▓▓█  ░██░▒▓▓▄ ▄██▒▓██ █▄        ██╔══██╗██║   ██║██║   ██║
 * ░▒█░   ▒▒█████▓ ▒ ▓███▀ ░▒██▒ █▄       ██████╔╝╚██████╔╝╚██████╔╝
 *  ▒ ░   ░▒▓▒ ▒ ▒ ░ ░▒ ▒  ░▒ ▒▒ ▓▒       ╚═════╝  ╚═════╝  ╚═════╝
 *  ░     ░░▒░ ░ ░   ░  ▒   ░ ░▒ ▒░
 *  ░ ░    ░░░ ░ ░ ░        ░ ░░ ░
 *           ░     ░ ░      ░  ░
 */