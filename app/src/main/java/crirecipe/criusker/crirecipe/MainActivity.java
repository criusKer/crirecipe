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
