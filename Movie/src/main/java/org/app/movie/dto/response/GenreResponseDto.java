package org.app.movie.dto.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GenreResponseDto {

    private Long id;
    private String name;
}
