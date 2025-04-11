package apis.Manga.API.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class StringList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    @Column(name = "list_key")
    private String key;
    @Lob
    @Column(name = "list_value")
    private String value;
    @Lob
    private String url;
}
