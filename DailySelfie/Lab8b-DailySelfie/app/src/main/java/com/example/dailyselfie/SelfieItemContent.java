package com.example.dailyselfie;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class SelfieItemContent {

    public static final List<SelfieItem> ITEMS = new ArrayList<>();

    public static void addItem(SelfieItem item) {
        ITEMS.add(item);
    }

    public static SelfieItem createSelfieItem(String name, String path) {
        return new SelfieItem(name, path);
    }

    public static void clearAllItems() {
        ITEMS.clear();
    }

    public static boolean isEmpty() {
        return ITEMS.isEmpty();
    }

    public static class SelfieItem {

        public final String name;
        public final String imagePath;

        public SelfieItem(String name, String imagePath) {
            this.name = name;
            this.imagePath = imagePath;        }
    }
}
