package youth.freecoder.graduationthesis.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import youth.freecoder.graduationthesis.R;
import youth.freecoder.graduationthesis.server.ServerConnection;
import youth.freecoder.graduationthesis.server.ServerUtils;

public class LoginActivity extends Activity implements View.OnClickListener {
    private EditText username;
    private EditText password;
    private TextView register_link;
    private Button login_btn, exit_btn;
    private ChatActivity chatActivity;


    private String TAG = "LoginTest";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(LoginActivity.this, "登陆失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login_view);
        init();
    }

    private void init() {
        register_link = (TextView) findViewById(R.id.user_register);
        register_link.setOnClickListener(this);

        username = (EditText) findViewById(R.id.edit_username);
        password = (EditText) findViewById(R.id.edit_password);

        login_btn = (Button) findViewById(R.id.enter_btn);
        exit_btn = (Button) findViewById(R.id.exit_btn);

        login_btn.setOnClickListener(this);
        exit_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        final String un = username.getText().toString();
        final String pwd = password.getText().toString();
        switch (v.getId()) {
            case R.id.enter_btn:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            /**
                             * 登陆验证
                             */
                            ServerUtils.loginMatch(un, pwd, LoginActivity.this);
                            /**
                             * 登陆跳转
                             */
                            Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                            intent.putExtra("username", un);
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                            handler.sendEmptyMessage(1);
                        }
                    }
                }).start();
                break;
            case R.id.exit_btn:
                LoginActivity.this.finish();
                break;
            case R.id.user_register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ServerConnection.closeXMPPConnection();
    }
}
