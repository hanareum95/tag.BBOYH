package com.example.a1004y.fam_vertab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Random;

public class MakeGroupActivity extends AppCompatActivity {

    Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://10.0.2.2:3000");
        } catch (URISyntaxException e) {}
    }
    PreferenceClass pref = new PreferenceClass(MakeGroupActivity.this);
    Button btn_addRoom, btn_cancel, btn_makeGroupCode;
    TextView TextView_groupCode;

    private Emitter.Listener chatRoomName = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        String crname = data.getString("CRNAME");

                        String filePath = "/data/data/com.example.fam/files/" + crname + ".txt";
                        File file = new File(filePath);

                        try {
                            file.createNewFile();
                            Toast toast =Toast.makeText(getApplicationContext(),"성공", Toast.LENGTH_SHORT);
                            toast.show();

                        } catch(IOException ie){
                            Toast toast =Toast.makeText(getApplicationContext(),"실패", Toast.LENGTH_SHORT);
                            toast.show();
                        }

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
        setContentView(R.layout.activity_make_group);

        mSocket.connect();

        TextView_groupCode = (TextView)findViewById(R.id.textView_makeRoom_madeGroupCode);

        btn_makeGroupCode = (Button)findViewById(R.id.button_makeRoom_makeCode);
        btn_makeGroupCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView_groupCode.setText(makeGroupCode());
                btn_makeGroupCode.setEnabled(false);
            }
        });

        btn_addRoom = (Button)findViewById(R.id.button_makeRoom_makeRoom);
        btn_addRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText_makeRoom_groupName = (EditText)findViewById(R.id.editText_makeRoom_groupName);
                EditText editText_makeRoom_groupPassword = (EditText)findViewById(R.id.editText_makeRoom_groupPassword);

                String[] message = new String[6];

                message[0] = editText_makeRoom_groupName.getText().toString();
                message[1] = editText_makeRoom_groupPassword.getText().toString();
                message[2] = (String) TextView_groupCode.getText();
                message[3] = pref.getValue("ID","");
                message[4] = pref.getValue("NAME","");
                message[5] = pref.getValue("PHONE","");

                if (TextUtils.isEmpty(message[0])||TextUtils.isEmpty(message[1])||TextUtils.isEmpty(message[2])||TextUtils.isEmpty(message[3])||TextUtils.isEmpty(message[4])||TextUtils.isEmpty(message[5])) {
                    return;
                }

                editText_makeRoom_groupName.setText("");
                editText_makeRoom_groupPassword.setText("");

                mSocket.emit("createGroup", message[0], message[1], message[2], message[3], message[4], message[5]);

                mSocket.on("createChatRoom", chatRoomName);

                startActivity(new Intent(MakeGroupActivity.this, GroupListActivity.class));
            }
        });

        btn_cancel = (Button)findViewById(R.id.button_makeRoom_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MakeGroupActivity.this, GroupListActivity.class));
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public StringBuffer makeGroupCode(){
        Random rnd =new Random();
        StringBuffer groupCode =new StringBuffer();

        for(int i=0;i<5;i++){
            if(rnd.nextBoolean()){
                groupCode.append((char)((int)(rnd.nextInt(26))+97));
            }else{
                groupCode.append((rnd.nextInt(10)));
            }
        }
        return groupCode;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Intent intent = new Intent(MakeGroupActivity.this, GroupListActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
