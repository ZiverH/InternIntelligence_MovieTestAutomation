package org.app.movie.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.app.movie.dto.request.MovieRequestDto;
import org.app.movie.dto.request.MovieSearchRequest;
import org.app.movie.dto.response.MovieResponsetDto;
import org.app.movie.service.MovieService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(MovieController.class)
class MovieControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService MovieService;

    @Test
    void returnAllMovies() throws Exception {

        //Arrange
        MovieResponsetDto dto = MovieResponsetDto.builder().
                id(1L).
                title("Sherlock Holmes").
                year(2009).
                director("Lionel Wigram").
                imdb("7.6").build();

        when(MovieService.getAllMovies()).thenReturn(List.of(dto));

        //Act&Assert
        mockMvc.perform(get("/movie/all")).andExpect(status().isOk());

    }

    @Test
    void givenValidIdAndThenReturnSuccess() throws Exception {

        //Arrange
        MovieResponsetDto dto = MovieResponsetDto.builder().
                id(1L).
                title("Sherlock Holmes").
                year(2009).
                director("Lionel Wigram").
                imdb("7.6").build();

        when(MovieService.getMovie(anyLong())).thenReturn(dto);

        //Act&Assert
        mockMvc.perform(get("/movie/{id}", dto.getId())
                        .contentType(MediaType.APPLICATION_JSON)).
                 andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("title").value("Sherlock Holmes"))
                .andExpect(jsonPath("year").value(2009))
                .andExpect(jsonPath("imdb").value("7.6")).
                 andExpect(status().isOk());

    }

    @Test
    void givenValidDataAndThenReturnSuccess() throws Exception {
        // Arrange

        MovieRequestDto dto = MovieRequestDto.builder().
                genres("Detective").
                title("Sherlock Holmes").
                year(2009).
                director("Lionel Wigram").
                imdb("7.6").build();

        Long movieId = 1L;

        when(MovieService.addMovie(any())).thenReturn(movieId);

        // Act & Assert
        mockMvc.perform(post("/movie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void givenValidIdThenChangeAndReturnSuccess() throws Exception {
        // Arrange

        MovieResponsetDto dto = MovieResponsetDto.builder().
                id(1L).
                title("Sherlock Holmes").
                year(2009).
                director("Lionel Wigram").
                imdb("7.6").build();

        when(MovieService.updateMovie(anyLong(),any())).thenReturn(dto);

        // Act & Assert
        mockMvc.perform(put("/movie/{id}", dto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("title").value("Sherlock Holmes"))
                .andExpect(jsonPath("year").value(2009))
                .andExpect(jsonPath("imdb").value("7.6"));
    }

    @Test
    void givenValidIdThenDeleteAndReturnSuccess() throws Exception {

        // Arrange

        MovieResponsetDto dto = MovieResponsetDto.builder().
                id(1L).
                title("Sherlock Holmes").
                year(2009).
                director("Lionel Wigram").
                imdb("7.6").build();

        when(MovieService.deleteMovie(anyLong())).thenReturn(dto);

        // Act & Assert
        mockMvc.perform(delete("/movie/{id}", dto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void givenValidDataThenSearchDataAndReturnSuccess() throws Exception {

        // Arrange
        MovieResponsetDto dto = MovieResponsetDto.builder().
                id(1L).
                title("Sherlock Holmes").
                year(2009).
                director("Lionel Wigram").
                imdb("7.6").build();

        when(MovieService.searchMovie(any())).thenReturn(List.of(dto));

        // Act & Assert
        mockMvc.perform(post("/movie/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

}