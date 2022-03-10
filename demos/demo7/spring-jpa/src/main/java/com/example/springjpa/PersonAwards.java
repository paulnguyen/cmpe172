package com.example.springjpa;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "PERSON_AWARDS")
public class PersonAwards {

    @EmbeddedId
    private PersonAwardsPK id ;

    private java.sql.Date awardedYear ;
}
