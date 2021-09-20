package com.example.elcapi;

public class jnielc
{

    private static final int seek_red_right = 0xa1;
    private static final int seek_green_right = 0xa2;
    private static final int seek_blue_right = 0xa3;
    private static final int seek_green_blue_right = 0xa4;
    private static final int seek_red_blue_right = 0xa5;
    private static final int seek_red_green_right = 0xa6;
    private static final int seek_all_right = 0xa7;

    private static final int seek_red_left = 0xb1;
    private static final int seek_green_left = 0xb2;
    private static final int seek_blue_left = 0xb3;
    private static final int seek_green_blue_left = 0xb4;
    private static final int seek_red_blue_left = 0xb5;
    private static final int seek_red_green_left = 0xb6;
    private static final int seek_all_left = 0xb7;

    public final static native int ledoff();
    public final static native int seekstart();
    public final static native int seekstop();
    public final static native int ledseek(int flag, int progress);

    static {
        System.loadLibrary("jnielc");
    }
}
