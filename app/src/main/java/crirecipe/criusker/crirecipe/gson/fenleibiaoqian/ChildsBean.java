package crirecipe.criusker.crirecipe.gson.fenleibiaoqian;

import org.litepal.crud.LitePalSupport;

/**
 * Create by 李菀直 on 2018/12/31.
 */
public class ChildsBean extends LitePalSupport{
        /**
         * categoryInfo : {"ctgId":"0010001007","name":"荤菜","parentId":"0010001002"}
         */

        private CategoryInfoBeanXX categoryInfo;

        public CategoryInfoBeanXX getCategoryInfo() {
            return categoryInfo;
        }

        public void setCategoryInfo(CategoryInfoBeanXX categoryInfo) {
            this.categoryInfo = categoryInfo;
        }

}
