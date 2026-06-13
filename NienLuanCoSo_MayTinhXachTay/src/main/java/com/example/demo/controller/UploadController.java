package com.example.demo.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

	private final String UPLOAD_DIR =
	        "src/main/resources/static/images/";

    @PostMapping
    public String uploadFile(
            @RequestParam("file") MultipartFile file)
            throws IOException {

        File dir = new File(UPLOAD_DIR);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        String fileName = file.getOriginalFilename();

        Path path = Paths.get(UPLOAD_DIR + fileName);

        Files.write(path, file.getBytes());

        return fileName;
    }
}
