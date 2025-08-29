package dev.syntax.dto;

import dev.syntax.model.Major;

import java.util.List;
import java.util.stream.Collectors;

// DB를 통해 조회한 Major 엔티티의 결과 데이터를 임시로 담아두는 역할
public class MajorData {
    private int id;
    private String name;

    // 제네릭 타입이 Student가 아니라 DTO타입인 StudentData
    private List<StudentData> students;

    public MajorData(int id, String name, List<StudentData> students) {
        this.id = id;
        this.name = name;
        this.students = students;
    }

    // 정적 팩토리 메서드
    public static MajorData from(Major major) {
        // 파라미터의 인수로 전달받은 Major 엔티티(major)에 담긴 값들을 하나씩 추출,
        // MajorData 필드에 초기화

        final int id = major.getId();
        final String name = major.getName();
        // 학생 데이터 추출
        List<StudentData> students = major.getStudents()
                .stream()
                .map(student -> StudentData.from(student))
                // StudentData 객체, StudentData 객체, StudentData 객체...
                .collect(Collectors.toList());
        // [StudentData 객체, StudentData 객체, StudentData 객체...]

        return new MajorData(id, name, students);
    }

    @Override
    public String toString() {
        return "MajorData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", students=" + students +
                '}';
    }
}
