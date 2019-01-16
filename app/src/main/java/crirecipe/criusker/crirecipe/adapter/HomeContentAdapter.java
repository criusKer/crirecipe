package crirecipe.criusker.crirecipe.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import crirecipe.criusker.crirecipe.R;
import crirecipe.criusker.crirecipe.activity.RecipeDetailsActivity;
import crirecipe.criusker.crirecipe.gson.biaoqiancaipu.ListBean;
import crirecipe.criusker.crirecipe.gson.biaoqiancaipu.RecipeBean;

/**
 * Create by 李菀直 on 2018/12/31.
 */
public class HomeContentAdapter extends RecyclerView.Adapter<HomeContentAdapter.ViewHolder> {

    private Context mContext;

    private static final String TAG = "HomeContentAdapter";

    private List<ListBean> listBeanList;
    private List<RecipeBean> recipeBeanList;
    private View view;


    //控件初始化
    static class ViewHolder extends  RecyclerView.ViewHolder{
        LinearLayout contentLayout;
        TextView homeRecipeName;
        TextView homeRecipeMaterial;
        ImageView imageView;
        ViewHolder(View view) {
            super(view);
            contentLayout = view.findViewById(R.id.content_layout);
            homeRecipeName = view.findViewById(R.id.home_recipe_name);
            homeRecipeMaterial = view.findViewById(R.id.home_recipe_material);
            imageView = view.findViewById(R.id.home_recipe_image);
        }
    }

    public HomeContentAdapter(List<ListBean> listBeanList,List<RecipeBean> recipeBeanList){
        this.listBeanList = listBeanList;
        this.recipeBeanList = recipeBeanList;
    }


    //界面加载
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        view = LayoutInflater.from(mContext).inflate(R.layout.content_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.contentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                ListBean listBean = listBeanList.get(position);
                RecipeBean recipeBean = recipeBeanList.get(position);
                Intent intent = new Intent(mContext, RecipeDetailsActivity.class);
                intent.putExtra(RecipeDetailsActivity.RECIPE_NAME,listBean.getName());
                if (recipeBean.getMethod() != null) {
                    intent.putExtra(RecipeDetailsActivity.RECIPE_STEP, recipeBean.getMethod());
                }else {
                    intent.putExtra(RecipeDetailsActivity.RECIPE_STEP,"[{\"img\":\"NONE\",\"step\":\"NONE\"}]");
                }
                if (recipeBean.getImg() != null){
                    intent.putExtra(RecipeDetailsActivity.RECIPE_IMG,recipeBean.getImg());
                }else {
                    intent.putExtra(RecipeDetailsActivity.RECIPE_IMG,"https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2451649211,3295736033&fm=26&gp=0.jpg");
                }

                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    //数据绑定
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: "+"listBeanList.size: "+listBeanList.size());
        Log.d(TAG, "onBindViewHolder: "+"recipeBeanList.size: "+recipeBeanList.size());
        Log.d(TAG, "onBindViewHolder: "+position);
        ListBean listBean = listBeanList.get(position);
        RecipeBean recipeBean = recipeBeanList.get(position);
        holder.homeRecipeName.setText(listBean.getName());
        if (recipeBean.getIngredients() != null && !"".equals(recipeBean.getIngredients())){
            String str = recipeBean.getIngredients().substring(2);//取字符串中的 下标为2开始到最后 的字符串
            String newStr = str.replaceAll("\"","").replace("]","").replace("(","");//取出字符串中的引号
            holder.homeRecipeMaterial.setText(newStr);
        }else {
            holder.homeRecipeMaterial.setText("暂无数据");
        }
        Glide.with(view).load(listBean.getThumbnail()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return listBeanList.size();
    }
}
