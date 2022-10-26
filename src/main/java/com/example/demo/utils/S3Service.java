package com.example.demo.utils;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile multipartFile, String dirName) throws BaseException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.FILE_CONVERT_ERROR));
        return upload(uploadFile, dirName);
    }

    private String upload(File uploadFile, String dirName) throws BaseException {
        String fileName = dirName + "/" +uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    private void removeNewFile(File targetFile) {
        targetFile.delete();
    }

    private String putS3(File uploadFile, String fileName) throws BaseException {
        try {
            amazonS3Client.putObject(new PutObjectRequest(
                    bucket,
                    fileName,
                    uploadFile
            ).withCannedAcl(CannedAccessControlList.PublicRead));
            return amazonS3Client.getResourceUrl(bucket, fileName);
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.S3_FAIL);
        }
    }

    private Optional<File> convert(MultipartFile file) throws BaseException {
        File convertFile = new File(UUID.randomUUID()+file.getOriginalFilename());
        try {
            if (convertFile.createNewFile()) {
                FileOutputStream fos = new FileOutputStream(convertFile);
                fos.write(file.getBytes());
                return Optional.of(convertFile);
            }
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.FILE_CONVERT_ERROR);
        }
        return Optional.empty();
    }
}
