package com.example.dailyselfie;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class SelfieItemContent {

    public static final List<SelfieItem> ITEMS = new ArrayList<>();

    public static void addItem(SelfieItem item) {
        ITEMS.add(item);
    }

    public static SelfieItem createSelfieItem(String name, Bitmap imageBitmap) {
        return new SelfieItem(name, imageBitmap);
    }

    public static void clearAllItems() {
        ITEMS.clear();
    }

    public static boolean isEmpty() {
        return ITEMS.isEmpty();
    }

    public static class SelfieItem {

        public final String name;
        public final Bitmap imageBitmap;

        public SelfieItem(String name, Bitmap imageBitmap) {
            this.name = name;
            this.imageBitmap = imageBitmap;
        }
    }
}
