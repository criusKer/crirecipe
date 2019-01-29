package crirecipe.criusker.crirecipe.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.picasso.Picasso;

import crirecipe.criusker.crirecipe.R;
import crirecipe.criusker.crirecipe.Util.CONST;
import crirecipe.criusker.crirecipe.Util.ToolKits;
import crirecipe.criusker.crirecipe.entity.Favorite;
import crirecipe.criusker.crirecipe.entity.Goods;
import crirecipe.criusker.crirecipe.entity.ResponseObject;
import crirecipe.criusker.crirecipe.entity.Shop;
import crirecipe.criusker.crirecipe.entity.User;

/**
 * 商品详情页
 *  呈现的效果和应有的功能：
 *   1.商品详情的展示√
 *   2.点击号码图案 自动跳转手机的拨号界面 并将商家的电话号码自动输好√
 *   3.点击收藏图标 可根据具体情况进行收藏和取消收藏操作√
 *   4.点击立即购买应跳转购买的界面
 *  目前存在的问题和未完善的功能：
 *   1.收藏的状态没有实时同步 △（该问题归商品列表页 不归我管哦 QAQ）
 *   2.收藏按钮的图案不会改变 √（小case）
 *   3.需要在表中添加collected状态 来记录该商品是否被收藏 ×（该方法没有考虑多用户 每个用户都有不同的状态 不可行）
 *     ----正确方法：通过查询是否有数据来判断是否收藏 √（已实现）
 */

public class GoodsDetailsActivity extends AppCompatActivity {

    private static final String TAG = "GoodsDetailsActivity";

    public static final String FAVORITE_COLLECTION = "favorite_collection";

    private Goods goods;

    private boolean collected;

    @ViewInject(R.id.goods_detail_favorite)
    private ImageView goods_detail_favorite;

    @ViewInject(R.id.goods_image)
    private ImageView goods_image;
    @ViewInject(R.id.goods_title)
    private TextView goods_title;
    @ViewInject(R.id.goods_desc)
    private TextView goods_desc;
    @ViewInject(R.id.goods_price)
    private TextView goods_price;
    @ViewInject(R.id.goods_old_price)
    private TextView goods_old_price;

    @ViewInject(R.id.shop_title)
    private TextView shop_title;
    @ViewInject(R.id.shop_phone)
    private TextView shop_phone;
    @ViewInject(R.id.shop_address)
    private TextView shop_address;

