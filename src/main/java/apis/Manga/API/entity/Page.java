package apis.Manga.API.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Page {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String keywords;
    private String description;
    private String canonical;
    private String title;

    @Column(name = "page_index") // Vermeidet Konflikte, da 'index' reserviert ist
    private String index;
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private ArrayList<StringList> colors;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private ArrayList<StringList> texts;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private ArrayList<StringList> images;



}
