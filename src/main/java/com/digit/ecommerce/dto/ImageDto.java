package com.digit.ecommerce.dto;

import com.digit.ecommerce.model.AddImage;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ImageDto {
    private byte[] image;
    private String image_name;


    public ImageDto( AddImage addImage) {
        this.image = addImage.getImage();

    }
}
