package crirecipe.criusker.crirecipe.activity;

import android.app.UiAutomation;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import crirecipe.criusker.crirecipe.R;
import crirecipe.criusker.crirecipe.adapter.DetailsAdapter;
import crirecipe.criusker.crirecipe.basic.BaseActivity;
import crirecipe.criusker.crirecipe.gson.biaoqiancaipu.Method;
import crirecipe.criusker.crirecipe.ui.MyScrollView;
import crirecipe.criusker.crirecipe.ui.UnScrollListView;

public class RecipeDetailsActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "RecipeDetailsActivity";

    public static final String RECIPE_NAME = "recipe_name";
    public static final String RECIPE_STEP = "recipe_step";
    public static final String RECIPE_IMG = "recipe_img";

    private UnScrollListView mListView;
    private MyScrollView mScrollView;
    private Button detailBack;
    private TextView detailRecipeName;
    private ImageView ivBackground;

    private DetailsAdapter adapter;

    private List<Method> methodList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        mListView=findViewById(R.id.usedBooksList);
        mScrollView =findViewById(R.id.scrollView);
        detailBack = findViewById(R.id.detail_back);
        detailRecipeName = findViewById(R.id.detail_recipe_name);
        ivBackground = findViewById(R.id.iv_background);
        detailBack.setOnClickListener(this);
        mListView.setFocusable(false);
        initData();
        adapter = new DetailsAdapter(this,R.layout.detail_item,methodList);
        mListView.setAdapter(adapter);
    }

    private void initData(){
        String recipeName = getIntent().getStringExtra(RECIPE_NAME);
        detailRecipeName.setText(recipeName);
        String recipeImg = getIntent().getStringExtra(RECIPE_IMG);
        Glide.with(this).load(recipeImg).into(ivBackground);
        String steps = getIntent().getStringExtra(RECIPE_STEP);
        try {
            JSONArray jsonArray = new JSONArray(steps);
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Method method = new Method();
                    method.setImg(jsonObject.optString("img"));
                    method.setStep(jsonObject.optString("step"));
                methodList.add(method);
                Log.d(TAG, "img: "+jsonObject.optString("img"));
            }
            Log.d(TAG, "methodList: "+methodList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //JSONObject jsonObject = new JSONObject(steps);
        //Log.d(TAG, "initData: "+jsonObject);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.detail_back:
                finish();
                break;
        }
    }
}
