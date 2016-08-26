package com.example.a1004y.fam_vertab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WriteBoardActivity extends AppCompatActivity {

    Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://10.0.2.2:3000");
        } catch (URISyntaxException e) {}
    }
    PreferenceClass pref = new PreferenceClass(WriteBoardActivity.this);
    Button btn_writeBoard_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_board);

        mSocket.connect();

        final Intent intent = getIntent();
        final String c_no = (String) intent.getSerializableExtra("c_no");
        final String c_edit = (String) intent.getSerializableExtra("c_edit");

        String str = pref.getValue("GROUP_NAME","");

        /** ********8현재 시간 구하기******** */
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String  formatDate = sdfNow.format(date);
        /** ***********************************/

        btn_writeBoard_submit = (Button)findViewById(R.id.button_writeBoard_submit);
        btn_writeBoard_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText_writeBoard_content = (EditText)findViewById(R.id.editText_writeBoard_content);

                /** ********8현재 시간 구하기******** */
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String  formatDate = sdfNow.format(date);
                /** ***********************************/

                /**************************21일 수정*******************************/
                String[] message = new String[6];

                message[0] = pref.getValue("NAME","");;
                message[1] = formatDate;
                message[2] = editText_writeBoard_content.getText().toString();
                message[3] = "사진경로";
                message[4] = "1";
                message[5] = c_no;

                Toast t =Toast.makeText(getApplicationContext(),"*"+message[2], Toast.LENGTH_SHORT);
                t.show();

                if (TextUtils.isEmpty(message[0])||TextUtils.isEmpty(message[1])||TextUtils.isEmpty(message[2])||TextUtils.isEmpty(message[3])||TextUtils.isEmpty(message[4])) {

                    return;
                }


                if(c_edit.equals("editTrue")){

                    mSocket.emit("uploadContent", pref.getValue("GROUP_NAME",""), message[0], message[1], message[2], message[3], message[4], message[5],"1");

                    startActivity(new Intent(WriteBoardActivity.this, BoardActivity.class));

                }else{
                    Toast t2 =Toast.makeText(getApplicationContext(),c_edit, Toast.LENGTH_SHORT);
                    t2.show();
                    mSocket.emit("uploadContent", pref.getValue("GROUP_NAME",""), message[0], message[1], message[2], message[3], message[4], message[5],"0");
                    startActivity(new Intent(WriteBoardActivity.this, BoardActivity.class));
                }
                editText_writeBoard_content.setText("");


            }
        });


        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(c_edit.equals("editTrue")) {
            mSocket.emit("loadContent", c_no, pref.getValue("GROUP_NAME", ""));
            mSocket.on("getContent", loadContent);
        }
    }

    /**@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            pref.remove("GROUP_NAME");
            Intent intent = new Intent(WriteBoardActivity.this, GroupListActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    private Emitter.Listener loadContent = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    JSONObject data = (JSONObject) args[0];

                    Toast toast =Toast.makeText(getApplicationContext(),"emitter", Toast.LENGTH_SHORT);
                    toast.show();

                    try {
                        final String c_no = data.getString("NO");
                        String c_name = data.getString("NAME");
                        String c_date = data.getString("DATE");
                        String c_content = data.getString("CONTENT");

                        EditText editText_writeBoard_content = (EditText)findViewById(R.id.editText_writeBoard_content);
                        editText_writeBoard_content.setText(c_content);


                    } catch (JSONException e) {
                        return;
                    }
                }
            });
        }
    };


}
