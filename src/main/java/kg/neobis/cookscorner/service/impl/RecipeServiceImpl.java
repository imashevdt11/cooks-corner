package kg.neobis.cookscorner.service.impl;

import kg.neobis.cookscorner.dto.*;
import kg.neobis.cookscorner.entity.*;
import kg.neobis.cookscorner.enums.Category;
import kg.neobis.cookscorner.enums.Difficulty;
import kg.neobis.cookscorner.exception.ResourceAlreadyExistsException;
import kg.neobis.cookscorner.exception.ResourceNotFoundException;
import kg.neobis.cookscorner.mapper.RecipeMapper;
import kg.neobis.cookscorner.repository.*;
import kg.neobis.cookscorner.service.CloudinaryService;
import kg.neobis.cookscorner.service.RecipeService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RecipeServiceImpl implements RecipeService {

    CloudinaryService cloudinaryService;
    ImageRepository imageRepository;
    IngredientRepository ingredientRepository;
    LikedRecipeRepository likedRecipeRepository;
    SavedRecipeRepository savedRecipeRepository;
    RecipeRepository recipeRepository;
    UserRepository userRepository;

    @Override
    public RecipeDto createRecipe(MultipartFile file, String name, String description, Difficulty difficulty, Category category,
                                  String preparation_time, String username, List<Ingredient> ingredients) throws IOException {

        if (recipeRepository.existsRecipeByName(name)) {
            throw new ResourceAlreadyExistsException("Recipe with name '" + name + "' is already exists!", HttpStatus.CONFLICT.value());
        }

        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with name: '" + username + "'", HttpStatus.NOT_FOUND.value()));

        Recipe recipe = Recipe.builder()
                .name(name)
                .description(description)
                .difficulty(difficulty)
                .category(category)
                .preparation_time(preparation_time)
                .user(user).build();

        for (Ingredient ingredient : ingredients) {
            ingredient.setRecipe(recipe);
        }
        recipe.setIngredients(ingredients);

        Image image;
        if (!file.isEmpty()) {
            String url = cloudinaryService.uploadFile(file, "images");

            image = new Image();
            image.setName(UUID.randomUUID() + "_" + file.getOriginalFilename());
            image.setUrl(url);
            imageRepository.save(image);
            recipe.setImage(image);
        }
        Recipe savedRecipe = recipeRepository.save(recipe);

        return RecipeMapper.toRecipeDto(savedRecipe);
    }

    @Override
    public List<PageRecipeDto> getRecipesByCategory(Category category, String currentUsername) {

        List<Recipe> recipes = recipeRepository.findByCategory(category);
        List<PageRecipeDto> recipeList = toListPageRecipeDto(recipes, currentUsername);

        if (recipeList.isEmpty()) {
            throw new ResourceNotFoundException("Recipes with category '" + category + "' not found", HttpStatus.NOT_FOUND.value());
        }
        return recipeList;
    }

    @Override
    public RecipeDetailPageDto getRecipeDetails(String recipeName, String username) {

        Recipe recipe = findRecipeByName(recipeName);

        RecipeDetailPageDto recipeDto = RecipeDetailPageDto.builder()
                .recipeName(recipe.getName())
                .preparationTime(recipe.getPreparation_time())
                .difficulty(recipe.getDifficulty())
                .authorName(recipe.getUser().getUsername())
                .description(recipe.getDescription())
                .imageUrl(recipe.getImage().getUrl())
                .likeCount(likedRecipeRepository.countByRecipe(recipe))
                .isLikedByCurrentUser(isRecipeLikedByUser(username, recipeName))
                .isSavedByCurrentUser(isRecipeSavedByUser(username, recipeName))
                .build();

        List<IngredientDto> ingredients = ingredientRepository.findByRecipe(recipe).stream()
                .map(ingredient -> IngredientDto.builder()
                        .name(ingredient.getName())
                        .amount(ingredient.getAmount())
                        .unit(ingredient.getUnit())
                        .build()).collect(Collectors.toList());

        recipeDto.setIngredients(ingredients);

        return recipeDto;
    }

    @Override
    public List<PageRecipeDto> getUserRecipes(String username) {

        User user = findUserByUsername(username);
        List<Recipe> recipes = recipeRepository.findByUserId(user.getId());
        List<PageRecipeDto> recipeList = toListPageRecipeDto(recipes, username);

        if (recipeList.isEmpty()) {
            throw new ResourceNotFoundException("There is no recipes created by '" + username + "'", HttpStatus.NOT_FOUND.value());
        }
        return recipeList;
    }

    @Override
    public List<PageRecipeDto> getSavedRecipes(String username) {

        User user = findUserByUsername(username);
        List<SavedRecipe> savedRecipes = savedRecipeRepository.findByUserId(user.getId());
        List<Recipe> recipes = savedRecipes.stream()
                .map(SavedRecipe::getRecipe)
                .collect(Collectors.toList());
        List<PageRecipeDto> recipeList = toListPageRecipeDto(recipes, username);

        if (recipeList.isEmpty()) {
            throw new ResourceNotFoundException("There are no saved recipes", HttpStatus.NOT_FOUND.value());
        }
        return recipeList;
    }

    @Override
    public List<RecipeSearchPageDto> searchRecipesByName(String name) {
        List<Recipe> recipes = recipeRepository.findByNameStartingWithIgnoreCase(name);

        if (recipes.isEmpty())
            throw new ResourceNotFoundException("No recipes found with name '" + name + "'", HttpStatus.NOT_FOUND.value());

        return recipes.stream().map(RecipeMapper::toRecipeSearchPageDto).toList();
    }

    // LIKE

    @Override
    public void likeRecipe(String username, String recipeName) {

        User user = findUserByUsername(username);
        Recipe recipe = findRecipeByName(recipeName);

        Optional<LikedRecipe> likedRecipeOptional = likedRecipeRepository.findByUserAndRecipe(user, recipe);
        LikedRecipe likedRecipe;
        if (likedRecipeOptional.isPresent()) {
            likedRecipe = likedRecipeOptional.get();
            likedRecipe.setIsLiked(!likedRecipe.getIsLiked());
        } else {
            likedRecipe = LikedRecipe.builder()
                    .user(user)
                    .recipe(recipe)
                    .isLiked(true)
                    .build();
        }
        likedRecipeRepository.save(likedRecipe);
    }

    @Override
    public boolean isRecipeLikedByUser(String username, String recipeName) {

        User user = findUserByUsername(username);
        Recipe recipe = findRecipeByName(recipeName);

        return likedRecipeRepository.findByUserAndRecipe(user, recipe)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found for user and recipe", HttpStatus.NOT_FOUND.value()))
                .getIsLiked();
    }

    // SAVE

    @Override
    public void saveRecipe(String username, String recipeName) {

        User user = findUserByUsername(username);
        Recipe recipe = findRecipeByName(recipeName);

        Optional<SavedRecipe> savedRecipeOptional = savedRecipeRepository.findByUserAndRecipe(user, recipe);
        SavedRecipe savedRecipe;
        if (savedRecipeOptional.isPresent()) {
            savedRecipe = savedRecipeOptional.get();
            savedRecipe.setIsSaved(!savedRecipe.getIsSaved());
        } else {
            savedRecipe = SavedRecipe.builder()
                    .user(user)
                    .recipe(recipe)
                    .isSaved(true)
                    .build();
        }
        savedRecipeRepository.save(savedRecipe);
    }

    @Override
    public boolean isRecipeSavedByUser(String username, String recipeName) {

        User user = findUserByUsername(username);
        Recipe recipe = findRecipeByName(recipeName);

        return savedRecipeRepository.findByUserAndRecipe(user, recipe)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found for user and recipe", HttpStatus.NOT_FOUND.value()))
                .getIsSaved();
    }

    private Recipe findRecipeByName(String recipeName) {
        return recipeRepository.findRecipeByName(recipeName)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found with name: '" + recipeName + "'", HttpStatus.NOT_FOUND.value()));
    }

    private User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with name: '" + username + "'", HttpStatus.NOT_FOUND.value()));
    }

    private List<PageRecipeDto> toListPageRecipeDto(List<Recipe> recipes, String username) {

        List<PageRecipeDto> recipeList = new ArrayList<>();

        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: '" + username + "'", HttpStatus.NOT_FOUND.value()));

        for (Recipe recipe : recipes) {
            PageRecipeDto pageRecipeDto = PageRecipeDto.builder()
                    .recipeId(recipe.getId())
                    .recipeName(recipe.getName())
                    .imageUrl(recipe.getImage().getUrl())
                    .authorName(recipe.getUser().getUsername())
                    .likeCount(likedRecipeRepository.countByRecipe(recipe))
                    .saveCount(savedRecipeRepository.countByRecipe(recipe))
                    .isLikedByCurrentUser(likedRecipeRepository.findByUserAndRecipe(user, recipe).isPresent())
                    .isSavedByCurrentUser(savedRecipeRepository.findByUserAndRecipe(user, recipe).isPresent())
                    .build();
            recipeList.add(pageRecipeDto);
        }
        return recipeList;
    }
}