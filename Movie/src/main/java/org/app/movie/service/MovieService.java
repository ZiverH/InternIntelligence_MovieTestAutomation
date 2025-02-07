package org.app.movie.service;

import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.app.movie.dto.request.MovieSearchRequest;
import org.app.movie.dto.update.MovieUpdateDto;
import org.app.movie.dto.request.MovieRequestDto;
import org.app.movie.dto.response.MovieResponsetDto;
import org.app.movie.exception.NotFoundException;
import org.app.movie.mapper.MovieMapper;
import org.app.movie.model.Genre;
import org.app.movie.model.Movie;
import org.app.movie.repository.GenreRepository;
import org.app.movie.repository.MovieRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final MovieMapper movieMapper;

    public List<MovieResponsetDto> getAllMovies() {
        List<Movie> movieList = movieRepository.findAll();
        return movieList.stream()
                .map(movie -> {
                    MovieResponsetDto dto = movieMapper.toDto(movie);
                    String genres = getGenresAsString(movie.getGenres());
                    dto.setGenre(genres);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<MovieResponsetDto> getByGenre(String genre) {
        List<Movie> movieList = movieRepository.findByGenre(genre);
        return movieList.stream()
                .map(movie -> {
                    MovieResponsetDto dto = movieMapper.toDto(movie);
                    String genres = getGenresAsString(movie.getGenres());
                    dto.setGenre(genres);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public MovieResponsetDto getMovie(Long id) {
        Movie movie = movieRepository.findById(id).orElseThrow(()-> new NotFoundException(Movie.class.getSimpleName()));
        String genres = getGenresAsString(movie.getGenres());
        MovieResponsetDto dto = movieMapper.toDto(movie);
        dto.setGenre(genres);
        return dto;
    }

    public Long addMovie(MovieRequestDto movieRequestDto) {

        String[] genreNames = movieRequestDto.getGenres().split(",");
        List<Genre> genreList = Arrays.stream(genreNames).map(String::trim).map(name ->
                genreRepository.findByName(name.toLowerCase()).orElseThrow(() -> new NotFoundException(Genre.class.getSimpleName()))).toList();
        Movie movie = movieMapper.dtoToEntity(movieRequestDto);
        movie.setGenres(genreList);
        Movie save = movieRepository.save(movie);
        return save.getId();
    }

    public MovieResponsetDto updateMovie(Long id, MovieUpdateDto movieUpdateDto) {
        Movie movie = movieRepository.findById(id).orElseThrow(()-> new NotFoundException(Movie.class.getSimpleName()));

        movieMapper.updatetoMovie(movie, movieUpdateDto);
        if(movieUpdateDto.getGenres() != null) {
            String[] genreNames = movieUpdateDto.getGenres().split(",");
            List<Genre> genreList = Arrays.stream(genreNames).map(String::trim).map(name ->
                    genreRepository.findByName(name.toLowerCase()).orElseThrow(() -> new NotFoundException(Genre.class.getSimpleName()))).toList();
            movie.setGenres(genreList);
        }
        movieRepository.save(movie);
        MovieResponsetDto responsetDto = movieMapper.toDto(movie);
        String genres = getGenresAsString(movie.getGenres());
        responsetDto.setGenre(genres);
        return responsetDto;
    }

    public MovieResponsetDto deleteMovie(Long id){
        Movie movie = movieRepository.findById(id).orElseThrow(()-> new NotFoundException(Movie.class.getSimpleName()));
        String genres = getGenresAsString(movie.getGenres());
        movieRepository.delete(movie);
        MovieResponsetDto dto = movieMapper.toDto(movie);
        dto.setGenre(genres);
        return dto;
    }

    public String getGenresAsString(List<Genre> genreList) {
        return genreList.stream()
                .map(Genre::getName)
                .collect(Collectors.joining(","));
    }

    public List<MovieResponsetDto> searchMovie(MovieSearchRequest movieSearchRequest) {

        List<Movie> all = movieRepository.findAll(specMovie(movieSearchRequest));
        return all.stream()
                .map(movie -> {
                    MovieResponsetDto dto = movieMapper.toDto(movie);
                    String genres = getGenresAsString(movie.getGenres());
                    dto.setGenre(genres);
                    return dto;
                })
                .collect(Collectors.toList());


    }

    public static Specification<Movie> specMovie(MovieSearchRequest searchRequest) {
        return (Root<Movie> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            root.fetch("genres", JoinType.LEFT);
            Predicate predicate = cb.conjunction();
            if (searchRequest.getTitle() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("title"), searchRequest.getTitle()));
            }
            if (searchRequest.getDirector() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("director"), searchRequest.getDirector()));
            }

            if (searchRequest.getBeginyear() != 0 && searchRequest.getEndyear() != 0) {
                predicate = cb.and(predicate, cb.between(root.get("year"),
                        searchRequest.getBeginyear(), searchRequest.getEndyear()));
            } else if (searchRequest.getBeginyear() != 0) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("year"), searchRequest.getBeginyear()));
            } else if (searchRequest.getEndyear() != 0) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("year"), searchRequest.getEndyear()));
            }

            if (searchRequest.getImdb() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("imdb"), searchRequest.getImdb()));
            }
            return predicate;
        };
    }
}
