package crirecipe.criusker.crirecipe.Util;

/**
 * Create by 李菀直 on 2018/12/8.
 */
public class CONST {


    public static final String HOST="http://192.168.43.228:8080/spapp-server";
    //public static final String HOST="http://192.168.43.228:8080/spapp-server";

    //城市列表
    public static final String CITY_LIST=HOST+"/servlet/CityServlet";
    //商品列表
    public static final String GOODS_LIST=HOST+"/servlet/GoodsServlet";
    //
    public static final String GOODS_NEARBY=HOST+"/servlet/NearbyServlet";
    //注册
    public static final String USER_REGISTER=HOST+"/servlet/UserServlet?flag=register";
    //登录
    public static final String USER_LOGIN=HOST+"/servlet/UserServlet?flag=login";
    //第三方登录
    public static final String USER_SOCIAL=HOST+"/servlet/UserServlet?flag=social";
    //修改用户信息
    public static final String USER_UPDATE=HOST+"/servlet/UserServlet?flag=update";
    //收藏-将userId和prodouctId存入favorite表
    public static final String FAVORITE_COLLECTON = HOST+"/servlet/FavoriteServlet?flag=collection";
    //收藏-以userId查询prodouctId列表
    public static final String PRODOUCTID_LIST = HOST+"/servlet/FavoriteServlet?flag=collectList";
    //收藏-以userId和prodouctId查询单个商品
    public static final String FAVORITE_GETONECOLLECTON = HOST+"/servlet/FavoriteServlet?flag=getOneCollection";
    //收藏-以prodouctId为参数查询goods表
    public static final String COLLECTION_GOOD = HOST+"/servlet/CollectionServlet?flag=collectedGoods";
    //收藏-取消收藏
    public static final String CANCEL_COLLECTION = HOST+"/servlet/FavoriteServlet?flag=cancel";
    //收藏-改变收藏状态
    public static final String CHANGE_ISCOLLECTED = HOST+"/servlet/CollectionServlet?flag=isCollected";
}
