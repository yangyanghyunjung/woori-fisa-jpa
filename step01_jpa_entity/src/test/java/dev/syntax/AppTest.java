package dev.syntax;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// 영속성 컨텍스트의 개념 및 생명주기 동작을 테스트하는 코드
public class AppTest {

    EntityManagerFactory factory
            = Persistence.createEntityManagerFactory("step01");
    EntityManager manager = factory.createEntityManager();
    EntityTransaction transaction = manager.getTransaction();

    @Test
    @DisplayName("Transient 상태 확인 테스트")
    void testTransient() {

        // Book 객체 생성
        Book book = new Book("하루하나 금융 지식", "권정주");

        // EntityManager를 통해 영속화 되었는지 확인
        boolean isManaged = manager.contains(book);
        // Check if the instance is a managed(영속화된) entity instance
        // belonging to the current persistence context.

        assertFalse(isManaged); // "managed 상태가 아닐 것이다."
    }

    // PC - Persistence Context(영속성 컨텍스트)
    @Test
    @DisplayName("persist()를 수행할 경우, 엔티티가 PC에 영속화된다.")
    void testPersist() {
        // 엔티티가 PC에 잘 적용되었는지?
        // Hint. 영속성 컨텍스트는 본질적으로 캐싱 동작을 수행
        // 커밋 후 다시 조회(SELECT 쿼리가 한 번 더 실행되는가?)
        transaction.begin();
        Book book = new Book("하루하나 금융 지식", "권정주");
        manager.persist(book);
        transaction.commit();

        // 저장된 것을 확인해야하니까 조회된 엔티티와 삽입 시 적용된 엔티티 객체의 참조값 비교
        Book foundBook = manager.find(Book.class, book.getId());
        assertEquals(book, foundBook); // 참조값 비교

    }

    @Test
    @DisplayName("한 번 영속화된 엔티티는 다시 조회 시 DB가 아닌 PC에서 조회된다.")
    void testFindWithPC() {
        // TODO: find()를 두 번 수행하고, 각각의 반환값을 비교
        int id = 5;

        Book book = manager.find(Book.class, id);
        System.out.println("첫 번째로 조회한 책 = " + book);

        // 한 번 조회 후(첫 조회할 때 PC에 캐싱) 다시 한 번 조회할 때에도 SELECT를 날리는지 확인
        // -> SELECT가 한 번만 실행됨

        Book bookTwice = manager.find(Book.class, id);
        System.out.println("두 번째로 조회한 책 = " + bookTwice);

        assertEquals(book, bookTwice);

    }

    @Test
    @DisplayName("엔티티의 값을 수정하고 트랜잭션을 commit()하면 변경 감지가 발생되어 UPDATE 쿼리가 수행된다.")
    void testUpdate() {
        // TODO: 단정문 없이 콘솔 확인 용도로 작성해도 됨, DB에서 확인
        int id = 3;
        Book book1 = manager.find(Book.class, id);
        System.out.println("book = " + book1);

        String originalName = book1.getBookName();
        String updatedName = "책";

        transaction.begin();
        book1.updateBookName(updatedName);
        transaction.commit();

        assertNotEquals(originalName, updatedName);

    }

    @Test
    @DisplayName("만약 엔티티를 detach할경우 필드의 값을 변경해도 UPDATE쿼리가 수행되지 않는다")
    void testDetach() {
        Book book1 = manager.find(Book.class, 3);
        System.out.println("book = " + book1);

        String updateName = "엄청졸린책22";

        manager.detach(book1); // 컨텍스트에서 해제
        assertFalse(manager.contains(book1)); // 정말 detached인지 확인

        transaction.begin();
        book1.updateBookName(updateName);
        transaction.commit();     // ★ UPDATE 발생하지 않아야 함

        Book reloaded = manager.find(Book.class, 3);
        assertNotEquals(reloaded.getBookName(), updateName);
    }

    @Test
    @DisplayName("remove를 수행할경우 PC와 DB에서 데이터가 제거된다")
    void testRemove() {
        Book book = manager.find(Book.class, 1);
    }

    @Test
    @DisplayName("flush()를 수행하면 변경 사항이 즉시 DB에 반영되지만, 트랜잭션이 롤백되면 변경 사항도 취소된다.")
    void testFlushWithRollback() {
        Book book = manager.find(Book.class, 3);

        transaction.begin();
            book.updateBookName("변경책");
            manager.flush();

            Book book2 = manager.find(Book.class, 3);
            assertEquals(book2.getBookName(), "변경책");

        transaction.rollback();

        assertNotEquals(book.getBookName(), book2.getBookName());
    }

    @Test
    @DisplayName("remove()로 삭제 예약된 엔티티는 변경해도 UPDATE가 수행되지 않는다")
    void testRemoveNoUpdate() {
        // given: 한 건 저장
        transaction.begin();
        Book book = new Book("삭제 예정 책", "저자C");
        manager.persist(book);
        transaction.commit();
        int id = book.getId();

        // when: remove 호출
        transaction.begin();
        manager.remove(book);                   // 삭제 예약
        book.updateBookName("삭제 후 변경");     // 값 변경 시도
        transaction.commit();                   // DELETE만 발생해야 함

        // then: DB에서 조회하면 null 이어야 함
        Book reloaded = manager.find(Book.class, id);
        assertNull(reloaded);
    }

    @Test
    @DisplayName("영속화된 엔티티를 detatch 후  다시 조회 시 PC가 아닌 DB에서 조회된다.")
    void testFindDetachFind() {
        int id = 3;
        Book book1 = manager.find(Book.class, id);

        manager.detach(book1);

        Book book2 = manager.find(Book.class, id);

        assertNotEquals(book1, book2);
    }
}
