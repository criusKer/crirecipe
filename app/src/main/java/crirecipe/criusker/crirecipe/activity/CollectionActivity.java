package crirecipe.criusker.crirecipe.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import crirecipe.criusker.crirecipe.R;
import crirecipe.criusker.crirecipe.Util.CONST;
import crirecipe.criusker.crirecipe.Util.HttpUtil;
import crirecipe.criusker.crirecipe.Util.ToolKits;
import crirecipe.criusker.crirecipe.adapter.GoodsAdapter;
import crirecipe.criusker.crirecipe.entity.Goods;
import crirecipe.criusker.crirecipe.entity.ResponseObject;
import crirecipe.criusker.crirecipe.entity.User;

/**
 * 收藏列表界面
 *  存在的问题：
 *   1.下拉刷新和上拉加载的逻辑不对
 */
public class CollectionActivity extends AppCompatActivity {

    private static final String TAG = "CollectionActivity";

    public static final String COLLECT_LIST = "collect_list";

    @ViewInject(R.id.collection_list_view)
    private PullToRefreshListView collection_list_view;
    @ViewInject(R.id.show_list_view)
    private LinearLayout show_list_view;
    @ViewInject(R.id.should_login)
    private TextView should_login;

    private List<Goods> mList = new ArrayList<>();

    private int page=0;//当前页码
    private int size=20;//每页多少条数据
    private int count=0;//总页数

    private GoodsAdapter mGoodsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        ViewUtils.inject(this);
        //对listview进行基础设置
        collection_list_view.setMode(PullToRefreshBase.Mode.BOTH);//同时支持上拉加载更多下拉刷新
        collection_list_view.setScrollingWhileRefreshingEnabled(true);//加载数据时不允许滚动操作
        collection_list_view.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                //进行数据加载操作
                //loadDatas(refreshView.getScrollY()<0);//滚动条y值小于0时：下拉操作
               showCollectView(refreshView);


            }
        });//滚动刷新监听
        //首次自动加载数据
        new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                collection_list_view.setRefreshing();
                return true;
            }
        }).sendEmptyMessageDelayed(0,200);
    }

    //根据是否登陆显示不同的界面
    private void showCollectView(PullToRefreshBase<ListView> refreshView){
        //final List<String> prodouctIdList = new ArrayList<>();
        Gson gson = new GsonBuilder().create();
        User user = gson.fromJson(ToolKits.fetchString(this,LoginActivity.LOGIN_USER),User.class);
        if(user != null){
            collection_list_view.onRefreshComplete();//取消转圈，恢复正常状态
            show_list_view.setVisibility(View.VISIBLE);
            should_login.setVisibility(View.GONE);
            //loadDatas(refreshView.getScrollY()<0);//滚动条y值小于0时：下拉操作
            getProdouctIdList(user);//获取用户的收藏商品ID数组 userId=20 -> ["848659","7937680","7948641"]



        }else{
            collection_list_view.onRefreshComplete();//取消转圈，恢复正常状态
            show_list_view.setVisibility(View.GONE);
            should_login.setVisibility(View.VISIBLE);
        }

    }

    private void getCollection(String prodouctId){
        new HttpUtils().send(HttpRequest.HttpMethod.GET, CONST.COLLECTION_GOOD + "&prodouctId=" + prodouctId, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                JsonReader reader = new JsonReader(new StringReader(responseInfo.result));
                reader.setLenient(true);//没这条会出错，需要将responseInfo.result用JsonReader转换一下设置setLenient(true)
                ResponseObject<Goods> object = new GsonBuilder().create().fromJson(reader,new TypeToken<ResponseObject<Goods>>(){}.getType());
                Log.d(TAG, "onSuccess: "+object.getDatas());
                Log.d(TAG, "onSuccess:1 "+mList);//根据商品ID查询出来商品并添加进mList
                mList.add((Goods) object.getDatas());
                Log.d(TAG, "onSuccess:2 "+mList);
                mGoodsAdapter = new GoodsAdapter(mList);
                collection_list_view.setAdapter(mGoodsAdapter);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(CollectionActivity.this,msg,Toast.LENGTH_LONG).show();
            }
        });

    }

    private void getProdouctIdList(User user){
        //获取用户的收藏商品ID数组
        new HttpUtils().send(HttpRequest.HttpMethod.GET, CONST.PRODOUCTID_LIST + "&userId=" + user.getId(), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new GsonBuilder().create();
                ResponseObject<List<String>> object =new GsonBuilder().create().fromJson(responseInfo.result,new TypeToken<ResponseObject<List<String>>>(){}.getType());
                if(object.getState() == 1){
                    ToolKits.putString(CollectionActivity.this,COLLECT_LIST,gson.toJson(object.getDatas()));
                    Log.d(TAG, "onSuccess: "+gson.toJson(object.getDatas()));
                    Toast.makeText(CollectionActivity.this,"获取商品ID列表成功！",Toast.LENGTH_LONG).show();
                    List<String> prodouctIdList = (List<String>) object.getDatas();
                    for (int i=0;i<prodouctIdList.size();i++){
                        getCollection(prodouctIdList.get(i));
                    }

                }else {
                    Toast.makeText(CollectionActivity.this,object.getMsg(),Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(CollectionActivity.this,msg,Toast.LENGTH_LONG).show();
            }
        });
    }

    //如果是true表示从上往下拉刷新，如果false从下往上拉加载更多
    private void loadDatas(final boolean direction){
        final RequestParams params = new RequestParams();
        if(direction){
            page = 1;
        }else {
            page++;
        }
        params.addQueryStringParameter("page",String.valueOf(page));
        params.addQueryStringParameter("size",String.valueOf(size));
        new HttpUtils().send(HttpRequest.HttpMethod.GET, CONST.GOODS_LIST, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.d(TAG, "onSuccess: "+params);
                collection_list_view.onRefreshComplete();//取消转圈，恢复正常状态
                //转换成json格式
                JsonReader reader = new JsonReader(new StringReader(responseInfo.result));
                reader.setLenient(true);//没这条会出错，需要将responseInfo.result用JsonReader转换一下设置setLenient(true)
                ResponseObject<List<Goods>> object = new GsonBuilder().create().fromJson(reader,new TypeToken<ResponseObject<List<Goods>>>(){}.getType());
                page = object.getPage();
                size = object.getSize();
                count = object.getCount();

                //将数据渲染到listview
                if(direction){//下拉刷新
                    mList = (List<Goods>) object.getDatas();
                    mGoodsAdapter = new GoodsAdapter(mList);
                    collection_list_view.setAdapter(mGoodsAdapter);

                }else {//加载更多
                    mList.addAll((List<Goods>) object.getDatas());
                    mGoodsAdapter.notifyDataSetChanged();//更新已有数据
                }

                if(count == page){//告诉listview下部没有更多数据可以加载（count == page：最后一页时）
                    collection_list_view.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                collection_list_view.onRefreshComplete();//取消转圈，恢复正常状态
                Toast.makeText(CollectionActivity.this,msg,Toast.LENGTH_LONG).show();
            }
        });
    }

    @OnClick({R.id.collection_back})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.collection_back:
                finish();
                break;
        }
    }

    /**
     * 通过意图把当前选中的商品信息传递到下个页面当中去
     * 传递的时候需要传递可序列化的对象（object）
     */
    @OnItemClick(R.id.collection_list_view)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        Intent intent =  new Intent(this,GoodsDetailsActivity.class);//创建意图
        intent.putExtra("goods",mGoodsAdapter.getItem(position-1));
        startActivity(intent);

    }

}
