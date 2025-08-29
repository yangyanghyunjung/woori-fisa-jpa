package dev.syntax.model;

import dev.syntax.dto.MovieData;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    @ManyToMany(mappedBy = "movies")
    @Builder.Default
    private List<Actor> actors = new ArrayList<>();

    public void setActor(Actor actor) {
        this.actors.add(actor);
        actor.getMovies().add(this);
    }


}
