package com.bonitasoft.cookingapp.controller;

import com.bonitasoft.cookingapp.entity.Recipe;
import com.bonitasoft.cookingapp.entity.User;
import com.bonitasoft.cookingapp.service.RecipeService;
import com.bonitasoft.cookingapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class RecipeControllerTest {
    @Mock
    private RecipeService recipeService;

    @Mock
    private UserService userService;

    @InjectMocks
    private RecipeController recipeController;

    @Test
    public void testGetAllRecipes() {
        // Mocking behavior for recipeService.getAllRecipes()
        List<Recipe> mockRecipes = new ArrayList<>();
        when(recipeService.getAllRecipes()).thenReturn(mockRecipes);

        // Test the getAllRecipes method
        ResponseEntity<List<Recipe>> responseEntity = recipeController.getAllRecipes();

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockRecipes, responseEntity.getBody());
    }

    @Test
    public void testAddRecipe_WithExistingAuthor() {
        Recipe mockRecipe = new Recipe(); // Mocking a recipe
        User mockAuthor = new User(); // Mocking an existing author

        mockRecipe.setAuthor(mockAuthor);
        // Mocking behavior for userService.findUser() when author exists
        when(userService.findUser(mockAuthor.getUsername())).thenReturn(mockAuthor);

        // Mocking behavior for recipeService.addRecipe()
        when(recipeService.addRecipe(mockRecipe)).thenReturn(mockRecipe);

        // Test the addRecipe method when the author exists
        ResponseEntity<?> responseEntity = recipeController.addRecipe(mockRecipe);

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockRecipe, responseEntity.getBody());
    }

    @Test
    public void testAddRecipe_WithNonExistingAuthor() {
        Recipe mockRecipe = new Recipe(); // Mocking a recipe
        User nonExistingAuthor = new User(); // Mocking a non-existing author
        mockRecipe.setAuthor(nonExistingAuthor);
        // Mocking behavior for userService.findUser() when author doesn't exist
        when(userService.findUser(nonExistingAuthor.getUsername())).thenReturn(null);

        // Test the addRecipe method when the author doesn't exist
        ResponseEntity<?> responseEntity = recipeController.addRecipe(mockRecipe);

        // Assertions
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertEquals("Unable to create recipe, Author does not exist !", responseEntity.getBody());
    }

    @Test
    public void testGetRecipesByAuthor_AuthorFound() {
        String authorUsername = "existingAuthor";
        User mockAuthor = new User(); // Mocking an existing author

        // Mocking behavior for userService.findUser() when author exists
        when(userService.findUser(authorUsername)).thenReturn(mockAuthor);

        List<Recipe> mockRecipes = new ArrayList<>(); // Mocking recipes
        when(recipeService.getRecipesByAuthor(mockAuthor)).thenReturn(mockRecipes);

        // Test the getRecipesByAuthor method when the author is found
        ResponseEntity<?> responseEntity = recipeController.getRecipesByAuthor(authorUsername);

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockRecipes, responseEntity.getBody());
    }

    @Test
    public void testGetRecipesByAuthor_AuthorNotFound() {
        String authorUsername = "nonExistingAuthor";

        // Mocking behavior for userService.findUser() when author doesn't exist
        when(userService.findUser(authorUsername)).thenReturn(null);

        // Test the getRecipesByAuthor method when author is not found
        ResponseEntity<?> responseEntity = recipeController.getRecipesByAuthor(authorUsername);

        // Assertions
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("No Author found !", responseEntity.getBody());
    }

    @Test
    public void testGetRecipesByKeywords_WithEmptyKeywords() {
        List<String> emptyKeywords = new ArrayList<>(); // Mocking empty keywords list

        // Test the getRecipesByKeywords method with empty keywords
        ResponseEntity<?> responseEntity = recipeController.getRecipesByKeywords(emptyKeywords);

        // Assertions
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("No given keywords !", responseEntity.getBody());
    }

    @Test
    public void testGetRecipesByKeywords_WithValidKeywords() {
        List<String> emptyKeywords = new ArrayList<>(); // Mocking empty keywords list
        emptyKeywords.add("keyword");

        Recipe recipe=new Recipe();
        recipe.setKeywords(emptyKeywords);

        List<Recipe> recipes = new ArrayList<>();
        recipes.add(recipe);
        // Test the getRecipesByKeywords method with empty keywords
        ResponseEntity<?> responseEntity = recipeController.getRecipesByKeywords(emptyKeywords);

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testGetRecipesByIngredients_WithValidIngredients() {
        List<String> validIngredients = List.of("ingredient1", "ingredient2"); // Mocking valid ingredients

        Set<Recipe> mockRecipes = new HashSet<>(); // Mocking recipes
        when(recipeService.getRecipesByIngredients(validIngredients)).thenReturn(mockRecipes);

        // Test the getRecipesByIngredients method with valid ingredients
        ResponseEntity<?> responseEntity = recipeController.getRecipesByIngredients(validIngredients);

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockRecipes, responseEntity.getBody());
    }

    @Test
    public void testGetRecipesByIngredients_WithEmptyIngredients() {
        List<String> emptyIngredients = new ArrayList<>(); // Mocking empty ingredients list

        // Test the getRecipesByIngredients method with empty ingredients
        ResponseEntity<?> responseEntity = recipeController.getRecipesByIngredients(emptyIngredients);

        // Assertions
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("No given keywords !", responseEntity.getBody());
    }

    @Test
    public void testUpdateRecipe() {
        Long recipeId = 1L; // Mocking recipe ID
        Long authorId = 2L; // Mocking author ID
        Recipe mockUpdatedRecipe = new Recipe(); // Mocking an updated recipe

        // Mocking behavior for recipeService.updateRecipe()
        when(recipeService.updateRecipe(recipeId, mockUpdatedRecipe, authorId)).thenReturn(mockUpdatedRecipe);

        // Test the updateRecipe method
        ResponseEntity<Recipe> responseEntity = recipeController.updateRecipe(recipeId, mockUpdatedRecipe, authorId);

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockUpdatedRecipe, responseEntity.getBody());
    }

    @Test
    public void testDeleteRecipe() {
        Long recipeId = 1L; // Mocking recipe ID

        // Test the deleteRecipe method
        ResponseEntity<Void> responseEntity = recipeController.deleteRecipe(recipeId);

        // Verifying if recipeService.deleteRecipe() was called
        verify(recipeService, times(1)).deleteRecipe(recipeId);

        // Assertions
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody());
    }

    @Test
    public void testGetRecipeById() {
        Long recipeId = 1L; // Mocking recipe ID
        Recipe mockRecipe = new Recipe(); // Mocking a recipe

        // Mocking behavior for recipeService.getRecipeById()
        when(recipeService.getRecipeById(recipeId)).thenReturn(mockRecipe);

        // Test the getRecipeById method
        ResponseEntity<Recipe> responseEntity = recipeController.getRecipeById(recipeId);

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockRecipe, responseEntity.getBody());
    }

}
