package fr.twitmund.keaver.db.repositories;

import fr.twitmund.keaver.db.entities.ProductEntity;
import fr.twitmund.keaver.db.entities.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductReposotiry extends MongoRepository<ProductEntity, String> {

}
