package youth.freecoder.graduationthesis.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import youth.freecoder.graduationthesis.R;
import youth.freecoder.graduationthesis.server.ServerConnection;
import youth.freecoder.graduationthesis.utils.CommonTextUtils;
import youth.freecoder.graduationthesis.utils.ToastUtils;

public class AddContactsActivity extends Activity implements View.OnClickListener {
    private EditText username;
    private Button add, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcontact_view);
        init();
    }

    private void init() {
        username = (EditText) findViewById(R.id.add_contact_name);
        add = (Button) findViewById(R.id.add_contact_btn);
        back = (Button) findViewById(R.id.back);
        add.setOnClickListener(this);
        back.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_contact_btn:
                addContacts();
                break;
            case R.id.back:
                onBackPressed();
                break;
            default:
                break;
        }
    }


    private void addContacts() {
        boolean isSuccess = false;
        String name = username.getText().toString().trim();
        if (!TextUtils.isEmpty(name)) {
            isSuccess = ServerConnection.addUser(ServerConnection.getConnection().getRoster(),
                    CommonTextUtils.getServerUserJIDByName(name,ServerConnection.getConnection()),
                    name);
            if (isSuccess) {
                ToastUtils.show(AddContactsActivity.this, "添加好友成功，等待同意!");
            } else {
                ToastUtils.show(AddContactsActivity.this, "添加失败!");
            }
        } else {
            ToastUtils.show(AddContactsActivity.this, "用户名不能为空");
        }
    }
}
