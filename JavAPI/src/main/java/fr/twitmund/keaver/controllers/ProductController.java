package fr.twitmund.keaver.controllers;

import fr.twitmund.keaver.config.JwtTokenUtil;
import fr.twitmund.keaver.db.Role;
import fr.twitmund.keaver.db.entities.ProductEntity;
import fr.twitmund.keaver.db.entities.Student;
import fr.twitmund.keaver.db.entities.UserEntity;
import fr.twitmund.keaver.db.repositories.UserRepository;
import fr.twitmund.keaver.db.service.ProductService;
import fr.twitmund.keaver.db.service.StudentService;
import fr.twitmund.keaver.utils.DbError;
import lombok.AllArgsConstructor;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private final UserRepository userRepository;
    @RequestMapping(value = "api/v1/products/fetch", method = RequestMethod.GET, produces = "application/json")
    public List<ProductEntity> studentFetchAll() {
        return productService.productFetch();
    }
    @RequestMapping(value = "api/v1/products/create", method = RequestMethod.POST, produces = "application/json")
    public JsonObject createProduct(@RequestParam(value = "price") Double price,
                                    @RequestParam(value = "token") String token,
                                    @RequestParam(value = "name") String name,
                                    @RequestParam(value = "description") String description,
                                    @RequestParam(value = "imageUrl") String imageUrl,
                                    @RequestParam(value = "discount") String discount,
                                    @RequestParam(value = "stock") String stock) {
        JsonObject tokenJsonObject = jwtTokenUtil.retrieveUserFromToken(token);
        Integer stockInt = Integer.parseInt(stock);
        Double discountDouble = Double.parseDouble(discount.substring(0, discount.length() - 1));
        JsonObject jsonObject = new JsonObject();
        ProductEntity entity = new ProductEntity(price, name, description, imageUrl, stockInt, discountDouble);
        if (tokenJsonObject == null) {
            jsonObject.addProperty("status", "error");
            jsonObject.addProperty("message", "Invalid token");
            return jsonObject;
        }
        if (tokenJsonObject.get("status").getAsString().equals("error")) {
            jsonObject.addProperty("status", "error");
            jsonObject.addProperty("message", tokenJsonObject.get("message").getAsString());
            return jsonObject;
        }
        try {
            JsonObject tokenPayloadJsonObject = tokenJsonObject.get("payload").getAsJsonObject();
            //System.out.println(tokenPayloadJsonObject);
            String issuerUsername = tokenPayloadJsonObject.get("username").getAsString();
            UserEntity issuer = userRepository.findByUsername(issuerUsername).get();
            //System.out.println();
            if (issuer.getRole().equals(Role.ADMIN)) {
                if (productService.createProduct(entity) == DbError.Success) {
                    jsonObject.addProperty("status", "success");
                    jsonObject.addProperty("message", "Product created successfully");
                    return jsonObject;
                }
                if (productService.createProduct(entity) == DbError.DuplicateKey) {
                    jsonObject.addProperty("status", "error");
                    jsonObject.addProperty("message", "Product already exists");
                    return jsonObject;
                }
            }
            jsonObject.addProperty("status", "error");
            jsonObject.addProperty("message", "Something went wrong");
            return jsonObject;
        } catch (Exception e) {
            jsonObject.addProperty("status", "error");
            jsonObject.addProperty("message", "Something went wrong");
            return jsonObject;
        }
    }
    @RequestMapping(value = "api/v1/products/get/{id}", method = RequestMethod.GET, produces = "application/json")
    public ProductEntity createProduct(@PathVariable(value = "id") String id) {
        return productService.productFetchById(id);
    }
    @RequestMapping(value = "api/v1/products/delete", method = RequestMethod.POST, produces = "application/json")
    public JsonObject createProduct(@RequestParam(value = "token") String token,
                                    @RequestParam(value = "id") String id) {
        JsonObject tokenJsonObject = jwtTokenUtil.retrieveUserFromToken(token);
        JsonObject jsonObject = new JsonObject();
        if (tokenJsonObject == null) {
            jsonObject.addProperty("status", "error");
            jsonObject.addProperty("message", "Invalid token");
            return jsonObject;
        }
        if (tokenJsonObject.get("status").getAsString().equals("error")) {
            jsonObject.addProperty("status", "error");
            jsonObject.addProperty("message", tokenJsonObject.get("message").getAsString());
            return jsonObject;
        }
        try {
            JsonObject tokenPayloadJsonObject = tokenJsonObject.get("payload").getAsJsonObject();
            //System.out.println(tokenPayloadJsonObject);
            String issuerUsername = tokenPayloadJsonObject.get("username").getAsString();
            UserEntity issuer = userRepository.findByUsername(issuerUsername).get();

            //System.out.println();
            if (issuer.getRole().equals(Role.ADMIN)) {
                if (productService.deleteProduct(id) == DbError.Success) {
                    jsonObject.addProperty("status", "success");
                    jsonObject.addProperty("message", "Product created successfully");
                    return jsonObject;
                }
                if (productService.deleteProduct(id) == DbError.InternalServerError) {
                    jsonObject.addProperty("status", "error");
                    jsonObject.addProperty("message", "An internal server error occurred");
                    return jsonObject;
                }
            }
            jsonObject.addProperty("status", "error");
            jsonObject.addProperty("message", "Something went wrong");
            return jsonObject;
        } catch (Exception e) {
            jsonObject.addProperty("status", "error");
            jsonObject.addProperty("message", "Something went wrong, call an administator");
            return jsonObject;
        }
    }
}
