package com.digit.ecommerce.model;

import com.digit.ecommerce.dto.BooksDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.engine.internal.Cascade;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "Books_data")
public class Books {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Book_id")
    private Long id;

    @Column(name = "Book_name")
    private String bookName;

    @Column(name = "Book_author")
    private String bookAuthor;

    @Column(name = "Book_description")
    private String bookDescription;

    @Column(name = "book_price")
    private Long bookPrice;

    @Column(name = "book_quantity")
    private Long bookQuantity;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id" )
    private AddImage addImage;

    public Books(BooksDto booksDto) {
        this.bookName = booksDto.getBookName();
        this.bookAuthor = booksDto.getBookAuthor();
        this.bookDescription = booksDto.getBookDescription();
        this.bookPrice = booksDto.getBookPrice();
        this.bookQuantity = booksDto.getBookQuantity();
        this.addImage = booksDto.getAddImage(); // Assuming BooksDto has a field for AddImage
    }
}
