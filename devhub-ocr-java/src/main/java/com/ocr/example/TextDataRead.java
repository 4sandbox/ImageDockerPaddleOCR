package com.ocr.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TextDataRead {
    public static List<TextData> readFormJsonString(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<TextData> textDataList = new ArrayList<>();
        try {
            // Chuyển đổi chuỗi JSON thành danh sách các đối tượng TextData
            textDataList = objectMapper.readValue(json,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, TextData.class));

            // In ra kết quả
            for (TextData textData : textDataList) {
                System.out.println("Text: " + textData.getText());
                System.out.println("Score: " + textData.getScore());
                System.out.println("Box: " + textData.getBox());
                System.out.println("-----------");
            }
            return textDataList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return textDataList;
    }
    
    public static List<TextData> readFormJsonStringv2(String inputString) {
        // Sử dụng regex để tìm các đối tượng JSON trong chuỗi

        // Sử dụng regex để tìm các đối tượng JSON trong chuỗi
        String regex = "\\{.*?\\}"; // Biểu thức chính quy để tìm đối tượng JSON
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL); // DOTALL để xử lý các dòng nhiều dòng
        Matcher matcher = pattern.matcher(inputString);
        List<TextData> textDataList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper(); // Jackson ObjectMapper

        // Lặp qua tất cả các đối tượng JSON tìm thấy
        while (matcher.find()) {
            String jsonString = matcher.group(); // Lấy chuỗi JSON

            try {
                // Chuyển đổi JSON thành đối tượng Java
                TextData textData = objectMapper.readValue(jsonString, TextData.class);
                textDataList.add(textData); // Thêm vào danh sách
            } catch (IOException e) {
                System.err.println("Error parsing JSON: " + e.getMessage());
            }
        }
        return textDataList;
    }
}
