package com.example.a1004y.fam_vertab;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class CalendarActivity extends AppCompatActivity {

    Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://10.0.2.2:3000");
        } catch (URISyntaxException e) {}
    }
    //Button btn_board,btn_calendar,btn_home,btn_set,btn_talk,btn_account, btn_setting;
    PreferenceClass pref = new PreferenceClass(CalendarActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        mSocket.connect();

        String str = pref.getValue("GROUP_NAME","");
        Toast toast =Toast.makeText(getApplicationContext(),str, Toast.LENGTH_SHORT);
        toast.show();

        /**btn_board = (Button)findViewById(R.id.button_calendar_board);
        btn_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CalendarActivity.this, BoardActivity.class));
            }
        });

        btn_calendar = (Button)findViewById(R.id.button_calendar_calendar);
        btn_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CalendarActivity.this, CalendarActivity.class));
            }
        });

        btn_account = (Button)findViewById(R.id.button_calendar_account);
        btn_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CalendarActivity.this, AccountBookActivity.class));
            }
        });

        btn_home = (Button)findViewById(R.id.button_calendar_home);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CalendarActivity.this, HomeActivity.class));
            }
        });

        btn_talk = (Button)findViewById(R.id.button_calendar_chat);
        btn_talk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CalendarActivity.this, TalkActivity.class));
            }
        });

        btn_setting = (Button)findViewById(R.id.button_calendar_setting);
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(CalendarActivity.this, SettingsActivity.class));
            }
        });*/

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            pref.remove("GROUP_NAME");
            Intent intent = new Intent(CalendarActivity.this, GroupListActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

}
