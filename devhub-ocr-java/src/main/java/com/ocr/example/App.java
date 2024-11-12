package com.ocr.example;

import java.io.*;
import java.nio.file.Files;
import java.util.Base64;

public final class App {
    private App() {}

    public static void main(String[] args) {
        // Ví dụ imagePath truyền vào (cần thay đổi với từng ảnh bạn muốn xử lý)
        String imagePath = "a.jpg";
        runOCRContainer(imagePath);
    }

    public static void runOCRContainer(String imagePath) {
        try {
            // Kiểm tra ảnh tồn tại trước khi chạy lệnh
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

            // Đọc file ảnh và chuyển đổi sang Base64
            byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            // Tạo ProcessBuilder để chạy Docker và truyền base64Image vào STDIN
            ProcessBuilder processBuilder = new ProcessBuilder("docker", "run", "--rm", "4sandbox/devhub-ocr");
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // Ghi Base64 vào STDIN của Docker container
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()))) {
                writer.write(base64Image);
                writer.flush();
            }

            // Đọc kết quả đầu ra từ STDOUT của Docker container
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("[OUTPUT] " + line);
                }
            }

            // Chờ container hoàn tất xử lý ảnh
            int exitCode = process.waitFor();
            System.out.println("Container kết thúc với mã thoát: " + exitCode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Phương thức kiểm tra xem Docker image đã tồn tại chưa
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

    // Phương thức pull Docker image
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
