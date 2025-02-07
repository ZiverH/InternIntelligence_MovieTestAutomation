package org.app.movie.model;

import jakarta.persistence.*;

@Entity
@Table(name = "movie_genre", indexes = {
        @Index(name = "idx_movie_genre_movie_id", columnList = "movie_id"),
        @Index(name = "idx_movie_genre_genre_id", columnList = "genre_id")
})
public class MovieGenre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "movie_id")
    private Long movieId;

    @Column(name = "genre_id")
    private Long genreId;

    // Other fields and methods
}