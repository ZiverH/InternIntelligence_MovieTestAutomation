package org.app.movie.dto.update;


import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.app.movie.annotation.MaxYear;

@Data
@Builder
public class MovieUpdateDto {

    @Size(min = 3, max = 50, message = "Title length must be between 2 and 50")
    private String title;
    @Size(min = 3, max = 50, message = "Name length must be between 2 and 50")
    private String director;
    @Min(value = 1000, message = "Year must be a 4-digit number")
    @MaxYear
    private int year;
    private String genres;
    @Pattern(regexp = "^(?:[1-9]\\d?|10)\\.\\d$", message = "IMDb rating must be a valid decimal like 7.8")
    @Pattern(regexp = "^(?:[1-9]\\d?|10)\\.\\d$", message = "IMDb rating must be a valid decimal like 7.8")
    @DecimalMax(value = "10.0", message = "IMDb rating must be a maximum of 10.0")
    private String imdb;
}
