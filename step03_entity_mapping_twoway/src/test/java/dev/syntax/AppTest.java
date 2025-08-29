package dev.syntax;

import dev.syntax.dto.MajorData;
import dev.syntax.model.Major;
import dev.syntax.model.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

public class AppTest {

    EntityManagerFactory factory
            = Persistence.createEntityManagerFactory("step03");
    EntityManager manager = factory.createEntityManager();
    EntityTransaction transaction = manager.getTransaction();

    @Test
    @DisplayName("양방향 연관관계 저장")
    void testSave() {
        transaction.begin();
        // 학과 데이터 생성 및 저장
        Major cs = Major.builder().name("컴공").build();
        manager.persist(cs);

        // 학생 데이터 생성 및 저장
        Student lee = Student.builder().name("살모사").build();
        lee.setMajor(cs); // 연관관계를 설정하는 부분 student -> major
        // JPA는 이곳에 입력된 값을 통해 외래키를 관리함(Student.major)

        manager.persist(lee);

        transaction.commit();
        /*
            실행 결과
            +----+----------+------+
            | id | major_id | name |
            +----+----------+------+
            |  1 |        1 | 종혁 |
            +----+----------+------+
            -> 양방향 연관관계에서는 연관관계의 주인(외래키를 가진 쪽, Student)이 외래키를 관리하기 때문에
            주인이 아닌 방향인 Major.students에는 값을 설정하지 않아도 Database에 값이 정상적으로 입력됨
         */
    }

    @Test
    @DisplayName("양방향 연관관계에서 외래키 설정이 미흡한 경우?")
    void testNotEnoughFK() {
        transaction.begin();
        // 학과 데이터 생성
        Major kor = Major.builder().name("국문학과").build();

        // 학과 데이터 영속화(저장)
        manager.persist(kor);

        // 학생 데이터 생성,
        Student park = Student.builder().name("소희").build();

        // 연관관계의 주인이 아닌 쪽(외래키를 가지지 않은 쪽, Major)에
        // 연관관계(외래키)를 설정하였을 경우
        kor.getStudents().add(park);

        manager.persist(park);

        transaction.commit();

        /*
            실행 결과
            +----+----------+------+
            | id | major_id | name |
            +----+----------+------+
            |  1 |        1 | 종혁 |
            |  2 |     NULL | 소희 |
            +----+----------+------+

            +----+----------+
            | id | name     |
            +----+----------+
            |  1 | 컴공     |
            |  3 | 국문학과  |
            +----+----------+
            -> 소희 학생이 국문학과인 3번으로 매핑되지 않았음
            외래키를 관리하는 연관관계의 주인인 Student.major에 별도의 값을 입력하지 않았기 때문에
            major_id의 값도 null로 설정되었음
         */
    }

    @Test
    @DisplayName("양방향 연관관계에서 특정 학과에 속한 학생들이 누가 있는지 조회")
    void testFindStudentsByMajor() {
        transaction.begin();
        // 컴퓨터 공학과에 2명의 학생 데이터를 추가
        Student jung = Student.builder().name("용준").build();

        Major cs = manager.find(Major.class, 1); // 컴공 조회
        jung.setMajor(cs);

        manager.persist(jung);
        transaction.commit();

        // 컴퓨터 공학과 엔티티를 조회하여, 해당 학과에 속한 학생들을 조회
        Major computerScience = manager.find(Major.class, 1);

        // 2명이 조회되는지 확인
        List<Student> csStudents = computerScience.getStudents();
        System.out.println("csStudents = " + csStudents);
        /*
            실행 결과
            2명의 학생이 조회됨
            csStudents = [dev.syntax.model.Student@14ed7ddf, dev.syntax.model.Student@4ff66917]
         */
    }

    @Test
    @DisplayName("양방향 연관관계 중 객체지향 프로그래밍 과정에서 연관관계 설정이 미흡한 경우?")
    void test_Not_Enough_FK_Mapping_In_OOP_Programming() {
        /*
            연관관계의 주인쪽에'만' 값을 설정하는 양방향 연관관계의 외래키 설정은
            객체지향 프로그래밍(패러다임)으로 올 경우 문제가 생길 수 있음
         */

        // JPA 없이 객체지향 코드로만 작성한 예시 케이스
        // 전공 생성
        Major cs = Major.builder().name("컴퓨터 공학").build();

        // 학생 2명 생성
        Student yoo = Student.builder().name("YOO").build();
        Student kang = Student.builder().name("KANG").build();

        yoo.setMajor(cs); // 학생에게 학과 연관관계 설정(student -> major)
        kang.setMajor(cs);

        System.out.println("yoo의 학과 = " + yoo.getMajor()); // CS

        // 컴공과에 속한 학생 목록 조회
        List<Student> students = cs.getStudents();
        System.out.println("students = " + students); // []
        // -> JPA를 사용하지 않는 객체 패러다임 맥락에서
        // 외래키의 반대 방향은 연관관계를 설정하지 않았기 때문에 학생 목록이 없는 빈 배열로 출력됨
    }

