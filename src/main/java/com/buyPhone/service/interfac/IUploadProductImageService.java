package com.buyPhone.service.interfac;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface IUploadProductImageService {

    Map<String, Object> uploadImage(MultipartFile photo, String folderName);

    boolean deleteImage(String imageUrl);

}
