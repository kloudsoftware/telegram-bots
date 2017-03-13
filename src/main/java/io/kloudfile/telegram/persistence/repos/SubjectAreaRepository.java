package io.kloudfile.telegram.persistence.repos;

import io.kloudfile.telegram.persistence.entities.SubjectArea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubjectAreaRepository extends JpaRepository<SubjectArea, Integer> {
    Optional<SubjectArea> findByHostkey(String hostkey);
}
