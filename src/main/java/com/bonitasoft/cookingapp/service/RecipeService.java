package com.bonitasoft.cookingapp.service;

import com.bonitasoft.cookingapp.entity.Recipe;
import com.bonitasoft.cookingapp.entity.User;
import com.bonitasoft.cookingapp.respository.RecipeRepository;
import com.bonitasoft.cookingapp.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

    public Recipe addRecipe(Recipe recipe) {
        recipeRepository.save(recipe);
        return recipe;
    }

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public List<Recipe> getRecipesByAuthor(User author) {
        return recipeRepository.getByAuthor(author);
    }

    public Set<Recipe> getRecipesByKeywords(List<String> keywords) {
        List<Recipe> recipes = new ArrayList<>();
        for (String key : keywords){
            recipes.addAll(recipeRepository.getRecipesByKey(key)); // get all recipes containing a keyword
        }
        return sortRecipes(recipes);
    }

    public Set<Recipe> getRecipesByIngredients(List<String> ingredients) {
        List<Recipe> recipes = new ArrayList<>();
        for (String key : ingredients){
            recipes.addAll(recipeRepository.getRecipesByIngredients(key)); // get all recipes containing a keyword
        }
        return sortRecipes(recipes);
    }

    private Set<Recipe> sortRecipes(List<Recipe> recipes) {
        // sorting the list of recipes starting with the ones with most keywords
        recipes.sort(Comparator.comparingInt(i-> Collections.frequency(recipes, i)).reversed());
        Set<Recipe> recipesResults = new LinkedHashSet<>(recipes);

        return recipesResults;
    }

    public Recipe updateRecipe(Long recipeId, Recipe updatedRecipe, Long authorId) {
        // Find the existing recipe by ID
        Recipe existingRecipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException("Recipe not found with id: " + recipeId));

        // Update the properties of the existing recipe with the new values
        existingRecipe.setKeywords(updatedRecipe.getKeywords());
        existingRecipe.setIngredients(updatedRecipe.getIngredients());
        existingRecipe.setSteps(updatedRecipe.getSteps());

        // Find the author by ID and set it to the updated recipe
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + authorId));
        existingRecipe.setAuthor(author);

        // Save the updated recipe to the database
        return recipeRepository.save(existingRecipe);
    }


    // Delete logic for Recipe entity
    public void deleteRecipe(Long recipeId) {
        Recipe existingRecipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException("Recipe not found with id: " + recipeId));

        // Delete the recipe from the database
        recipeRepository.delete(existingRecipe);
    }

    // Retrieve a recipe by ID
    public Recipe getRecipeById(Long recipeId) {
        return recipeRepository.findById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException("Recipe not found with id: " + recipeId));
    }

}
