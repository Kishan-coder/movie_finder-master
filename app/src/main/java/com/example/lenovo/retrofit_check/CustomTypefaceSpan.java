package com.example.lenovo.retrofit_check;

/**
 * Created by Lenovo on 2/1/2019.
 */

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;

public class CustomTypefaceSpan extends TypefaceSpan {
    int Size=0;

    public CustomTypefaceSpan(String family, int size) {
        super(family);
        Size=size;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        applyCustomTypeFace(ds, Size);
    }

    @Override
    public void updateMeasureState(TextPaint paint) {
        applyCustomTypeFace(paint, Size);
    }

    private static void applyCustomTypeFace(Paint paint, int size) {
        paint.setTextSize(size);
    }
}