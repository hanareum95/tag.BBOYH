package com.example.a1004y.fam_vertab;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class ShowBoardActivity extends AppCompatActivity {

    Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://10.0.2.2:3000");
        } catch (URISyntaxException e) {}
    }
    PreferenceClass pref = new PreferenceClass(ShowBoardActivity.this);
    Intent intent;
    TextView textView_showBoard_name, textView_showBoard_time, textView_showBoard_writing;
    EditText editText_showBoard_reply;
    Button btn_showBoard_send;
    ArrayList<Reply> arReply;
    Reply myReply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_board);

        String str = pref.getValue("GROUP_NAME","");
        String gnameBoard = str + "_Board";

        intent = getIntent();
        final String c_no = (String) intent.getSerializableExtra("c_no");

        mSocket.emit("getContentOnN", Integer.parseInt(c_no), gnameBoard);
        mSocket.once("getContentOnAS", listen_content);

        mSocket.emit("getContentNOOnN", str);
        mSocket.on("getContentNOOnAS",listen_c_no_re);

        /*****************************댓글**************************/
        editText_showBoard_reply = (EditText)findViewById(R.id.editText_showBoard_reply);
        btn_showBoard_send = (Button)findViewById(R.id.button_showBoard_send);

        final EditText editText_showBoard_reply = (EditText)findViewById(R.id.editText_showBoard_reply);

        btn_showBoard_send = (Button)findViewById(R.id.button_showBoard_send);
        btn_showBoard_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] message = new String[6];

                message[0] = pref.getValue("NAME","");;
                message[1] = "시간";
                message[2] = editText_showBoard_reply.getText().toString();
                message[3] = "사진경로";
                message[4] = "0";
                message[5] = c_no;

                if (TextUtils.isEmpty(message[0])||TextUtils.isEmpty(message[1])||TextUtils.isEmpty(message[2])||TextUtils.isEmpty(message[3])||TextUtils.isEmpty(message[4])||TextUtils.isEmpty(message[5])) {
                    return;
                }

                editText_showBoard_reply.setText("");

                mSocket.emit("uploadContent", pref.getValue("GROUP_NAME",""), message[0], message[1], message[2], message[3], message[4],message[5],"0");

            }
        });
        /*********************댓글**************************/

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    class Reply {
        int re_Icon;
        String re_name;
        String re_content;

        Reply(int Icon, String name, String content){
            re_Icon = Icon;
            re_name = name;
            re_content = content;
        }
    }

    class MyAdapter extends BaseAdapter {
        Context con;
        LayoutInflater inflater;
        ArrayList<Reply> arR;
        int layout;

        public MyAdapter (Context context, int alayout, ArrayList<Reply> aarR) {
            con = context;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            arR = aarR;
            layout = alayout;
        }

        @Override
        public int getCount() {
            return arR.size();
        }

        @Override
        public Object getItem(int position) {
            return arR.get(position).re_content;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(layout, parent, false);
            }
            ImageView img = (ImageView) convertView.findViewById(R.id.listview_item_image);
            img.setImageResource(arR.get(position).re_Icon);

            TextView txt_name = (TextView) convertView.findViewById(R.id.listview_item_name);
            txt_name.setText(arR.get(position).re_name);

            TextView txt_reply = (TextView) convertView.findViewById(R.id.listview_item_reply);
            txt_reply.setText(arR.get(position).re_content);

            return convertView;
        }

    }

    private Emitter.Listener listen_c_no_re = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];

                    try {
                        String g_name = data.getString("GNAMEBOARD");
                        String no = data.getString("NO");

                        arReply = new ArrayList<Reply>();

                        for (int i = Integer.parseInt(no) ; i > 1 ; i-- ) {
                            mSocket.emit("getReplyOnN", i, (String) intent.getSerializableExtra("c_no"), g_name);
                            mSocket.on("getReplyOnAS", listen_reply);
                        }

                        MyAdapter adapter = new MyAdapter(ShowBoardActivity.this, R.layout.listview_item, arReply);

                        ListView listView_showBoard_replys;
                        listView_showBoard_replys = (ListView)findViewById(R.id.listView_showBoard_replys);

                        listView_showBoard_replys.setAdapter(adapter);

                    } catch (JSONException e) {
                        return;
                    }
                }
            });
        }
    };

    private Emitter.Listener listen_reply = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];

                    try {
                        String name = data.getString("NAME");
                        String reply = data.getString("REPLY");

                        //arReply = new ArrayList<Reply>();
                        //Reply myReply;
                        myReply = new Reply(R.drawable.users, name, reply);
                        arReply.add(myReply);

                    } catch (JSONException e) {
                        return;
                    }
                }
            });
        }
    };

    private Emitter.Listener listen_content = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];

                    try {
                        final String c_no = data.getString("NO");
                        String c_name = data.getString("NAME");
                        String c_date = data.getString("DATE");
                        String c_content = data.getString("CONTENT");

                        textView_showBoard_name = (TextView)findViewById(R.id.textView_showBoard_topInfo_name);
                        textView_showBoard_time = (TextView)findViewById(R.id.textView_showBoard_topInfo_time);
                        textView_showBoard_writing = (TextView)findViewById(R.id.textView_showBoard_writing);

                        textView_showBoard_name.setText(c_name);
                        textView_showBoard_time.setText(c_date);
                        textView_showBoard_writing.setText(c_content);

                        String name__ = pref.getValue("NAME","");

                        if ( c_name.equals(name__) ) {
                            //수정 및 삭제버튼을 추가할 Layout
                            LinearLayout Linear_showBoard_topInfo = (LinearLayout)findViewById(R.id.linearLayout_showBoard_topInfo);

                            Button btn_showBoard_edit = new Button(ShowBoardActivity.this);
                            Button btn_showBoard_delete = new Button(ShowBoardActivity.this);

                            LinearLayout.LayoutParams pm = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT );
                            pm.weight = 1;

                            btn_showBoard_edit.setLayoutParams(pm);
                            btn_showBoard_edit.setText("EDIT");

                            btn_showBoard_delete.setLayoutParams(pm);
                            btn_showBoard_delete.setText("DELETE");

                            Linear_showBoard_topInfo.addView(btn_showBoard_edit);
                            Linear_showBoard_topInfo.addView(btn_showBoard_delete);

                            btn_showBoard_edit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent2 = new Intent(ShowBoardActivity.this, WriteBoardActivity.class);
                                    intent2.putExtra("c_no",c_no);
                                    intent2.putExtra("c_edit","editTrue");
                                    startActivity(intent2);
                                }
                            });

                            btn_showBoard_delete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mSocket.emit("deleteContent",c_no,pref.getValue("GROUP_NAME",""));

                                    startActivity(new Intent(ShowBoardActivity.this, BoardActivity.class));
                                }
                            });
                        }

                    } catch (JSONException e) {
                        return;
                    }
                }
            });
        }
    };

    /**@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            pref.remove("GROUP_NAME");
            Intent intent = new Intent(ShowBoardActivity.this, GroupListActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

}