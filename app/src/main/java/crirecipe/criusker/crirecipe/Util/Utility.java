package crirecipe.criusker.crirecipe.Util;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import crirecipe.criusker.crirecipe.gson.biaoqiancaipu.BQCaipuBasic;
import crirecipe.criusker.crirecipe.gson.biaoqiancaipu.ListBean;
import crirecipe.criusker.crirecipe.gson.biaoqiancaipu.RecipeBean;
import crirecipe.criusker.crirecipe.gson.fenleibiaoqian.CategoryInfoBeanX;
import crirecipe.criusker.crirecipe.gson.fenleibiaoqian.CategoryInfoBeanXX;
import crirecipe.criusker.crirecipe.gson.fenleibiaoqian.ChildsBean;
import crirecipe.criusker.crirecipe.gson.fenleibiaoqian.FenleiBasic;
import okhttp3.Response;

/**
 * Create by 李菀直 on 2018/12/31.
 */
public class Utility {

    /**
     * 解析和处理服务器返回的分类标签数据
     */
    public static boolean handleFLBiaoQianResponse(Response response){
        try {
            Gson gson = new Gson();
            String responseData = response.body().string();
            FenleiBasic fenleiBasic = gson.fromJson(responseData,FenleiBasic.class);
            fenleiBasic.save();
            for (int i=0;i<fenleiBasic.getResult().getChilds().size();i++){
                CategoryInfoBeanX categoryInfoBeanX = fenleiBasic.getResult().getChilds().get(i).getCategoryInfo();
                categoryInfoBeanX.save();

                List<ChildsBean> childsBean = fenleiBasic.getResult().getChilds().get(i).getChilds();
                for (int j =0;j<childsBean.size();j++){
                    CategoryInfoBeanXX categoryInfoBeanXX = childsBean.get(j).getCategoryInfo();
                    categoryInfoBeanXX.save();
                }
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 解析和处理服务器返回的按标签查询菜谱数据
     */

    public static boolean handleBQCaipuResponse(Response response){
        try {
            Gson gson = new Gson();
            String responseData = response.body().string();
            BQCaipuBasic bqCaipuBasic = gson.fromJson(responseData,BQCaipuBasic.class);
            bqCaipuBasic.save();
            for (int i=0;i<bqCaipuBasic.getResult().getList().size();i++){

                ListBean listBean = new ListBean();
                listBean.setName(bqCaipuBasic.getResult().getList().get(i).getName());
                listBean.setThumbnail(bqCaipuBasic.getResult().getList().get(i).getThumbnail());
                listBean.setCtgIds(bqCaipuBasic.getResult().getList().get(i).getCtgIds());
                listBean.save();
                if (bqCaipuBasic.getResult().getList().get(i).getRecipe() != null){
                    RecipeBean recipeBean = bqCaipuBasic.getResult().getList().get(i).getRecipe();
                    recipeBean.save();
                }

            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
