package com.mustafasuleymankinik.orderapp;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdminPanelAdapter extends RecyclerView.Adapter<AdminPanelAdapter.ViewHolder> {

    ArrayList<Model> modelArrayList;
    public AdminPanelAdapter(ArrayList<Model> modelArrayList)
    {
        this.modelArrayList=modelArrayList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_adater_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);

    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView val1TextView,val2TextView;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            val1TextView=itemView.findViewById(R.id.list_itemTextView1);
            val2TextView=itemView.findViewById(R.id.list_itemTextView2);
            imageView=itemView.findViewById(R.id.list_itemImageView);

        }

        public void bind(int position) {
            val2TextView.setText(modelArrayList.get(position).value2);
            val1TextView.setText(modelArrayList.get(position).value1);
            Picasso.get().load(modelArrayList.get(position).uri).into(imageView);
        }
    }
}
