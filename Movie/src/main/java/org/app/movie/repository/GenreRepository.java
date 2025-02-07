package org.app.movie.repository;

import org.app.movie.model.Genre;
import org.app.movie.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    @Query("SELECT g FROM Genre g where :name=g.name")
    Optional<Genre> findByName(String name);

}
