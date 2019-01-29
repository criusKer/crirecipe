package crirecipe.criusker.crirecipe.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;

import crirecipe.criusker.crirecipe.R;
import crirecipe.criusker.crirecipe.Util.CONST;
import crirecipe.criusker.crirecipe.activity.GoodsDetailsActivity;
import crirecipe.criusker.crirecipe.adapter.GoodsAdapter;
import crirecipe.criusker.crirecipe.entity.Goods;
import crirecipe.criusker.crirecipe.entity.ResponseObject;

/**
 * Create by 李菀直 on 2019/1/10.
 * 商品列表页面
 *  存在的问题：
 *   1.当详情页收藏状态改变时返回该页面 需要后台自动刷新获取最新的数据 以保持收藏状态的同步
 *  可拓展的功能：
 *   1.定位功能 显示与当前定位距离多少M以内的商店 且可以自选定位
 */
public class ShoucangFragment extends Fragment {

    private static final String TAG = "ShoucangFragment";

    @ViewInject(R.id.home_city)
    private TextView home_city;
    @ViewInject(R.id.goods_list_view)
    private PullToRefreshListView goods_list_view;

    private List<Goods> mList;

    private int page=0;//当前页码
    private int size=20;//每页多少条数据
    private int count=0;//总页数

    private GoodsAdapter mGoodsAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_shoucang,container,false);
        ViewUtils.inject(this,view);
        //对listview进行基础设置
        goods_list_view.setMode(PullToRefreshBase.Mode.BOTH);//同时支持上拉加载更多下拉刷新
        goods_list_view.setScrollingWhileRefreshingEnabled(true);//加载数据时不允许滚动操作
        goods_list_view.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                //进行数据加载操作
                loadDatas(refreshView.getScrollY()<0);//滚动条y值小于0时：下拉操作

            }
        });//滚动刷新监听
        //首次自动加载数据
        new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                goods_list_view.setRefreshing();
                return true;
            }
        }).sendEmptyMessageDelayed(0,200);
        return view;
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
                goods_list_view.onRefreshComplete();//取消转圈，恢复正常状态
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
                    goods_list_view.setAdapter(mGoodsAdapter);

                }else {//加载更多
                    mList.addAll((List<Goods>) object.getDatas());
                    mGoodsAdapter.notifyDataSetChanged();//更新已有数据
                }

                if(count == page){//告诉listview下部没有更多数据可以加载（count == page：最后一页时）
                    goods_list_view.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                goods_list_view.onRefreshComplete();//取消转圈，恢复正常状态
                Toast.makeText(getActivity(),msg,Toast.LENGTH_LONG).show();
            }
        });
    }

    @OnClick({R.id.home_city,R.id.home_map,R.id.home_search})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.home_city:
                //startActivity(new Intent(getActivity(),CityActivity.class));
                break;
            case R.id.home_map:
                //startActivity(new Intent(getActivity(), NearbyMapActivity.class));
                break;
            case R.id.home_search:
                break;
        }
    }
    /**
     * 通过意图把当前选中的商品信息传递到下个页面当中去
     * 传递的时候需要传递可序列化的对象（object）
     */
    @OnItemClick(R.id.goods_list_view)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        Log.d(TAG, "onItemClick: "+mGoodsAdapter.getItem(position-1).isCollected());
        Intent intent =  new Intent(getActivity(),GoodsDetailsActivity.class);//创建意图
        intent.putExtra("goods",mGoodsAdapter.getItem(position-1));
        startActivity(intent);

    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if(this.getView()!=null){
            this.getView().setVisibility(menuVisible?View.VISIBLE:View.GONE);
        }
    }
}
