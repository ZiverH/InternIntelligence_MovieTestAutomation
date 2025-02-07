package org.app.movie.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String title;
    String director;
    int year;
    @ManyToMany
    @JoinTable(
            name = "movie_genre",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    @ToString.Exclude
    @JsonIgnore
    List<Genre> genres;
    String imdb;
}
