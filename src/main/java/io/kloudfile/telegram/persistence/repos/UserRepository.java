package io.kloudfile.telegram.persistence.repos;

import io.kloudfile.telegram.persistence.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByChatId(int id);

}
