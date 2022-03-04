package com.example.springjpa;

import lombok.Data;
import javax.persistence.*;

@Entity
@Data
@Table(name = "CONTRIBS")
public class Contribs {

    private @Id @GeneratedValue Long id ;
    private String contribution ;

    @ManyToOne(optional = true)
    @JoinColumn(name = "person_id", nullable = false, referencedColumnName = "id")
    private Person person;

}
