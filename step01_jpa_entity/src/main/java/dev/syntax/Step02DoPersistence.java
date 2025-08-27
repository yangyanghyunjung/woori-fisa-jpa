package dev.syntax;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

// JPA의 ENtityManager를 통해 기본적인 데이터(Entoty)조작, DBMS에 영속화(INSERT)
public class Step02DoPersistence {
    public static void main(String[] args) {
        EntityManagerFactory factory
                = Persistence.createEntityManagerFactory("step01");

        EntityManager entityManager = factory.createEntityManager();

        // 트랜잭션 처리
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        // Book이라는 모델 클래스를 생성해서 DBMS에 영속화
        // Book 테이블 생성, 추가

        Book book = new Book("금융 지식", "쿠로미");

        // JDBC 방식
        // Connection 객체 생성, PreparedStatement, ResultSet 등등

        // JPA
        entityManager.persist(book); //INSERT INTO book; 쿼리가 생성됨
        transaction.commit();
    }
}
