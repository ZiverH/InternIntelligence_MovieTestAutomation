package org.app.movie.dto.request;



import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MovieSearchRequest {

    private String title;
    private String director;
    private int beginyear;
    private int endyear;
    private String imdb;
}
