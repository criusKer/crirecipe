package crirecipe.criusker.crirecipe.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import crirecipe.criusker.crirecipe.R;
import crirecipe.criusker.crirecipe.gson.biaoqiancaipu.Method;

/**
 * Create by 李菀直 on 2019/1/4.
 */
public class DetailsAdapter extends ArrayAdapter<Method> {

    private static final String TAG = "DetailsAdapter";

    private int resourseId;

    public DetailsAdapter(@NonNull Context context, int resource, @NonNull List<Method> objects) {
        super(context, resource, objects);
        resourseId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Method method = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourseId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.step_image = view.findViewById(R.id.step_image);
            viewHolder.step_text = view.findViewById(R.id.step_text);
            viewHolder.step_num = view.findViewById(R.id.step_num);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        int stepNum = position+1;
        if ("NONE".equals(method.getImg())){
            viewHolder.step_num.setText("暂无数据！");
            viewHolder.step_image.setVisibility(View.INVISIBLE);
            viewHolder.step_text.setVisibility(View.INVISIBLE);
        }else {
            viewHolder.step_num.setText("步骤 "+stepNum);
            viewHolder.step_text.setText(method.getStep());
            Log.d(TAG, "getView: "+"step: "+method.getStep()+"img: "+method.getImg());
            Glide.with(getContext()).load(method.getImg()).into(viewHolder.step_image);
        }
        return view;
    }
    class ViewHolder{
        ImageView step_image;
        TextView step_text;
        TextView step_num;
    }
}
