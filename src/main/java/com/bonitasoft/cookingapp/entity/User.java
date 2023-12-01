package com.bonitasoft.cookingapp.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users") // Define the table name explicitly
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id") // Define the column name explicitly
    private Long id;

    @Column(name = "username", nullable = false) // Define the column name and properties
    private String username;

    @Column(name = "password", nullable = false)
    //@JsonIgnore
    private String password;



}