package ra.edu.hackathon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ra.edu.hackathon.model.entity.Book;
import ra.edu.hackathon.repository.BookRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public List<Book> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) return findAll();
        return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword);
    }

    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    public Optional<Book> findByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    public void save(Book book) {
        bookRepository.save(book);
    }

    public void delete(Long id) {
        bookRepository.deleteById(id);
    }

    public List<Book> sort(List<Book> list, String sortBy, String direction) {
        Comparator<Book> comparator = Comparator.comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER);
        if ("publicationYear".equals(sortBy)) comparator = Comparator.comparing(Book::getPublicationYear);
        if ("price".equals(sortBy)) comparator = Comparator.comparing(Book::getPrice);
        if ("desc".equalsIgnoreCase(direction)) comparator = comparator.reversed();
        return list.stream().sorted(comparator).collect(Collectors.toList());
    }
}
