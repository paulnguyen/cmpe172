package com.example.springjpa;

import lombok.Data;
import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "AWARDS")
public class Awards {

    private @Id @GeneratedValue Long id ;
    private String awardName ;
    private String awardedBy ;

    @ManyToMany(mappedBy = "awards")
    private List<Person> person ;
}
