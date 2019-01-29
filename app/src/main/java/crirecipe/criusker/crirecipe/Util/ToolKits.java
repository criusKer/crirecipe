package crirecipe.criusker.crirecipe.Util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Create by 李菀直 on 2018/12/3.
 */
public class ToolKits {
    public static SharedPreferences getSharedPreferences(Context context){
        return context.getSharedPreferences("com.example.spapp",Context.MODE_PRIVATE);
    }

    public static void putBooble(Context context,String key,boolean value){
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key,value);
        editor.commit();
    }

    public static boolean fetchBooble(Context context,String key,boolean defaultValue){
        return getSharedPreferences(context).getBoolean(key,defaultValue);
    }

    public static void putString(Context context,String key,String value){
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public static String fetchString(Context context, String key){
        return getSharedPreferences(context).getString(key,null);
    }

    public static void clearShare(Context context){
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();//清空所有存到共享参数当中的数据
        editor.commit();
    }

}
