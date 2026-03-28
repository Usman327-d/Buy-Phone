package com.buyPhone.service.impl;

import com.buyPhone.service.interfac.IUploadProductImageService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadProductImageService implements IUploadProductImageService {

    private final Cloudinary cloudinary;

    @Override
    public Map<String, Object> uploadImage(MultipartFile photo, String folderName) {

        try{
            Map<String, Object> uploadResult = cloudinary.uploader().upload(photo.getBytes(), ObjectUtils.asMap(
                    "folder", folderName,
                    "folder", folderName,
                    "use_filename", true,
                    "unique_filename", true, // Add this
                    "overwrite", false ));
//            return uploadResult.get("secure_url").toString();
            return uploadResult;
        }
        catch (Exception e){
            log.error("error while uploading image on cloudinary cloud " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean deleteImage(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            log.warn("Delete skipped: Image URL is null or empty");
            return true; // Technically "successful" because there's nothing to delete
        }

        try {
            String publicId = extractPublicId(imageUrl);
            log.info("Requesting Cloudinary deletion for Public ID: {}", publicId);

            // Cloudinary destroy method
            Map<String, Object> result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

            String status = result.get("result").toString();

            if ("ok".equals(status)) {
                log.info("Cloudinary deletion successful.");
                return true;
            } else if ("not found".equals(status)) {
                log.warn("Image not found on Cloudinary (Public ID: {}). It might have been deleted already.", publicId);
                return true; // Return true because the end goal (image gone) is met
            }

            log.error("Cloudinary delete failed with status: {}", status);
            return false;
        } catch (Exception e) {
            log.error("Cloudinary connection error during deletion: {}", e.getMessage());
            return false;
        }
    }



    private String extractPublicId(String url) {
        try {
            // Find where the path starts after 'upload/'
            String partAfterUpload = url.split("/upload/")[1];

            // Remove the version (e.g., v1234567/) if it exists
            if (partAfterUpload.contains("/") && partAfterUpload.startsWith("v")) {
                partAfterUpload = partAfterUpload.substring(partAfterUpload.indexOf("/") + 1);
            }

            // Remove the file extension (.jpg, .png, etc)
            return partAfterUpload.substring(0, partAfterUpload.lastIndexOf("."));
        } catch (Exception e) {
            return null;
        }
    }
}
