package com.telcco.klipmunk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by PHD on 12/23/2018.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    Activity context;
    ArrayList<ScreensModel> getScreens_Path = new ArrayList<>();


    public void updateList(ArrayList<ScreensModel> list){
        getScreens_Path = list;
        notifyDataSetChanged();
    }

    public NewsAdapter(Activity context, ArrayList<ScreensModel> getScreens_Path) {
        this.context = context;
        this.getScreens_Path = getScreens_Path;

    }

    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_xml, parent, false);
        NewsAdapter.ViewHolder news_adapter = new NewsAdapter.ViewHolder(view);
        return news_adapter;
    }

    @Override
    public void onBindViewHolder(NewsAdapter.ViewHolder holder, final int position) {
//        holder.screens.setImageResource(R.drawable.ic_menu_camera);
        File file = new File(getScreens_Path.get(position).getPath());
        Uri imageuri = Uri.fromFile(file);
        Log.i("imageuri", imageuri + "");
        Picasso.with(context).load(imageuri).into(holder.screens);
        holder.screen_notes.setText(getScreens_Path.get(position).getNotes());

        holder.screens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(context,ScreenFullView.class);
                in.putExtra("Image_Path_For_FullView",getScreens_Path.get(position).getPath());
                context.startActivity(in);
            }
        });



    }

    @Override
    public int getItemCount() {
        return getScreens_Path.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.screens_id)
        ImageView screens;
        @BindView(R.id.screen_notes)
        TextView screen_notes;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
