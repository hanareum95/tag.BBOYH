package com.example.a1004y.fam_vertab;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceClickListener {

    Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://10.0.2.2:3000");
        } catch (URISyntaxException e) {}
    }
    PreferenceClass pref = new PreferenceClass(SettingsActivity.this);

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        mSocket.connect();

        Preference box_famAccount = (Preference)findPreference("setting_personalInformation_famAccount");
        box_famAccount.setOnPreferenceClickListener(this);

        Preference box_logout = (Preference)findPreference("setting_personalInformation_logout");
        box_logout.setOnPreferenceClickListener(this);

        Preference box_dropout = (Preference)findPreference("setting_personalInformation_dropout");
        box_dropout.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {

        switch(preference.getKey()) {
            case "setting_personalInformation_famAccount":
                startActivity(new Intent(SettingsActivity.this, UserAccountActivity.class));
                break;

            case "setting_personalInformation_logout":
                pref.remove("ID");
                pref.remove("PW");
                pref.remove("NAME");
                pref.remove("PHONE");
                mSocket.disconnect();
                startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
                break;

            case "setting_personalInformation_dropout":
                mSocket.emit("deleteAccount", pref.getValue("ID", ""), pref.getValue("NAME", ""));
                pref.remove("ID");
                pref.remove("PW");
                pref.remove("NAME");
                pref.remove("PHONE");
                mSocket.disconnect();
                startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
                break;
        }
        return false;
    }

}