package com.digit.ecommerce.service;

import com.digit.ecommerce.dto.BooksDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BooksInterface {
   ResponseEntity<?> addBooks(BooksDto booksDto ,  Long imageId,String token);
    public List<BooksDto> viewAllBooks(String token);
    public String deleteBook(Long id, String token);
    public BooksDto updateBooks(Long id, BooksDto bookDetails , String token);
    public BooksDto updatePrice( Long id, BooksDto booksDto, String token);
}
