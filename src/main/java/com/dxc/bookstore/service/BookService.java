package com.dxc.bookstore.service;

import com.dxc.bookstore.entity.Author;
import com.dxc.bookstore.entity.Book;
import com.dxc.bookstore.exception.BookNotFoundException;
import com.dxc.bookstore.repository.AuthorRepository;
import com.dxc.bookstore.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookService {
    
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }
    public List<Book> findBooksByTitleOrAuthorName(String titleOrAuthorName) {
        return bookRepository.findByTitleOrAuthorName(titleOrAuthorName);
    }

    public Book createBook(Book book) {
        // Convert array of authors to Set to ensure uniqueness
        Set<Author> uniqueAuthors = new HashSet<>(Arrays.asList(book.getAuthors()));

        // Manage authors to prevent duplicates
        Set<Author> managedAuthors = new HashSet<>();
        for (Author author : uniqueAuthors) {
            // Check if author already exists based on name and birthday
            Author existingAuthor = authorRepository.findByNameAndBirthday(author.getName(), author.getBirthday())
                    .orElse(author); // If not found, use the provided author
            managedAuthors.add(existingAuthor);
        }
        book.setAuthors(managedAuthors.toArray(new Author[0]));

        return bookRepository.save(book);
    }

    public Book updateBook(String isbn, Book updatedBook) {
        Optional<Book> optionalBook = bookRepository.findById(isbn);
        if (optionalBook.isEmpty()) {
            throw new BookNotFoundException("Book not found with ISBN " + isbn);
        }

        Book existingBook = optionalBook.get();

        // Update title and other fields
        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setYear(updatedBook.getYear());
        existingBook.setPrice(updatedBook.getPrice());
        existingBook.setGenre(updatedBook.getGenre());

        // Convert array of authors to Set to ensure uniqueness
        Set<Author> uniqueAuthors = new HashSet<>(Arrays.asList(updatedBook.getAuthors()));

        // Manage authors to prevent duplicates
        Set<Author> managedAuthors = new HashSet<>();
        for (Author author : uniqueAuthors) {
            // Check if author already exists based on name and birthday
            Author existingAuthor = authorRepository.findByNameAndBirthday(author.getName(), author.getBirthday())
                    .orElse(author); // If not found, use the provided author
            managedAuthors.add(existingAuthor);
        }

        // Clear existing authors and set the managed authors
        existingBook.setAuthors(managedAuthors.toArray(new Author[0]));

        return bookRepository.save(existingBook);
    }



    public boolean deleteBook(String isbn) {
        Optional<Book> bookOptional = bookRepository.findById(isbn);

        if (bookOptional.isPresent()) {
            // Remove associations with authors
            bookOptional.get().setAuthors(new Author[0]);
            bookRepository.save(bookOptional.get());

            bookRepository.deleteById(isbn);
            return true;
        } else {
            return false;
        }

    }


    public Optional<Book> findById(String isbn) {
        return bookRepository.findById(isbn);
    }
}
