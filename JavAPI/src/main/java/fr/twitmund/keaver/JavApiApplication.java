package fr.twitmund.keaver;

import fr.twitmund.keaver.db.Adress;
import fr.twitmund.keaver.db.Gender;
import fr.twitmund.keaver.db.entities.ProductEntity;
import fr.twitmund.keaver.db.entities.Student;
import fr.twitmund.keaver.db.entities.UserEntity;
import fr.twitmund.keaver.db.repositories.ProductReposotiry;
import fr.twitmund.keaver.db.repositories.StudentRepository;
import fr.twitmund.keaver.db.service.ProductService;
import fr.twitmund.keaver.db.service.StudentService;

import fr.twitmund.keaver.utils.DbError;
import lombok.Getter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPublicKey;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;


@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class JavApiApplication {
    @Getter
    public static String privateRsaKey = "MIICWwIBAAKBgQCl5xjsPEPXuDunBFgS2Zmbb9UCjnSMaICWRLIHXuS5bkL7oaxthUKrDT+HFbx7pPOpDqhkBH3YGj9ZGJC7FzIyFNaW9rMLHZ/HziHiK0oo8rYdaRzbsm8OOni0xleV/PysGvkspUWgZSzhshP23bZ7Osx9DrW1NnGpOS5ZPLUzAwIDAQABAoGAZYkwubSQmQBKkgeYl8DRwE4LgksJjb2PpWhZxSUQu2R+Xl8ulsEOcn3jOZvWBZ6I+hjceoo9MvutdawROFAUCZJufawnOW3/cimyfzyKyNRqr+KRI847VC8Kp2ZWSJJG7lVWh6yUMefdaiIrB1mF1dgJKrmZwi9k83QYzITcW2ECQQDPxat7B5dVpYnY /H+JTfHAOM3bD4YCDWncrvHP3AVcZl8WjZVNrZFL11IlpF1X/waDaPB9PdheOJ4UJOVo50hJAkEAzGlxmYirq7xoaAag8cC/evi+0YiXFmqwkTZp3ssFSDgKfCMA5pmWcdz1P8m/m/iWIrw1jswZfv0rtsjvSqcY6wJAAz1z5SFDLOS995JIct2yTWsVWrstPc7zvatEvhcSRlSaWwGuOF3ijCo5b7jWc87YTShPx8Q1IZMEdA/3O+GPcQJAURUSZqB72CdoErtuY/bc/V5l8p+HrvCrES7QV/uYIMl5/zuzQ8Vwk8Hd1mukga3df/HAHOCAzIqIKH0Tzjzx+QJAY8M19MLhfiOCAQCCWf1TgNHR+cs41YMrpO+NvxIGWXUdKVpjuYGfXUtnxc3pnr97Q4nC8pkyEq0VpqdY1cdLWw==";
    @Getter
    public static String publicRsaKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCl5xjsPEPXuDunBFgS2Zmbb9UCjnSMaICWRLIHXuS5bkL7oaxthUKrDT+HFbx7pPOpDqhkBH3YGj9ZGJC7FzIyFNaW9rMLHZ/HziHiK0oo8rYdaRzbsm8OOni0xleV/PysGvkspUWgZSzhshP23bZ7Osx9DrW1NnGpOS5ZPLUzAwIDAQAB";
    public static void main(String[] args) {
        SpringApplication.run(JavApiApplication.class, args);
    }

   @Bean
    CommandLineRunner runner(StudentRepository studentRepository,
                             ProductReposotiry productReposotiry,
                             ProductService productService){
        return args -> {
            Adress adress = new Adress(
                    "France",
                    "75000",
                    "Paris",
                    "Rue de l'Eiffel",
                    "10");
            Student student = new Student(
                    "Ellwane",
                    "Romdhame",
                    "e.romdhame@gmail.com",
                    Gender.Male,
                    adress,
                    List.of("Computer Science", "English"),
                     BigDecimal.TEN,
                    LocalDateTime.now()
            );
            ProductEntity product = new ProductEntity(
                    35.5,
                    "Keyboard",
                    "One handed keyboard",
                    "https://th.bing.com/th/id/OIP.yPIRBBWocfGgVdYOEPL6ygHaHa?pid=ImgDet&rs=1",
                    30,
                    0d
            );
            UserEntity entity = new UserEntity(
                    "Ellwane",
                    "Romdhame",
                    "Twitmund",
                    "e.romdhame@gmail.com",
                    "123456",
                    "127.0.0.1"
            );
            //Optional<Student> studentsList = studentRepository.findByEmail(student.getEmail());
            //if (studentsList.isPresent()) {
            //    throw new IllegalStateException("Student already exists");
    //
            //}else{
            //    studentRepository.insert(student);
            //}
            //List<ProductEntity> productsList = productReposotiry.findAll();
            //if (productsList.contains(product)) {
            //    throw new IllegalStateException("Product already exists");
            //}else{
            //    productReposotiry.insert(product);
            //}
            if (productService.createProduct(product) == DbError.Success) {
                System.out.println("Product created successfully");
            }
            else{
                System.out.println("Product Already exists");
            }
            //if (userService.createUser(entity) == DbError.Success) {
            //    System.out.println("User created successfully");
            //}
            //else{
            //    System.out.println("User Already exists");
            //}




        };
   }

}
