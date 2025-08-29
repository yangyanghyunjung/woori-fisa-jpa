package dev.syntax.dto;

import dev.syntax.model.Student;

// Student 엔티티에 대한 결과 데이터를 임시로 담아두는 DTO 클래스
public class StudentData {
    private int id;
    private String name;

    // ★ major 필드가 없기 때문에 순환참조가 발생하지 않음


    public StudentData(int id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    // 정적 팩토리 메서드
    public static StudentData from(Student student) {
        final int id = student.getId();
        final String name = student.getName();

        return new StudentData(id, name);
    }

    @Override
    public String toString() {
        return "StudentData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
