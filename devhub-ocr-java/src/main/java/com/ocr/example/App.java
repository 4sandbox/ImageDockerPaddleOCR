package com.ocr.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class App {
    private App() {
    }

    public static void main(String[] args) {
        // Đường dẫn file ảnh cần xử lý
        String imagePath = "a.jpg"; // Thay đổi tên file ảnh cho phù hợp
        runOCRContainer(imagePath);
    }

    public static void runOCRContainer(String imagePath) {
        try {
            // Kiểm tra sự tồn tại của file ảnh
            File imageFile = new File(imagePath);
            if (!imageFile.exists()) {
                System.out.println("File không tồn tại: " + imagePath);
                return;
            }

            // Kiểm tra xem Docker image đã tồn tại chưa
            if (!dockerImageExists("4sandbox/devhub-ocr")) {
                System.out.println("Docker image 4sandbox/devhub-ocr chưa tồn tại. Đang pull...");
                pullDockerImage("4sandbox/devhub-ocr");
            } else {
                System.out.println("Docker image 4sandbox/devhub-ocr đã tồn tại.");
            }

            // Tạo tên file duy nhất cho ảnh
            String uniqueFileName = UUID.randomUUID().toString() + ".jpg";

            // Chạy container với file ảnh mount vào tên file duy nhất
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "docker", "run", "--rm",
                    "-v", imageFile.getAbsolutePath() + ":/app/" + uniqueFileName, // Mount file ảnh vào container với
                                                                                   // tên duy nhất
                    "4sandbox/devhub-ocr", "python", "appv2.py", "/app/" + uniqueFileName, "--lang", "vi");
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // Đọc kết quả đầu ra từ STDOUT của Docker container
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                StringBuilder output = new StringBuilder();
                String line;
                StringBuilder result = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    result.append(line).append("\n");
                }
                String regex = "\\{.*?\\}";  // Regex để tìm đối tượng JSON
                Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);  // DOTALL để xử lý các dòng nhiều dòng
                Matcher matcher = pattern.matcher(result.toString());
        
                List<OcrResult> ocrResults = new ArrayList<>();
                ObjectMapper objectMapper = new ObjectMapper();
        
                // Lọc ra tất cả các đối tượng JSON hợp lệ và chuyển chúng thành đối tượng Java
                while (matcher.find()) {
                    String jsonString = matcher.group();  // Lấy chuỗi JSON
        
                    try {
                        // Chuyển chuỗi JSON thành đối tượng OcrResult
                        OcrResult ocrResult = objectMapper.readValue(jsonString, OcrResult.class);
                        ocrResults.add(ocrResult);
                    } catch (IOException e) {
                        System.err.println("Error parsing JSON: " + e.getMessage());
                    }
                }
        
                // In kết quả
                for (OcrResult result1 : ocrResults) {
                    System.out.println(result1);
                }
              //  System.out.println("[OUTPUT] " + output.toString().trim());
            }

            // Chờ container hoàn tất xử lý ảnh
            int exitCode = process.waitFor();
            System.out.println("Container kết thúc với mã thoát: " + exitCode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Kiểm tra sự tồn tại của Docker image
    public static boolean dockerImageExists(String imageName) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("docker", "images", "-q", imageName);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            process.waitFor();
            return (line != null && !line.isEmpty());

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    // Phương thức pull Docker image từ Docker Hub
    public static void pullDockerImage(String imageName) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("docker", "pull", imageName);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            process.waitFor();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
