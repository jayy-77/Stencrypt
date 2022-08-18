package com.example.stencrypt;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StensAdapter extends RecyclerView.Adapter<StensAdapter.ViewHolder>{
    private ArrayList<StenShareObject> stenData = new ArrayList<>();
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
    public StensAdapter(ArrayList<StenShareObject> stenData) {
        this.stenData = stenData;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.sten_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.stenImage.setImageBitmap(StringToBitMap(stenData.get(position).getBitString()));
        holder.name.setText(stenData.get(position).getName());
        holder.email.setText(stenData.get(position).getEmail());
        holder.date.setText(stenData.get(position).getDate());
        holder.time.setText(stenData.get(position).getTime());
    }


    @Override
    public int getItemCount() {
        return stenData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView stenImage;
        public TextView name,email,time,date;
        public ViewHolder(View itemView) {
            super(itemView);
            stenImage = itemView.findViewById(R.id.stenImage);
            name = itemView.findViewById(R.id.sendrerName);
            email = itemView.findViewById(R.id.senderEmail);
            time= itemView.findViewById(R.id.timeView);
            date = itemView.findViewById(R.id.dateView);
        }
    }
}