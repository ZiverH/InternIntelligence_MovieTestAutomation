package org.app.movie.mapper;

import org.app.movie.dto.update.MovieUpdateDto;
import org.app.movie.dto.request.MovieRequestDto;
import org.app.movie.dto.response.MovieResponsetDto;
import org.app.movie.model.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface MovieMapper {


//    @Mapping(target = "genres",ignore = true)
    MovieResponsetDto toDto(Movie movie);
    @Mapping(target = "genres",ignore = true)
    Movie dtoToEntity(MovieRequestDto dto);

    default void updatetoMovie(Movie movie, MovieUpdateDto dto) {

        if(dto.getTitle() != null){
            movie.setTitle(dto.getTitle());
        }
        if (dto.getImdb()!=null){
            movie.setImdb(dto.getImdb());
        }
        if (dto.getDirector()!=null){
            movie.setDirector(dto.getDirector());
        }
        if(dto.getYear()!=0){
            movie.setYear(dto.getYear());
        }
    }

}
