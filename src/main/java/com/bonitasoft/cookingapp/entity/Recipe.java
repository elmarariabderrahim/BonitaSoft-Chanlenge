package com.bonitasoft.cookingapp.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "recipes") // Define the table name explicitly
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_id") // Define the column name explicitly
    private Long id;

    @ElementCollection
    @CollectionTable(name = "recipe_keywords", joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "keywords")
    private List<String> keywords;

    @ElementCollection
    @CollectionTable(name = "recipe_ingredients", joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "ingredients")
    private List<String> ingredients;

    @ElementCollection
    @CollectionTable(name = "recipe_steps", joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "steps")
    private List<String> steps;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id") // Define the column name for the author ID
    private User author;

}
