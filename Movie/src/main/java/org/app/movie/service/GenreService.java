package org.app.movie.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.app.movie.dto.response.GenreResponseDto;
import org.app.movie.exception.DataNotDeleteableException;
import org.app.movie.exception.NotFoundException;
import org.app.movie.mapper.GenreMapper;
import org.app.movie.model.Genre;
import org.app.movie.repository.GenreRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreService {

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    public List<GenreResponseDto> getAllGenres() {
        List<Genre> genres = genreRepository.findAll();
        return genres.stream().map(genreMapper::toDto).toList();
    }

    public GenreResponseDto getGenre(Long id) {
        Genre genre = genreRepository.findById(id).orElseThrow(()-> new NotFoundException(Genre.class.getSimpleName()));
        return genreMapper.toDto(genre);
    }

    public Long addGenre(String name) {
        Genre genre = Genre.builder().name(name.toLowerCase()).build();
        Genre save = genreRepository.save(genre);
        return save.getId();
    }

    public GenreResponseDto updateGenre(Long id, String name) {
        Genre genre = genreRepository.findById(id).orElseThrow(()-> new NotFoundException(Genre.class.getSimpleName()));
        genre.setName(name);
        genreRepository.save(genre);
        return genreMapper.toDto(genre);
    }

    public GenreResponseDto deleteGenre(Long id) {
        Genre genre = genreRepository.findById(id).orElseThrow(()-> new NotFoundException(Genre.class.getSimpleName()));
        if(!genre.getMovies().isEmpty()){
            throw new DataNotDeleteableException("Cannot delete this genre because there are movies associated with it.");
        }
        genreRepository.delete(genre);
        return genreMapper.toDto(genre);
    }
}
