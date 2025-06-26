package dev.qdo.htmx_demo.dto;

import java.util.concurrent.atomic.AtomicInteger;

public record Book(Integer id, String title, String author) {

  private static final AtomicInteger nextId = new AtomicInteger(0);

  public Book(String title, String author) {
    this(nextId.incrementAndGet(), title, author);
  }

  public static Book toBook(BookForm form) {
    return new Book(form.title(), form.author());
  }
}
