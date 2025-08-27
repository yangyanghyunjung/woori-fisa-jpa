package dev.syntax;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

// EntityManagmer를 통해 DB에 저장된 데이터(Entity) 조회
public class Step03FindEntity {
    public static void main(String[] args) {
        EntityManagerFactory factory
                = Persistence.createEntityManagerFactory("step01");

        EntityManager manager = factory.createEntityManager();

        // 조회할 때는 manager.find(조회하고자 하는 엔티티 클래스의 타입, 식별자);
        Book book = manager.find(Book.class, 1);// dev.syntax.Book 타입 중 id가 1번인 엔티티 조회
        // JPA없이 순수 JDBC로 작성했다면?
        /*
            if (rs.next()) {
                int id = rs.getInt("id");
                Stirng bookName = rs.getString("bookName");
                Book book = new Book(id, bookName...);
            }
         */
        System.out.println("book = " + book);

    }
}
