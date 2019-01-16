package crirecipe.criusker.crirecipe.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import crirecipe.criusker.crirecipe.R;

public class FenleiActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fenlei);
        Button detailBack = findViewById(R.id.detail_back);
        TextView detailName = findViewById(R.id.detail_recipe_name);
        detailName.setText("分类");
        detailBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.detail_back :
                finish();
                break;
        }
    }
}
