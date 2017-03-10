package io.kloudfile.telegram.persistence.repos;

import io.kloudfile.telegram.persistence.entities.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeywordRepository extends JpaRepository<Keyword, Integer> {
}
