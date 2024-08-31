package com.digit.ecommerce.service;

import com.digit.ecommerce.dto.DataHolder;
import com.digit.ecommerce.dto.ImageDto;
import com.digit.ecommerce.exception.ImageNotFoundException;
import com.digit.ecommerce.exception.RoleNotAllowedException;
import com.digit.ecommerce.model.AddImage;
import com.digit.ecommerce.repository.ImageRepository;
import com.digit.ecommerce.util.TokenUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageService implements ImageInterface {

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    TokenUtility tokenUtility;

    public ImageDto imageAdding(MultipartFile image, String token) throws IOException {
        DataHolder dataHolder = tokenUtility.decode(token);
        String role = dataHolder.getRole();
        String requiredRole = "admin";
        if (requiredRole.equalsIgnoreCase(role)) {
            AddImage addImage = new AddImage();
            addImage.setImage_name(image.getOriginalFilename());
            addImage.setImage(image.getBytes());
            addImage.setImageType(image.getContentType());
            ImageDto imageDto = new ImageDto(addImage);
             imageRepository.save(addImage);
            return imageDto;
        } else {
            throw new RoleNotAllowedException("Only Admin have the access..");
        }
    }

    public ImageDto updateImage(Long id, MultipartFile image, String token) throws IOException {
        DataHolder dataHolder = tokenUtility.decode(token);
        String role = dataHolder.getRole();
        String requiredRole = "admin";
        if (requiredRole.equalsIgnoreCase(role)) {
            AddImage existingImage = imageRepository.findById(id).orElseThrow(() -> new ImageNotFoundException("The id you provided has no Image"));
            existingImage.setImage(image.getBytes());
            existingImage.setImageType(image.getContentType());
            ImageDto imageDto = new ImageDto(existingImage);
             imageRepository.save(existingImage);
            return imageDto;
        } else {
            throw new RoleNotAllowedException("Only Admin have the access.");
        }
    }
}