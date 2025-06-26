package dev.qdo.htmx_demo.controller;

import dev.qdo.htmx_demo.dto.Book;
import dev.qdo.htmx_demo.dto.BookForm;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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

  private final Map<Integer, Book> books = new ConcurrentHashMap<>();

  BookController() {
    addBook("Game of Thrones", "George R. R. Martin");
    addBook("Sword of Destiny", "Andrzej Sapkowski");
  }

  private void addBook(String title, String author) {
    Book book = new Book(title, author);
    books.put(book.id(), book);
  }

  @GetMapping("/")
  public String index() {
    return "index";
  }

  @GetMapping("/books")
  public String getBooks(Model model) {
    model.addAttribute("books", books.values());
    return "book :: book-all";
  }

  @GetMapping("/books/edit-modal/{bookId}")
  public String getBookModal(@PathVariable Integer bookId, Model model) {
    var book = books.get(bookId);
    if (book != null) {
      model.addAttribute("book", book);
    }
    return "modal :: edit-modal";
  }

  @PostMapping("/books")
  public String createBooks(@ModelAttribute BookForm bookForm, Model model) {
    Book book = new Book(bookForm.title(), bookForm.author());
    books.put(book.id(), book);
    model.addAttribute("books", Collections.singletonList(book));
    return "book :: book-row";
  }

  @PostMapping("/books/search")
  public String searchBooks(@RequestParam("search") String search, Model model) {
    String query = search.toLowerCase().trim();

    if (query.isEmpty()) {
      model.addAttribute("books", books.values());
    } else {
      List<Book> filteredBooks = books.values().stream()
          .filter(b -> b.title().toLowerCase().contains(query)
              || b.author().toLowerCase().contains(query))
          .toList();
      model.addAttribute("books", filteredBooks);
    }

    return "book :: book-all";
  }

  @PutMapping(path = "books/{bookId}")
  public String editBooks(@ModelAttribute BookForm bookForm, @PathVariable Integer bookId,
      Model model) {
    Book updatedBook = new Book(bookId, bookForm.title(), bookForm.author());
    books.put(bookId, updatedBook);
    model.addAttribute("books", Collections.singletonList(updatedBook));
    return "book :: book-row";
  }

  @ResponseBody
  @DeleteMapping(produces = MediaType.TEXT_HTML_VALUE, path = "books/{bookId}")
  public String deleteBooks(@PathVariable Integer bookId) {
    books.remove(bookId);
    return "";
  }
}
