package com.suntelecoms.dynamicform;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.suntelecoms.dynamicform.materialLeanBack.MaterialLeanBack;

import java.util.ArrayList;

/**
 * Created by Djvmil_ on 2020-02-05
 */

public class GaleryAdapter extends MaterialLeanBack.Adapter<GaleryAdapter.GaleryViewHolder> {
    private Context ctx;
    private ArrayList<Galery> galeries;
    private int line;

    public GaleryAdapter(Context ctx, ArrayList<Galery> galeries, int line){
        this.ctx = ctx;
        this.galeries = galeries;
        this.line = line;
    }

    @Override
    public int getLineCount() {
        return line;
    }

    @Override
    public int getCellsCount(int line) {
        return galeries.size();
    }

    @Override
    public GaleryViewHolder onCreateViewHolder(ViewGroup viewGroup, int line) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_test, viewGroup, false);
        return new GaleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GaleryViewHolder viewHolder, int i) {
        viewHolder.textView.setText(galeries.get(i).getTitle());
        Picasso.with(viewHolder.imageView.getContext()).load(galeries.get(i).getLink()).into(viewHolder.imageView);
    }

    @Override
    public String getTitleForRow(int row) {
        return "Line " + row;
    }

    class GaleryViewHolder extends MaterialLeanBack.ViewHolder {

        TextView textView;
        ImageView imageView;

        GaleryViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
