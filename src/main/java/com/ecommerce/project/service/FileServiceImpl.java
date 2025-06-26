package com.ecommerce.project.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService{
    @Override
    public String uploadProductImage(String path, MultipartFile image) throws IOException {
        String originalFileName = image.getOriginalFilename();
        String uniquePath = UUID.randomUUID().toString();

        String fileName = uniquePath.concat(originalFileName.substring(originalFileName.lastIndexOf(".")));
        String file = path + File.separator + fileName;
        System.out.println(file);

        File folder = new File(path);
        if(!folder.exists())
            folder.mkdir();

        Files.copy(image.getInputStream(), Path.of(file));
        return fileName;
    }
}
