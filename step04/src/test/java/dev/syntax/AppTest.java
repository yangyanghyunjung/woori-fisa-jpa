package dev.syntax;

import dev.syntax.dto.ActorData;
import dev.syntax.dto.MovieData;
import dev.syntax.model.Actor;
import dev.syntax.model.Genre;
import dev.syntax.model.Movie;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest {
    EntityManagerFactory factory
            = Persistence.createEntityManagerFactory("step04");
    EntityManager manager = factory.createEntityManager();
    EntityTransaction transaction = manager.getTransaction();


    @Test
    @DisplayName("양방향 데이터 저장")
    void testSave() {
        transaction.begin();
        Actor wooz = Actor.builder().name("우즈").build();
        manager.persist(wooz);

        Actor gunne = Actor.builder().name("건애").build();
        manager.persist(gunne);

        Actor taekki = Actor.builder().name("태끼곤듀").build();
        manager.persist(taekki);

        Movie movie1 = Movie.builder().title("너의결혼식").build();
        manager.persist(movie1);

        Movie movie2 = Movie.builder().title("인어공주").build();
        manager.persist(movie2);

        wooz.setMovie(movie1);
        gunne.setMovie(movie1);
        gunne.setMovie(movie2);
        taekki.setMovie(movie2);

        transaction.commit();
    }

    @Test
    void testFind() {
        transaction.begin();
        List<Movie> movies = manager.find(Movie.class, 1);
        ActorData.from(movie);

        transaction.commit();
    }


}
