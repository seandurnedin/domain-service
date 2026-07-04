package com.libraryapp.domain.repository;

import com.libraryapp.domain.entity.Book;
import com.libraryapp.domain.enums.EBookStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "books", collectionResourceRel = "books")
public interface BookRepository extends JpaRepository<Book, Long> {

    @RestResource(path = "by-isbn", rel = "byIsbn")
    Optional<Book> findByIsbn(@Param("isbn") String isbn);

    @RestResource(path = "by-status", rel = "byStatus")
    List<Book> findByStatus(@Param("status") EBookStatus status);

    @RestResource(path = "by-genre", rel = "byGenre")
    List<Book> findByGenreIgnoreCase(@Param("genre") String genre);
    
    @RestResource(path = "by-title", rel = "byTitle")
    Page<Book> findByTitleContainingIgnoreCase(@Param("title") String title, Pageable pageable);
}
