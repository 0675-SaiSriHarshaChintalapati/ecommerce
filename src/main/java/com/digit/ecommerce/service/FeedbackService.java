package com.digit.ecommerce.service;

import com.digit.ecommerce.dto.DataHolder;
import com.digit.ecommerce.dto.FeedbackDto;
import com.digit.ecommerce.exception.AccessDeniedException;
import com.digit.ecommerce.exception.DataNotFoundException;
import com.digit.ecommerce.exception.RoleNotAllowedException;
import com.digit.ecommerce.model.Books;
import com.digit.ecommerce.model.Feedback;
import com.digit.ecommerce.repository.BookRepository;
import com.digit.ecommerce.repository.FeedbackRepository;
import com.digit.ecommerce.util.TokenUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.util.List;
import java.util.Optional;


@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository FeedbackRepository;

    @Autowired
    private TokenUtility tokenUtility;

    @Autowired
    private BookRepository bookRepository;


    public ResponseEntity<String> submitFeedback(String token,Long book_id, Feedback feedback) {
        DataHolder dataHolder=tokenUtility.decode(token);
        Long userId=dataHolder.getId();
        if(dataHolder.getRole().equalsIgnoreCase("User")) {
            Books book = bookRepository.findById(book_id).orElse(null);
            if (book != null) {
                feedback.setBook_id(book_id);
                feedback.setUser_id(userId);
                feedback.setRating(feedback.getRating());
                feedback.setComment(feedback.getComment());

                FeedbackRepository.save(feedback);
                return new ResponseEntity<>("Feedback submitted", HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
            }
        }
        else{
            return new ResponseEntity<>("Admin can't add the feedback",HttpStatus.NOT_ACCEPTABLE);
        }
    }

    public ResponseEntity<?> getAllFeedbacks(String token) {
        DataHolder dataHolder=tokenUtility.decode(token);
        if ((dataHolder.getRole().equalsIgnoreCase("Admin") || dataHolder.getRole().equalsIgnoreCase("User"))&& token!=null) {
            return new ResponseEntity<>(FeedbackRepository.findAll(),HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("please login",HttpStatus.UNAUTHORIZED);
        }
    }
}









