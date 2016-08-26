package com.example.a1004y.fam_vertab;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class BoardActivity extends AppCompatActivity {

    Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://10.0.2.2:3000");
        } catch (URISyntaxException e) {}
    }
    //Button btn_board, btn_calendar, btn_home, btn_set, btn_talk, btn_account, btn_board_write, btn_setting;
    Button btn_board_write;
    PreferenceClass pref = new PreferenceClass(BoardActivity.this);
    boolean temp = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        mSocket.connect();

        //한개의 글을 추가할 Layout
        LinearLayout Linear_board_writing = (LinearLayout)findViewById(R.id.board_linearlayout);

        String str = pref.getValue("GROUP_NAME","");
        //Toast toast =Toast.makeText(getApplicationContext(),str, Toast.LENGTH_SHORT);
        //toast.show();

        /**btn_board = (Button)findViewById(R.id.button_board_board);
        btn_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BoardActivity.this, BoardActivity.class));
            }
        });

        btn_calendar = (Button)findViewById(R.id.button_board_calendar);
        btn_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BoardActivity.this, CalendarActivity.class));
            }
        });

        btn_account = (Button)findViewById(R.id.button_board_account);
        btn_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BoardActivity.this, AccountBookActivity.class));
            }
        });

        btn_home = (Button)findViewById(R.id.button_board_home);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BoardActivity.this, HomeActivity.class));
            }
        });

        btn_talk = (Button)findViewById(R.id.button_board_chat);
        btn_talk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BoardActivity.this, TalkActivity.class));
            }
        });

        btn_setting = (Button)findViewById(R.id.button_board_setting);
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(BoardActivity.this, SettingsActivity.class));
            }
        });*/

        btn_board_write = (Button)findViewById(R.id.button_board_write);
        btn_board_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(BoardActivity.this, WriteBoardActivity.class);
                //intent2.putExtra("c_no",c_no);
                intent2.putExtra("c_edit","editFalse");
                startActivity(intent2);
            }
        });

        if((Linear_board_writing).getChildCount() > 0)
            (Linear_board_writing).removeAllViews();

        mSocket.emit("getContentNOOnN", str);
        //Toast toast2 =Toast.makeText(getApplicationContext(),"여긴여긴들어옴", Toast.LENGTH_SHORT);
        //toast2.show();
        mSocket.on("getContentNOOnAS",listen_c_no);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private Emitter.Listener listen_contents = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];

                    //Toast toast2 =Toast.makeText(getApplicationContext(),"여긴들어옴", Toast.LENGTH_SHORT);
                    //toast2.show();
                    try {
                        final String c_no = data.getString("NO");
                        String c_name = data.getString("NAME");
                        String c_date = data.getString("DATE");
                        String c_content = data.getString("CONTENT");

                        if(c_content == null){
                            return;
                        }
                        //한개의 글을 추가할 Layout
                        LinearLayout Linear_board_writing = (LinearLayout)findViewById(R.id.board_linearlayout);

                        //한개의 글 Layout -> board_a_writing
                        LinearLayout board_a_writing = new LinearLayout(BoardActivity.this);
                        LinearLayout.LayoutParams board_a_writing_Params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        board_a_writing.setOrientation(LinearLayout.VERTICAL);
                        board_a_writing.setLayoutParams(board_a_writing_Params);

                        /** ********************************************************* */
                        //한개의 글 안에 프로필사진, 이름, 날짜를 연결하는 LinearLayout
                        LinearLayout in_a_writing = new LinearLayout(BoardActivity.this);
                        in_a_writing.setOrientation(LinearLayout.HORIZONTAL);

                        /**
                         ImageView user_profile = new ImageView(MainActivity.this);
                         user_profile.setBackgroundColor(Color.parseColor("#000333"));
                         in_a_writing.addView(user_profile);
                         */

                        TextView user_profile = new TextView(BoardActivity.this);
                        user_profile.setText("글쓴이사진");
                        user_profile.setBackgroundColor(Color.parseColor("#987333"));
                        in_a_writing.addView(user_profile);

                        LinearLayout.LayoutParams user_profile_Params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                150,1);
                        user_profile.setLayoutParams(user_profile_Params);

                        TextView user_name = new TextView(BoardActivity.this);
                        user_name.setText(c_name);
                        user_name.setBackgroundColor(Color.parseColor("#764334"));
                        in_a_writing.addView(user_name);

                        LinearLayout.LayoutParams user_name_Params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                150,1);
                        user_name.setLayoutParams(user_name_Params);

                        TextView write_time = new TextView(BoardActivity.this);
                        write_time.setText(c_date);
                        write_time.setBackgroundColor(Color.parseColor("#555555"));
                        in_a_writing.addView(write_time);

                        LinearLayout.LayoutParams write_time_Params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                150,1);
                        write_time.setLayoutParams(write_time_Params);

                        LinearLayout.LayoutParams in_a_writing_Params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                150);
                        in_a_writing.setLayoutParams(in_a_writing_Params);

                        board_a_writing.addView(in_a_writing);
                        /** ********************************************************* */

                        TextView write_content = new TextView(BoardActivity.this);
                        write_content.setText(c_content);
                        write_content.setBackgroundColor(Color.parseColor("#765432"));

                        LinearLayout.LayoutParams write_content_Params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                600);
                        write_content_Params.bottomMargin = 20;
                        write_content.setLayoutParams(write_content_Params);

                        LinearLayout temp = (LinearLayout)findViewById(Integer.parseInt(c_no));
                        if(temp == null){
                            board_a_writing.setId(Integer.parseInt(c_no));
                            board_a_writing.addView(write_content);
                        }else {
                            return;
                        }

                        Linear_board_writing.addView(board_a_writing);

                        board_a_writing.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent2 = new Intent(BoardActivity.this, ShowBoardActivity.class);
                                intent2.putExtra("c_no",c_no);
                                startActivity(intent2);
                            }
                        });

                    } catch (JSONException e) {
                        return;
                    }
                }
            });
        }
    };

    private Emitter.Listener listen_c_no = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];

                    try {

                        String g_name = data.getString("GNAMEBOARD");
                        String c_no = data.getString("NO");


                        for (int i = Integer.parseInt(c_no); i > 1; i--) {

                            mSocket.emit("getContentOnN", i, g_name);
                            mSocket.on("getContentOnAS",listen_contents);

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
            Intent intent = new Intent(BoardActivity.this, GroupListActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

}