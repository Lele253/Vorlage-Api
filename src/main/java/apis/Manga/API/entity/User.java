package apis.Manga.API.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long nutzerId;
    private String status;
    @JsonIgnore
    private String password;
    @Column(unique = true)
    private String username;
    private String email;

    public String getStatus() {
        return status;
    }

    public String getUsername() {
        return username;
    }
}
