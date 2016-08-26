package com.example.a1004y.fam_vertab;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class HomeActivity extends AppCompatActivity {

    Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://10.0.2.2:3000");
        } catch (URISyntaxException e) {}
    }
    //Button btn_board,btn_calendar,btn_home,btn_set,btn_talk,btn_account,btn_addNotice, btn_setting;
    Button btn_addNotice;
    ScrollView scrollView;
    PreferenceClass pref = new PreferenceClass(HomeActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mSocket.connect();

        String str = pref.getValue("GROUP_NAME","");
        Toast toast =Toast.makeText(getApplicationContext(),str, Toast.LENGTH_SHORT);
        toast.show();

        /**btn_board = (Button)findViewById(R.id.button_main_board);
        btn_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, BoardActivity.class));
            }
        });

        btn_calendar = (Button)findViewById(R.id.button_main_calendar);
        btn_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, CalendarActivity.class));
            }
        });

        btn_account = (Button)findViewById(R.id.button_main_account);
        btn_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AccountBookActivity.class));
            }
        });

        btn_home = (Button)findViewById(R.id.button_main_home);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, HomeActivity.class));
            }
        });

        btn_talk = (Button)findViewById(R.id.button_main_chat);
        btn_talk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, TalkActivity.class));

            }
        });

        btn_setting = (Button)findViewById(R.id.button_main_setting);
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });*/

        btn_addNotice = (Button)findViewById(R.id.button_main_addNotice);
        btn_addNotice.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);

                alert.setTitle("공지 입력");
                alert.setMessage("공지를 입력하세요");

                final EditText groupNotice = new EditText(HomeActivity.this);
                alert.setView(groupNotice);

                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String input_groupNotice = groupNotice.getText().toString();
                        String user_name = pref.getValue("NAME","");
                        TextView notice = (TextView)findViewById(R.id.textView_main_notice);
                        notice.setText(user_name+" : "+input_groupNotice);
                        dialog.cancel();
                    }
                });

                alert.setNegativeButton("취소",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                alert.show();
            }
        });

        scrollView = (ScrollView)findViewById(R.id.scrollView);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            pref.remove("GROUP_NAME");
            Intent intent = new Intent(HomeActivity.this, GroupListActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

}
