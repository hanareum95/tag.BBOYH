package com.example.a1004y.fam_vertab;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class SearchGroupActivity extends AppCompatActivity {

    Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://10.0.2.2:3000");
        } catch (URISyntaxException e) {}
    }
    PreferenceClass pref = new PreferenceClass(SearchGroupActivity.this);
    Button button_search_room_search;

    private Emitter.Listener findGroupName = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        final String findGname = data.getString("GNAME");

                        final String id = pref.getValue("ID","");
                        final String name = pref.getValue("NAME","");
                        final String phone = pref.getValue("PHONE","");

                        if (findGname == "") {

                        } else {
                            final Button myButton = new Button(SearchGroupActivity.this);
                            myButton.setText(findGname);
                            myButton.setId(0);

                            FrameLayout ll = (FrameLayout) findViewById(R.id.FrameLayout_search_room);
                            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                            ll.addView(myButton, lp);

                            myButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AlertDialog.Builder alert = new AlertDialog.Builder(SearchGroupActivity.this);

                                    alert.setTitle("그룹 비밀번호 입력");
                                    alert.setMessage("그룹 비밀번호를 입력하세요");

                                    final EditText groupPW = new EditText(SearchGroupActivity.this);
                                    alert.setView(groupPW);

                                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            String input_groupPW = groupPW.getText().toString();

                                            mSocket.emit("joinGroup", findGname, input_groupPW, id, name, phone);

                                            String filePath = "/data/data/com.example.fam/files/" + findGname + ".txt";
                                            File file = new File(filePath);

                                            try {
                                                file.createNewFile();
                                                Toast toast =Toast.makeText(getApplicationContext(),"성공", Toast.LENGTH_SHORT);
                                                toast.show();

                                            } catch(IOException ie){
                                                Toast toast =Toast.makeText(getApplicationContext(),"실패", Toast.LENGTH_SHORT);
                                                toast.show();
                                            }

                                            startActivity(new Intent(SearchGroupActivity.this, GroupListActivity.class));
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
        setContentView(R.layout.activity_search_group);

        mSocket.connect();

        button_search_room_search = (Button)findViewById(R.id.button_search_room_search);
        button_search_room_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText_search_room_groupCode = (EditText)findViewById(R.id.editText_search_room_groupCode);

                String groupCode = editText_search_room_groupCode.getText().toString();

                mSocket.emit("searchGroup", groupCode);

                mSocket.on("findGroup", findGroupName);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            pref.remove("GROUP_NAME");
            Intent intent = new Intent(SearchGroupActivity.this, GroupListActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
