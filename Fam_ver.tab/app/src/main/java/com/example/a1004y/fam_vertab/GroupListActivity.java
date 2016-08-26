package com.example.a1004y.fam_vertab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class GroupListActivity extends AppCompatActivity {

    Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://10.0.2.2:3000");
        } catch (URISyntaxException e) {}
    }
    PreferenceClass pref = new PreferenceClass(GroupListActivity.this);
    Button btn_add, btn_search;
    private BackPressCloseHandler backPressCloseHandler;

    private Emitter.Listener listen_groups = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        String groups = data.getString("GROUPS");
                        String[] group_name;
                        group_name = groups.split(":");

                        if (groups == "" || groups == "null") {
                        } else {
                            for (int btn_id_groupNum = 0; btn_id_groupNum < group_name.length; btn_id_groupNum++) {
                                final Button myButton = new Button(GroupListActivity.this);
                                myButton.setText(group_name[btn_id_groupNum]);
                                myButton.setId(btn_id_groupNum);

                                myButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        pref.put("GROUP_NAME", myButton.getText().toString());
                                        startActivity(new Intent(GroupListActivity.this, MainActivity.class));
                                    }
                                });

                                LinearLayout ll = (LinearLayout)findViewById(R.id.listRoom_linearlayout);
                                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                ll.addView(myButton, lp);
                            }
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
        setContentView(R.layout.activity_group_list);

        mSocket.connect();

        backPressCloseHandler = new BackPressCloseHandler(this);

        String id = pref.getValue("ID","");

        mSocket.emit("getGroupOnN", id);
        mSocket.on("getGroupOnAS",listen_groups);

        btn_add = (Button)findViewById(R.id.button_listRoom_plus);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GroupListActivity.this, MakeGroupActivity.class));
            }
        });

        btn_search = (Button)findViewById(R.id.button_listRoom_searchRoom);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GroupListActivity.this, SearchGroupActivity.class));
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Intent intent = new Intent(GroupListActivity.this, GroupListActivity.class);
            startActivity(intent);
            return true;

            /*******8/23추가************/
        }else if (id == R.id.settings){
            Intent intent = new Intent(GroupListActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflater함수를 이용해서 menu 리소스를 menu로 변환.

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }

}