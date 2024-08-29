package com.digit.ecommerce.controller;

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


    //    @PostMapping(value="/addBooks", consumes = { "multipart/form-data" })
//    public ResponseEntity<?> addBooks(
//            @RequestPart("booksDto") BooksDto booksDto,
//            @RequestPart("imageFile") MultipartFile imageFile,
//            @RequestHeader String token) {
//        return bookService.addBooks(booksDto, token, imageFile);
//    }
    @PostMapping(value = "/addBooks")
    public ResponseEntity<?> addBooks(@RequestBody BooksDto booksDto, @RequestHeader String token) {
        return bookService.addBooks(booksDto, token);
    }


    @GetMapping("/viewBooks")
    public ResponseEntity<List<BooksDto>> viewAllBooks(@RequestHeader String token) {
        List<BooksDto> booksDtoList = bookService.viewAllBooks(token);
        return ResponseEntity.ok(booksDtoList);
    }

    @DeleteMapping("/delete/{Book_id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id, @RequestHeader String token) {
        bookService.deleteBook(id, token);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
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

    //    @PutMapping("/change_quantity/{book_id}/{quantity}")
//    public ResponseEntity<?> change(@RequestHeader String token, @PathVariable long book_id, @PathVariable Long quantity) {
//        return new ResponseEntity<>(bookService.changeQuantityByToken(token, book_id, quantity), HttpStatus.ACCEPTED);
//    }
    @PutMapping("/quantity/{Book_id}/{quantity}")
    public ResponseEntity<BooksDto> updateBooksQuantity(@RequestHeader String token, @PathVariable Long Book_id, @PathVariable Long quantity) {
        BooksDto updated = bookService.updateQuantity(token, Book_id, quantity);
        return ResponseEntity.ok(updated);


    }
}

