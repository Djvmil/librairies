package com.suntelecoms.dynamicform.materialLeanBack;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.suntelecoms.dynamicform.R;

/**
 * Created by Djvmil_ on 2020-02-05
 */

public class MaterialLeanBackSettings {

    public Integer titleColor;
    public int titleSize;
    public boolean animateCards;
    public boolean overlapCards;
    public int elevationReduced;
    public int elevationEnlarged;

    public Integer backgroundId;
    public Float backgroundOverlay;
    public Integer backgroundOverlayColor;

    public Integer paddingTop;
    public Integer paddingBottom;
    public Integer paddingLeft;
    public Integer paddingRight;

    public Integer lineSpacing;

    protected void handleAttributes(Context context, AttributeSet attrs) {
        try {
            TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.MaterialLeanBack);//ici

            {
                if(styledAttrs.hasValue(R.styleable.MaterialLeanBack_mlb_titleColor))//ici
                    titleColor = styledAttrs.getColor(R.styleable.MaterialLeanBack_mlb_titleColor, -1);//ici
                titleSize = styledAttrs.getDimensionPixelSize(R.styleable.MaterialLeanBack_mlb_titleSize, -1);//ici
                animateCards = styledAttrs.getBoolean(R.styleable.MaterialLeanBack_mlb_animateCards, true);//ici
                overlapCards = styledAttrs.getBoolean(R.styleable.MaterialLeanBack_mlb_overlapCards, true);//ici
                elevationEnlarged = styledAttrs.getInteger(R.styleable.MaterialLeanBack_mlb_cardElevationEnlarged, 8);//ici
                elevationReduced = styledAttrs.getInteger(R.styleable.MaterialLeanBack_mlb_cardElevationReduced, 5);//ici

                {
                    if (styledAttrs.hasValue(R.styleable.MaterialLeanBack_mlb_paddingTop))//ici
                        paddingTop = styledAttrs.getDimensionPixelOffset(R.styleable.MaterialLeanBack_mlb_paddingTop, -1);//ici
                    if (styledAttrs.hasValue(R.styleable.MaterialLeanBack_mlb_paddingBottom))//ici
                        paddingBottom = styledAttrs.getDimensionPixelOffset(R.styleable.MaterialLeanBack_mlb_paddingBottom, -1);//ici
                    if (styledAttrs.hasValue(R.styleable.MaterialLeanBack_mlb_paddingLeft))//ici
                        paddingLeft = styledAttrs.getDimensionPixelOffset(R.styleable.MaterialLeanBack_mlb_paddingLeft, -1);//ici
                    if (styledAttrs.hasValue(R.styleable.MaterialLeanBack_mlb_paddingRight))//ici
                        paddingRight = styledAttrs.getDimensionPixelOffset(R.styleable.MaterialLeanBack_mlb_paddingRight, -1);//ici
                }

                {
                    if (styledAttrs.hasValue(R.styleable.MaterialLeanBack_mlb_background))//ici
                        backgroundId = styledAttrs.getResourceId(R.styleable.MaterialLeanBack_mlb_background, -1);//ici

                    if (styledAttrs.hasValue(R.styleable.MaterialLeanBack_mlb_backgroundOverlay))//ici
                        backgroundOverlay = styledAttrs.getFloat(R.styleable.MaterialLeanBack_mlb_backgroundOverlay, -1);//ici

                    if (styledAttrs.hasValue(R.styleable.MaterialLeanBack_mlb_backgroundOverlayColor))//ici
                        backgroundOverlayColor = styledAttrs.getColor(R.styleable.MaterialLeanBack_mlb_backgroundOverlayColor, -1);//ici
                }

                if(styledAttrs.hasValue(R.styleable.MaterialLeanBack_mlb_lineSpacing))//ici
                    lineSpacing = styledAttrs.getDimensionPixelOffset(R.styleable.MaterialLeanBack_mlb_lineSpacing, -1);//ici
            }

            styledAttrs.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
