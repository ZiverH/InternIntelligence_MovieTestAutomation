package org.app.movie.service;

import static org.assertj.core.api.Assertions.assertThat;
import org.app.movie.dto.response.GenreResponseDto;
import org.app.movie.exception.DataNotDeleteableException;
import org.app.movie.exception.NotFoundException;
import org.app.movie.mapper.GenreMapper;
import org.app.movie.model.Genre;
import org.app.movie.model.Movie;
import org.app.movie.repository.GenreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.internal.matchers.ThrowableMessageMatcher.hasMessage;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class GenreServiceTest {

    @InjectMocks
    private GenreService genreService;
    @Mock
    private GenreRepository genreRepository;
    @Mock
    private GenreMapper genreMapper;

    private Genre genre;
    private GenreResponseDto genreResponseDto;

    @BeforeEach
    public void setUp() {
         genre = Genre.builder().
                id(1L).
                name("Comedy").
                 movies(List.of(new Movie())).
                build();

         genreResponseDto = GenreResponseDto.builder().
                 id(1L).name("Comedy").
                 build();
    }

    @Test
    void getAllGenresWithSuccess() {

        //Arrange
        when(genreRepository.findAll()).thenReturn(List.of(genre));
        when(genreMapper.toDto(any())).thenReturn(genreResponseDto);

        //Act
        List<GenreResponseDto> allGenres = genreService.getAllGenres();
        //Assert
        assertThat(allGenres).isEqualTo(List.of(genreResponseDto));

        verify(genreRepository,times(1)).findAll();
        verify(genreRepository,times(0)).save(any());
        verify(genreMapper,times(1)).toDto(any());
        verifyNoMoreInteractions(genreRepository, genreMapper);


    }

    @Test
    void getGenreWithIdAndThenReturnSuccess() {

        //Arrange
        when(genreRepository.findById(anyLong())).thenReturn(Optional.of(genre));
        when(genreMapper.toDto(any())).thenReturn(genreResponseDto);

        //Act
        GenreResponseDto result = genreService.getGenre(anyLong());

        //Assert
        assertThat(result.getId()).isEqualTo(genreResponseDto.getId());
        assertThat(result.getName()).isEqualTo(genreResponseDto.getName());

        verify(genreRepository,times(1)).findById(anyLong());
        verify(genreRepository,times(0)).save(any());
        verify(genreMapper,times(1)).toDto(any());
    }

    @Test
    void getGenreWithIdAndThenThrowsNotFoundException() {

        //Arrange
        when(genreRepository.findById(anyLong())).thenReturn(Optional.empty());

        //Act&Assert

        assertThatThrownBy(()-> genreService.getGenre(anyLong())).isInstanceOf(NotFoundException.class);

        verify(genreRepository,times(1)).findById(anyLong());
        verify(genreRepository,times(0)).save(any());
        verify(genreMapper,times(0)).toDto(any());
    }

    @Test
    void deleteWithIdAndThenReturnSuccess() {

        // Act
        Genre delete = Genre.builder().
                id(1L).
                name("Comedy").
                movies(Collections.emptyList()).
                build();
        when(genreRepository.findById(1L)).thenReturn(Optional.of(delete));
        when(genreMapper.toDto(delete)).thenReturn(genreResponseDto);

        //Arrange
        GenreResponseDto result = genreService.deleteGenre(1L);

        // Assert
        assertThat(result).isEqualTo(genreResponseDto);

        verify(genreRepository, times(1)).findById(1L);
        verify(genreRepository, times(1)).delete(delete);
        verify(genreMapper, times(1)).toDto(delete);
        verifyNoMoreInteractions(genreRepository, genreMapper);
    }


    @Test
    void deleteWithIdAndThenThrowsNotFoundException() {


        //Arrange
        when(genreRepository.findById(anyLong())).thenReturn(Optional.empty());

        //Act&Assert

        assertThatThrownBy(()-> genreService.deleteGenre(anyLong())).isInstanceOf(NotFoundException.class);

        verify(genreRepository,times(1)).findById(anyLong());
        verify(genreRepository,times(0)).save(any());
        verify(genreMapper,times(0)).toDto(any());
    }

    @Test
    void deleteWithIdAndThenThrowsDataNotDeleteableException() {


        //Arrange
        when(genreRepository.findById(anyLong())).thenReturn(Optional.of(genre));

        //Act&Assert

        assertThat(genre.getMovies()).isNotNull();
        assertThatThrownBy(()-> genreService.deleteGenre(anyLong())).isInstanceOf(DataNotDeleteableException.class)
        .hasMessage("Cannot delete this genre because there are movies associated with it.");

        verify(genreRepository,times(1)).findById(anyLong());
        verify(genreRepository,times(0)).save(any());
        verify(genreMapper,times(0)).toDto(any());
    }

    @Test
    void addGenreWithGenreNameAndThenReturnSuccess() {
        //Arrange

        String genreName="Comedy";
        Genre genre = Genre.builder().name(genreName.toLowerCase()).build();
        when(genreRepository.save(any(Genre.class))).thenReturn(genre);

        //Act
        Long id = genreService.addGenre(genreName);

        //Assert
        verify(genreRepository,times(1)).save(genre);
        verify(genreMapper,times(0)).toDto(any());
    }

    @Test
    void updateGenreWithIdAndNameThenReturnSuccess() {

        String updatedGenreName = "Horror";
        genreResponseDto.setName(updatedGenreName);
        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre));
        when(genreMapper.toDto(genre)).thenReturn(genreResponseDto);
        when(genreRepository.save(any())).thenReturn(genre);

        // Act
        GenreResponseDto dto = genreService.updateGenre(1L, updatedGenreName);

        // Assert
        var captor = ArgumentCaptor.forClass(Genre.class);
        verify(genreRepository, times(1)).save(captor.capture());

        Genre value = captor.getValue();

        assertThat(value.getId()).isEqualTo(genre.getId());
        assertThat(value.getMovies()).isEqualTo(genre.getMovies());
        assertThat(value.getName()).isEqualTo(genre.getName());
        assertThat(value.getName()).isEqualTo(genreResponseDto.getName());
        assertThat(value.getId()).isEqualTo(genreResponseDto.getId());

        verify(genreMapper, times(1)).toDto(value);
        verify(genreRepository, times(1)).findById(1L);

    }

    @Test
    void updateWithIdAndThenThrowsNotFoundException() {


        //Arrange
        when(genreRepository.findById(anyLong())).thenReturn(Optional.empty());

        //Act&Assert

        assertThatThrownBy(()-> genreService.updateGenre(1L,"Horror")).isInstanceOf(NotFoundException.class);

        verify(genreRepository,times(1)).findById(anyLong());
        verify(genreRepository,times(0)).save(any());
        verify(genreMapper,times(0)).toDto(any());
    }
}