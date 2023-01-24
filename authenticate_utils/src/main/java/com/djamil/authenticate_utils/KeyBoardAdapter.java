package com.djamil.authenticate_utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.djamil.authenticate_utils.interfaces.OnClickListener;
import com.djamil.utils.UtilsFunction;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @Author Moustapha S. Dieme ( Djvmil_ ) on 10/12/19.
 */
public class KeyBoardAdapter extends RecyclerView.Adapter<KeyBoardAdapter.MyViewHolder> {
    private static final String TAG = "KeyBoardAdapter";
    private Context mContext;
    private ArrayList<Integer> numList;
    private TextView textView;
    private Boolean userFgp = false;
    private int keyHeight = -1;
    private int keyWidth = -1;

    private int colorKey = 0;
    private Drawable iconFingerPrint;
    private Drawable iconNoFingerPrint;
    private Drawable iconBackSpace;

    private Boolean isShuffle;
    private OnClickListener onClickListener;

    public KeyBoardAdapter(Context context, TextView textView, Boolean isShuffle, int colorKey, Drawable iconBackSpace, Drawable iconFingerPrint, Drawable iconNoFingerPrint, boolean userFgp){
        this.mContext          = context;
        this.isShuffle         = isShuffle;
        this.userFgp           = userFgp;
        this.textView          = textView;
        this.numList           = getNumList();
        this.colorKey          = colorKey;

        this.iconBackSpace     = iconBackSpace;
        this.iconFingerPrint   = iconFingerPrint;
        this.iconNoFingerPrint = iconNoFingerPrint;
    }
    public KeyBoardAdapter(Context context, TextView textView, Boolean isShuffle, int colorKey, Drawable iconBackSpace, Drawable iconFingerPrint, Drawable iconNoFingerPrint, boolean userFgp, int keyHeight, int keyWidth){
        this.mContext          = context;
        this.isShuffle         = isShuffle;
        this.userFgp           = userFgp;
        this.textView          = textView;
        this.numList           = getNumList();
        this.colorKey          = colorKey;
        this.keyHeight         = keyHeight;
        this.keyWidth          = keyWidth;

        this.iconBackSpace     = iconBackSpace;
        this.iconFingerPrint   = iconFingerPrint;
        this.iconNoFingerPrint = iconNoFingerPrint;
    }

    public void shuffleKey(){
        this.numList = getNumList();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {
        TextView  key;
        Button backSpaceBtn, fingerPrintBtn;
        CardView mCardView;
        public MyViewHolder(final View parent) {
            super(parent);
            key          = itemView.findViewById(R.id.key);
            backSpaceBtn = itemView.findViewById(R.id.backspace);
            fingerPrintBtn = itemView.findViewById(R.id.finger);
            mCardView    = itemView.findViewById(R.id.cardviewClavier);

            if (colorKey != 0){
                key.setTextColor(colorKey);
                backSpaceBtn.setTextColor(colorKey);
                fingerPrintBtn.setTextColor(colorKey);
            }


            if (keyHeight != -1 && keyWidth != -1)  UtilsFunction.resizeView(mCardView, mContext.getResources().getDimensionPixelSize(keyWidth), mContext.getResources().getDimensionPixelSize(keyHeight));
            else if (keyWidth != -1) UtilsFunction.resizeView(mCardView, mContext.getResources().getDimensionPixelSize(keyWidth), mContext.getResources().getDimensionPixelSize(mCardView.getHeight()));
            else if (keyHeight != -1) UtilsFunction.resizeView(mCardView, mContext.getResources().getDimensionPixelSize(mCardView.getWidth()), mContext.getResources().getDimensionPixelSize(keyHeight));

            if (iconBackSpace != null)
                backSpaceBtn.setBackground(iconBackSpace);


        }

    }

    @Override
    public KeyBoardAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_key, parent, false);
        return new MyViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final int num = numList.get(position);

//        Log.e(TAG, "onBindViewHolder: color "+colorKey );
        holder.key.setText(String.format("%s", num));

        if (numList.get(position) == 10){
            holder.key.setVisibility(View.GONE);
            holder.backSpaceBtn.setVisibility(View.VISIBLE);
            holder.fingerPrintBtn.setVisibility(View.GONE);

        }else if (numList.get(position) == 11){
            holder.key.setVisibility(View.GONE);
            holder.backSpaceBtn.setVisibility(View.GONE);
            holder.fingerPrintBtn.setVisibility(View.VISIBLE);
            holder.fingerPrintBtn.setEnabled(true);

            if (userFgp){
                if (iconFingerPrint != null)
                    holder.fingerPrintBtn.setBackground(iconFingerPrint);
                else
                    holder.fingerPrintBtn.setBackground(mContext.getResources().getDrawable(R.drawable.ic_baseline_fingerprint_24));

            } else{
                holder.fingerPrintBtn.setVisibility(View.GONE);
                holder.fingerPrintBtn.setEnabled(false);
                    //holder.fingerPrintBtn.setBackground(mContext.getResources().getDrawable(R.drawable.ic_baseline_no_fingerprint_24));
            }
        }

        holder.fingerPrintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (new SessionManager().getKeyUseFingerprint() && userFgp){
                    if (onClickListener != null)
                        onClickListener.keyFingerPrintClicked();
                }else if(!(new SessionManager().getKeyUseFingerprint()) && userFgp)
                    Toast.makeText(mContext, "Le code secret est requis", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(mContext, "Non disponnible", Toast.LENGTH_SHORT).show();

            }
        });

        holder.backSpaceBtn.setLongClickable(true);
        holder.backSpaceBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (numList.get(position) == 10 && textView.getText().length() > 0)
                    textView.setText("");
                return false;
            }
        });

        holder.backSpaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textView.getText().length() > 0)
                    textView.setText(String.format("%s", textView.getText().toString().substring(0, textView.getText().length()-1)));
            }
        });

        holder.mCardView.setLongClickable(true);
        holder.mCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (numList.get(position) == 10 && textView.getText().length() > 0)
                    textView.setText("");
                return false;
            }
        });


        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.e(TAG, "onClick key: "+num );

                if (numList.get(position) == 10 && textView.getText().length() > 0)
                    textView.setText(String.format("%s", textView.getText().toString().substring(0, textView.getText().length()-1)));

                else if (numList.get(position) == 11){
                    if (onClickListener != null)
                        onClickListener.keyFingerPrintClicked();

                } else if (num != 10 && num != 11)
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

    public OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

//    private void resizeView(View view, float newWidth, float newHeight) {
//        try {
//            Log.e(TAG, "resizeView: newHeight = "+newHeight+" -- newWidth = "+newWidth );
//            Log.e(TAG, "resizeView: newHeight = "+view.getHeight()+" -- newWidth = "+view.getWidth() );
//            Constructor<? extends ViewGroup.LayoutParams> ctor = view.getLayoutParams().getClass().getDeclaredConstructor(int.class, int.class);
//            view.setLayoutParams(ctor.newInstance(newWidth, newHeight));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}