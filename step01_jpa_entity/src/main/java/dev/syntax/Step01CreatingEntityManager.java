package dev.syntax;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
// javax == jakarta

// EntityManager 객체 생성 코드
// EntityManager - Entity를 관리해주는 관리자
// Entity - 자바 모델 객체(이자 DB에서는 테이블)
public class Step01CreatingEntityManager {
    public static void main(String[] args) {
        // EntityManager를 생성해주는 공장(Factory) 객체를 먼저 생성
        EntityManagerFactory factory
                = Persistence.createEntityManagerFactory("step01");
        System.out.println("factory = " + factory);
        // -> EntityManager를 생성해서 Manager를 통해 DB에 접근해서 데이터를 조작하게 하려고 함
        // persistence.xml - JPA, Database 접근 관련한 설정 정보가 작성된 파일

        // EntityManager 생성
        // EntityManager - 영속성 컨텍스트에 접근하여 엔티티들을 관리하는 역할을 수행하는 객체
        // -> DB에 접근하여 데이터를 조작, 관리해주는 추상화된 객체
        EntityManager entityManager = factory.createEntityManager();
        System.out.println("entityManager = " + entityManager);

        /**
         * tip.
         * EntityManagerFactory - 커넥션 풀, 트랜잭션 처리 등과 같은 각 Session에서 사용되는 서비스들을 유지, 관리하는 역할
         * EntityManager - 하나의 작업 단위를 모델링한 싱글 스레드 객체
         */
    }
}
