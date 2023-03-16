package com.example.codeshortbackend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileService {
    private final String path = "../codeshort-backend-resources";
    private final String pathImages = path + "/images";

    public FileService() {
        if (!new File(this.path).mkdir())
            System.out.println("INFO: " + this.path + " already exists.");
        if (!new File(this.pathImages).mkdir())
            System.out.println("INFO: " + this.pathImages + " already exists.");
    }

    public String uploadFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        //TODO générer un nom de fichier aléatoire hashé en MD5 pour empecher deux fichiers d'avoir le même nom
        Path pathFile = Paths.get(pathImages + "/" + fileName);
        try {
            Files.copy(file.getInputStream(), pathFile, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
