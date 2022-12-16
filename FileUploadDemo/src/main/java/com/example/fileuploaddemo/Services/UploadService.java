package com.example.fileuploaddemo.Services;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    boolean upload(MultipartFile file);
}
