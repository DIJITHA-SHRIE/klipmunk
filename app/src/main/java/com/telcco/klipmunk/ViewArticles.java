package com.telcco.klipmunk;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewArticles extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DataBaseHelper db;
    ArrayList<String> items;
    int[] drawableList = new int[]{R.drawable.recent,R.drawable.news,R.drawable.receipt,R.drawable.proof,R.drawable.article,
            R.drawable.ic_send_black_24dp,R.drawable.ic_add_black_24dp};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_articles);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView title_toolbar =(TextView)toolbar.findViewById(R.id.title_toolbar);
        title_toolbar.setText("#Recent");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        db = new DataBaseHelper(ViewArticles.this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        Menu drawerMenu = navigationView.getMenu();
        items= new ArrayList<>();
        items=db.getTag();
        if(items.size()>=7){

        }
        else{
            db.insertTag(getResources().getString(R.string.recent));
            db.insertTag(getResources().getString(R.string.news));
            db.insertTag(getResources().getString(R.string.reciepts));
            db.insertTag(getResources().getString(R.string.proof));
            db.insertTag(getResources().getString(R.string.articles));
            db.insertTag(getResources().getString(R.string.send));
            db.insertTag(getResources().getString(R.string.addViews));
            items=db.getTag();
        }
        for(int i =0;i<items.size();i++){
            MenuItem menuitem =drawerMenu.add(items.get(i));
            if(i<=6){
            menuitem.setIcon(getResources().getDrawable(drawableList[i]));
            }
            if(i>6){
                menuitem.setIcon(getResources().getDrawable(R.drawable.icon));
            }
        }
        drawer.closeDrawers();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.view_articles, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//        Fragment fragment = null;
//
//        if (id == R.id.recent) {
//           fragment = new NewsFragment();
//
//        } else if (id == R.id.news) {
//
//        } else if (id == R.id.reciept) {
//
//        } else if (id == R.id.proof) {
//
//        } else if (id == R.id.articles) {
//
//        } else if (id == R.id.send) {
//
//        }
//        FragmentTransaction ft  = getFragmentManager().beginTransaction();
//        ft.replace(R.id.content,fragment).commit();

        for(int i =0;i<items.size();i++){
            if(item.toString()==items.get(i) && item.toString()!=getResources().getString(R.string.addViews)){
                Log.i("items",items.get(i));
                Fragment fragment = NewsFragment.newInstance(items.get(i));
                FragmentTransaction ft  = getFragmentManager().beginTransaction();
                ft.replace(R.id.content,fragment).commit();

            }
            if(item.toString().equals(getResources().getString(R.string.addViews))){
                showTagDialog();

            }
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showTagDialog() {
        LayoutInflater li = LayoutInflater.from(this);
        View view = li.inflate(R.layout.tag_dialog,null);
        AlertDialog.Builder alertdialogBuilder = new AlertDialog.Builder(this);
        alertdialogBuilder.setView(view);
        final EditText tag_edit = (EditText)view.findViewById(R.id.tag_edit);
        alertdialogBuilder.setCancelable(false);
        alertdialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if(tag_edit.getText().toString().length()>0)
                {
                db.insertTag(tag_edit.getText().toString());
                }

                Intent in = new Intent(ViewArticles.this,ViewArticles.class);
                startActivity(in);
                finish();

            }
        });
        alertdialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                Intent in = new Intent(ViewArticles.this,ViewArticles.class);
                startActivity(in);
                finish();
            }
        });
        AlertDialog alertDialog = alertdialogBuilder.create();
        alertDialog.show();
    }
}
