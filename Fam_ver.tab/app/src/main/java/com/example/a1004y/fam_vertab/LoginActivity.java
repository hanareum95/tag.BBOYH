package com.example.a1004y.fam_vertab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class LoginActivity extends Activity {

    Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://10.0.2.2:3000");
        } catch (URISyntaxException e) {}
    }
    PreferenceClass pref = new PreferenceClass(LoginActivity.this);
    Button btn_login, btn_join;

    private Emitter.Listener listen_start_person = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        String id = data.getString("ID");
                        String pw = data.getString("PW");
                        String name = data.getString("NAME");
                        String phone = data.getString("PHONE");

                        pref.put("ID",id);
                        pref.put("PW",pw);
                        pref.put("NAME",name);
                        pref.put("PHONE",phone);

                        startActivity(new Intent(LoginActivity.this, GroupListActivity.class));

                    } catch (JSONException e) {
                        return;
                    }
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(pref.getValue("ID","")!=""){
            startActivity(new Intent(LoginActivity.this, GroupListActivity.class));
            LoginActivity.this.finish();
        }

        mSocket.connect();

        btn_join = (Button)findViewById(R.id.button_login_join);
        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, JoinActivity.class));
            }
        });

        btn_login = (Button)findViewById(R.id.button_login_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
    }

    private void attemptLogin() {

        EditText editText_login_email = (EditText)findViewById(R.id.editText_login_email);
        EditText editText_login_password = (EditText)findViewById(R.id.editText_login_password);

        String[] message = new String[2];

        message[0] = editText_login_email.getText().toString();
        message[1] = editText_login_password.getText().toString();

        if (TextUtils.isEmpty(message[0])||TextUtils.isEmpty(message[1])) {
            return;
        }

        editText_login_email.setText("");
        editText_login_password.setText("");

        mSocket.emit("connectMember", message[0], message[1]);

        mSocket.on("successLogin", listen_start_person);
    }

}