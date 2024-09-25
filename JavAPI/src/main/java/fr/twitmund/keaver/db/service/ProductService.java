package fr.twitmund.keaver.db.service;

import fr.twitmund.keaver.db.entities.ProductEntity;
import fr.twitmund.keaver.db.entities.Student;
import fr.twitmund.keaver.db.repositories.ProductReposotiry;
import fr.twitmund.keaver.db.repositories.StudentRepository;
import fr.twitmund.keaver.utils.DbError;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Service
public class ProductService {
    private final ProductReposotiry productReposotiry;
    private final MongoTemplate mongoTemplate;

    public List<ProductEntity> productFetch() {
        return productReposotiry.findAll();
    }

    public ProductEntity productFetchById(String id) {
        ProductEntity product = productReposotiry.findById(id).get();
        return productReposotiry.findById(id).get();
    }


    public ProductEntity productUpdate(ProductEntity productEntity) {
        ProductEntity entity = productReposotiry.findById(productEntity.getId()).get();

        return productReposotiry.save(productEntity);
    }

    public void productDelete(String id) {
        productReposotiry.deleteById(id);
    }

    //public DbError createProduct(ProductEntity productEntity) {
    //    System.out.println("A");
    //    DbError dbError = null;
    //    List<ProductEntity> entities = productReposotiry.findAll();
    //    if (entities.isEmpty()) {
    //        System.out.println("E");
    //        productReposotiry.insert(productEntity);
    //        dbError = DbError.Success;
    //    } else {
    //        for (ProductEntity entity : entities) {
    //            System.out.println("B");
    //            if (Objects.equals(entity.getName(), productEntity.getName()) || Objects.equals(entity.getId(), productEntity.getId())) {
    //                System.out.println("C");
    //                dbError = DbError.DuplicateKey;
    //            } else {
    //                System.out.println("D");
    //                productReposotiry.insert(productEntity);
    //                dbError = DbError.Success;
    //            }
    //        }
    //    }
    //    return dbError;
    //
    //}
    public DbError createProduct(ProductEntity productEntity) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(productEntity.getName()));
        if (mongoTemplate.find(query, ProductEntity.class).size() > 0){
            return DbError.DuplicateKey;
        }else {
         productReposotiry.insert(productEntity);
         return DbError.Success;
        }
    }
    public DbError deleteProduct(String id) {
        try{
            productReposotiry.deleteById(id);
            return DbError.Success;
        } catch (Exception e) {
            return DbError.InternalServerError;
        }

    }


}
