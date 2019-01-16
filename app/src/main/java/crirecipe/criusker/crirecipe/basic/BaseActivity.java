package crirecipe.criusker.crirecipe.basic;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Create by 李菀直 on 2019/1/2.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //使图片融入状态栏
        if(Build.VERSION.SDK_INT >= 21){//需要安卓5.0及以上才支持
            /**
             * 获取DecorView：activity窗口的根视图
             * setSystemUiVisibility:改变系统UI的显示
             * View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE：活动的布局会显示在状态栏上面
             * getWindow().setStatusBarColor(Color.TRANSPARENT:将状态栏改成透明色
             */
            View decorView = getWindow().getDecorView();
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
}
