package com.xyf.model;

import java.io.Serializable;

/**
 * Created by shxiayf on 2015/11/5.
 */
public class VideoItemModel implements Serializable {

    private String name;
    private String imageUri;
    private String videoPath;

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
