package org.app.movie.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.app.movie.dto.response.GenreResponseDto;
import org.app.movie.service.GenreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@WebMvcTest(GenreController.class)
class GenreControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenreService genreService;

    @Test
    void returnAllGenres() throws Exception {

        //Arrange
        GenreResponseDto dto = GenreResponseDto.builder().id(1L).name("Comedy").build();
        when(genreService.getAllGenres()).thenReturn(List.of(dto));

        //Act&Assert
        mockMvc.perform(get("/genre/all")).andExpect(status().isOk());

    }

    @Test
    void givenValidIdAndThenReturnSuccess() throws Exception {

        //Arrange
        GenreResponseDto dto = GenreResponseDto.builder().id(1L).name("Comedy").build();
        when(genreService.getGenre(anyLong())).thenReturn(dto);

        //Act&Assert
        mockMvc.perform(get("/genre/{id}", dto.getId())
                        .contentType(MediaType.APPLICATION_JSON)).
                andExpect(jsonPath("name").value("Comedy")).
                andExpect(status().isOk());

    }

    @Test
    void givenValidDataAndThenReturnSuccess() throws Exception {
        // Arrange
        String genreName = "Drama";
        Long genreId = 1L;

        when(genreService.addGenre(any())).thenReturn(genreId);

        // Act & Assert
        mockMvc.perform(post("/genre")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(genreName)))
                .andExpect(status().isCreated());
    }

    @Test
    void givenValidIdThenChangeAndReturnSuccess() throws Exception {
        // Arrange

        GenreResponseDto dto = GenreResponseDto.builder().id(1L).name("Comedy").build();

        when(genreService.updateGenre(anyLong(),any())).thenReturn(dto);

        // Act & Assert
        mockMvc.perform(put("/genre/{id}", dto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("name").value("Comedy"));
    }

    @Test
    void givenValidIdThenDeleteAndReturnSuccess() throws Exception {

        // Arrange

        GenreResponseDto dto = GenreResponseDto.builder().id(1L).name("Comedy").build();
        when(genreService.deleteGenre(anyLong())).thenReturn(dto);

        // Act & Assert
        mockMvc.perform(delete("/genre/{id}", dto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

}