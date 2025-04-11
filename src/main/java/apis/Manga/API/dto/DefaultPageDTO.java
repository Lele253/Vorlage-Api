package apis.Manga.API.dto;

import apis.Manga.API.entity.StringList;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
public class DefaultPageDTO {
    private Long id;
    private String email;
    private String phonenumber;
    private String adress;
    @Lob
    private String logo;
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<StringList> orgNames;
    private String openingHours;
    private ArrayList<String> titles;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private ArrayList<StringList> texts;
}
