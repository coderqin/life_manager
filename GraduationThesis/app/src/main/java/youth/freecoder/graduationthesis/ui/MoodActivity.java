package youth.freecoder.graduationthesis.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import youth.freecoder.graduationthesis.R;
import youth.freecoder.graduationthesis.constant.ConstantValue;
import youth.freecoder.graduationthesis.utils.ToastUtils;

public class MoodActivity extends Activity implements View.OnClickListener {
    private TextView moodCancel, moodSend, affirmAddress;
    private EditText moodInputContent;
    private ImageView coverAddress;
    //地址是否显示，默认显示
    private boolean visible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mood_view);
        InitView();
        InitData();
    }

    /**
     * 界面组件初始化
     */
    private void InitView() {
        moodCancel = (TextView) findViewById(R.id.mood_cancel);
        moodSend = (TextView) findViewById(R.id.mood_send);
        affirmAddress = (TextView) findViewById(R.id.affirm_address);
        coverAddress = (ImageView) findViewById(R.id.cover_address);

        moodCancel.setOnClickListener(this);
        moodSend.setOnClickListener(this);
        affirmAddress.setOnClickListener(this);
        coverAddress.setOnClickListener(this);

        moodInputContent = (EditText) findViewById(R.id.mood_input_content);
    }


    private void InitData() {
        Intent comingIntent = getIntent();
        String comingAddress = comingIntent.getStringExtra("selected_address");
        if (!TextUtils.isEmpty(comingAddress)) {
            affirmAddress.setText(comingAddress);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.mood_cancel:
                startActivity(new Intent(MoodActivity.this, MainMenuActivity.class));
                MoodActivity.this.finish();
                break;
            case R.id.mood_send:
                String moodContent = moodInputContent.getText().toString();
                if (TextUtils.isEmpty(moodContent)) {
                    ToastUtils.show(MoodActivity.this, "发送内容不能为空！");
                } else {
                    ToastUtils.show(MoodActivity.this, "地址:" + affirmAddress.getText().toString() +
                            ",签到内容:" + moodContent);
                }
                break;
            case R.id.affirm_address:
                ToastUtils.show(MoodActivity.this, "重新定位功能请稍后关注！");
//                Intent intent = new Intent(MoodActivity.this, LocationActivity.class);
//                startActivityForResult(intent, ConstantValue.RESULT_CODE);
                break;
            case R.id.cover_address:
                if (visible) {
                    affirmAddress.setVisibility(View.GONE);
                    visible = false;
                } else {
                    affirmAddress.setVisibility(View.VISIBLE);
                    visible = true;
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("Test", "requestCode=" + requestCode + ",resultCode=" + resultCode);
        if (resultCode == ConstantValue.RESULT_CODE) {
            String address = data.getStringExtra("location_address");
            if (!TextUtils.isEmpty(address)) {
                affirmAddress.setText(address);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
