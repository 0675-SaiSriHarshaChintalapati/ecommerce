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


    @PostMapping(value="/addBooks")
    public ResponseEntity<?> addBooks(@RequestBody BooksDto booksDto,@RequestHeader String token) {
        return  bookService.addBooks(booksDto,token);
    }

    @GetMapping("/viewBooks")
    public ResponseEntity<List<BooksDto>> viewAllBooks(String token) {
        List<BooksDto> booksDtoList = bookService.viewAllBooks(token);
        return ResponseEntity.ok(booksDtoList);
    }

    @DeleteMapping("/delete/{Book_id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id,@RequestHeader String token) {
        bookService.deleteBook(id,token);
        return new ResponseEntity<>("Deleted",HttpStatus.OK);
    }

    @PutMapping("/update/{Book_id}")
    public ResponseEntity<BooksDto> updateBooks(@PathVariable Long id, @RequestBody Books books,@RequestHeader String token) {
        BooksDto updated = bookService.updateBooks(id, books,token);
        return ResponseEntity.ok(updated);
    }


    @PutMapping("/update/price/{id}")
    public ResponseEntity<BooksDto> updateBooksPrice(@PathVariable Long id, @RequestBody Books books, @RequestHeader String token) {
        BooksDto updated = bookService.updatePrice(id, books,token);
        return ResponseEntity.ok(updated);
    }
}
