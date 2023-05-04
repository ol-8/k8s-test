package k8s.local.k8stest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import k8s.local.k8stest.model.Book;
import k8s.local.k8stest.repo.BookRepo;

@RestController
@RequestMapping(value = "/books")
@RequiredArgsConstructor
public class BookController {

  private final BookRepo bookRepo;

  @PostMapping
  public BookIdentifier saveBook(@RequestBody Book book) {
    return new BookIdentifier(bookRepo.save(book).getId());
  }

  public record BookIdentifier(String id) {
  }

  @GetMapping("/{id}")
  public Book getBook(@PathVariable String id) {
    return bookRepo.findById(id).orElse(null);
  }

}