    @ViewInject(R.id.tv_more_details_web_view)
    private WebView tv_more_details_web_view;
    @ViewInject(R.id.wv_gn_warm_prompt)
    private WebView wv_gn_warm_prompt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_details);
        ViewUtils.inject(this);
        Bundle bundle = getIntent().getExtras();//接收意图
        if(bundle != null){
            goods = (Goods)bundle.get("goods");
        }
        if(goods != null){//开始渲染数据到页面上
            //collected = goods.isCollected();//给collected赋初值
            //加载商品标题图片
            Picasso.with(this).load(goods.getImgUrl()).placeholder(R.drawable.default_pic).into(goods_image);
            //开始渲染商品信息
            goods_title.setText(goods.getSortTitle());
            goods_desc.setText(goods.getTitle());
            Log.d(TAG, "onCreate: price:"+goods.getPrice()+" oldPrice:"+goods.getValue()+goods.isCollected());
            goods_price.setText("￥"+goods.getPrice());
            goods_old_price.setText("￥"+goods.getValue());
            goods_old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);//在价格上加一条横线
            //开始商家信息渲染
            Shop shop = goods.getShop();
            shop_title.setText(shop.getName());
            shop_phone.setText(shop.getTel());
            shop_address.setText(shop.getAddress());
            //根据是否收藏显示不同样式
            //开始渲染本单详情和温馨提示

            //设置webview内容的宽度自适应屏幕的宽度
            WebSettings webSettings1 = tv_more_details_web_view.getSettings();
            webSettings1.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            WebSettings webSettings2 = wv_gn_warm_prompt.getSettings();
            webSettings2.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

            String[] data =  htmlSub(goods.getDetail());
            tv_more_details_web_view.loadDataWithBaseURL("",data[1],"text/html","utf-8","");
            wv_gn_warm_prompt.loadDataWithBaseURL("",data[0],"text/html","utf-8","");
        }
        //初始化收藏状态
        Log.d(TAG, "onCreate: "+collected);
        initCollectState();
        Log.d(TAG, "onCreate: "+collected);
    }
    //从大段的商品信息描述中截取本单详情和温馨提示等信息
    public String[] htmlSub(String html){
        char[] str = html.toCharArray();
        int len = str.length;
        System.out.println(len);
        int n = 0;
        String[] data = new String[3];
        int oneindex = 0;
        int secindex = 1;
        int thrindex = 2;
        for(int i=0;i<len;i++){
            if(str[i] == '【'){
                n++;
                if(n == 1)oneindex = i;
                if(n == 2)secindex = i;
                if(n == 3)thrindex = i;
            }
        }
        if(oneindex >0&&secindex>1&&thrindex>2){
            data[0] = html.substring(oneindex,secindex);
            data[1] = html.substring(secindex,thrindex);
            data[2] = html.substring(thrindex,html.length()-6);
        }
        return data;

    }
    //初始化收藏状态
    private void initCollectState(){
        Gson gson = new GsonBuilder().create();
        User user = gson.fromJson(ToolKits.fetchString(this,LoginActivity.LOGIN_USER),User.class);
        if(user != null){
            //收藏-以userId和prodouctId查询单个商品
            new HttpUtils().send(HttpRequest.HttpMethod.GET, CONST.FAVORITE_GETONECOLLECTON+"&userId="
                    +user.getId()+"&prodouctId="+goods.getId(), new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    ResponseObject<Favorite> object = new GsonBuilder().create().fromJson(responseInfo.result,new TypeToken<ResponseObject<Favorite>>(){}.getType());
                    if (object.getState() == 1){
                        Log.d(TAG, "onSuccess: 有数据");
                        collected = true;
                        goods_detail_favorite.setImageResource(R.drawable.ic_collection_yes);
                    }else {
                        Log.d(TAG, "onSuccess: 没有数据");
                        collected = false;
                        goods_detail_favorite.setImageResource(R.drawable.ic_collection);
                    }
                }
                @Override
                public void onFailure(HttpException error, String msg) {
                    Toast.makeText(GoodsDetailsActivity.this,msg,Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    @OnClick({R.id.shop_call,R.id.goods_detail_back,R.id.goods_detail_favorite,R.id.btn_buy_now})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.goods_detail_back://返回
                finish();
                break;
            case R.id.shop_call://呼叫商家
                Intent callin = new Intent(Intent.ACTION_DIAL);//调用系统的拨号界面 通过意图把电话号码传递给拨号界面 ---需添加拨打电话的权限！
                callin.setData(Uri.parse("tel:"+goods.getShop().getTel()));
                startActivity(callin);
                break;
            case R.id.btn_buy_now://点击购买
                Intent intent =  new Intent(GoodsDetailsActivity.this,GoodsDetailsActivity.class);//创建意图
                intent.putExtra("goods",goods);
                startActivity(intent);
                break;
            case R.id.goods_detail_favorite://收藏
                Gson gson = new GsonBuilder().create();
                User user = gson.fromJson(ToolKits.fetchString(this,LoginActivity.LOGIN_USER),User.class);
                if (user != null){
                    Log.d(TAG, "onClick: userId="+user.getId()+" prodouctId="+goods.getId());
                    Log.d(TAG, "onClick: collected："+collected);
                    if (collected){
                        Log.d(TAG, "onClick:已收藏时 collected改前"+collected);
                        collected = false;//改变收藏状态为未收藏
                        Log.d(TAG, "onClick:已收藏时 collected改后"+collected);
                        //取消收藏
                        new HttpUtils().send(HttpRequest.HttpMethod.GET, CONST.CANCEL_COLLECTION+"&userId="
                                +user.getId()+"&prodouctId="+goods.getId(), new RequestCallBack<String>() {
                            @Override
                            public void onSuccess(ResponseInfo<String> responseInfo) {
                                ResponseObject<Favorite> object = new GsonBuilder().create().fromJson(responseInfo.result,new TypeToken<ResponseObject<Favorite>>(){}.getType());
                                if (object.getState() == 1){
                                    goods_detail_favorite.setImageResource(R.drawable.ic_collection);
                                    Toast.makeText(GoodsDetailsActivity.this,"取消收藏成功!",Toast.LENGTH_LONG).show();
                                    Log.d(TAG, "onSuccess: 取消收藏成功");
                                }else {
                                    Toast.makeText(GoodsDetailsActivity.this,object.getMsg(),Toast.LENGTH_LONG).show();
                                }
                            }
                            @Override
                            public void onFailure(HttpException error, String msg) {
                                Toast.makeText(GoodsDetailsActivity.this,msg,Toast.LENGTH_LONG).show();
                            }
                        });
                    }else{
                        Log.d(TAG, "onClick:未收藏时 collected改前"+collected);
                        collected = true;//已收藏
                        Log.d(TAG, "onClick:未收藏时 collected改后"+collected);
                        //收藏
                        new HttpUtils().send(HttpRequest.HttpMethod.GET, CONST.FAVORITE_COLLECTON+"&userId="
                                +user.getId()+"&prodouctId="+goods.getId(), new RequestCallBack<String>() {
                            @Override
                            public void onSuccess(ResponseInfo<String> responseInfo) {
                                ResponseObject<Favorite> object = new GsonBuilder().create().fromJson(responseInfo.result,new TypeToken<ResponseObject<Favorite>>(){}.getType());
                                if (object.getState() == 1){
                                    goods_detail_favorite.setImageResource(R.drawable.ic_collection_yes);
                                    Toast.makeText(GoodsDetailsActivity.this,"收藏成功!",Toast.LENGTH_LONG).show();
                                    Log.d(TAG, "onSuccess: 收藏成功");
                                }else {
                                    Toast.makeText(GoodsDetailsActivity.this,object.getMsg(),Toast.LENGTH_LONG).show();
                                    Log.d(TAG, "onSuccess: 已收藏");
                                }
                            }
                            @Override
                            public void onFailure(HttpException error, String msg) {
                                Toast.makeText(GoodsDetailsActivity.this,msg,Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }else{
                    Toast.makeText(GoodsDetailsActivity.this,"请先登录！",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

}
