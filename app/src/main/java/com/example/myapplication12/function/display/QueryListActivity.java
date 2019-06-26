package com.example.myapplication12.function.display;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.myapplication12.R;
import com.example.myapplication12.bean.TreeImage;
import com.example.myapplication12.tool.IntentUtil;

import java.util.ArrayList;
import java.util.List;

import static com.example.myapplication12.tool.Constant.SERVER_URL;

public class QueryListActivity extends Activity implements AdapterView.OnItemClickListener {

    private List<Item> itemList;

    private ListView displayListView;

    private ItemAdapter itemAdapter;

    private List<TreeImage> treeImages;

    private static final String IMG_FOLDER = "upload/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        treeImages = (List<TreeImage>) getIntent().getSerializableExtra("treeImages");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_query_list);
        itemList = new ArrayList<>();
        displayListView = findViewById(R.id.lv);
        initData();
        itemAdapter = new ItemAdapter(this, itemList);
        displayListView.setAdapter(itemAdapter);
        displayListView.setOnItemClickListener(this);

    }

    private void initData() {
        for (int i = 0; i < treeImages.size(); i++) {
            itemList.add(new Item(treeImages.get(i).getName()));
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle bundle=new Bundle();
        bundle.putString("url",SERVER_URL + IMG_FOLDER + itemList.get(position).getPhotoName());
        bundle.putSerializable("treeImage",treeImages.get(position));
        IntentUtil.sendIntent(QueryListActivity.this,DisplayPhotoActivity.class,bundle);
    }
}

