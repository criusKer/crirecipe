package crirecipe.criusker.crirecipe.gson.biaoqiancaipu;

import org.litepal.crud.LitePalSupport;

import java.util.List;

/**
 * Create by 李菀直 on 2018/12/31.
 */
public class ListBean extends LitePalSupport{
        /**
         * ctgIds : ["0010001014","0010001017","0010001037"]
         * ctgTitles : 小吃,煎,徽菜
         * menuId : 00100010140000039489
         * name : 徽州饼
         * recipe : {"img":"http://f2.mob.com/null/2015/08/20/1440057833676.jpg","ingredients":"[\"面粉 550克 红枣400克 白糖 150克 芝麻油 250克 猪油 25克\"]","method":"[{\"img\":\"http://f2.mob.com/null/2015/08/20/1440057834223.jpg\",\"step\":\"1.1 选用上等红枣放入盆内，用清水泡涨，洗净。笼蒸1小时左右取出。 2 取出来晾凉。 3 除去皮和核。 4 白糖备好。 5 锅内放入芝麻油和白糖溶化，加入枣泥。 6 用小火炒至稠糊、能挂在锅铲上时，盛起晾凉即成枣泥馅料。 7 盛出来晾凉备用。 8 取250克面粉加150克开水和成烫面。 9 用250克面粉加150克凉水和成冷水面团。反复搓揉上劲，盖上湿布略饧。 10 把烫面和冷水面团合到一起揉匀。 11 盆内加面粉50克、熟猪油拌匀，擦成油酥面。 12 把面擀开，放入油酥面团卷起来。 13 擀成长方形薄片。 14 叠成三折，为了使面更均匀。这个过程重复三次。 15 最后擀成薄片。 16 从上向下卷起来。 17 切成均匀的剂子。 18 然后把剂子按扁。 19 擀成中间稍厚的圆皮，放入馅料包起来，收口向下。 20 锅里刷油，将饼坯一面刷上芝麻油，将有油的一面朝下放在锅内。 21 另一而也刷上一层芝麻油，待一面烙至微黄色时，翻身烙另一面。 22 如此反复4次，烙至两面呈平透明状时，即可出锅。 23 出锅摆盘，即可享用。\"}]","sumary":"菜品口味：甜味 主要工艺：煎 主要食材：米面类 所需时间：一小时 制作难度：高级 原创属性：原创 徽州饼是安徽徽州地区的传统点心，原名为枣泥酥馃。光绪年间有一徽州饮食经营者在扬州制作此面饼出售，颇受食者欢迎，故当地入称之\"徽州饼\"。 产品特点：金黄色，扁圆形，外皮透明，香甜味美。 徽州饼的特色--色泽金黄，酥香甜润。非常受人欢迎。","title":"【徽菜】--徽州饼"}
         * thumbnail : http://f2.mob.com/null/2015/08/20/1440057762633.jpg
         */

        private String ctgTitles;
        private String menuId;
        private String name;
        private RecipeBean recipe;
        private String thumbnail;
        private List<String> ctgIds;

        public String getCtgTitles() {
            return ctgTitles;
        }

        public void setCtgTitles(String ctgTitles) {
            this.ctgTitles = ctgTitles;
        }

        public String getMenuId() {
            return menuId;
        }

        public void setMenuId(String menuId) {
            this.menuId = menuId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public RecipeBean getRecipe() {
            return recipe;
        }

        public void setRecipe(RecipeBean recipe) {
            this.recipe = recipe;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public List<String> getCtgIds() {
            return ctgIds;
        }

        public void setCtgIds(List<String> ctgIds) {
            this.ctgIds = ctgIds;
        }


}
