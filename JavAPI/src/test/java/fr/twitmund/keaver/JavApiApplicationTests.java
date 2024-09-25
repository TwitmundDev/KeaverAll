package fr.twitmund.keaver;

import fr.twitmund.keaver.db.entities.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JavApiApplicationTests {

    @Test
    void contextLoads() {
    }


    @Test
    public void Test_0001createUser() {
        UserEntity entity = new UserEntity(
                "*******",
                "*******",
                "Twitmund",
                "********@gmail.com",
                "123456",
                "127.0.0.1"
        );

    }

}
