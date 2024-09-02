package com.digit.ecommerce.controller;

import com.digit.ecommerce.model.Books;
import com.digit.ecommerce.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.digit.ecommerce.dto.BooksDto;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    BookService bookService;

    /**
     * Endpoint to add a new book.
     * This method accepts a BooksDto object and a token in the request header.
     * It calls the bookService to add the book and returns the appropriate response.
     *
     * @param booksDto the book data transfer object containing book details
     * @param token the authorization token
     * @return ResponseEntity with the status and body
     */
    @PostMapping(value = "/addBooks")
    public ResponseEntity<?> addBooks(@RequestBody BooksDto booksDto, @RequestHeader String token) {
        return bookService.addBooks(booksDto, token);
    }

    /**
     * Endpoint to view all books.
     * This method accepts a token in the request header and returns a list of all books.
     *
     * @param token the authorization token
     * @return ResponseEntity with the list of BooksDto
     */
    @GetMapping("/viewBooks")
    public ResponseEntity<List<BooksDto>> viewAllBooks(@RequestHeader String token) {
        List<BooksDto> booksDtoList = bookService.viewAllBooks(token);
        return ResponseEntity.ok(booksDtoList);
    }

    /**
     * Endpoint to delete a book by its ID.
     * This method accepts the book ID and a token in the request header.
     * It calls the bookService to delete the book and returns a confirmation message.
     *
     * @param Book_id the ID of the book to be deleted
     * @param token the authorization token
     * @return ResponseEntity with the status and confirmation message
     */
    @DeleteMapping("/delete/{Book_id}")

    public ResponseEntity<String> deleteBook(@PathVariable Long Book_id,@RequestHeader String token) {
        bookService.deleteBook(Book_id,token);
        return new ResponseEntity<>("Deleted",HttpStatus.OK);
    }

    /**
     * Endpoint to update book details by its ID.
     * This method accepts the book ID, a BooksDto object, and a token in the request header.
     * It calls the bookService to update the book details and returns the updated book.
     *
     * @param Book_id the ID of the book to be updated
     * @param booksDto the book data transfer object containing updated book details
     * @param token the authorization token
     * @return ResponseEntity with the updated BooksDto
     */
    @PutMapping("/update/{Book_id}")
    public ResponseEntity<BooksDto> updateBooks(@PathVariable Long Book_id, @RequestBody BooksDto booksDto, @RequestHeader String token) {
        BooksDto updated = bookService.updateBooks(Book_id, booksDto, token);

        return ResponseEntity.ok(updated);
    }

    /**
     * Endpoint to update book price by its ID.
     * This method accepts the book ID, a BooksDto object, and a token in the request header.
     * It calls the bookService to update the book price and returns the updated book.
     *
     * @param Book_id the ID of the book to be updated
     * @param booksDto the book data transfer object containing updated book price
     * @param token the authorization token
     * @return ResponseEntity with the updated BooksDto
     */
    @PutMapping("/update/price/{Book_id}")

    public ResponseEntity<BooksDto> updateBooksPrice(@PathVariable Long Book_id, @RequestBody BooksDto booksDto, @RequestHeader String token) {
        BooksDto updated = bookService.updatePrice(Book_id, booksDto, token);

        return ResponseEntity.ok(updated);
    }

    /**
     * Endpoint to update book quantity by its ID and order ID.
     * This method accepts the book ID, order ID, and a token in the request header.
     * It calls the bookService to update the book quantity and returns the updated book.
     *
     * @param token the authorization token
     * @param Book_id the ID of the book to be updated
     * @param orderId the ID of the order
     * @return ResponseEntity with the updated BooksDto
     */
    @PutMapping("/quantity/{Book_id}/{orderId}")
    public ResponseEntity<BooksDto> updateBooksQuantity(@RequestHeader String token, @PathVariable Long Book_id, @PathVariable Long orderId) {
        BooksDto updated = bookService.updateQuantity(token, Book_id, orderId);
        return ResponseEntity.ok(updated);
    }

    /**
     * Endpoint to add an image to a book by its ID and image ID.
     * This method accepts the book ID, image ID, and a token in the request header.
     * It calls the bookService to add the image to the book and returns the updated book.
     *
     * @param token the authorization token
     * @param book_id the ID of the book to which the image will be added
     * @param image_id the ID of the image to be added
     * @return ResponseEntity with the updated BooksDto
     */
    @PutMapping("/addImage/{book_id}/{image_id}")
    public ResponseEntity<BooksDto> addImage(@RequestHeader String token, @PathVariable Long book_id, @PathVariable Long image_id) {
        return new ResponseEntity<>(bookService.updateBookImage(token, book_id, image_id), HttpStatus.CREATED);
    }
}
