package youth.freecoder.graduationthesis.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import youth.freecoder.graduationthesis.R;
import youth.freecoder.graduationthesis.server.ServerConnection;

public class RegisterActivity extends Activity implements View.OnClickListener {

    private EditText reg_username, reg_pwd, reg_email;
    private Button reg_user, reg_cancel;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(RegisterActivity.this, "服务器无响应", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(RegisterActivity.this, "账号已存在", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(RegisterActivity.this, " 注册失败", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(RegisterActivity.this, "恭喜您，注册成功！", Toast.LENGTH_SHORT).show();
                    RegisterActivity.this.onBackPressed();
                    break;
                case 4:
                    Toast.makeText(RegisterActivity.this, "服务器连接失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_view);

        reg_username = (EditText) findViewById(R.id.edit_reg_username);
        reg_pwd = (EditText) findViewById(R.id.edit_reg_password);
        reg_email = (EditText) findViewById(R.id.edit_email);
        reg_user = (Button) findViewById(R.id.user_register);
        reg_cancel = (Button) findViewById(R.id.user_cancel);
        reg_user.setOnClickListener(this);
        reg_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final String register_username = reg_username.getText().toString().trim();
        final String register_pwd = reg_pwd.getText().toString().trim();
        final String register_email = reg_email.getText().toString().trim();
        switch (v.getId()) {
            case R.id.user_register:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            int result = ServerConnection.registerUser(register_username,
                                    register_pwd, register_email);
                            RegisterActivity.this.handler.sendEmptyMessage(result);
                        } catch (Exception e) {
                            e.printStackTrace();
                            RegisterActivity.this.handler.sendEmptyMessage(4);
                        }
                    }
                }).start();
                break;
            case R.id.user_cancel:
                RegisterActivity.this.onBackPressed();
                break;
        }
    }

}
