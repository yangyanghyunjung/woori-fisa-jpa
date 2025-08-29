package dev.syntax;

/**
 * 1. 조회를 수행할 때(Major, Student) 엔티티를 불러오는 로딩 전략
 *
 * 2. 엔티티 매핑의 경우 필요에 따라 M:N 혹은 1:1관계에 대한 매핑 방법?
 *
 * @ManyToMany
 * @OneToOne..
 *
 * 3. 발생할 수 있는 문제
 * 대표적인 문제 N+1
 *
 * 4. OSIV
 *
 * 5. 복합키 매핑, Cascade 옵션
 *
 * 가급적 테스트케이스로 작성
 *
 * 6. 동작 원리에 대해 디버깅
 *
 * step04_practice로 프로젝트 생성 후 진행
 * 오후 4시 정도에 확인
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }
}
