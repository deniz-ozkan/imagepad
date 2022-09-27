package com.denizozkan.imagepad;

import android.graphics.Bitmap;

// Eklenen görsellerin program arayüzüne uygun hale getirilmesi ve kapladığı alanı küçültmek için yeniden boyutlandırılması

public class imageKucult {

    public Bitmap imageKucult(Bitmap secilenGorsel, int maximumSize) {

        int genislik = secilenGorsel.getWidth();
        int uzunluk = secilenGorsel.getHeight();

        float bitmapRatio = (float) genislik / (float) uzunluk;

        if (bitmapRatio > 1) {
            genislik = maximumSize;
            uzunluk = (int) (genislik / bitmapRatio);
        } else {
            uzunluk = maximumSize;
            genislik = (int) (uzunluk * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(secilenGorsel,genislik,uzunluk,true);
    }
}
