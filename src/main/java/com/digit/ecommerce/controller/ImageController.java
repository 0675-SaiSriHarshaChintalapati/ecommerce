package com.digit.ecommerce.controller;


import com.digit.ecommerce.dto.ImageDto;
import com.digit.ecommerce.model.AddImage;
import com.digit.ecommerce.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.plaf.PanelUI;
import java.io.IOException;
import java.nio.file.LinkOption;

@RestController
@RequestMapping("/books")
public class ImageController {

    @Autowired
    ImageService imageService;

    @PostMapping("/addImage")
    public ResponseEntity<ImageDto> addImage(@RequestPart MultipartFile image , @RequestHeader String token) throws IOException {
        ImageDto addImage = imageService.imageAdding(image,token);
        return new ResponseEntity<>(addImage, HttpStatus.CREATED);
    }

    @PutMapping("/updateImage/{id}")
    public ResponseEntity<ImageDto> updateBookImage(@PathVariable Long id,@RequestPart MultipartFile image , @RequestHeader String token) throws IOException{
        ImageDto addImage = imageService.updateImage(id,image,token);
        return new ResponseEntity<>(addImage,HttpStatus.CREATED);
    }

}
