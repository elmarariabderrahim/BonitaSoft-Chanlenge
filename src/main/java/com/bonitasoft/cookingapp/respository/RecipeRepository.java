package com.bonitasoft.cookingapp.respository;

import com.bonitasoft.cookingapp.entity.Recipe;
import com.bonitasoft.cookingapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    List<Recipe> getByAuthor(User author);
    @Query("select r from Recipe r join r.keywords rk where lower(rk) like lower(?1)")
    Set<Recipe> getRecipesByKey(String key);

    @Query("select r from Recipe r join r.ingredients ri where lower(ri) like lower(concat('%', ?1,'%'))")
    Collection<? extends Recipe> getRecipesByIngredients(String key);
}
