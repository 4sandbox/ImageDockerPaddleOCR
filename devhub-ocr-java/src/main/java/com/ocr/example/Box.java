package com.ocr.example;
import java.util.List;

public class Box {
    private List<Float> topLeft;
    private List<Float> topRight;
    private List<Float> bottomRight;
    private List<Float> bottomLeft;

    // Getters và Setters
    public List<Float> getTopLeft() {
        return topLeft;
    }

    public void setTopLeft(List<Float> topLeft) {
        this.topLeft = topLeft;
    }

    public List<Float> getTopRight() {
        return topRight;
    }

    public void setTopRight(List<Float> topRight) {
        this.topRight = topRight;
    }

    public List<Float> getBottomRight() {
        return bottomRight;
    }

    public void setBottomRight(List<Float> bottomRight) {
        this.bottomRight = bottomRight;
    }

    public List<Float> getBottomLeft() {
        return bottomLeft;
    }

    public void setBottomLeft(List<Float> bottomLeft) {
        this.bottomLeft = bottomLeft;
    }
}
