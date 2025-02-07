package org.app.movie.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.app.movie.dto.response.GenreResponseDto;
import org.app.movie.service.GenreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/genre")
@RequiredArgsConstructor
@Tag(name="Genre", description = "Genre API. Contains all operations that can be performed with genres")
public class GenreController {
    private final GenreService genreService;

    @GetMapping("/all")
    public ResponseEntity<List<GenreResponseDto>> getAllgenres() {
       List<GenreResponseDto> genreResponseDtoList = genreService.getAllGenres();
       return ResponseEntity.ok().body(genreResponseDtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreResponseDto> getGenre(@PathVariable Long id) {

        GenreResponseDto genre = genreService.getGenre(id);
        return ResponseEntity.ok().body(genre);
    }
    @PostMapping()
    public ResponseEntity<Void> addGenre(@RequestBody String name) {
        Long id = genreService.addGenre(name);
        return ResponseEntity.created(URI.create("genre/" + id)).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenreResponseDto> updateGenre(@PathVariable Long id, @RequestBody  String name) {
        return  ResponseEntity.ok(genreService.updateGenre(id, name));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<GenreResponseDto> deleteGenre(@PathVariable Long id) {
        return  ResponseEntity.ok(genreService.deleteGenre(id));
    }





}
