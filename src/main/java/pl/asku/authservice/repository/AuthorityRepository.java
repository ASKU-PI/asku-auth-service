package pl.asku.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.asku.authservice.model.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
