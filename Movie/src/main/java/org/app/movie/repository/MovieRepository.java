package org.app.movie.repository;

import org.app.movie.model.Movie;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    @EntityGraph(attributePaths = {"genres"})
    Optional<Movie> findById(Long id);

    @Query("SELECT m FROM Movie m JOIN FETCH m.genres")
    List<Movie> findAll();

    @Query("SELECT m FROM Movie m JOIN FETCH m.genres g WHERE g.name = :genre")
    List<Movie> findByGenre(String genre);

    List<Movie> findAll(Specification<Movie> movieSpecification);

}
