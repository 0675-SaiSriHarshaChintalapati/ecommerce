package com.digit.ecommerce.model;

import com.digit.ecommerce.dto.BooksDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.engine.internal.Cascade;

import java.util.List;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "Books_data")
@ToString(exclude = {"wishlists", "orders"})
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

    @OneToMany(mappedBy = "book", cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<Wishlist> wishlists;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "image_id")
    private AddImage addImage;

    @OneToMany(mappedBy = "book", cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<Orders> orders;

    public Books(BooksDto booksDto) {
        this.bookName = booksDto.getBookName();
        this.bookAuthor = booksDto.getBookAuthor();
        this.bookDescription = booksDto.getBookDescription();
        this.bookPrice = booksDto.getBookPrice();
        this.bookQuantity = booksDto.getBookQuantity();
        this.addImage = booksDto.getAddImage(); // Assuming BooksDto has a field for AddImage
    }
}



//package com.digit.ecommerce.model;
//
//import com.digit.ecommerce.dto.BooksDto;
//import com.fasterxml.jackson.annotation.JsonManagedReference;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.hibernate.engine.internal.Cascade;
//
//import java.util.List;
//
//
//@AllArgsConstructor
//@NoArgsConstructor
//@Data
//@Entity
//@Table(name = "Books_data")
//public class Books {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "Book_id")
//    private Long id;
//
//    @Column(name = "Book_name")
//    private String bookName;
//
//    @Column(name = "Book_author")
//    private String bookAuthor;
//
//    @Column(name = "Book_description")
//    private String bookDescription;
//
//    @Column(name = "book_price")
//    private Long bookPrice;
//
//    @Column(name = "book_quantity")
//    private Long bookQuantity;
//
//
//    @OneToMany(mappedBy = "book", cascade = CascadeType.PERSIST)
//    private List<Wishlist> wishlists;
//
//    @OneToOne(cascade = CascadeType.PERSIST)
//    @JoinColumn(name = "image_id" )
//    private AddImage addImage;
//
//    @OneToMany(mappedBy = "book",cascade = CascadeType.PERSIST)
//    private List<Orders> orders;
//
//
//    public Books(BooksDto booksDto) {
//        this.bookName = booksDto.getBookName();
//        this.bookAuthor = booksDto.getBookAuthor();
//        this.bookDescription = booksDto.getBookDescription();
//        this.bookPrice = booksDto.getBookPrice();
//        this.bookQuantity = booksDto.getBookQuantity();
//        this.addImage = booksDto.getAddImage(); // Assuming BooksDto has a field for AddImage
//    }
//}
