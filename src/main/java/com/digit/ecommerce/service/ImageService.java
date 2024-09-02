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

    /**
     * Method to add an image.
     * This method accepts a MultipartFile object and a token.
     * It decodes the token to check the user's role.
     * If the user is an admin, it saves the image to the repository and returns the image details.
     *
     * @param image the image file to be added
     * @param token the authorization token
     * @return ImageDto containing the added image details
     * @throws IOException if an I/O error occurs
     * @throws RoleNotAllowedException if the user is not an admin
     */
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
            throw new RoleNotAllowedException("Only Admin have the access.");
        }
    }

    /**
     * Method to update an image by its ID.
     * This method accepts the image ID, a MultipartFile object, and a token.
     * It decodes the token to check the user's role.
     * If the user is an admin, it updates the image in the repository and returns the updated image details.
     *
     * @param id the ID of the image to be updated
     * @param image the new image file to be added
     * @param token the authorization token
     * @return ImageDto containing the updated image details
     * @throws IOException if an I/O error occurs
     * @throws ImageNotFoundException if the image with the given ID is not found
     * @throws RoleNotAllowedException if the user is not an admin
     */
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
