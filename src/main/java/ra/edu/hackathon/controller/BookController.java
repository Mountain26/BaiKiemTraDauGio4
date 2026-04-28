package ra.edu.hackathon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ra.edu.hackathon.model.entity.Book;
import ra.edu.hackathon.service.BookService;

import jakarta.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    private static final int PAGE_SIZE = 5;

    @GetMapping
    public String list(Model model,
                       @RequestParam(value = "keyword", required = false) String keyword,
                       @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                       @RequestParam(value = "sortBy", required = false, defaultValue = "title") String sortBy,
                       @RequestParam(value = "dir", required = false, defaultValue = "asc") String dir) {

        List<Book> results;
        if (keyword != null && !keyword.trim().isEmpty()) {
            results = bookService.search(keyword);
            model.addAttribute("keyword", keyword);
        } else {
            results = bookService.findAll();
        }

        results = bookService.sort(results, sortBy, dir);

        int total = results.size();
        int totalPages = Math.max(1, (int) Math.ceil((double) total / PAGE_SIZE));
        if (page < 1) page = 1;
        if (page > totalPages) page = totalPages;

        int fromIndex = (page - 1) * PAGE_SIZE;
        int toIndex = Math.min(fromIndex + PAGE_SIZE, total);
        List<Book> pageList = results.subList(fromIndex, toIndex);

        model.addAttribute("books", pageList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("dir", dir);

        return "book/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("bookReq", new Book());
        return "book/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Optional<Book> opt = bookService.findById(id);
        if (opt.isPresent()) {
            model.addAttribute("bookReq", opt.get());
            return "book/form";
        }
        return "redirect:/book";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("bookReq") Book bookReq, BindingResult result, Model model,
                       @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                       @RequestParam(value = "keyword", required = false) String keyword,
                       @RequestParam(value = "sortBy", required = false, defaultValue = "title") String sortBy,
                       @RequestParam(value = "dir", required = false, defaultValue = "asc") String dir) {
        boolean isNew = (bookReq.getId() == null);

        // check unique ISBN using repository
        Optional<Book> existing = bookService.findByIsbn(bookReq.getIsbn());
        boolean isbnUsed = existing.isPresent() && !Objects.equals(existing.get().getId(), bookReq.getId());
        if (isbnUsed) {
            result.rejectValue("isbn", "isbn.duplicate", "ISBN đã tồn tại");
        }

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        if (bookReq.getPublicationYear() != null && bookReq.getPublicationYear() > currentYear) {
            result.rejectValue("publicationYear", "year.invalid", "Năm xuất bản không được lớn hơn năm hiện tại");
        }

        if (result.hasErrors()) {
            return "book/form";
        }

        bookService.save(bookReq);

        // After save, redirect to first page (when created) or keep same page when updated
        if (isNew) {
            return "redirect:/book";
        } else {
            return String.format("redirect:/book?page=%d&sortBy=%s&dir=%s", page, sortBy, dir);
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id,
                         @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                         @RequestParam(value = "keyword", required = false) String keyword,
                         @RequestParam(value = "sortBy", required = false, defaultValue = "title") String sortBy,
                         @RequestParam(value = "dir", required = false, defaultValue = "asc") String dir) {
        bookService.delete(id);
        return String.format("redirect:/book?page=%d&keyword=%s&sortBy=%s&dir=%s", page, keyword == null ? "" : keyword, sortBy, dir);
    }
}
