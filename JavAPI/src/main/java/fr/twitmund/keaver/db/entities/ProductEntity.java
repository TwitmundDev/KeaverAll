package fr.twitmund.keaver.db.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@Document()
public class ProductEntity {
    @Id
    public String id;

    private Double price;
    @Indexed(unique = true)
    private String name;
    private String description;
    private String image;
    private Integer stock;
    private Double discount;

    public ProductEntity(Double price, String name, String description, String image, Integer stock, Double discount) {
        this.price = price;
        this.name = name;
        this.description = description;
        this.image = image;
        this.stock = stock;
        this.discount = discount;
    }
}
