package com.example.a1004y.fam_vertab;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;

public class MainActivity extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Resources res = getResources(); //리소스 객체 생성
        TabHost tabHost = getTabHost(); //탭을 붙이기위한 탭호스객체선언
        TabHost.TabSpec spec; //탭호스트에 붙일 각각의 탭스펙을 선언 ; 각 탭의 메뉴와 컨텐츠를 위한 객체
        Intent intent; //각탭에서 사용할 인텐트 선언

        //인텐트 생성
        intent = new Intent().setClass(this, HomeActivity.class);
        //각 탭의 메뉴와 컨텐츠를 위한 객체 생성
        spec = tabHost.newTabSpec("HOME").setIndicator("H").setContent(intent);
        tabHost.addTab(spec);


        //인텐트 생성
        intent = new Intent().setClass(this, TalkActivity.class);
        //각 탭의 메뉴와 컨텐츠를 위한 객체 생성
        spec = tabHost.newTabSpec("CHAT").setIndicator("T").setContent(intent);
        tabHost.addTab(spec);


        //인텐트 생성
        intent = new Intent().setClass(this, BoardActivity.class);
        //각 탭의 메뉴와 컨텐츠를 위한 객체 생성
        spec = tabHost.newTabSpec("BOARD").setIndicator("B").setContent(intent);
        tabHost.addTab(spec);


        //인텐트 생성
        intent = new Intent().setClass(this, CalendarActivity.class);
        //각 탭의 메뉴와 컨텐츠를 위한 객체 생성
        spec = tabHost.newTabSpec("CALENDAR").setIndicator("C").setContent(intent);
        tabHost.addTab(spec);


        //인텐트 생성
        intent = new Intent().setClass(this, AccountBookActivity.class);
        //각 탭의 메뉴와 컨텐츠를 위한 객체 생성
        spec = tabHost.newTabSpec("ACCOUNTBOOK").setIndicator("A").setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0); //현재화면에 보여질 탭의 위치를 결정

        //getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflater함수를 이용해서 menu 리소스를 menu로 변환.

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Intent intent = new Intent(MainActivity.this, GroupListActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}