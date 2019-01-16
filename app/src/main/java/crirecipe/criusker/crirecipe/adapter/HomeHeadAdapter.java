package crirecipe.criusker.crirecipe.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

import crirecipe.criusker.crirecipe.R;
import crirecipe.criusker.crirecipe.gson.fenleibiaoqian.CategoryInfoBeanXX;

/**
 * Create by 李菀直 on 2018/12/31.
 */
public class HomeHeadAdapter extends RecyclerView.Adapter<HomeHeadAdapter.ViewHolder> {

    private static final String TAG = "HomeHeadAdapter";

    private List<CategoryInfoBeanXX> categoryInfoBeanXXList;

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }

    //控件初始化
    static class ViewHolder extends  RecyclerView.ViewHolder{
        TextView textView;
        ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.head_text);
        }
    }

    public HomeHeadAdapter(List<CategoryInfoBeanXX> categoryInfoBeanXXList){
        this.categoryInfoBeanXXList = categoryInfoBeanXXList;
        }


    //界面加载
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.head_item,parent,false);
        return new ViewHolder(view);
    }

    //数据绑定
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        CategoryInfoBeanXX categoryInfoBeanXX = categoryInfoBeanXXList.get(position);
        holder.textView.setText(categoryInfoBeanXX.getName());
        Log.d(TAG, "onBindViewHolder: "+position);

        //判断是否设置了监听器
        if(mOnItemClickListener != null){
            //为ItemView设置监听器
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition(); // 1
                    //holder.textView.setTextColor(Color.parseColor("#00a0e9"));
                    notifyDataSetChanged();
                    mOnItemClickListener.onItemClick(holder.textView,position); // 2
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return categoryInfoBeanXXList.size();
    }

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

}
