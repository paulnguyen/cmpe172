package com.example.springjpa;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public class PersonAwardsPK implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "person_id")
    private Person person;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "award_id")
    private Awards awards;

}
