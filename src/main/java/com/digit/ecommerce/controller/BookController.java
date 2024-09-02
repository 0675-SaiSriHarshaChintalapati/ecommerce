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


    @PostMapping(value = "/addBooks/{imageId}")
    public ResponseEntity<?> addBooks(@RequestBody BooksDto booksDto, @RequestHeader String token,@PathVariable Long imageId) {
        return bookService.addBooks(booksDto,imageId,token);
    }


    @GetMapping("/viewBooks")
    public ResponseEntity<List<BooksDto>> viewAllBooks(@RequestHeader String token) {
        List<BooksDto> booksDtoList = bookService.viewAllBooks(token);
        return ResponseEntity.ok(booksDtoList);
    }

    @DeleteMapping("/delete/{Book_id}")

    public ResponseEntity<String> deleteBook(@PathVariable Long Book_id,@RequestHeader String token) {
        bookService.deleteBook(Book_id,token);
        return new ResponseEntity<>("Deleted",HttpStatus.OK);
    }

    @PutMapping("/update/{Book_id}")
    public ResponseEntity<BooksDto> updateBooks(@PathVariable Long Book_id, @RequestBody BooksDto booksDto, @RequestHeader String token) {
        BooksDto updated = bookService.updateBooks(Book_id, booksDto, token);

        return ResponseEntity.ok(updated);
    }


    @PutMapping("/update/price/{Book_id}")

    public ResponseEntity<BooksDto> updateBooksPrice(@PathVariable Long Book_id, @RequestBody BooksDto booksDto, @RequestHeader String token) {
        BooksDto updated = bookService.updatePrice(Book_id, booksDto, token);

        return ResponseEntity.ok(updated);
    }


    @PutMapping("/quantity/{Book_id}/{orderId}")
    public ResponseEntity<BooksDto> updateBooksQuantity(@RequestHeader String token, @PathVariable Long Book_id, @PathVariable Long orderId) {
        BooksDto updated = bookService.updateQuantity(token, Book_id, orderId);
        return ResponseEntity.ok(updated);
    }

}

