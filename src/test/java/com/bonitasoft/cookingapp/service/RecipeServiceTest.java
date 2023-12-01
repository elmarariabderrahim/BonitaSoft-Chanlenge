package com.bonitasoft.cookingapp.service;
import com.bonitasoft.cookingapp.entity.Recipe;
import com.bonitasoft.cookingapp.entity.User;
import com.bonitasoft.cookingapp.respository.RecipeRepository;
import com.bonitasoft.cookingapp.respository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RecipeService recipeService;

    @Test
    public void testAddRecipe() {
        Recipe mockRecipe = new Recipe(); // Create a mock recipe

        // Mocking behavior for recipeRepository.save() method
        when(recipeRepository.save(mockRecipe)).thenReturn(mockRecipe);

        // Call the RecipeService addRecipe method
        Recipe addedRecipe = recipeService.addRecipe(mockRecipe);

        // Verify that recipeRepository.save() was called and returned the mock recipe
        verify(recipeRepository, times(1)).save(mockRecipe);
        assertEquals(mockRecipe, addedRecipe);
    }

    @Test
    public void testGetAllRecipes() {
        List<Recipe> mockRecipes = new ArrayList<>(); // Mocking a list of recipes

        // Mocking behavior for recipeRepository.findAll() method
        when(recipeRepository.findAll()).thenReturn(mockRecipes);

        // Call the RecipeService getAllRecipes method
        List<Recipe> recipes = recipeService.getAllRecipes();

        // Verify that recipeRepository.findAll() was called and returned the list of recipes
        verify(recipeRepository, times(1)).findAll();
        assertEquals(mockRecipes, recipes);
    }

    // Add tests for other methods like getRecipesByAuthor, getRecipesByKeywords, getRecipesByIngredients, etc.

    // Example test for getRecipeById
    @Test
    public void testGetRecipeById() {
        Long recipeId = 1L; // Mocking recipe ID
        Recipe mockRecipe = new Recipe(); // Mocking a recipe

        // Mocking behavior for recipeRepository.findById() method
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(mockRecipe));

        // Call the RecipeService getRecipeById method
        Recipe recipe = recipeService.getRecipeById(recipeId);

        // Verify that recipeRepository.findById() was called and returned the mock recipe
        verify(recipeRepository, times(1)).findById(recipeId);
        assertEquals(mockRecipe, recipe);
    }

    @Test
    public void testDeleteRecipe_RecipeNotFound() {
        Long recipeId = 1L; // Mocking recipe ID

        // Mocking behavior for recipeRepository.findById() when recipe is not found
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.empty());

        // Call the RecipeService deleteRecipe method and expect an EntityNotFoundException
        assertThrows(EntityNotFoundException.class, () -> recipeService.deleteRecipe(recipeId));

        // Verify that recipeRepository.findById() was called
        verify(recipeRepository, times(1)).findById(recipeId);
        // Verify that recipeRepository.delete() was not called
        verify(recipeRepository, never()).delete(any());
    }

    @Test
    public void testDeleteRecipe_RecipeFound() {
        Long recipeId = 1L; // Mocking recipe ID
        Recipe mockRecipe = new Recipe(); // Mocking a recipe

        // Mocking behavior for recipeRepository.findById() when recipe is found
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(mockRecipe));

        // Call the RecipeService deleteRecipe method
        recipeService.deleteRecipe(recipeId);

        // Verify that recipeRepository.findById() was called and recipeRepository.delete() was called
        verify(recipeRepository, times(1)).findById(recipeId);
        verify(recipeRepository, times(1)).delete(mockRecipe);
    }

    @Test
    public void testUpdateRecipe_RecipeAndAuthorFound() {
        Long recipeId = 1L; // Mocking recipe ID
        Long authorId = 2L; // Mocking author ID
        Recipe mockExistingRecipe = new Recipe(); // Mocking an existing recipe
        Recipe mockUpdatedRecipe = new Recipe(); // Mocking an updated recipe
        User mockAuthor = new User(); // Mocking an author

        // Mocking behavior for recipeRepository.findById() when recipe is found
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(mockExistingRecipe));

        // Mocking behavior for userRepository.findById() when author is found
        when(userRepository.findById(authorId)).thenReturn(Optional.of(mockAuthor));

        // Mocking behavior for recipeRepository.save()
        when(recipeRepository.save(mockExistingRecipe)).thenReturn(mockExistingRecipe);

        // Call the RecipeService updateRecipe method
        Recipe updatedRecipe = recipeService.updateRecipe(recipeId, mockUpdatedRecipe, authorId);

        // Verify that recipeRepository.findById() and userRepository.findById() were called
        verify(recipeRepository, times(1)).findById(recipeId);
        verify(userRepository, times(1)).findById(authorId);
        // Verify that recipeRepository.save() was called
        verify(recipeRepository, times(1)).save(mockExistingRecipe);

        // Assertions
        assertEquals(mockExistingRecipe, updatedRecipe);
    }

    @Test
    public void testUpdateRecipe_RecipeNotFound() {
        Long recipeId = 1L; // Mocking recipe ID
        Recipe mockUpdatedRecipe = new Recipe(); // Mocking an updated recipe
        Long authorId = 2L; // Mocking author ID

        // Mocking behavior for recipeRepository.findById() when recipe is not found
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.empty());

        // Call the RecipeService updateRecipe method and expect an EntityNotFoundException
        assertThrows(EntityNotFoundException.class, () -> recipeService.updateRecipe(recipeId, mockUpdatedRecipe, authorId));

        // Verify that recipeRepository.findById() was called
        verify(recipeRepository, times(1)).findById(recipeId);
        // Verify that userRepository.findById() and recipeRepository.save() were not called
        verify(userRepository, never()).findById(any());
        verify(recipeRepository, never()).save(any());
    }

    @Test
    public void testUpdateRecipe_AuthorNotFound() {
        Long recipeId = 1L; // Mocking recipe ID
        Recipe mockExistingRecipe = new Recipe(); // Mocking an existing recipe
        Recipe mockUpdatedRecipe = new Recipe(); // Mocking an updated recipe
        Long authorId = 2L; // Mocking author ID

        // Mocking behavior for recipeRepository.findById() when recipe is found
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(mockExistingRecipe));
        // Mocking behavior for userRepository.findById() when author is not found
        when(userRepository.findById(authorId)).thenReturn(Optional.empty());

        // Call the RecipeService updateRecipe method and expect an EntityNotFoundException
        assertThrows(EntityNotFoundException.class, () -> recipeService.updateRecipe(recipeId, mockUpdatedRecipe, authorId));

        // Verify that recipeRepository.findById() and userRepository.findById() were called
        verify(recipeRepository, times(1)).findById(recipeId);
        verify(userRepository, times(1)).findById(authorId);
        // Verify that recipeRepository.save() was not called
        verify(recipeRepository, never()).save(any());
    }

    @Test
    public void testGetRecipesByKeywords() {
        List<String> keywords = Arrays.asList("keyword1", "keyword2");
        Set<Recipe> mockRecipes = new HashSet<>(); // Mocking a list of recipes
        // Populate mockRecipes with some mock data

        // Mocking behavior for recipeRepository.getRecipesByKey() method
        when(recipeRepository.getRecipesByKey(anyString())).thenReturn(mockRecipes);

        // Call the RecipeService getRecipesByKeywords method
        Set<Recipe> recipes = recipeService.getRecipesByKeywords(keywords);

        // Verify that recipeRepository.getRecipesByKey() was called for each keyword
        verify(recipeRepository, times(keywords.size())).getRecipesByKey(anyString());

        // Add your assertions here based on the expected behavior of sorting recipes by keywords
        // For example:
        // assertEquals(expectedSortedRecipes, recipes);
    }

    @Test
    public void testGetRecipesByIngredients() {
        List<String> ingredients = List.of("ingredient1", "ingredient2");
        Collection mockRecipes = new ArrayList<>(); // Mocking a list of recipes
        // Populate mockRecipes with some mock data

        // Mocking behavior for recipeRepository.getRecipesByIngredients() method
        when(recipeRepository.getRecipesByIngredients(anyString())).thenReturn(mockRecipes);

        // Call the RecipeService getRecipesByIngredients method
        Collection<? extends Recipe> recipes = recipeService.getRecipesByIngredients(ingredients);

        verify(recipeRepository, times(ingredients.size())).getRecipesByIngredients(anyString());

    }
    @Test
    public void testGetRecipesByAuthor() {
        User author = new User(); // Mocking a user object
        List<Recipe> mockRecipes = new ArrayList<>(); // Mocking a list of recipes
        // Populate mockRecipes with some mock data

        // Mocking behavior for recipeRepository.getByAuthor() method
        when(recipeRepository.getByAuthor(author)).thenReturn(mockRecipes);

        // Call the RecipeService getRecipesByAuthor method
        List<Recipe> recipes = recipeService.getRecipesByAuthor(author);

        // Verify that recipeRepository.getByAuthor() was called with the provided author
        verify(recipeRepository, times(1)).getByAuthor(author);

    }



}
