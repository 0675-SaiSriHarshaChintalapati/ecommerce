package com.digit.ecommerce.service;
import com.digit.ecommerce.dto.BooksDto;
import com.digit.ecommerce.dto.DataHolder;
import com.digit.ecommerce.exception.DataNotFoundException;
import com.digit.ecommerce.exception.RoleNotAllowedException;
import com.digit.ecommerce.model.AddImage;
import com.digit.ecommerce.model.Books;
import com.digit.ecommerce.model.Orders;
import com.digit.ecommerce.model.User;
import com.digit.ecommerce.repository.BookRepository;
import com.digit.ecommerce.repository.ImageRepository;
import com.digit.ecommerce.repository.OrderRepository;
import com.digit.ecommerce.repository.UserRepository;
import com.digit.ecommerce.util.TokenUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;


@Service
public class BookService implements BooksInterface {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    TokenUtility tokenUtility;

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ImageRepository imageRepository;


    @Transactional
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
        if (requiredRole.equalsIgnoreCase(dataHolder.getRole()) && objUser != null) {
            if (!bookRepository.existsById(id)) {
                throw new DataNotFoundException("Book with ID " + id + " not found");
            }
            bookRepository.deleteById(id);
            return "Deleted successfully";
        } else {
            throw new RoleNotAllowedException("Only admin can have Access to Delete");
        }
    }

    /**
     * Method to update the details of a book by its ID.
     * This method accepts the book ID, a BooksDto object containing the new details, and a token.
     * It decodes the token to check the user's role and retrieves the user by their ID.
     * If the user is an admin, it updates the book's details and saves the book.
     *
     * @param id the ID of the book to be updated
     * @param bookDetails the book data transfer object containing the new details
     * @param token the authorization token
     * @return BooksDto containing the updated book details
     * @throws RuntimeException if the book with the given ID is not found
     * @throws RoleNotAllowedException if the user is not an admin
     */
    public BooksDto updateBooks(Long id, BooksDto bookDetails, String token) {
        DataHolder dataHolder = tokenUtility.decode(token);
        String requiredRole = "admin";
        Books books = new Books(bookDetails);
        Long Admin_id = dataHolder.getId();
        User objUser = userRepository.findById(Admin_id).orElse(null);

        // Check if the user has the required role and exists
        if (requiredRole.equalsIgnoreCase(dataHolder.getRole())) {
            Books existing = bookRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));

            // Update book details if they are provided
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

            // Save the updated book and return the updated details
            Books updatedBooks = bookRepository.save(existing);
            return new BooksDto(updatedBooks);
        } else {
            throw new RoleNotAllowedException("Only admin can have Access to update");
        }
    }


    /**
     * Method to update the price of a book by its ID.
     * This method accepts the book ID, a BooksDto object containing the new price, and a token.
     * It decodes the token to check the user's role and retrieves the user by their ID.
     * If the user is an admin and exists, it updates the book's price and saves the book.
     *
     * @param id the ID of the book to be updated
     * @param booksDto the book data transfer object containing the new price
     * @param token the authorization token
     * @return BooksDto containing the updated book details
     * @throws RuntimeException if the book with the given ID is not found
     * @throws RoleNotAllowedException if the user is not an admin
     */
    public BooksDto updatePrice(Long id, BooksDto booksDto, String token) throws RuntimeException {
        DataHolder dataHolder = tokenUtility.decode(token);
        Books books = new Books(booksDto);
        String requiredRole = "admin";
        Long Admin_id = dataHolder.getId();
        User objUser = userRepository.findById(Admin_id).orElse(null);
        if (requiredRole.equalsIgnoreCase(dataHolder.getRole()) && objUser != null) {
            Books existing = bookRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Book not found with id " + id));
            if (books.getBookPrice() != null) {
                existing.setBookPrice(books.getBookPrice());
            }
            Books obj = bookRepository.save(existing);
            BooksDto objdto = new BooksDto(obj);
            return objdto;
        } else {
            throw new RoleNotAllowedException("Only admin can change the prices");
        }
    }

    /**
     * Method to retrieve a book by its ID.
     * This method accepts a book ID and fetches the book from the repository.
     * If the book is not found, it throws a RuntimeException.
     *
     * @param id the ID of the book to be retrieved
     * @return Books the book object retrieved from the repository
     * @throws RuntimeException if the book with the given ID is not found
     */
    public Books getBookByID(Long id) {
        Books book = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
        return book;
    }


    /**
     * Method to update the quantity of a book based on the order status.
     * This method accepts a token, order ID, and quantity.
     * It decodes the token to retrieve the user ID and fetches the order and book by their IDs.
     * Depending on the order status, it updates the book quantity and saves the book.
     *
     * @param token the authorization token
     * @param orderId the ID of the order
     * @param quantity the quantity to be updated
     * @return BooksDto containing the updated book details
     * @throws RuntimeException if the order or book is not found, or if the order status is invalid
     */
    public BooksDto updateQuantity(String token, Long orderId, Long quantity) {
        DataHolder dataHolder = tokenUtility.decode(token);
        Long userId = dataHolder.getId();

        Orders order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            throw new RuntimeException("Order not found");
        }

        Books book = bookRepository.findById(order.getBook().getId()).orElse(null);
        if (book == null) {
            throw new RuntimeException("Book not found");
        }

        if (order.getStatus().equalsIgnoreCase("cancelled")) {
            book.setBookQuantity(book.getBookQuantity() + order.getQuantity());
        } else if (order.getStatus().equalsIgnoreCase("ordered")) {
            book.setBookQuantity(book.getBookQuantity() - order.getQuantity());
        } else {
            throw new RuntimeException("Invalid status");
        }

        Books updatedBook = bookRepository.save(book);
        return new BooksDto(updatedBook);
    }


    /**
     * Method to update the image of a book.
     * This method accepts a token, book ID, and image ID.
     * It decodes the token to check the user's role and retrieves the book and user by their IDs.
     * If the user is an admin and exists, it updates the book's image and saves the book.
     *
     * @param token   the authorization token
     * @param bookId  the ID of the book to be updated
     * @param imageId the ID of the new image to be added
     * @return BooksDto containing the updated book details
     * @throws DataNotFoundException if the image with the given ID is not found
     */
    public BooksDto updateBookImage(String token, Long bookId, Long imageId) {
        DataHolder dataHolder = tokenUtility.decode(token);
        String requiredRole = "admin";
        Long Admin_id = dataHolder.getId();
        Books book = getBookByID(bookId);
        User objUser = userRepository.findById(Admin_id).orElse(null);
        if (requiredRole.equalsIgnoreCase(dataHolder.getRole()) && objUser != null) {
            AddImage image = imageRepository.findById(imageId).orElseThrow(() -> new DataNotFoundException("Image not found"));
            book.setAddImage(image);
            bookRepository.save(book);
        }
        return new BooksDto(book);
    }
}






