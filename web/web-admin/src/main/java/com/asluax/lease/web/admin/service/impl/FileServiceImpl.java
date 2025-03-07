package com.asluax.lease.web.admin.service.impl;

import com.asluax.lease.common.minio.MinioProperties;
import com.asluax.lease.web.admin.service.FileService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    MinioClient minioClient;

    @Autowired
    MinioProperties minioProperties;

    @SneakyThrows
    @Override
    public String upload(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();// 12u3o12u3i123y14o2i.p1u2oi3.oi12u3o.png
        String path = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        String name = UUID.randomUUID().toString().replace("-","");
        String extension = StringUtils.getFilenameExtension(originalFilename);
        String filePath = path+name+"."+extension;

        PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                .object(filePath)
                .bucket(minioProperties.getBucketName())
                .stream(file.getInputStream(), file.getSize(), -1)
                .build();
        minioClient.putObject(putObjectArgs);

        return minioProperties.getEndpoint()+"/"+minioProperties.getBucketName()+"/"+filePath;
    }


    private String createBucketPolicyConfig(String bucketName) {
        return """
                {
                  "Statement" : [ {
                    "Action" : "s3:GetObject",
                    "Effect" : "Allow",
                    "Principal" : "*",
                    "Resource" : "arn:aws:s3:::%s/*"
                  } ],
                  "Version" : "2012-10-17"
                }
                """.formatted(bucketName);
    }
}
