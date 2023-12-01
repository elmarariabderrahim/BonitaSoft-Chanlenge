package com.bonitasoft.cookingapp.controller;

import com.bonitasoft.cookingapp.entity.Recipe;
import com.bonitasoft.cookingapp.entity.User;
import com.bonitasoft.cookingapp.service.RecipeService;
import com.bonitasoft.cookingapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RecipeController {

    @Autowired
    private RecipeService recipeService;
    @Autowired
    private UserService userService;


    @GetMapping("/getAllRecipes")
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        return ResponseEntity.ok().body(recipeService.getAllRecipes());
    }

    @PostMapping("/addRecipe")
    public ResponseEntity<?> addRecipe(@RequestBody Recipe recipe) {
        User authorExist = userService.findUser(recipe.getAuthor().getUsername());
        if (authorExist!=null){
            recipe.setAuthor(authorExist);
            return ResponseEntity.ok().body(recipeService.addRecipe(recipe));
        }else {
            return new ResponseEntity<>(("Unable to create recipe, Author does not exist !"), HttpStatus.CONFLICT);
        }

    }

    @GetMapping("/search/keywords")
    public ResponseEntity<?> getRecipesByKeywords(@RequestBody List<String> keywords) {
        if (!keywords.isEmpty()){
            return ResponseEntity.ok().body(recipeService.getRecipesByKeywords(keywords));
        }else {
            return new ResponseEntity<>(("No given keywords !"), HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/search/ingredients")
    public ResponseEntity<?> getRecipesByIngredients(@RequestBody List<String> keywords) {
        if (!keywords.isEmpty()){
            return ResponseEntity.ok().body(recipeService.getRecipesByIngredients(keywords));
        }else {
            return new ResponseEntity<>(("No given keywords !"), HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/search/author/{authorUsername}")
    public ResponseEntity<?> getRecipesByAuthor(@PathVariable("authorUsername") String authorUsername) {
        User author = userService.findUser(authorUsername);
        if (author==null){
            return new ResponseEntity<>(("No Author found !"), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok().body(recipeService.getRecipesByAuthor(author));
    }

    @PutMapping("/update/{recipeId}")
    public ResponseEntity<Recipe> updateRecipe(
            @PathVariable Long recipeId,
            @RequestBody Recipe updatedRecipe,
            @RequestParam Long authorId
    ) {
        Recipe updated = recipeService.updateRecipe(recipeId, updatedRecipe,authorId);
        return ResponseEntity.ok().body(updated);
    }

    @DeleteMapping("/delete/{recipeId}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long recipeId) {
        recipeService.deleteRecipe(recipeId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getRecipe/{recipeId}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable Long recipeId) {
        Recipe recipe = recipeService.getRecipeById(recipeId);
        return ResponseEntity.ok().body(recipe);
    }

}
