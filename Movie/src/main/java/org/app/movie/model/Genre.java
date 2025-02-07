package org.app.movie.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "genre", indexes = {
        @Index(name = "idx_genre_name", columnList = "name")
})
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(unique = true)
    String name;
    @ManyToMany(mappedBy = "genres")
    @JsonIgnore
    @ToString.Exclude
    List<Movie> movies = new ArrayList<>();
}
