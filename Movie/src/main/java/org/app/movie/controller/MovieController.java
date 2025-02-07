package org.app.movie.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.app.movie.dto.request.MovieSearchRequest;
import org.app.movie.dto.update.MovieUpdateDto;
import org.app.movie.dto.request.MovieRequestDto;
import org.app.movie.dto.response.MovieResponsetDto;
import org.app.movie.service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/movie")
@RequiredArgsConstructor
@Tag(name="Movie", description = "Movie API. Contains all operations that can be performed with movies")
public class MovieController {
    private final MovieService movieService;

    @GetMapping("/all")
    public ResponseEntity<List<MovieResponsetDto>> getAllMovies() {
       List<MovieResponsetDto> movieResponsetDtoList = movieService.getAllMovies();
       return ResponseEntity.ok().body(movieResponsetDtoList);
    }

    @GetMapping("/genre")
    public ResponseEntity<List<MovieResponsetDto>> getByGenre(@RequestParam String genre) {
        List<MovieResponsetDto> movieResponsetDtoList = movieService.getByGenre(genre);
        return ResponseEntity.ok().body(movieResponsetDtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieResponsetDto> getMovie(@PathVariable Long id) {
        MovieResponsetDto movie = movieService.getMovie(id);
        return ResponseEntity.ok().body(movie);
    }
    @PostMapping()
    public ResponseEntity<Void> addMovie(@RequestBody @Valid MovieRequestDto movieRequestDto) {
        Long id = movieService.addMovie(movieRequestDto);
        return ResponseEntity.created(URI.create("movie/" + id)).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieResponsetDto> updateMovie(@PathVariable Long id, @RequestBody @Valid MovieUpdateDto movieUpdateDto) {
        return  ResponseEntity.ok(movieService.updateMovie(id, movieUpdateDto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<MovieResponsetDto> deleteMovie(@PathVariable Long id) {
        return  ResponseEntity.ok(movieService.deleteMovie(id));
    }

    @PostMapping("/search")
    public ResponseEntity<List<MovieResponsetDto>> searchMovie(@RequestBody @Valid MovieSearchRequest movieSearchRequest) {
        return ResponseEntity.ok(movieService.searchMovie(movieSearchRequest));
    }




}
