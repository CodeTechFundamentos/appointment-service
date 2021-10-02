package com.nutrix.command.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="diet_recipes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DietRecipes implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "diet_id", nullable = false)
    private Diet diet;

    @Column(name = "recipe_id", nullable = false)
    private Integer recipeId;
}