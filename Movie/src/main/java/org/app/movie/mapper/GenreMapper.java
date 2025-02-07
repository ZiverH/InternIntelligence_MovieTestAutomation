package org.app.movie.mapper;

import org.app.movie.dto.response.GenreResponseDto;
import org.app.movie.model.Genre;
import org.mapstruct.Mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface GenreMapper {


    GenreResponseDto toDto(Genre genre);



}
