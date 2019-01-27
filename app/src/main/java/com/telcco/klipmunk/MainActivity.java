package com.telcco.klipmunk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.TextureView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.TextView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.tagline)
    TextView tagline;
    @BindView(R.id.terms)
    CheckBox terms;
    @BindString(R.string.tagLine)
    String tagline_str;
    @BindString(R.string.terms)
    String terms_str;
    @BindView(R.id.signup)
    Button signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        tagline.setText(Html.fromHtml(tagline_str));
        terms.setText(Html.fromHtml(terms_str));



    }
    @OnClick(R.id.signup)
    public void onSignUp(){
        Intent in = new Intent(MainActivity.this,ViewArticles.class);
        startActivity(in);
        finish();
    }
}
