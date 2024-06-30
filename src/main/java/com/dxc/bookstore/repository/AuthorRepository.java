package com.dxc.bookstore.repository;

import com.dxc.bookstore.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    Optional<Author> findByNameAndBirthday(String name, Date birthday);
}
