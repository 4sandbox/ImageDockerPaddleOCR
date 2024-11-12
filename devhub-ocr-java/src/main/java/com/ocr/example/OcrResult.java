package com.ocr.example;

import java.util.List;

public class OcrResult {
    private List<List<Float>> box;  // Mảng 2D cho các tọa độ
    private String text;            // Văn bản
    private double score;           // Điểm

    // Getters and setters
    public List<List<Float>> getBox() {
        return box;
    }

    public void setBox(List<List<Float>> box) {
        this.box = box;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "OcrResult{" +
                "box=" + box +
                ", text='" + text + '\'' +
                ", score=" + score +
                '}';
    }
}
