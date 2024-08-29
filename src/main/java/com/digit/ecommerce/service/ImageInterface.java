package com.digit.ecommerce.service;

import com.digit.ecommerce.dto.ImageDto;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Repository
public interface ImageInterface {
    public ImageDto imageAdding(MultipartFile image, String token) throws IOException;
    public ImageDto updateImage(Long id, MultipartFile image, String token) throws IOException;
}
