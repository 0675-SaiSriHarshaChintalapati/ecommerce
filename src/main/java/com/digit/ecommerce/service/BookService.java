package com.digit.ecommerce.service;



import com.digit.ecommerce.dto.BooksDto;
import com.digit.ecommerce.dto.DataHolder;
import com.digit.ecommerce.exception.DataNotFoundException;
import com.digit.ecommerce.exception.RoleNotAllowedException;
import com.digit.ecommerce.model.Books;
import com.digit.ecommerce.model.User;
import com.digit.ecommerce.repository.BookRepository;
import com.digit.ecommerce.repository.UserRepository;
import com.digit.ecommerce.util.TokenUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.List;



@Service
public class BookService implements BooksInterface{

    @Autowired
    BookRepository bookRepository;

    @Autowired
    TokenUtility tokenUtility;

    @Autowired
    UserRepository userRepository;


    public ResponseEntity<?> addBooks(BooksDto booksDto, String token) {
        DataHolder dataHolder = tokenUtility.decode(token);
        String requiredRole = "admin";
        Long Admin_id = dataHolder.getId();
        Books existBook = null;
        User objUser = userRepository.findById(Admin_id).orElse(null);
        Books objBook = new Books(booksDto);
        if (requiredRole.equalsIgnoreCase(dataHolder.getRole()) && token != null && objUser != null) {
            existBook = bookRepository.findBybookName(booksDto.getBookName());
            if (existBook == null) {
                Books savedBook = bookRepository.save(objBook);
                BooksDto booksDto1 = new BooksDto(savedBook);
                return new ResponseEntity<>(booksDto1, HttpStatus.CREATED);
            } else {
                Long quantity = existBook.getBookQuantity() + objBook.getBookQuantity();
                existBook.setBookQuantity(quantity);
                bookRepository.save(existBook);
            }
        } else {
            throw new RoleNotAllowedException("Only Admin can add the books");
        }
        BooksDto updatedBookQuantity = new BooksDto(existBook);
        return new ResponseEntity<>(updatedBookQuantity, HttpStatus.OK);
    }


    public List<BooksDto> viewAllBooks(String token) {
        DataHolder dataHolder = tokenUtility.decode(token);
        Long Admin_id = dataHolder.getId();
        User objUser = userRepository.findById(Admin_id).orElse(null);
        if ((dataHolder.getRole().equalsIgnoreCase("Admin") || dataHolder.getRole().equalsIgnoreCase("User")) && token != null && objUser != null) {
            List<Books> booksList = bookRepository.findAll();
            List<BooksDto> booksDtoList = booksList.stream()
                    .map(BooksDto::new)
                    .toList();
            return booksDtoList;
        } else {
            throw new RoleNotAllowedException("Please login to Access the data");
        }
    }

    public String deleteBook(Long id, String token) {
        DataHolder dataHolder = tokenUtility.decode(token);
        String requiredRole = "admin";
        Long Admin_id = dataHolder.getId();
        User objUser = userRepository.findById(Admin_id).orElse(null);
        if (requiredRole.equalsIgnoreCase(dataHolder.getRole())&& objUser != null) {
            if (!bookRepository.existsById(id)) {
                throw new DataNotFoundException("Book with ID " + id + " not found");
            }
            bookRepository.deleteById(id);
            return "Deleted successfully";
        } else {
            throw new RoleNotAllowedException("Only admin can have Access to Delete");
        }
    }


    public BooksDto updateBooks(Long id, BooksDto bookDetails , String token) {
        DataHolder dataHolder = tokenUtility.decode(token);
        String requiredRole = "admin";
        Books books=new Books(bookDetails);
        Long Admin_id = dataHolder.getId();
        User objUser = userRepository.findById(Admin_id).orElse(null);
        if (requiredRole.equalsIgnoreCase(dataHolder.getRole())&& objUser != null) {
            Books existing = bookRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
            if (bookDetails.getBookAuthor() != null) {
                existing.setBookAuthor(bookDetails.getBookAuthor());
            }
            if (bookDetails.getBookDescription() != null) {
                existing.setBookDescription(bookDetails.getBookDescription());
            }
            if (bookDetails.getBookPrice() != null) {
                existing.setBookPrice(bookDetails.getBookPrice());
            }
            if (bookDetails.getBookQuantity() != null) {
                existing.setBookQuantity(bookDetails.getBookQuantity());
            }
            if (bookDetails.getBookName() != null) {
                existing.setBookName(bookDetails.getBookName());
            }
            Books updatedBooks = bookRepository.save(existing);
            return new BooksDto(updatedBooks);
        }
        else {
            throw new RoleNotAllowedException("Only admin can have Access to update");
        }
    }


    public BooksDto updatePrice( Long id, BooksDto booksDto, String token) throws RuntimeException {
        DataHolder dataHolder = tokenUtility.decode(token);
        Books books=new Books(booksDto);
        String requiredRole = "admin";
        Long Admin_id = dataHolder.getId();
        User objUser = userRepository.findById(Admin_id).orElse(null);
        if (requiredRole.equalsIgnoreCase(dataHolder.getRole())&& objUser != null) {
            Books existing = bookRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Book not found with id " + id));
            if (books.getBookPrice() != null) {
                existing.setBookPrice(books.getBookPrice());
            }
            Books obj=bookRepository.save(existing);
            BooksDto objdto=new BooksDto(obj);
            return objdto;
        } else {
            throw new RoleNotAllowedException("Only admin can change the prices");
        }
    }

//public void updateQuantity(Order order) {
    // Order BookId = bookRepository.getById();
//        Books books = bookRepository.findById(book_Id).orElse(null);
//        if (books != null) {
//            if (status.equalsIgnoreCase("ordered")) {
//                books.setBookQuantity(books.getBookQuantity() - quantity);
//            } else {
//                books.setBookQuantity(books.getBookQuantity() + quantity);
//            }
//            bookRepository.save(books);
//        } else {
//            // Handle the case where the book is not found
//            System.out.println("Book not found");
//        }
//    }
}


