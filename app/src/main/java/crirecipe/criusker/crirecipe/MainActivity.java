package crirecipe.criusker.crirecipe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;

import crirecipe.criusker.crirecipe.basic.BaseActivity;
import crirecipe.criusker.crirecipe.fragment.HomeFragment;
import crirecipe.criusker.crirecipe.fragment.MineFragment;
import crirecipe.criusker.crirecipe.fragment.QuanziFragment;
import crirecipe.criusker.crirecipe.fragment.ShoucangFragment;

/**
 * 需要动态添加fragment
 * 总部！
 * 已有页面：
 *  1.HomeFragment ：首页
 *  2.ShoucangFragment ：商品列表页面
 *  3.MineFragment ：我的
 *  4.GoodsDetailsActivity ：商品详情页
 *  5.CollectionActivity ：收藏列表界面
 *  6.AccountActivity ：账户设置页面
 *  7.LoginActivity ：登录页面
 *  8.RegisterActivity ：注册页面
 *  9.UpdateTelActivity ：修改手机号页面
 *  还需完成的功能：
 *   1.用户与用户之间发送消息的功能
 *    -思路:①消息记录可用本地存储的方式
 *   2.订单功能（包含支付）
 *   3.类似朋友圈的分享功能（可评论）
 *   4.界面丑且不统一：时间跨度大 不同时间风格不同 且没时间去修改之前的界面 视情况而定
 */

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout homeLayout = findViewById(R.id.home_layout);
        LinearLayout shoucangLayout = findViewById(R.id.shoucang_layout);
        LinearLayout quanziLayout = findViewById(R.id.quanzi_layout);
        LinearLayout mineLayout = findViewById(R.id.mine_layout);
        homeLayout.setOnClickListener(this);
        shoucangLayout.setOnClickListener(this);
        quanziLayout.setOnClickListener(this);
        mineLayout.setOnClickListener(this);
        replaceFragment(new HomeFragment());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_layout:
                replaceFragment(new HomeFragment());
                break;
            case R.id.shoucang_layout:
                replaceFragment(new ShoucangFragment());
                break;
            case R.id.quanzi_layout:
                replaceFragment(new QuanziFragment());
                break;
            case R.id.mine_layout:
                replaceFragment(new MineFragment());
                break;
        }
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frag_choose,fragment);
        transaction.commit();
    }
}
