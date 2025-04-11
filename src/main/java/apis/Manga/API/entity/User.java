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
    @JsonIgnore
    private String password;
    @Column(unique = true)
    private String email;
}
