package crirecipe.criusker.crirecipe.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import crirecipe.criusker.crirecipe.R;
import crirecipe.criusker.crirecipe.Util.HttpUtil;
import crirecipe.criusker.crirecipe.Util.Utility;
import crirecipe.criusker.crirecipe.adapter.HomeContentAdapter;
import crirecipe.criusker.crirecipe.basic.BaseActivity;
import crirecipe.criusker.crirecipe.gson.biaoqiancaipu.ListBean;
import crirecipe.criusker.crirecipe.gson.biaoqiancaipu.RecipeBean;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SearchActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "SearchActivity";

    private EditText etSearch;
    public SwipeRefreshLayout swipeRefresh;
    private HomeContentAdapter contentAdapter;

    private List<ListBean> listList = new ArrayList<>();
    private List<RecipeBean> recipeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Button detailBack = findViewById(R.id.search_back);
        TextView tvSearch = findViewById(R.id.tv_search);
        etSearch = findViewById(R.id.auto_search);
        detailBack.setOnClickListener(this);
        tvSearch.setOnClickListener(this);

    }

    //查询菜谱
    private void queryListBean(String ctgId){
        List<ListBean> listBeanList = LitePal.findAll(ListBean.class);
        Log.d(TAG, "queryListBean:listBeanList.size "+listBeanList.size());
        List<ListBean> listBeans = new ArrayList<>();

        List<RecipeBean> recipeBeanList = LitePal.findAll(RecipeBean.class);
        Log.d(TAG, "queryListBean:recipeBeanList.size "+recipeBeanList.size());
        List<RecipeBean> recipeBeans = new ArrayList<>();

        for (int i=0;i<listBeanList.size();i++){
            ListBean listBeanBQ = listBeanList.get(i);
            RecipeBean recipeBeanBQ = recipeBeanList.get(i);
            if (listBeanBQ.getCtgIds().contains(ctgId)) {
                Log.d(TAG, "queryListBean: " + listBeanBQ.getCtgIds());
                listBeans.add(listBeanBQ);
                if (recipeBeanBQ.getTitle().contains(listBeanBQ.getName().substring(0,1))){
                    recipeBeans.add(recipeBeanBQ);
                }
            }
        }
        if (listBeans.size()>0){//数据库有数据
            listList.clear();
            recipeList.clear();
            //if (listBeanList.get(0).getCtgIds().contains(ctgId)){//数据库里的数据
            listList.addAll(listBeans);
            recipeList.addAll(recipeBeans);
            // }
            contentAdapter.notifyDataSetChanged();
            swipeRefresh.setRefreshing(false);
        }else{
            String recipeAddress ="http://apicloud.mob.com/v1/cook/menu/search?key=29319fda3df98&cid="+ctgId+"&page=1&size=20";
            queryFromServer(recipeAddress,"ListBean");
        }

    }

    //网络获取数据并解析存储到本地
    private void queryFromServer(final String address, final String type){
        HttpUtil.sendOKHttpRequest(address, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d(TAG, "onFailure: "+address);
            }

            @SuppressLint("NewApi")
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                boolean result = false;

                result = Utility.handleBQCaipuResponse(response);

                if (result){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_back:
                finish();
                break;
            case R.id.tv_search:
                String recipeName = etSearch.getText().toString();

                Log.d(TAG, "onClick: "+recipeName);
                break;
        }
    }
}
