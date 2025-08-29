package dev.syntax;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
//        int id = new Student().getId();
//        System.out.println("id = " + id);

        Student student = Student.builder()
                .id(1)
                .name("쿠로미")
                .build();
        System.out.println("student = " + student);

        // 쿠로미에게 학과를 부여

            // 학과 객체 생성
            Major major = Major.builder().name("컴공").build();

            // 객체지향 방식에서 "학생은 하나의 학과에 속해있다"
            student.setMajor(major);

        // 쿠로미의 학과 조회
        Major major1 = student.getMajor();
        System.out.println("major = " + major1);
        // 객체지향 프로그래밍 방식에서는
        // 객체들 간의 연관 관계를 참조 연사자(dot,.기호)응 통해 탐색할 수 있음
    }
}
