package crirecipe.criusker.crirecipe.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import crirecipe.criusker.crirecipe.activity.FenleiActivity;
import crirecipe.criusker.crirecipe.activity.SearchActivity;
import crirecipe.criusker.crirecipe.adapter.HomeContentAdapter;
import crirecipe.criusker.crirecipe.adapter.HomeHeadAdapter;
import crirecipe.criusker.crirecipe.gson.biaoqiancaipu.ListBean;
import crirecipe.criusker.crirecipe.gson.biaoqiancaipu.RecipeBean;
import crirecipe.criusker.crirecipe.gson.fenleibiaoqian.CategoryInfoBeanXX;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Create by 李菀直 on 2019/1/1.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "HomeFragment";

    private List<CategoryInfoBeanXX> categoryInfoXXList = new ArrayList<>();
    private List<ListBean> listList = new ArrayList<>();
    private List<RecipeBean> recipeList = new ArrayList<>();

    public SwipeRefreshLayout swipeRefresh;

    private String ctgId = "0010001030";
    private final int HEAD_ONCLICK = 0;
    private final int REFRESH = 1;

    HomeContentAdapter contentAdapter;
    HomeHeadAdapter headAdapter;
    RecyclerView homeContent;
    EditText etHomeSearch;
    Button btnShare;
    Button btnMsg;
    Button btnFenlei;

    //onCreateView：加载布局
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home,container,false);
        RecyclerView homeHead = view.findViewById(R.id.home_head);
        homeContent = view.findViewById(R.id.home_content);
        etHomeSearch = view.findViewById(R.id.et_home_search);
        swipeRefresh = view.findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        btnShare = view.findViewById(R.id.btn_share);
        btnMsg = view.findViewById(R.id.btn_msg);
        btnFenlei = view.findViewById(R.id.btn_fenlei);
        //首页导航条
        LinearLayoutManager HeadLayoutManager = new LinearLayoutManager(getContext());
        HeadLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        homeHead.setLayoutManager(HeadLayoutManager);
        headAdapter = new HomeHeadAdapter(categoryInfoXXList);
        homeHead.setAdapter(headAdapter);
        //首页内容
        LinearLayoutManager ContentLayoutManager = new LinearLayoutManager(getContext());
        homeContent.setLayoutManager(ContentLayoutManager);
        contentAdapter = new HomeContentAdapter(listList,recipeList);
        homeContent.setAdapter(contentAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        queryCategoryInfoBeanXX();//分类标签
        queryListBean(ctgId);//查询 按标签查菜谱 菜谱
        Log.d(TAG, "onActivityCreated: ----------");
        //首页搜索框
        etHomeSearch.setFocusable(false);
        etHomeSearch.setCursorVisible(false);
        etHomeSearch.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnMsg.setOnClickListener(this);
        btnFenlei.setOnClickListener(this);
        //headadapter监听
        headAdapter.setOnItemClickListener(new HomeHeadAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final View view, final int position) {
                ctgId = categoryInfoXXList.get(position).getCtgId();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = HEAD_ONCLICK;
                        handler.sendMessage(message);
                    }
                }).start();
            }
        });
        //首页刷新
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = REFRESH;
                        handler.sendMessage(message);
                    }
                }).start();
            }
        });
    }

    //查询分类:按菜品分类 parentId：0010001004
    private void queryCategoryInfoBeanXX(){
        List<CategoryInfoBeanXX> categoryInfoBeanXXList = LitePal.findAll(CategoryInfoBeanXX.class);
        if (categoryInfoBeanXXList.size()>0){
            categoryInfoXXList.clear();
            for(CategoryInfoBeanXX categoryInfoBeanXX : categoryInfoBeanXXList){
                if ("0010001004".equals(categoryInfoBeanXX.getParentId())){
                    categoryInfoXXList.add(categoryInfoBeanXX);
                }
            }
            headAdapter.notifyDataSetChanged();
        }else {//没有查询内容则从服务器上获取
            String address ="http://apicloud.mob.com/v1/cook/category/query?key=29319fda3df98";
            queryFromServer(address,"CategoryInfoBeanXX");
        }
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
                if("ListBean".equals(type)){
                    result = Utility.handleBQCaipuResponse(response);
                }else if("CategoryInfoBeanXX".equals(type)){
                    result = Utility.handleFLBiaoQianResponse(response);
                }
                if (result) Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if ("ListBean".equals(type)) {
                            queryListBean(ctgId);
                            Log.d(TAG, "run: " + address);
                        } else if ("CategoryInfoBeanXX".equals(type)) {
                            queryCategoryInfoBeanXX();
                        }
                    }
                });
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.et_home_search:
                Intent searchIntent = new Intent(getActivity(), SearchActivity.class);
                startActivity(searchIntent);
                break;
            case R.id.btn_share:
                break;
            case R.id.btn_msg:
                break;
            case R.id.btn_fenlei:
                Intent fenleiIntent = new Intent(getActivity(), FenleiActivity.class);
                startActivity(fenleiIntent);
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HEAD_ONCLICK:
                queryListBean(ctgId);
                Log.d(TAG, "onClick: " + ctgId);
                    break;
                case REFRESH:
                    queryListBean(ctgId);
                    Log.d(TAG, "onRefresh: "+ctgId);
                    break;
                default:
                    break;
            }
        }
    };
}
