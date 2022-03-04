package com.example.springjpa;

import lombok.Data;
import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "PERSON")
public class Person {

    private @Id @GeneratedValue Long id ;
    private String firstName ;
    private String lastName ;
    private java.sql.Date birthDate ;
    private java.sql.Date deathDate ;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<Contribs> contribs;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "person_awards",
            joinColumns = @JoinColumn(name = "person_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "award_id", referencedColumnName = "id"))
    private List<Awards> awards;

}

