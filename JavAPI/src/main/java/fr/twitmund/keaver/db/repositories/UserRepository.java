package fr.twitmund.keaver.db.repositories;

import fr.twitmund.keaver.db.entities.Student;
import fr.twitmund.keaver.db.entities.UserEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<UserEntity, String> {

    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findOneByEmailAndPassword(String email, String password);



}
