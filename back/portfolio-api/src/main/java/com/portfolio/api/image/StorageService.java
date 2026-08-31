package com.portfolio.api.image;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    /** Uploads the file and returns its publicly reachable URL. */
    String upload(MultipartFile file);
}
