package org.app.movie.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.app.movie.annotation.MaxYear;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;

@Data
@Builder
public class MovieRequestDto {

    @NotNull
    @Size(min = 3, max = 50, message = "Title length must be between 2 and 50")
    private String title;
    @NotNull
    @Size(min = 3, max = 50, message = "Name length must be between 2 and 50")
    private String director;
    @NotNull
    @Min(value = 1800, message = "Year must be a 4-digit number")
    @MaxYear
    private int year;
    @NotNull
    private String genres;
    @NotNull
    @Pattern(regexp = "^(?:[1-9]\\d?|10)\\.\\d$", message = "IMDb rating must be a valid decimal like 7.8")
    @DecimalMax(value = "10.0", message = "IMDb rating must be a maximum of 10.0")
    private String imdb;
}
