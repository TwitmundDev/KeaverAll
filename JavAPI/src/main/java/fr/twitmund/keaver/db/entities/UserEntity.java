package fr.twitmund.keaver.db.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.twitmund.keaver.db.Role;
import lombok.Data;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document()
public class UserEntity {

    @Id
    public String id;


    private String firstName;

    private String lastName;

    private String username;

    @Indexed(unique = true)
    private String email;

    private String password;

    @Getter
    @Setter
    private Role role = Role.USER;

    private String lastIpConnected;

    public UserEntity(String firstName, String lastName,String username, String email, String password, String lastIpConnected) {
        this.firstName = firstName;
        this.username = username;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.lastIpConnected = lastIpConnected;
    }

    public boolean isAdmin() {
        return this.role == Role.ADMIN;
    }
}
