package com.ocr.example;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

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

            // Kiểm tra xem image đã tồn tại chưa
            if (!dockerImageExists("4sandbox/devhub-ocr")) {
                System.out.println("Docker image 4sandbox/devhub-ocr chưa tồn tại. Đang pull...");
                pullDockerImage("4sandbox/devhub-ocr");
            } else {
                System.out.println("Docker image 4sandbox/devhub-ocr đã tồn tại.");
            }

            // Chạy container với ảnh truyền vào
            ProcessBuilder processBuilder = new ProcessBuilder(
                "docker", "run", "--rm",
                "-v", imageFile.getParent() + ":/input", // Mount thư mục chứa ảnh vào container
                "4sandbox/devhub-ocr", "python", "app.py", "/input/" + imageFile.getName()
            );

            // Chạy lệnh Docker
            Process process = processBuilder.start();

            // Đọc kết quả đầu ra từ stdout của container
            BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = stdoutReader.readLine()) != null) {
                System.out.println("[STDOUT] " + line);
            }

            // Đọc kết quả đầu ra từ stderr của container (để log lỗi)
            BufferedReader stderrReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = stderrReader.readLine()) != null) {
                System.err.println("[STDERR] " + line);
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
