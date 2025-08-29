package dev.syntax;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AppTest {
    EntityManagerFactory factory
            = Persistence.createEntityManagerFactory("step02");
    EntityManager manager = factory.createEntityManager();
    EntityTransaction transaction = manager.getTransaction();

    @Test
    @DisplayName("두 엔티티(Student, Major) 간의 연관관계 맵핑 후 저장 테스트")
    void saveRelation() {
        transaction.begin();
        // 학과 데이터 생성, DB에 저장
        Major cs = Major.builder().name("컴공").build();
        manager.persist(cs);

        // 학생1 생성 및 저장
        Student lee = Student.builder().name("종혁").build();
        // TODO: 학생을 저장할 때, 학과를 추가해서 저장
        lee.setMajor(cs); // 종혁이의 학과를 컴공으로 지정
        manager.persist(lee);
        transaction.commit();
    }

    @Test
    @DisplayName("조회된 학생 엔티티를 통해 해당 학생의 전공이 무엇인지 확인할 수 있다.")
    void findRelation() {
        Student student = manager.find(Student.class, 1);
        System.out.println("student = " + student);

        Major major = student.getMajor();
        System.out.println("major = " + major);
    }

    @Test
    @DisplayName("조회된 학생의 전공을 다른 전공으로 변경할 수 있다.")
    void updateRelation() {

        transaction.begin();
            Major major = Major.builder().name("영문학과").build();
            manager.persist(major);

            Student student = manager.find(Student.class, 1);
            student.setMajor(major);
        transaction.commit();

    }

    @Test
    @DisplayName("Student와 연관된 엔티티(Major)의 관계를 제거할 수 있다.")
    void deleteEntity() {
        transaction.begin();
        Student student = manager.find(Student.class, 1);
        student.setMajor(null);

        Major kor = manager.find(Major.class, 3);
        manager.remove(kor);

        transaction.commit();
    }
}