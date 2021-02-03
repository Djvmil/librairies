package com.djamil.dynamickeyboard;

import android.content.Context;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

public class KeyBoardAdapter extends RecyclerView.Adapter<KeyBoardAdapter.MyViewHolder> {
    private static final String TAG = "KeyBoardAdapter";
    private Context mContext;
    private ArrayList<Integer> numList;
    private TextView textView;
    private Boolean tmp = false;
    private int colorKey = -1;

    private Boolean isShuffle;

    public KeyBoardAdapter(Context context, TextView textView, Boolean isShuffle, int colorKey){
        this.mContext=context;
        this.isShuffle = isShuffle;
        this.textView = textView;
        this.colorKey = colorKey;
        this.numList = getNumList();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {
        TextView  key;
        Button backSpaceBtn, doneBtn;
        CardView mCardView;
        public MyViewHolder(final View parent) {
            super(parent);
            key          = itemView.findViewById(R.id.key);
            backSpaceBtn = itemView.findViewById(R.id.backspace);
            doneBtn      = itemView.findViewById(R.id.done);
            mCardView    = itemView.findViewById(R.id.cardviewClavier);

        }
    }
    @Override
    public KeyBoardAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_button, parent, false);
        return new MyViewHolder(mView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final int num = numList.get(position);
        if (colorKey != -1){
            holder.key.setTextColor(colorKey);
            holder.backSpaceBtn.setTextColor(colorKey);
            holder.doneBtn.setTextColor(colorKey);
        }
        holder.key.setText(String.format("%s", num));

        if (numList.get(position) == 10){
            holder.key.setVisibility(View.GONE);
            holder.backSpaceBtn.setVisibility(View.VISIBLE);
            holder.doneBtn.setVisibility(View.GONE);

        }else if (numList.get(position) == 11){
            if (isShuffle){
                holder.key.setVisibility(View.GONE);
                holder.backSpaceBtn.setVisibility(View.GONE);
                holder.doneBtn.setVisibility(View.VISIBLE);
            }else {
                holder.key.setText("+");
            }
        }

        holder.doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isShuffle)
                    if (tmp){
                        //Show Password
                        textView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        tmp = false;
                    }else{
                        //Hide Password
                        textView.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        tmp = true;
                    }
                else
                    textView.setText(String.format("%s%s", textView.getText(), "+"));
            }
        });

        holder.backSpaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (textView.getText().length() > 0)
                    textView.setText(String.format("%s", textView.getText().toString().substring(0, textView.getText().length()-1)));
            }
        });

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (numList.get(position) == 10 && textView.getText().length() > 0)
                    textView.setText(String.format("%s", textView.getText().toString().substring(0, textView.getText().length()-1)));

                else if (numList.get(position) == 11)
                    textView.setText(String.format("%s%s", textView.getText(), "+"));

                else
                    textView.setText(String.format("%s%s", textView.getText(), num));

            }
        });
    }

    @Override
    public int getItemCount() {
        return numList.size();
    }

    private ArrayList<Integer> getNumList() {
        this.numList = new ArrayList<>();

        for (int i = 0; i < 10 ; i++){
            if (!isShuffle){
               if(i == 4)
                   numList.add(10);

               else if(i == 7)
                   numList.add(0);

                i = i == 0 ? i+1 : i;
            }

            numList.add(i);
        }

        if(isShuffle){
            Collections.shuffle(numList);
            numList.add(numList.get(3));
            numList.set(3, 10);
        }

        numList.add(11);

        return numList;
    }
}