# Description

A lightweight CRUD application built with Spring Boot, Thymeleaf, HTMX, and Bootstrap for a dynamic
and responsive user experience.

# Note

In Thymeleaf, using both `th:each` and `th:insert` (or `th:replace`) on the same tag can lead to
conflicts because each of these attributes modifies the structure of the HTML in different ways:

- `th:each` is used to iterate over a collection.

- `th:insert` (or `th:replace`) is used to include a fragment.

Using them together on the same tag usually causes one to override or interfere with the other.

### Don't Do This:

`<div th:each="book : ${books}" th:insert="book-fragment :: book-row(${book})"></div>`

This doesn’t work as expected because `th:insert` will replace the entire tag, so `th:each` is never
evaluated in the way you want.