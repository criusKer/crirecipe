package crirecipe.criusker.crirecipe.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

import crirecipe.criusker.crirecipe.R;
import crirecipe.criusker.crirecipe.Util.CONST;
import crirecipe.criusker.crirecipe.entity.ResponseObject;
import crirecipe.criusker.crirecipe.entity.User;

public class RegisterActivity extends AppCompatActivity {

    @ViewInject(R.id.register_username)
    private EditText username;
    @ViewInject(R.id.register_password)
    private EditText password;
    @ViewInject(R.id.register_repassword)
    private EditText repassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ViewUtils.inject(this);
    }

    @OnClick({R.id.register_button,R.id.register_back})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.register_button:
                if(username.getText().toString().trim().length()<=0){
                    username.setError("用户名不能为空！");
                    return;
                }
                if(password.getText().toString().trim().length()<=0){
                    password.setError("密码不能为空！");
                    return;
                }
                if(!password.getText().toString().equals(repassword.getText().toString())){
                    repassword.setError("两次密码不一致！");
                    return;
                }
                new HttpUtils().send(HttpRequest.HttpMethod.GET, CONST.USER_REGISTER+"&username="
                        +username.getText().toString().trim()+"&password="+password.getText().toString().trim(), new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        ResponseObject<User> object =new GsonBuilder().create().fromJson(responseInfo.result,new TypeToken<ResponseObject<User>>(){}.getType());
                        if(object.getState() == 1){
                            Toast.makeText(RegisterActivity.this,"注册成功！",Toast.LENGTH_LONG).show();
                            finish();
                        }else {
                            Toast.makeText(RegisterActivity.this,object.getMsg(),Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Toast.makeText(RegisterActivity.this,msg,Toast.LENGTH_LONG).show();
                    }
                });
                break;
            case R.id.register_back:
                finish();
                break;
        }

    }

}
