package com.digit.ecommerce.model;

import com.digit.ecommerce.dto.ImageDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    private String image_name;

    @Lob
    @Column(name = "book_logo")
    private byte[] image;

    private String imageType;

    public AddImage(ImageDto imageDto){
        this.image = imageDto.getImage();

    }
}
