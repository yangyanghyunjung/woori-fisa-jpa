package dev.syntax;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity // JPA가 해당 클래스를 DBMS의 테이블 형태로 인식, 관리하도록 명시
public class Book {
    @Id // JPA에게 해당 필드를 기본키로 인식하도록 명시
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String bookName;
    private String author;

    protected Book() {
    }
    public Book(String bookName, String author) {
        this.bookName = bookName;
        this.author = author;
    }
    public int getId() {
        return id;
    }

    public String getBookName() {
        return bookName;
    }
    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", bookName='" + bookName + '\'' +
                ", author='" + author + '\'' +
                '}';
    }

    // 본질적으로는 setter 메소드, 명시적으로 네이밍
    public void updateBookName(String bookName) {
        this.bookName = bookName;
    }
}
