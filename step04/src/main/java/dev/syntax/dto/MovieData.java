package dev.syntax.dto;

import dev.syntax.model.Genre;
import dev.syntax.model.Movie;

import java.util.List;
import java.util.stream.Collectors;

public class MovieData {
    private int id;
    private String title;
    private Genre genre;

    List<ActorData> actors;

    public MovieData(int id, String title, List<ActorData> actors) {
        this.id = id;
        this.title = title;
        this.actors = actors;
    }


    public static MovieData from(Movie movie) {
        final int id = movie.getId();
        final String title  = movie.getTitle();
        final Genre genre = movie.getGenre();
        List<ActorData> actors = movie.getActors().stream()
                .map(actor -> ActorData.from(actor))
                .collect(Collectors.toList());

        return new MovieData(id, title, actors);

    }
}
