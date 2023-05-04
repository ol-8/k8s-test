package k8s.local.k8stest;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import io.restassured.RestAssured;
import k8s.local.k8stest.model.Book;
import k8s.local.k8stest.repo.BookRepo;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookTest {

  @LocalServerPort
  private int port;

  @Autowired
  private BookRepo bookRepo;

  @BeforeEach
  void cleanUp() {
    bookRepo.deleteAll();
  }

  @Test
  void createBook() {
    RestAssured.given().port(port)
            .when()
            .contentType(APPLICATION_JSON_VALUE)
            .body("""
                    {
                      "title": "Chemistry",
                      "author": "Oleh K."
                    }""")
            .post("/books")
            .then()
            .statusCode(OK.value())
            .body("id", notNullValue());

    var book = bookRepo.findAll().get(0);
    assertEquals("Chemistry", book.getTitle());
    assertEquals("Oleh K.", book.getAuthor());
  }

  @Test
  void getBookById() {
    var book = new Book();
    book.setAuthor("Oleh K.");
    book.setTitle("Math");
    var id = bookRepo.save(book).getId();

    RestAssured.given().port(port)
            .when()
            .get("/books/%s".formatted(id))
            .then()
            .statusCode(OK.value())
            .body(equalTo("""
                    {"id":"%s","title":"Math","author":"Oleh K."}""".formatted(id)));
  }

}