    @Test
    @DisplayName("양방향 연관관계 중 객체 패러다임에서 연관관계 설정을 양쪽 모두 적용한 경우")
    void test_Enough_FK_Mapping_In_OOP_Programming() {
        // JPA 없이 객체지향 코드로만 작성한 예시 케이스
        // 전공 생성
        Major cs = Major.builder().name("컴퓨터 공학").build();

        // 학생 2명 생성
        Student yoo = Student.builder().name("YOO").build();
        Student kang = Student.builder().name("KANG").build();

        yoo.setMajor(cs); // 학생에게 학과 연관관계 설정(student -> major)
        kang.setMajor(cs);

        // ★ 반대편인 학과에서도 학생에게 연관관계 설정 ★
        cs.getStudents().add(yoo);
        cs.getStudents().add(kang);

        // 컴공과에 속한 학생 목록 조회
        List<Student> students = cs.getStudents();
        System.out.println("students = " + students);
        /*
            실행 결과.
            students = [dev.jpa.model.Student@9687f55, dev.jpa.model.Student@5700c9db]
            학생 목록이 정상적으로 조회됨,
            객체 패러다임에서는 연관관계의 주인뿐만 아니라
            반대 방향에도 적용해야 양방향에서 정상적으로 참조할 수 있음
            정리하면, 객체 패러다임까지 고려하기 위해서는 양쪽 다 관계를 맺어주어야함
         */
    }

    @Test
    @DisplayName("양방향 연관관계 중 JPA 관계형 패러다임과 객체 패러다임에서 연관관계 설정을 양쪽 모두 적용한 경우")
    void test_Enough_FK_Mapping_both() {
        transaction.begin();
        // 학과 데이터 생성 및 저장
        Major cs = Major.builder().name("컴퓨터 공학").build();
        manager.persist(cs);

        // 학생 1 생성 및 저장
        Student yoo = Student.builder().name("YOO").build();
        yoo.setMajor(cs); // 학생에게 학과 등록, 연관관계 설정 student -> major

            /*
                학과에 학생 등록, 연관관계 설정 major -> student,
                연관관계의 주인이 아니며, 저장 시 사용되진 않음,
                객체 패러다임에서 사용하기 위해 저장
             */
        cs.getStudents().add(yoo);
        manager.persist(yoo);
        transaction.commit();
    }

    @Test
    @DisplayName("연관관계 매핑용 편의 메서드를 활용하여 리팩토링")
    void testRefactor() {
        // TODO: 직접 작성 후 확인해보기
        transaction.begin();

        // 학과 데이터 생성 및 저장
        Major cs = Major.builder().name("컴퓨터 공학").build();
        manager.persist(cs);

        // 학생 1 생성 및 저장
        Student yoo = Student.builder().name("YOO").build();
        yoo.setMajor(cs); // setMajor()만 호출하면 반대편 연관관계 매핑 코드도 함께 적용되도록
        // cs.getStudents().add(yoo); // setMajor() 내부에 포함되도록 작성
        manager.persist(yoo);

        transaction.commit();
    }

    @Test
    @DisplayName("(문제) 어떤 학생의 학과 변경 시 발생할 수 있는 에러 케이스")
    void testIssue() {
        Major cs = Major.builder().name("컴퓨터 공학").build();
        Major ko = Major.builder().name("국어 국문").build();

        Student yoo = Student.builder().name("YOO").build();

        yoo.setMajor(cs); // 학생을 컴공과로 지정

        // 학과 변경 이슈로 인해 학과를 국문학과로 변경
        yoo.setMajor(ko);

        // 컴공과 학생 조회?
        List<Student> csStudents = cs.getStudents();
        System.out.println("컴공과 첫 번째 학생 이름: " + csStudents.get(0).getName());
        // -> YOO가 출력됨
    }

    @Test
    @DisplayName("(해결) 어떤 학생의 학과 변경 시 발생할 수 있는 에러 케이스")
    void testIssueSolved() {
        Major cs = Major.builder().name("컴퓨터 공학").build();
        Major ko = Major.builder().name("국어 국문").build();

        Student yoo = Student.builder().name("YOO").build();

        yoo.setMajor(cs); // 학생을 컴공과로 지정

        // 학과 변경 이슈로 인해 학과를 국문학과로 변경
        yoo.setMajor(ko);

        // 컴공과 학생 조회?
        List<Student> csStudents = cs.getStudents();
        System.out.println("컴공과 첫 번째 학생 이름: " + csStudents.get(0).getName());
        // 컴공과에 학생이 없기 때문에 예외 발생: Index 0 out of bounds for length 0

    }

    @Test
    @DisplayName("(문제) 순환참조")
    void circularReference() {
        // 컴공과에 속한 모든 학생 목록 조회
        Major cs = manager.find(Major.class, 1);
        System.out.println("cs.getStudents() = " + cs.getStudents());

        // 컴공과에 속한 학생 목록이 잘 출력되게
    }

    @Test
    @DisplayName("(문제) 순환참조")
    void circularReferenceSolved() {
        // 컴공과에 속한 모든 학생 목록 조회
        Major cs = manager.find(Major.class, 1);

        // Entity 타입인 Major를 DTO 차입인 MajorData로 변환
        MajorData csData = MajorData.from(cs); // from(엔티티 객체)
        // -> "Major 엔티티로부터 MajorData를 생성(변환)"

        System.out.println("csData = " + csData);
        // 컴공과에 속한 학생 목록이 잘 출력되게
    }


}