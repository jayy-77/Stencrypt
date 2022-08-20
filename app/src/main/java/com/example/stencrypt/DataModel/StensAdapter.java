package com.example.stencrypt.DataModel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stencrypt.Activities.HomePage;
import com.example.stencrypt.R;

import java.util.ArrayList;

public class StensAdapter extends RecyclerView.Adapter<StensAdapter.ViewHolder>{
    private ArrayList<StenShareObject> stenData = new ArrayList<>();
    Context context;
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
    public StensAdapter(ArrayList<StenShareObject> stenData, Context context) {
        this.stenData = stenData;
        this.context = context;
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
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View view) {
                ((HomePage) context).openDecodeFragmentWithData(StringToBitMap(stenData.get(position).getBitString()));
//                ((HomePage) context).tmpKey = stenData.get(position).getKey();
            }
        });
    }


    @Override
    public int getItemCount() {
        return stenData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView stenImage;
        public TextView name,email,time,date;
        public ConstraintLayout layout;
        public ViewHolder(View itemView) {
            super(itemView);
            stenImage = itemView.findViewById(R.id.stenImage);
            name = itemView.findViewById(R.id.sendrerName);
            email = itemView.findViewById(R.id.senderEmail);
            time= itemView.findViewById(R.id.timeView);
            date = itemView.findViewById(R.id.dateView);
            layout = itemView.findViewById(R.id.itemLayout);
        }
    }
}