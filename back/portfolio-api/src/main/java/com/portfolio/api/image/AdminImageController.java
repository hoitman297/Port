package com.portfolio.api.image;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.portfolio.api.image.dto.ImageUploadResponse;

/** BO — requires authentication. Shared by project thumbnails and per-feature images alike. */
@RestController
@RequestMapping("/api/admin/images")
public class AdminImageController {

    private final StorageService storageService;

    public AdminImageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ImageUploadResponse upload(@RequestParam("file") MultipartFile file) {
        return new ImageUploadResponse(storageService.upload(file));
    }
}
