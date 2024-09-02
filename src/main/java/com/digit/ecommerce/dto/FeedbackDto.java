package com.digit.ecommerce.dto;

import com.digit.ecommerce.model.Feedback;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackDto {

    private long user_id;
    private long book_id;
    private long rating;
    private String comment;

    public FeedbackDto(Feedback feedback){
        this.user_id=feedback.getUser_id();
        this.book_id=feedback.getBook_id();
        this.rating=feedback.getRating();
        this.comment=feedback.getComment();
    }
}
