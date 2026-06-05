package com.denkitronik.usuarioservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
public class MinioService {

    private final S3Client s3Client;

    @Value("${minio.bucket}")
    private String bucket;

    @Value("${minio.public-url}")
    private String publicUrl;

    public MinioService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    /**
     * Sube un archivo al bucket de MinIO y devuelve su URL pública.
     *
     * @param userId  ID del usuario — se usa como parte del nombre del objeto
     * @param archivo El archivo recibido desde el formulario del navegador
     * @return URL pública: http://localhost:9000/avatars/usuarios/{id}/foto_xxx.jpg
     */
    public String subirFoto(String userId, MultipartFile archivo) throws IOException {
        String extension = obtenerExtension(archivo.getOriginalFilename());
        String objectKey  = "usuarios/" + userId + "/foto_" + UUID.randomUUID() + extension;

        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(objectKey)
                .contentType(archivo.getContentType())
                .build();

        s3Client.putObject(putRequest,
                RequestBody.fromBytes(archivo.getBytes()));

        return publicUrl + "/" + bucket + "/" + objectKey;
    }

    private String obtenerExtension(String filename) {
        if (filename == null || !filename.contains(".")) return ".jpg";
        return filename.substring(filename.lastIndexOf('.'));
    }
}
