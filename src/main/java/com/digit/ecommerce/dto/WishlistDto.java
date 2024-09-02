package com.digit.ecommerce.dto;
import com.digit.ecommerce.model.Wishlist;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class WishlistDto {
    private long id;

    private Long bookId;
    //  private Long userId;

    public WishlistDto(Wishlist wishlist) {
        this.id = wishlist.getId();
       this.bookId = wishlist.getBook().getId();

        //(wishlist.getBook() != null) ? wishlist.getBook().getId() : null;
        //this.userId = (wishlist.getUser() != null) ? wishlist.getUser().getId() : null;
    }


}
