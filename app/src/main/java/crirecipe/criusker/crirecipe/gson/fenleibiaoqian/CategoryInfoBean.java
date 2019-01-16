package crirecipe.criusker.crirecipe.gson.fenleibiaoqian;

import org.litepal.crud.LitePalSupport;

/**
 * Create by 李菀直 on 2018/12/31.
 */
public class CategoryInfoBean extends LitePalSupport {
        /**
         * ctgId : 0010001001
         * name : 全部菜谱
         */

        private String ctgId;
        private String name;

        public String getCtgId() {
            return ctgId;
        }

        public void setCtgId(String ctgId) {
            this.ctgId = ctgId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
}
