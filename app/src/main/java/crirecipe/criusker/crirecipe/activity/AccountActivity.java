package crirecipe.criusker.crirecipe.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

import crirecipe.criusker.crirecipe.R;
import crirecipe.criusker.crirecipe.Util.ToolKits;

public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ViewUtils.inject(this);
    }

    @OnClick({R.id.account_back,R.id.logout_button,R.id.update_tel})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.account_back://返回
                finish();
                break;
            case R.id.logout_button://退出登录
                ToolKits.clearShare(this);
                finish();
                break;
            case R.id.update_tel:
                Intent intent = new Intent(AccountActivity.this,UpdateTelActivity.class);
                startActivity(intent);
                break;

        }
    }
}
