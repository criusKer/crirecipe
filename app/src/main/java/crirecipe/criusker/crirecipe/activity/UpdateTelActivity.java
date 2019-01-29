package crirecipe.criusker.crirecipe.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

import crirecipe.criusker.crirecipe.R;
import crirecipe.criusker.crirecipe.Util.CONST;
import crirecipe.criusker.crirecipe.Util.ToolKits;
import crirecipe.criusker.crirecipe.entity.ResponseObject;
import crirecipe.criusker.crirecipe.entity.User;

public class UpdateTelActivity extends AppCompatActivity {

    private static final String TAG = "UpdateTelActivity";

    public static final String UPDATE_TEL = "update_tel";

    @ViewInject(R.id.et_tel)
    private EditText etTel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_tel);
        ViewUtils.inject(this);
        etTel.setFocusable(false);
        etTel.setFocusableInTouchMode(false);
        etTel.setCursorVisible(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Gson gson = new GsonBuilder().create();
        User user = gson.fromJson(ToolKits.fetchString(this,LoginActivity.LOGIN_USER),User.class);
        if(user != null) {
            etTel.setText(user.getTel());
        }else {
            Log.d(TAG, "onResume:没有数据 ");
        }
    }

    @OnClick({R.id.update,R.id.check_update,R.id.update_back})
    public void onClick(View v){
          switch (v.getId()){
              case R.id.update:
                  etTel.setFocusableInTouchMode(true);
                  etTel.setFocusable(true);
                  etTel.requestFocus();
                  etTel.setCursorVisible(true);
                  etTel.setText("");
                  break;
              case R.id.check_update:
                  if(etTel.getText().toString().trim().length()<=0){
                      etTel.setError("请输入要修改的手机号！");
                      return;
                  }
                  Gson gson = new GsonBuilder().create();
                  User user = gson.fromJson(ToolKits.fetchString(this,LoginActivity.LOGIN_USER),User.class);
                  Log.d(TAG, "onClick: "+user.getId());
                  new HttpUtils().send(HttpRequest.HttpMethod.GET, CONST.USER_UPDATE+"&userId="
                          +user.getId()+"&tel="+etTel.getText().toString().trim(), new RequestCallBack<String>() {
                      @Override
                      public void onSuccess(ResponseInfo<String> responseInfo) {
                          Gson gson = new GsonBuilder().create();
                          ResponseObject<User> object =new GsonBuilder().create().fromJson(responseInfo.result,new TypeToken<ResponseObject<User>>(){}.getType());
                          if(object.getState() == 1){
                              ToolKits.putString(UpdateTelActivity.this,LoginActivity.LOGIN_USER,gson.toJson(object.getDatas()));
                              Toast.makeText(UpdateTelActivity.this,"绑定成功！",Toast.LENGTH_LONG).show();
                              etTel.setFocusable(false);
                              etTel.setFocusableInTouchMode(false);
                              etTel.setCursorVisible(false);
                          }else {
                              Toast.makeText(UpdateTelActivity.this,object.getMsg(),Toast.LENGTH_LONG).show();
                          }
                      }

                      @Override
                      public void onFailure(HttpException error, String msg) {
                          Toast.makeText(UpdateTelActivity.this,msg,Toast.LENGTH_LONG).show();
                      }
                  });
                  break;
              case R.id.update_back:
                  finish();
                  break;
          }
    }
}
