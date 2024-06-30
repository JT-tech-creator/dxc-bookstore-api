package com.dxc.bookstore.repository;

import com.dxc.bookstore.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {

    @Query("SELECT b FROM Book b JOIN b.authors a WHERE b.title = :titleOrAuthorName OR a.name = :titleOrAuthorName")
    List<Book> findByTitleOrAuthorName(String titleOrAuthorName);
}
