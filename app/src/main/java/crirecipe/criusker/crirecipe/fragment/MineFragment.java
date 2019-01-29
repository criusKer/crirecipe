package crirecipe.criusker.crirecipe.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import crirecipe.criusker.crirecipe.R;
import crirecipe.criusker.crirecipe.Util.ToolKits;
import crirecipe.criusker.crirecipe.activity.AccountActivity;
import crirecipe.criusker.crirecipe.activity.CollectionActivity;
import crirecipe.criusker.crirecipe.activity.LoginActivity;
import crirecipe.criusker.crirecipe.entity.User;

/**
 * Create by 李菀直 on 2019/1/10.
 */
public class MineFragment extends Fragment {

    private static final String TAG = "MineFragment";

    @ViewInject(R.id.to_login_group)
    private View to_login_group;
    @ViewInject(R.id.user_info_group)
    private View user_info_group;
    @ViewInject(R.id.tv_name)
    private TextView tv_name;
    @ViewInject(R.id.user_icon)
    private ImageView user_icon;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.frag_mine,null);
        ViewUtils.inject(this,view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Gson gson = new GsonBuilder().create();
        User user = gson.fromJson(ToolKits.fetchString(getActivity(),LoginActivity.LOGIN_USER),User.class);
        if(user != null){
            to_login_group.setVisibility(View.GONE);
            user_info_group.setVisibility(View.VISIBLE);
            tv_name.setText(user.getName());
            Log.d(TAG, "onResume: "+user.getId());
        }else {
            user_info_group.setVisibility(View.GONE);
            to_login_group.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if(this.getView()!=null){
            this.getView().setVisibility(menuVisible?View.VISIBLE:View.GONE);
        }
    }
    @OnClick({R.id.bt_login,R.id.user_info_group,R.id.btn_collection})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.bt_login:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            case R.id.user_info_group:
                startActivity(new Intent(getActivity(), AccountActivity.class));
                break;
            case R.id.btn_collection:
                startActivity(new Intent(getActivity(), CollectionActivity.class));
                break;
        }

    }
}
