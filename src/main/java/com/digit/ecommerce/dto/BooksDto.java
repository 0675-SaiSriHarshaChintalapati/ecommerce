package com.digit.ecommerce.dto;

import com.digit.ecommerce.model.AddImage;
import com.digit.ecommerce.model.Books;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BooksDto {

    @NotBlank(message = "Book name is mandatory")
    @Size(max = 100, message = "Book name must be less than 100 characters")
    private String bookName;

    @NotBlank(message = "Book author is mandatory")
    @Size(max = 100, message = "Book author must be less than 100 characters")
    private String bookAuthor;

    @Size(max = 500, message = "Book description must be less than 500 characters")
    private String bookDescription;

    private AddImage addImage;

    @NotNull(message = "Book price is mandatory")
    @Positive(message = "Book price must be positive")
    private Long bookPrice;

    @NotNull(message = "Book quantity is mandatory")
    @Positive(message = "Book quantity must be positive")
    private Long bookQuantity;

    public BooksDto(Books books) {
        this.bookName = books.getBookName();
        this.bookAuthor = books.getBookAuthor();
        this.bookDescription = books.getBookDescription();
        this.addImage = books.getAddImage();
        this.bookPrice = books.getBookPrice();
        this.bookQuantity = books.getBookQuantity();
    }
}
