package com.suntelecoms.dynamicform;


/**
 * Created by djvmil on 18/08/19.
 */

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.suntelecoms.td.dynamic_form.models.MenuFacturierObject;

import java.util.ArrayList;

public class RecyclerViewMenuFacturier extends RecyclerView.Adapter<RecyclerViewMenuFacturier.MyViewHolder> {

    private Context context;
    private ArrayList<MenuFacturierObject> mData;
    private static final String BASE_URL = "";


    @NonNull
    @Override
    public  RecyclerViewMenuFacturier.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(context);
        view = mInflater.inflate(R.layout.item_menu_facturier,parent,false);

        return new RecyclerViewMenuFacturier.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewMenuFacturier.MyViewHolder MyViewHolder , final int position) {

        Picasso.with(context)
                .load( mData.get(position).getUrlImage())
                //.placeholder(R.drawable.ic_insert_emoticon_black_24dp)
                //.error(R.drawable.ic_sentiment_very_dissatisfied_black_24dp)
                .into(MyViewHolder.icon);

        MyViewHolder.title.setText(mData.get(position).getName());

        MyViewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, FormulaireFacturierActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();    }

    public RecyclerViewMenuFacturier(Context context, ArrayList<MenuFacturierObject> mData) {
        this.context = context;
        this.mData = mData;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView title;
        RelativeLayout relativeLayout;

        public MyViewHolder(View itemView) {
            super(itemView);

            icon           = itemView.findViewById(R.id.icon);
            title          = itemView.findViewById(R.id.title);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }

    }
}
