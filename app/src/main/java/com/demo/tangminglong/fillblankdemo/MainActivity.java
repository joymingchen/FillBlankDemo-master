package com.demo.tangminglong.fillblankdemo;

import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ReplaceSpan.OnClickListener {

    private String mTestStr = "我是个学生的人.";
    private String mTestStr2 = "我是个____学生的人.";
    private String mTestStr3 = "我是____个学____生的人.";
    private TextView mTvContent;
    private EditText mEtInput;
    private SpansManager mSpansManager;
    private Button mBtnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTvContent = (TextView) findViewById(R.id.tv_content);
        mEtInput = (EditText) findViewById(R.id.et_input);
        mBtnSubmit = (Button) findViewById(R.id.btn_submit);
        mBtnSubmit.setOnClickListener(this);
        mSpansManager = new SpansManager(this, mTvContent, mEtInput);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                Toast.makeText(this, getMyAnswerStr(), Toast.LENGTH_LONG).show();
                break;
        }
    }

    //填空题点击响应事件
    @Override
    public void OnClick(TextView v, int id, ReplaceSpan span) {
        mEtInput.setVisibility(View.VISIBLE);
        //保存前一个的内容
        mSpansManager.setData(mEtInput.getText().toString(), null, mSpansManager.mOldSpan);
        mSpansManager.mOldSpan = id;
        //如果当前span身上有值，先赋值给et身上
        mEtInput.setText(TextUtils.isEmpty(span.mText) ? "" : span.mText);
        mEtInput.setSelection(span.mText.length());
        span.mText = "";
        //通过rf计算出et当前应该显示的位置
        RectF rf = mSpansManager.drawSpanRect(span);
        //设置EditText填空题中的相对位置
        mSpansManager.setEtXY(rf);
        mSpansManager.setSpanChecked(id);
    }

    private String getMyAnswerStr() {
        mSpansManager.setLastCheckedSpanText(mEtInput.getText().toString());
        return mSpansManager.getMyAnswer().toString();
    }

    public void one(View view) {
        mSpansManager = new SpansManager(this, mTvContent, mEtInput);
        mEtInput.setVisibility(View.GONE);
        mSpansManager.doFillBlank(mTestStr);
    }

    public void two(View view) {
        mEtInput.setVisibility(View.GONE);
        mSpansManager = new SpansManager(this, mTvContent, mEtInput);
        mSpansManager.doFillBlank(mTestStr2);
        mEtInput.setText("");

        mSpansManager.setData("改变1", null, 0);
    }

    public void three(View view) {
        mEtInput.setVisibility(View.GONE);
        mEtInput.setText("");
        mSpansManager = new SpansManager(this, mTvContent, mEtInput);
        mSpansManager.doFillBlank(mTestStr3);
        mSpansManager.setData("改变21", null, 0);
        mSpansManager.setData("改变22", null, 1);
    }
}
