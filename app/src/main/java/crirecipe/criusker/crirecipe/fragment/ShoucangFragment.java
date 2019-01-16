package crirecipe.criusker.crirecipe.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import crirecipe.criusker.crirecipe.R;

/**
 * Create by 李菀直 on 2019/1/10.
 */
public class ShoucangFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_shoucang,container,false);
        return view;
    }
}
