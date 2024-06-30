package com.dxc.bookstore.controller;

import com.dxc.bookstore.entity.Book;
import com.dxc.bookstore.exception.BookNotFoundException;
import com.dxc.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    BookService bookService;

    @GetMapping()
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.findAll();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/get/{titleOrAuthorName}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<Book>> getBooksByTitleOrAuthor(@PathVariable String titleOrAuthorName) {
        List<Book> books = bookService.findBooksByTitleOrAuthorName(titleOrAuthorName);
        if (books.isEmpty()) {
            throw new BookNotFoundException("Book not found with title or author name as "+ titleOrAuthorName);
        } else {
            return ResponseEntity.ok(books);
        }
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        Book newBook = bookService.createBook(book);
        return ResponseEntity.ok(newBook);
    }

    @PutMapping("/update/{isbn}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Book> updateBook(@PathVariable String isbn, @RequestBody Book book) {
        Book updatedBook = bookService.updateBook(isbn, book);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/delete/{isbn}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteBook(@PathVariable String isbn) {
        Optional<Book> book = bookService.findById(isbn);

        if (book.isPresent()) {
            bookService.deleteBook(isbn);
            return ResponseEntity.ok("Book with ISBN " + isbn + " successfully deleted.");
        } else {
            throw new BookNotFoundException("Book not found with ISBN " + isbn);
        }
    }

}
