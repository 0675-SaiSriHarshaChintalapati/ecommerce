package com.digit.ecommerce.controller;

import com.digit.ecommerce.dto.BooksDto;
import com.digit.ecommerce.dto.FeedbackDto;
import com.digit.ecommerce.model.Feedback;
import com.digit.ecommerce.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    FeedbackService feedbackService;

    @PostMapping("/add/{book_id}")
    public ResponseEntity<String> addReview(@RequestHeader String token,@PathVariable long book_id,@RequestBody Feedback feedback){
        return feedbackService.submitFeedback(token,book_id,feedback);
    }

    @GetMapping("/getall")
    public ResponseEntity<?> getAllFeedbacks(@RequestHeader String token){
        return feedbackService.getAllFeedbacks(token);

    }

}
