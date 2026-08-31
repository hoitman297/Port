package com.portfolio.api.image;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.portfolio.api.exception.InvalidFileException;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

/** Works for both AWS S3 and Cloudflare R2 — R2 is S3-API-compatible via a custom endpoint + path-style access. */
@Service
public class S3StorageService implements StorageService {

    private final S3Client s3Client;
    private final StorageProperties properties;

    public S3StorageService(StorageProperties properties) {
        this.properties = properties;

        S3ClientBuilder builder = S3Client.builder()
            .region(Region.of(StringUtils.hasText(properties.region()) ? properties.region() : "auto"))
            .credentialsProvider(StaticCredentialsProvider.create(
                AwsBasicCredentials.create(properties.accessKey(), properties.secretKey())
            ));

        if (StringUtils.hasText(properties.endpoint())) {
            builder.endpointOverride(URI.create(properties.endpoint())).forcePathStyle(true);
        }

        this.s3Client = builder.build();
    }

    @Override
    public String upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidFileException("업로드할 파일이 없습니다.");
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new InvalidFileException("이미지 파일만 업로드할 수 있습니다.");
        }

        String key = "uploads/" + UUID.randomUUID() + extensionOf(file.getOriginalFilename());

        try {
            s3Client.putObject(
                PutObjectRequest.builder()
                    .bucket(properties.bucket())
                    .key(key)
                    .contentType(contentType)
                    .build(),
                RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );
        } catch (IOException e) {
            throw new InvalidFileException("파일 업로드에 실패했습니다.");
        }

        String base = properties.publicBaseUrl().endsWith("/")
            ? properties.publicBaseUrl().substring(0, properties.publicBaseUrl().length() - 1)
            : properties.publicBaseUrl();

        return base + "/" + key;
    }

    private String extensionOf(String originalFilename) {
        if (originalFilename == null) {
            return "";
        }
        int dot = originalFilename.lastIndexOf('.');
        return dot == -1 ? "" : originalFilename.substring(dot);
    }
}
