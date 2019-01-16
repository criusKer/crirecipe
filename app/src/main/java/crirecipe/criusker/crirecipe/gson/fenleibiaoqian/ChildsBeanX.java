package crirecipe.criusker.crirecipe.gson.fenleibiaoqian;

import org.litepal.crud.LitePalSupport;

import java.util.List;

/**
 * Create by 李菀直 on 2018/12/31.
 */
public class ChildsBeanX extends LitePalSupport{
        /**
         * categoryInfo : {"ctgId":"0010001002","name":"按菜品选择菜谱","parentId":"0010001001"}
         * childs : [{"categoryInfo":{"ctgId":"0010001007","name":"荤菜","parentId":"0010001002"}},{"categoryInfo":{"ctgId":"0010001008","name":"素菜","parentId":"0010001002"}},{"categoryInfo":{"ctgId":"0010001009","name":"汤粥","parentId":"0010001002"}},{"categoryInfo":{"ctgId":"0010001010","name":"西点","parentId":"0010001002"}},{"categoryInfo":{"ctgId":"0010001011","name":"主食","parentId":"0010001002"}},{"categoryInfo":{"ctgId":"0010001012","name":"饮品","parentId":"0010001002"}},{"categoryInfo":{"ctgId":"0010001013","name":"便当","parentId":"0010001002"}},{"categoryInfo":{"ctgId":"0010001014","name":"小吃","parentId":"0010001002"}}]
         */

        private CategoryInfoBeanX categoryInfo;
        private List<ChildsBean> childs;

        public CategoryInfoBeanX getCategoryInfo() {
            return categoryInfo;
        }

        public void setCategoryInfo(CategoryInfoBeanX categoryInfo) {
            this.categoryInfo = categoryInfo;
        }

        public List<ChildsBean> getChilds() {
            return childs;
        }

        public void setChilds(List<ChildsBean> childs) {
            this.childs = childs;
        }


}
