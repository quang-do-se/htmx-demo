package dev.qdo.htmx_demo.controller;

import dev.qdo.htmx_demo.dto.Book;
import dev.qdo.htmx_demo.dto.BookForm;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller

public class BookController {

  private final Set<Book> books = new ConcurrentSkipListSet<>(Comparator.comparing(Book::id));

  BookController() {
    books.add(new Book("Game of Thrones", "George R. R. Martin"));
    books.add(new Book("Sword of Destiny", "Andrzej Sapkowski"));
  }

  @GetMapping("/")
  public String index() {
    return "index";
  }

  @GetMapping("/books")
  public String getBooks(Model model) {
    model.addAttribute("books", books);
    return "book :: book-all";
  }

  @GetMapping("/books/edit-modal/{bookId}")
  public String getBookModal(@PathVariable Integer bookId, Model model) {
    books.stream()
        .filter(b -> b.id().equals(bookId))
        .findFirst()
        .ifPresent(book -> model.addAttribute("book", book));
    return "modal :: edit-modal";
  }

  @PostMapping("/books")
  public String createBooks(@ModelAttribute BookForm bookForm, Model model) {
    var book = Book.toBook(bookForm);
    books.add(book);
    model.addAttribute("books", Collections.singletonList(book));
    return "book :: book-row";
  }

  @PostMapping("/books/search")
  public String searchBooks(@RequestParam("search") String search, Model model) {
    final String finalSearch = search.toLowerCase().trim();
    model.addAttribute("books",
        books.stream()
            .filter(b ->
                b.title().toLowerCase().contains(finalSearch)
                    || b.author().toLowerCase().contains(finalSearch))
            .toList());
    return "book :: book-all";
  }

  @PutMapping(path = "books/{bookId}")
  public String editBooks(@ModelAttribute BookForm bookForm, @PathVariable Integer bookId,
      Model model) {
    books.stream().filter(b -> b.id().equals(bookId)).forEach(books::remove);
    var updatedBook = new Book(bookId, bookForm.title(), bookForm.author());
    books.add(updatedBook);
    model.addAttribute("books", Collections.singletonList(updatedBook));
    return "book :: book-row";
  }

  @ResponseBody
  @DeleteMapping(produces = MediaType.TEXT_HTML_VALUE, path = "books/{bookId}")
  public String deleteBooks(@PathVariable Integer bookId) {
    books.stream().filter(b -> b.id().equals(bookId)).forEach(books::remove);
    return "";
  }
}
