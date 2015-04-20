package com.andhradroid.app.staggergridview;

import java.util.List;

/**
 * Created by pcs-03 on 20/4/15.
 */
public class Data {

    private List<Holder> mHolder;

    public List<Holder> getmHolder() {
        return mHolder;
    }

    public void setmHolder(List<Holder> mHolder) {
        this.mHolder = mHolder;
    }

    public static class Holder {
        private String imageFilename;
        private String title;


        public String getImageFilename() {
            return imageFilename;
        }

        public void setImageFilename(String imageFilename) {
            this.imageFilename = imageFilename;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}


