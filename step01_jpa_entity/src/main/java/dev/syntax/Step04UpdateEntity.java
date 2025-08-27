package dev.syntax;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

// Entity 업데이트(DB 테이블 내 특정 레코드 값 갱신)
public class Step04UpdateEntity {
    public static void main(String[] args) {
        EntityManagerFactory factory
                = Persistence.createEntityManagerFactory("step01");
        EntityManager manager = factory.createEntityManager();
        EntityTransaction transaction = manager.getTransaction();

        // UPDATE를 수행하려면 어떤 레코드를 업데이트할 것인지 확인해야 하기 때문에
        // 조회를 먼저 해야함
        Book book = manager.find(Book.class, 1);
        System.out.println("book = " + book);

        // 일반적으로 객체지향 패러다임에서 필드의 값을 수정할 때? - setter 활용\
        transaction.begin();

            book.updateBookName("컴퓨터 시스템 딥다이브2"); // UPDATE book set ~ 쿼리가 실행됨
        // 트랜잭션 내에서 필드의 값이 갱신되면 JPA는 변경을 감지,
        // 영속성 컨텍스트(캐시)와 Database 간의 동기화 작업을 수행
        transaction.commit();
    }
}
