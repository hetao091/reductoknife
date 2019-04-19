package com.github.reductoknife;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.github.reductoannotations.BindView;

public class MainActivity extends AppCompatActivity {

  @BindView(R.id.tv_main)
  TextView mTextView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ReductoKnife.bind(this);
    mTextView.setText("11111");
  }
}
