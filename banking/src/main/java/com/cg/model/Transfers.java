package com.cg.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "deposits")
public class Transfers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "created_at")
    private Date createdAt = new Date();


}
