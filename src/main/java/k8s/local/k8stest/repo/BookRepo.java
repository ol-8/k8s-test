package k8s.local.k8stest.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import k8s.local.k8stest.model.Book;

@Repository
public interface BookRepo extends JpaRepository<Book, String> {
}
