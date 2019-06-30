package com.example.dailyselfie;

import java.util.ArrayList;
import java.util.List;

public class SelfieItemContent {

    public static final List<SelfieItem> ITEMS = new ArrayList<SelfieItem>();

    private static final int COUNT = 5;

    static {
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(SelfieItem item) {
        ITEMS.add(item);
    }

    private static SelfieItem createDummyItem(int position) {
        return new SelfieItem(position, R.drawable.picture, "Picture name");
    }


    public static class SelfieItem {

        public final int id;
        public final int imageId;
        public final String details;

        public SelfieItem(int id, int imageId, String details) {
            this.id = id;
            this.imageId = imageId;
            this.details = details;
        }

        @Override
        public String toString() {
            return "SelfieItem{" +
                    "id=" + id +
                    ", imageId=" + imageId +
                    ", details='" + details + '\'' +
                    '}';
        }
    }
}
