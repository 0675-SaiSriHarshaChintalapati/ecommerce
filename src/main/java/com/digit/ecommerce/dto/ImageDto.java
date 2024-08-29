package com.digit.ecommerce.dto;

import com.digit.ecommerce.model.AddImage;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ImageDto {
    private byte[] image;

    public ImageDto(AddImage addImage) {
        this.image = addImage.getImage();
    }
}
