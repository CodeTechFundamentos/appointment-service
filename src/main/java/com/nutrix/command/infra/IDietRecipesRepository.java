package com.nutrix.command.infra;

import com.nutrix.command.domain.DietRecipes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IDietRecipesRepository extends JpaRepository<DietRecipes, Integer> {
    @Query("Select b.recipeId from DietRecipes b where b.diet.id = :diet_id")
    public List<Integer> findByDiet(@Param("diet_id") Integer diet_id);

    @Query("Select b.id from DietRecipes b where b.diet.id = :diet_id and b.recipeId = :recipe_id")
    public Integer findByDietRecipe(@Param("recipe_id") Integer recipe_id, @Param("diet_id") Integer diet_id);
}
