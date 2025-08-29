package dev.syntax.dto;

import dev.syntax.model.Actor;
import dev.syntax.model.Movie;

import java.util.List;
import java.util.stream.Collectors;

public class ActorData {
    private int id;
    private String name;

    List<MovieData> movies;

    public ActorData(int id, String name, List<MovieData> movies) {
        this.id = id;
        this.name = name;
        this.movies = movies;
    }

    public static ActorData from(Actor actor) {
        final int id = actor.getId();
        final String name = actor.getName();
        final List<MovieData> movies = actor.getMovies().stream()
                .map(movie -> MovieData.from(movie))
                .collect(Collectors.toList());

        return new ActorData(id, name, movies);
    }
}
