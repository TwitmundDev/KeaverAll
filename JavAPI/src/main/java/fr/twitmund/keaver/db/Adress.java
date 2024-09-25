package fr.twitmund.keaver.db;

import lombok.Data;

@Data
public class Adress {
    private String country, postCode, city, street,streetNumber;

    public Adress(String country, String postCode, String city, String street, String streetNumber) {
        this.country = country;
        this.postCode = postCode;
        this.city = city;
        this.street = street;
        this.streetNumber = streetNumber;
    }
}
