package crirecipe.criusker.crirecipe.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import crirecipe.criusker.crirecipe.R;
import crirecipe.criusker.crirecipe.basic.BaseActivity;

public class SearchActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Button detailBack = findViewById(R.id.detail_back);

        detailBack.setOnClickListener(this);
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
