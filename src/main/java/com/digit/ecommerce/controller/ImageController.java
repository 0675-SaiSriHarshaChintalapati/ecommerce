package com.digit.ecommerce.controller;

import com.digit.ecommerce.dto.ImageDto;
import com.digit.ecommerce.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/books")
public class ImageController {

    @Autowired
    ImageService imageService;

    /**
     * Endpoint to add an image.
     * This method accepts a MultipartFile object and a token in the request header.
     * It calls the imageService to add the image and returns the added image details.
     *
     * @param image the image file to be added
     * @param token the authorization token
     * @return ResponseEntity with the added ImageDto and status
     * @throws IOException if an I/O error occurs
     */
    @PostMapping("/addImage")
    public ResponseEntity<ImageDto> addImage(@RequestPart MultipartFile image, @RequestHeader String token) throws IOException {
        ImageDto addImage = imageService.imageAdding(image, token);
        return new ResponseEntity<>(addImage, HttpStatus.CREATED);
    }

    /**
     * Endpoint to update an image for a book by its ID.
     * This method accepts the book ID, a MultipartFile object, and a token in the request header.
     * It calls the imageService to update the image and returns the updated image details.
     *
     * @param id the ID of the book for which the image will be updated
     * @param image the new image file to be added
     * @param token the authorization token
     * @return ResponseEntity with the updated ImageDto and status
     * @throws IOException if an I/O error occurs
     */
    @PutMapping("/updateBookImage/{id}")
    public ResponseEntity<ImageDto> updateBookImage(@PathVariable Long id, @RequestPart MultipartFile image, @RequestHeader String token) throws IOException {
        ImageDto addImage = imageService.updateImage(id, image, token);
        return new ResponseEntity<>(addImage, HttpStatus.CREATED);
    }
}
