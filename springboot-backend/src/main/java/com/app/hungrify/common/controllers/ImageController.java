package com.app.hungrify.common.controllers;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/image")
public class ImageController {

    private final String uploadDir = "uploads"; // Directory to store files

    // POST API to upload multiple images
    @PostMapping("/upload")
    public ResponseEntity<?> uploadImages(@RequestParam("files") List<MultipartFile> files) {
        if (files.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No files provided");
        }

        List<String> uploadedFiles = new ArrayList<>();

        try {
            // Create the directory if it doesn't exist
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    // Generate a random UUID for the file name
                    String originalFilename = file.getOriginalFilename();
                    String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                    String randomFileName = UUID.randomUUID().toString() + extension;

                    // Save the file
                    Path filePath = Paths.get(directory.getAbsolutePath() + File.separator + randomFileName);
                    Files.write(filePath, file.getBytes());

                    uploadedFiles.add(randomFileName);
                }
            }

            return ResponseEntity.ok(uploadedFiles);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while uploading files");
        }
    }

    // GET API to retrieve a specific image
    @GetMapping("/{fileName}")
    public ResponseEntity<byte[]> getImage(@PathVariable String fileName) {
        try {
            Path filePath = Paths.get(uploadDir + File.separator + fileName);
            byte[] imageBytes = Files.readAllBytes(filePath);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);

            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}