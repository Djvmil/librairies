package com.djamil.dynamickeyboard;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by Djvmil_ on 2020-03-05
 */
public class KeyboardDynamic {

    public final static Boolean SHUFFLE = true;
    public final static Boolean NORMAL  = false;

    /**
     *
     * @param ctx
     * @param view emplacement du clavier dans le layout
     * @param editText champs du saisi
     * @param isShuffle dynamic ou normal
     */
    public static void showKeyBoard(Context ctx, ViewGroup view, EditText editText, Boolean isShuffle){
        view.removeAllViews();
        View keyBoadView = ((Activity)ctx).getLayoutInflater().inflate(R.layout.input_keyboard, null);
        view.addView(keyBoadView);
        view.setVisibility(View.VISIBLE);

        RecyclerView recyclerView = keyBoadView.findViewById(R.id.recyclerviewKeyBoard);
        KeyBoardAdapter keyBoardAdapter = new KeyBoardAdapter(ctx, editText, isShuffle );
        recyclerView.setLayoutManager(new GridLayoutManager(ctx, 4));
        recyclerView.setAdapter(keyBoardAdapter);
    }
}
