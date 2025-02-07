package org.app.movie.service;

import org.app.movie.dto.request.MovieRequestDto;
import org.app.movie.dto.request.MovieSearchRequest;
import org.app.movie.dto.response.MovieResponsetDto;
import org.app.movie.dto.update.MovieUpdateDto;
import org.app.movie.exception.DataNotDeleteableException;
import org.app.movie.exception.NotFoundException;
import org.app.movie.mapper.MovieMapper;
import org.app.movie.model.Genre;
import org.app.movie.model.Movie;
import org.app.movie.model.Movie;
import org.app.movie.repository.GenreRepository;
import org.app.movie.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @InjectMocks
    private MovieService movieService;
    @Mock
    private MovieRepository movieRepository;
    @Mock
    private GenreRepository genreRepository;
    @Mock
    private MovieMapper movieMapper;

    private Movie movie;
    private MovieResponsetDto movieResponseDto;


    @BeforeEach
    public void setUp() {
        movie = Movie.builder().
                id(1L).
                title("Sherlock Holmes").
                year(2009).
                director("Lionel Wigram").
                genres(new ArrayList<Genre>()).
                imdb("7.6").
                build();

        movieResponseDto = MovieResponsetDto.builder().
                id(1L).
                title("Sherlock Holmes").
                year(2009).
                director("Lionel Wigram").
                imdb("7.6").
                build();
    }

    @Test
    void getAllMoviesWithSuccess() {

        //Arrange
        when(movieRepository.findAll()).thenReturn(List.of(movie));
        when(movieMapper.toDto(any())).thenReturn(movieResponseDto);

        //Act
        List<MovieResponsetDto> allMovies = movieService.getAllMovies();
        //Assert
        assertThat(allMovies).isEqualTo(List.of(movieResponseDto));

        verify(movieRepository,times(1)).findAll();
        verify(movieRepository,times(0)).save(any());
        verify(movieMapper,times(1)).toDto(any());
        verifyNoMoreInteractions(movieRepository, movieMapper);

    }

    @Test
    void getMovieWithIdAndThenReturnSuccess() {

        //Arrange
        when(movieRepository.findById(anyLong())).thenReturn(Optional.of(movie));
        when(movieMapper.toDto(any())).thenReturn(movieResponseDto);

        //Act
        MovieResponsetDto  result = movieService.getMovie(anyLong());

        //Assert
        assertThat(result.getId()).isEqualTo(movieResponseDto.getId());
        assertThat(result.getDirector()).isEqualTo(movieResponseDto.getDirector());
        assertThat(result.getTitle()).isEqualTo(movieResponseDto.getTitle());
        assertThat(result.getImdb()).isEqualTo(movieResponseDto.getImdb());

        verify(movieRepository,times(1)).findById(anyLong());
        verify(movieRepository,times(0)).save(any());
        verify(movieMapper,times(1)).toDto(any());
    }

    @Test
    void getMovieWithIdAndThenThrowsNotFoundException() {

        //Arrange
        when(movieRepository.findById(anyLong())).thenReturn(Optional.empty());

        //Act&Assert

        assertThatThrownBy(()-> movieService.getMovie(anyLong())).isInstanceOf(NotFoundException.class);

        verify(movieRepository,times(1)).findById(anyLong());
        verify(movieRepository,times(0)).save(any());
        verify(movieMapper,times(0)).toDto(any());
    }

    @Test
    void deleteWithIdAndThenReturnSuccess() {

        // Act
        Movie delete = Movie.builder().
                id(1L).
                title("Sherlock Holmes").
                year(2009).
                director("Lionel Wigram").
                genres(new ArrayList<Genre>()).
                imdb("7.6").
                build();

        when(movieRepository.findById(1L)).thenReturn(Optional.of(delete));
        when(movieMapper.toDto(delete)).thenReturn(movieResponseDto);

        //Arrange
        MovieResponsetDto result = movieService.deleteMovie(1L);

        // Assert
        assertThat(result).isEqualTo(movieResponseDto);

        verify(movieRepository, times(1)).findById(1L);
        verify(movieRepository, times(1)).delete(delete);
        verify(movieMapper, times(1)).toDto(delete);
        verifyNoMoreInteractions(movieRepository, movieMapper);
    }


    @Test
    void deleteWithIdAndThenThrowsNotFoundException() {


        //Arrange
        when(movieRepository.findById(anyLong())).thenReturn(Optional.empty());

        //Act&Assert

        assertThatThrownBy(()-> movieService.deleteMovie(anyLong())).isInstanceOf(NotFoundException.class);

        verify(movieRepository,times(1)).findById(anyLong());
        verify(movieRepository,times(0)).save(any());
        verify(movieMapper,times(0)).toDto(any());
    }


    @Test
    void addMovieAndThenReturnSuccess() {
        //Arrange

        MovieRequestDto dto = MovieRequestDto.builder().
                title("Sherlock Holmes").
                year(2009).
                director("Lionel Wigram").
                genres("Detective").
                imdb("7.6").
                build();

        Genre genre = Genre.builder().id(1L).name("Detective").build();

        when(genreRepository.findByName(any())).thenReturn(Optional.of(genre));
        when(movieMapper.dtoToEntity(any())).thenReturn(movie);
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);

        //Act
        Long id = movieService.addMovie(dto);

        //Assert
        assertThat(id).isEqualTo(movie.getId());
        assertThat(movie.getTitle()).isEqualTo(dto.getTitle());
        assertThat(movie.getYear()).isEqualTo(dto.getYear());
        assertThat(movie.getDirector()).isEqualTo(dto.getDirector());
        assertThat(movie.getImdb()).isEqualTo(dto.getImdb());

        verify(movieRepository,times(1)).save(movie);
        verify(movieMapper,times(0)).toDto(any());
        verify(genreRepository,times(1)).findByName(any());
    }

    @Test
    void addMovieAndThenThrowGenreNotFoundException() {
        //Arrange

        MovieRequestDto dto = MovieRequestDto.builder().
                title("Sherlock Holmes").
                year(2009).
                director("Lionel Wigram").
                genres("Detective").
                imdb("7.6").
                build();

        when(genreRepository.findByName(any())).thenReturn(Optional.empty());

        //Act&Assert
        assertThatThrownBy(()-> movieService.addMovie(dto)).isInstanceOf(NotFoundException.class);

        verify(movieRepository,times(0)).save(movie);
        verify(genreRepository,times(1)).findByName(any());
    }

    @Test
    void getMoviesByGenre() {

        Genre genre = Genre.builder().
                id(1L).name("Detective").
                build();

        when(movieRepository.findByGenre(any())).thenReturn(List.of(movie));
        when(movieMapper.toDto(any())).thenReturn(movieResponseDto);

        // Act
        List<MovieResponsetDto> dto = movieService.getByGenre("Detective");

        verify(movieRepository, times(1)).findByGenre(any());
    }

    @Test
    void searchMoviesAndThenReturnSuccess() {
        //Arrange

        MovieSearchRequest dto = MovieSearchRequest.builder().
                title("Sherlock Holmes").
                director("Lionel Wigram").
                imdb("7.6").
                build();

        when(movieRepository.findAll(any(Specification.class))).thenReturn(List.of(movie));
        when(movieMapper.toDto(any())).thenReturn(movieResponseDto);

        //Act
        List<MovieResponsetDto> movieResponsetDtos = movieService.searchMovie(dto);
        // Assert

        verify(movieRepository,times(1)).findAll(any(Specification.class));
        verify(movieMapper,times(1)).toDto(any());
    }

    @Test
    void updateMoviesAndThenReturnSuccess() {
        //Arrange
        MovieUpdateDto dto = MovieUpdateDto.builder().
                title("Sherlock Holmes").
                director("Lionel Wigram").
                imdb("9.6").
                genres("Detective").
                build();
        Genre genre = Genre.builder().id(1L).name("Detective").build();

        when(movieRepository.findById(anyLong())).thenReturn(Optional.of(movie));
        when(movieMapper.toDto(movie)).thenReturn(movieResponseDto);
        when(genreRepository.findByName(any())).thenReturn(Optional.of(genre));

        //Act
        MovieResponsetDto movieResponsetDtos = movieService.updateMovie(movie.getId(),dto);

        // Assert
        assertThat(movie.getId()).isEqualTo(movieResponseDto.getId());
        assertThat(movie.getTitle()).isEqualTo(movieResponseDto.getTitle());
        assertThat(movie.getDirector()).isEqualTo(movieResponseDto.getDirector());
        assertThat(movie.getImdb()).isEqualTo(movieResponseDto.getImdb());

        verify(movieRepository,times(1)).findById(movie.getId());
        verify(movieRepository,times(1)).save(movie);
        verify(genreRepository,times(1)).findByName(any(String.class));
        verify(movieMapper,times(1)).toDto(any());
    }


    @Test
    void updateMoviesAndThenThrowGenreNotFoundException() {
        // Arrange
        MovieUpdateDto dto = MovieUpdateDto.builder()
                .title("Sherlock Holmes")
                .director("Lionel Wigram")
                .imdb("9.6")
                .genres("Detectiv")
                .build();

        when(movieRepository.findById(anyLong())).thenReturn(Optional.of(movie));
        when(genreRepository.findByName(any())).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> movieService.updateMovie(movie.getId(), dto))
                .isInstanceOf(NotFoundException.class);

        verify(movieRepository, times(1)).findById(movie.getId());
        verify(movieRepository, times(0)).save(movie);
        verify(movieMapper, times(0)).toDto(any());
    }




}