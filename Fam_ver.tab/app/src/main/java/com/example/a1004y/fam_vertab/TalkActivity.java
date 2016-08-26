package com.example.a1004y.fam_vertab;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URISyntaxException;

public class TalkActivity extends AppCompatActivity {

    Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://10.0.2.2:3000");
        } catch (URISyntaxException e) {}
    }
    //Button btn_board,btn_calendar,btn_home,btn_set,btn_talk,btn_account, btn_send, btn_setting;
    Button btn_send;
    String msg_view;
    String GroupName;
    TextView tv;
    PreferenceClass pref = new PreferenceClass(TalkActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);

        mSocket.connect();

        String str = pref.getValue("GROUP_NAME","");
        Toast toast =Toast.makeText(getApplicationContext(),str, Toast.LENGTH_SHORT);
        toast.show();

        tv = (TextView) findViewById(R.id.Textview_talk);

        mSocket.on("chatMessage", chatting); //처음에 받을 메세지가 있는지 확인하기 위하여 on
        if(msg_view!=null){
            saveFile();
            readFile();
        }

        /**btn_board = (Button)findViewById(R.id.button_talk_board);
        btn_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TalkActivity.this, BoardActivity.class));
            }
        });

        btn_calendar = (Button)findViewById(R.id.button_talk_calendar);
        btn_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TalkActivity.this, CalendarActivity.class));
            }
        });

        btn_account = (Button)findViewById(R.id.button_talk_account);
        btn_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TalkActivity.this, AccountBookActivity.class));
            }
        });

        btn_home = (Button)findViewById(R.id.button_talk_home);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TalkActivity.this, HomeActivity.class));
            }
        });

        btn_talk = (Button)findViewById(R.id.button_talk_chat);
        btn_talk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TalkActivity.this, TalkActivity.class));
            }
        });

        btn_setting = (Button)findViewById(R.id.button_talk_setting);
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(TalkActivity.this, SettingsActivity.class));
            }
        });*/

        btn_send= (Button)findViewById(R.id.button_talk_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText SendText =  (EditText)findViewById(R.id.editText_talk_ChattingEditText);
                String sendMessage = SendText.getText().toString();

                mSocket.emit("sendChatMessage", sendMessage); //메세지 보내기
                SendText.setText("");

                mSocket.on("chatMessage", chatting); // 보낸 메세지를 다시 받기

                saveFile();
                readFile();
            }
        });

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private Emitter.Listener chatting = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        String message = data.getString("msg");
                        msg_view = message;

                        if(msg_view==null){
                            return;
                        }
                        Toast toast =Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT);
                        toast.show();

                    } catch (JSONException e) {
                        return;
                    }
                }
            });
        }
    };

    private Emitter.Listener groupName = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        String groupName = data.getString("msg");
                        GroupName = groupName;

                    } catch (JSONException e) {
                        return;
                    }
                }
            });
        }
    };

    public void readFile(){
        /** txt파일에서 한줄 씩 읽어와서 보여주기 */
        try {
            // 파일에서 읽은 데이터를 저장하기 위해서 만든 변수
            String str2 = pref.getValue("GROUP_NAME","");
            StringBuffer data = new StringBuffer();
            FileReader fr= new FileReader("/data/data/com.example.fam/files/"+str2+".txt");//파일명
            BufferedReader buffer = new BufferedReader(fr);
            String str = buffer.readLine(); // 파일에서 한줄을 읽어옴
            while (str != null) {
                data.append(str + "\n");
                str = buffer.readLine();
            }
            tv.setText(data);
            buffer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        /** ************************************ */
    }

    public void saveFile(){
        /** ********txt파일에 한줄 씩 저장******** */
        try {
            String str2 = pref.getValue("GROUP_NAME","");
            StringBuffer data = new StringBuffer();
            String filePath = "/data/data/com.example.fam/files/"+str2+".txt";
            File file = new File(filePath);
            PrintWriter out = new PrintWriter(new FileWriter(filePath,true));
            out.println(msg_view);
            out.close();

            String temp = "";
            if(file.exists()) temp = "성공";
            else temp ="실패";
            Toast toast =Toast.makeText(getApplicationContext(),temp, Toast.LENGTH_SHORT);
            toast.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
        /** ************************************* */
    }

    /**@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            pref.remove("GROUP_NAME");
            Intent intent = new Intent(TalkActivity.this, GroupListActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

}