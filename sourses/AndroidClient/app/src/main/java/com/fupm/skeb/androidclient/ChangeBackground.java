package com.fupm.skeb.androidclient;

public class ChangeBackground{

    public int choose(int key) {
        switch (key) {
            case 0:
                return R.drawable.ferma;
            case 1:
                return R.drawable.p1;
            case 2:
                return R.drawable.p10;
            case 3:
                return R.drawable.p11;
            case 4:
                return R.drawable.p4;
            case 5:
                return R.drawable.p5;
            case 6:
                return R.drawable.p12;
            case 7:
                return R.drawable.p7;
            case 8:
                return R.drawable.p8;
            case 9:
                return R.drawable.p9;
            default:
                return R.drawable.ferma;
        }
    }
}


