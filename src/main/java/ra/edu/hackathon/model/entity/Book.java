package ra.edu.hackathon.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(min = 5, max = 150, message = "Tiêu đề phải từ 5 đến 150 ký tự")
    private String title;

    @NotBlank(message = "Tác giả không được để trống")
    @Size(min = 2, max = 100, message = "Tác giả phải từ 2 đến 100 ký tự")
    private String author;

    @NotBlank(message = "ISBN không được để trống")
    @Pattern(regexp = "\\d{13}", message = "ISBN phải gồm 13 chữ số")
    @Column(unique = true, nullable = false)
    private String isbn;

    @NotNull(message = "Năm xuất bản không được để trống")
    @Min(value = 1900, message = "Năm xuất bản phải >= 1900")
    private Integer publicationYear;

    @NotNull(message = "Giá không được để trống")
    @DecimalMin(value = "1000", message = "Giá phải >= 1.000")
    @DecimalMax(value = "1000000", message = "Giá phải <= 1.000.000")
    private Double price;

    public Book() {
    }

    public Book(Long id, String title, String author, String isbn, Integer publicationYear, Double price) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publicationYear = publicationYear;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
