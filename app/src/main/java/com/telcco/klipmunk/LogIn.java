package com.telcco.klipmunk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LogIn extends AppCompatActivity {
    @BindView(R.id.admin)
    TextView tag_login;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.signUp_text)
    TextView signUp_text;
    @BindString(R.string.tagLine)
    String tagline_str;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        ButterKnife.bind(this);
        tag_login.setText(Html.fromHtml(tagline_str));
    }
    @OnClick(R.id.signUp_text)
    public void onSignUp(){
        Intent in = new Intent(LogIn.this,MainActivity.class);
        startActivity(in);
        finish();
    }
    @OnClick(R.id.login)
    public void onLogIN(){
        Intent in = new Intent(LogIn.this,ViewArticles.class);
        startActivity(in);
        finish();
    }
}
