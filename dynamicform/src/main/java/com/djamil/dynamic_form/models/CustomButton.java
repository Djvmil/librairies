package com.djamil.dynamic_form.models;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;

/**
 * @Author Moustapha S. Dieme ( Djvmil_ ) on 10/12/19.
 */
public class CustomButton {
    @ColorRes
    int textColor = -1;
    @ColorRes
    int backgroundTint = -1;
    @DrawableRes
    int background = -1;

    String text;

    float textSize = -1;

    public CustomButton() {
    }
    public CustomButton(int textColor, int backgroundTint, int background, String text, float textSize) {
        this.textColor = textColor;
        this.backgroundTint = backgroundTint;
        this.background = background;
        this.text = text;
        this.textSize = textSize;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getBackgroundTint() {
        return backgroundTint;
    }

    public void setBackgroundTint(int backgroundTint) {
        this.backgroundTint = backgroundTint;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    @Override
    public String toString() {
        return "CustomButton{" +
                "textColor=" + textColor +
                ", backgroundTint=" + backgroundTint +
                ", background=" + background +
                ", text='" + text + '\'' +
                ", textSize=" + textSize +
                '}';
    }
}
