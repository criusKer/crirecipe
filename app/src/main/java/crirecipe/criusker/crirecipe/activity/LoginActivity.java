package crirecipe.criusker.crirecipe.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
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
import com.lidroid.xutils.view.annotation.event.OnRadioGroupCheckedChange;
import com.mob.MobSDK;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.weibo.TencentWeibo;
import crirecipe.criusker.crirecipe.R;
import crirecipe.criusker.crirecipe.Util.CONST;
import crirecipe.criusker.crirecipe.Util.ToolKits;
import crirecipe.criusker.crirecipe.entity.ResponseObject;
import crirecipe.criusker.crirecipe.entity.User;

public class LoginActivity extends AppCompatActivity implements PlatformActionListener {

    private static final String TAG = "LoginActivity";
    public static final String LOGIN_USER = "login_user";

    @ViewInject(R.id.fly_view)
    private View fly_view;
    @ViewInject(R.id.user_name)
    private EditText user_name;
    @ViewInject(R.id.pwd)
    private EditText pwd;
    @ViewInject(R.id.vertify_button)
    private Button vertify_button;
    @ViewInject(R.id.login_button)
    private Button login_button;
    private Animation move_to_left,move_to_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        MobSDK.init(this);
        ViewUtils.inject(this);
        move_to_left = AnimationUtils.loadAnimation(this,R.anim.move_to_left);
        move_to_right= AnimationUtils.loadAnimation(this,R.anim.move_to_right);
        user_name.addTextChangedListener(mTextWatcher);
        pwd.addTextChangedListener(mTextWatcher);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.tv_register,R.id.login_button,R.id.login_back,R.id.sina_weibo,R.id.tencet_weibo,R.id.qq_accout})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.login_back:
                finish();
                break;
            case R.id.tv_register:
                startActivity(new Intent(this,RegisterActivity.class));
                break;
            case R.id.sina_weibo: {
                Platform platform = ShareSDK.getPlatform(SinaWeibo.NAME);
                platform.removeAccount(true); //移除授权状态和本地缓存，下次授权会重新授权
                platform.SSOSetting(false); //SSO授权，传false默认是客户端授权，没有客户端授权或者不支持客户端授权会跳web授权
                platform.setPlatformActionListener(this);//授权回调监听，监听oncomplete，onerror，oncancel三种状态
                if(platform.isClientValid()){
                    //判断是否存在授权凭条的客户端，true是有客户端，false是无
                }
                if (platform.isAuthValid()) {
                    String openId = platform.getDb().getUserId();
                    String nikeName = platform.getDb().getUserName();
                    String nikeIcon = platform.getDb().getUserIcon();
                    SocialLogin(nikeName,openId,nikeIcon);
                    return;
                }else {
                    platform.showUser(null);//弹出授权登录窗口
                }
            }
            break;
            case R.id.tencet_weibo:{
                Platform platform = ShareSDK.getPlatform(TencentWeibo.NAME);
                platform.removeAccount(true); //移除授权状态和本地缓存，下次授权会重新授权
                platform.SSOSetting(false); //SSO授权，传false默认是客户端授权，没有客户端授权或者不支持客户端授权会跳web授权
                platform.setPlatformActionListener(this);//授权回调监听，监听oncomplete，onerror，oncancel三种状态
                if(platform.isClientValid()){
                    //判断是否存在授权凭条的客户端，true是有客户端，false是无
                }
                if (platform.isAuthValid()) {
                    String openId = platform.getDb().getUserId();
                    String nikeName = platform.getDb().getUserName();
                    String nikeIcon = platform.getDb().getUserIcon();
                    SocialLogin(nikeName,openId,nikeIcon);
                    return;
                }else {
                    platform.showUser(null);//弹出授权登录窗口
                }
            }
            break;
            case R.id.qq_accout:{
                Platform platform = ShareSDK.getPlatform(QQ.NAME);
                platform.removeAccount(true); //移除授权状态和本地缓存，下次授权会重新授权
                platform.SSOSetting(false); //SSO授权，传false默认是客户端授权，没有客户端授权或者不支持客户端授权会跳web授权
                platform.setPlatformActionListener(this);//授权回调监听，监听oncomplete，onerror，oncancel三种状态
                if(platform.isClientValid()){
                    //判断是否存在授权凭条的客户端，true是有客户端，false是无
                }
                if (platform.isAuthValid()) {
                    String openId = platform.getDb().getUserId();
                    String nikeName = platform.getDb().getUserName();
                    String nikeIcon = platform.getDb().getUserIcon();
                    SocialLogin(nikeName,openId,nikeIcon);
                    return;
                }else {
                    platform.showUser(null);//弹出授权登录窗口
                }
            }
            break;
            case R.id.login_button:
                String userName = user_name.getText().toString();
                String password = pwd.getText().toString();
                new HttpUtils().send(HttpRequest.HttpMethod.GET, CONST.USER_LOGIN + "&username="
                        + userName.trim() + "&password=" + password.trim(), new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        Gson gson = new GsonBuilder().create();
                        ResponseObject<User> object = gson.fromJson(responseInfo.result,new TypeToken<ResponseObject<User>>(){}.getType());
                        if(object.getState() == 1){
                            ToolKits.putString(LoginActivity.this,LOGIN_USER,gson.toJson(object.getDatas()));
                            Log.d(TAG, "onSuccess: "+gson.toJson(object.getDatas()));
                            Toast.makeText(LoginActivity.this,"登录成功!",Toast.LENGTH_LONG).show();
                            finish();
                        }else {
                            Toast.makeText(LoginActivity.this,object.getMsg(),Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Toast.makeText(LoginActivity.this,"登录失败请重试!",Toast.LENGTH_LONG).show();
                    }
                });
                break;
        }

    }


    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(user_name.getText().toString().trim().length()>0 &&
                    pwd.getText().toString().trim().length()>0){
                login_button.setEnabled(true);
            }else {
                login_button.setEnabled(false);
            }
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void afterTextChanged(Editable s) {}
    };


    @OnRadioGroupCheckedChange({R.id.rg_login})
    public void onCheckedChanged(RadioGroup group, int checkedId){
        switch (checkedId){
            case R.id.rb_account_login:
                fly_view.startAnimation(move_to_left);
                vertify_button.setVisibility(View.GONE);
                user_name.setHint("用户名/邮箱/手机号");
                pwd.setHint("请输入密码");
                break;
            case R.id.rb_quick_login:
                fly_view.startAnimation(move_to_right);
                vertify_button.setVisibility(View.VISIBLE);
                user_name.setHint("请输入手机号");
                pwd.setHint("请输入验证码");
                break;
        }
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        SocialLogin(platform.getDb().getUserName(),platform.getDb().getUserId(),platform.getDb().getUserIcon() );//获得第三方平台显示的名字和uid
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        Toast.makeText(this,platform.getName()+"授权失败，请重试！",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCancel(Platform platform, int i) {
        Toast.makeText(this,platform.getName()+"授权已取消！",Toast.LENGTH_LONG).show();
    }
    //第三方登录处理方法
    private void SocialLogin(String nickname,String uid,String nikeIcon){
        System.err.println(nickname+"<-------------->"+uid+"---------------"+nikeIcon+"pppppp");
        new HttpUtils().send(HttpRequest.HttpMethod.GET, CONST.USER_SOCIAL + "&username="
                + nickname.trim() + "&password=" + uid.trim(), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new GsonBuilder().create();
                ResponseObject<User> object = gson.fromJson(responseInfo.result,new TypeToken<ResponseObject<User>>(){}.getType());
                if(object.getState() == 1){
                    ToolKits.putString(LoginActivity.this,LOGIN_USER,gson.toJson(object.getDatas()));
                    Toast.makeText(LoginActivity.this,"登录成功!",Toast.LENGTH_LONG).show();
                    finish();
                }else {
                    Toast.makeText(LoginActivity.this,object.getMsg(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(LoginActivity.this,"登录失败请重试!",Toast.LENGTH_LONG).show();
            }
        });
    }
}
