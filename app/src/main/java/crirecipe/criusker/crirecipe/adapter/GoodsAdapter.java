package crirecipe.criusker.crirecipe.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.picasso.Picasso;

import java.util.List;

import crirecipe.criusker.crirecipe.R;
import crirecipe.criusker.crirecipe.entity.Goods;


/**
 * Create by 李菀直 on 2018/12/8.
 */
public class GoodsAdapter extends BaseAdapter{

    private List<Goods> mList;

    public GoodsAdapter(List<Goods> list){
        mList = list;
    }

    @Override
    public int getCount() {
        return (mList != null)?mList.size():0;
    }

    @Override
    public Goods getItem(int i) {
        return (mList != null && mList.size()>i)?mList.get(i):null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(view == null){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.goods_list_row,null);
            holder = new ViewHolder();
            ViewUtils.inject(holder,view);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        Goods goods = mList.get(i);
        Picasso.with(viewGroup.getContext()).load(goods.getImgUrl()).placeholder(R.drawable.default_pic).into(holder.photo);
        holder.title.setText(goods.getSortTitle());
        holder.tv_content.setText(goods.getTitle());
        holder.price.setText(String.valueOf("￥"+goods.getPrice()));
        holder.value.setText(String.valueOf("￥"+goods.getValue()));
        holder.value.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);//在价格上加一条横线
        if(goods.isOp()){
            holder.appoitment_img.setVisibility(View.VISIBLE);
        }else {
            holder.appoitment_img.setVisibility(View.GONE);
        }
        System.err.println("imgUrl:"+goods.getImgUrl());
        System.err.println("imgUrl2:"+goods.getImgUrl().toString().trim());
        return view;
    }

    class ViewHolder{
        @ViewInject(R.id.goods_title)
        TextView title;
        @ViewInject(R.id.tv_content)
        TextView tv_content;
        @ViewInject(R.id.price)
        TextView price;
        @ViewInject(R.id.value)
        TextView value;
        @ViewInject(R.id.appoitment_img)
        ImageView appoitment_img;
        @ViewInject(R.id.distance)
        TextView count;
        @ViewInject(R.id.photo)
        ImageView photo;


    }
}
