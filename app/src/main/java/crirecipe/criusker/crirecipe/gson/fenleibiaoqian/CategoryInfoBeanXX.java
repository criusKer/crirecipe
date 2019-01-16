package crirecipe.criusker.crirecipe.gson.fenleibiaoqian;

import org.litepal.crud.LitePalSupport;

/**
 * Create by 李菀直 on 2018/12/31.
 */
public class CategoryInfoBeanXX extends LitePalSupport {
        /**
         * ctgId : 0010001007
         * name : 荤菜
         * parentId : 0010001002
         */

        private String ctgId;
        private String name;
        private String parentId;

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

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }
}
