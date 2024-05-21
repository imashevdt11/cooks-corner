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
                                  String preparation_time, Long userId, List<Ingredient> ingredients) throws IOException {

        if (recipeRepository.existsRecipeByName(name)) {
            throw new ResourceAlreadyExistsException("Recipe with name '" + name + "' is already exists!", HttpStatus.CONFLICT.value());
        }

        User checkedUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId, HttpStatus.NOT_FOUND.value()));

        Recipe recipe = new Recipe();
        recipe.setName(name);
        recipe.setDescription(description);
        recipe.setDifficulty(difficulty);
        recipe.setCategory(category);
        recipe.setPreparation_time(preparation_time);
        recipe.setUser(checkedUser);

        for (Ingredient ingredient : ingredients) {
            ingredient.setRecipe(recipe);
        }
        recipe.setIngredients(ingredients);

        Image image;
        if (!file.isEmpty()) {
            String url = cloudinaryService.uploadFile(file, "images");
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            image = new Image();
            image.setName(fileName);
            image.setUrl(url);
            imageRepository.save(image);
            recipe.setImage(image);
        }
        Recipe savedRecipe = recipeRepository.save(recipe);

        return RecipeMapper.toRecipeDto(savedRecipe);
    }

    @Override
    public List<PageRecipeDto> getRecipesByCategory(Category category, Long currentUserId) {
        List<Recipe> recipes = recipeRepository.findByCategory(category);
        List<PageRecipeDto> recipeList = toListPageRecipeDto(recipes, currentUserId);

        if (recipeList.isEmpty()) {
            throw new ResourceNotFoundException("Recipes with category '" + category + "' not found", HttpStatus.NOT_FOUND.value());
        }
        return recipeList;
    }

    @Override
    public RecipeDetailPageDto getRecipeDetails(Long recipeId, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId, HttpStatus.NOT_FOUND.value()));
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id: " + recipeId, HttpStatus.NOT_FOUND.value()));

        RecipeDetailPageDto recipeDto = new RecipeDetailPageDto();
        recipeDto.setRecipeId(recipe.getId());
        recipeDto.setRecipeName(recipe.getName());
        recipeDto.setPreparationTime(recipe.getPreparation_time());
        recipeDto.setDifficulty(recipe.getDifficulty());
        recipeDto.setAuthorId(recipe.getUser().getId());
        recipeDto.setAuthorName(recipe.getUser().getUsername());
        recipeDto.setDescription(recipe.getDescription());
        recipeDto.setImageUrl(recipe.getImage().getUrl());
        recipeDto.setLikeCount(likedRecipeRepository.countByRecipe(recipe));
        recipeDto.setIsLikedByCurrentUser(likedRecipeRepository.findByUserAndRecipe(user, recipe).isPresent());
        recipeDto.setIsSavedByCurrentUser(savedRecipeRepository.findByUserAndRecipe(user, recipe).isPresent());

        List<IngredientDto> ingredients = ingredientRepository.findByRecipe(recipe).stream()
                .map(ingredient -> {
                    IngredientDto ingredientDTO = new IngredientDto();
                    ingredientDTO.setName(ingredient.getName());
                    ingredientDTO.setAmount(ingredient.getAmount());
                    ingredientDTO.setUnit(ingredient.getUnit());
                    return ingredientDTO;
                }).collect(Collectors.toList());

        recipeDto.setIngredients(ingredients);

        return recipeDto;
    }

    @Override
    public List<PageRecipeDto> getUserRecipes(Long userId) {
        List<Recipe> recipes = recipeRepository.findByUserId(userId);
        List<PageRecipeDto> recipeList = toListPageRecipeDto(recipes, userId);

        if (recipeList.isEmpty()) {
            throw new ResourceNotFoundException("There is no recipes created by this user", HttpStatus.NOT_FOUND.value());
        }
        return recipeList;
    }

    public List<PageRecipeDto> getSavedRecipes(Long userId) {
        List<SavedRecipe> savedRecipes = savedRecipeRepository.findByUserId(userId);
        List<Recipe> recipes = savedRecipes.stream()
                .map(SavedRecipe::getRecipe)
                .collect(Collectors.toList());
        List<PageRecipeDto> recipeList = toListPageRecipeDto(recipes, userId);

        if (recipeList.isEmpty()) {
            throw new ResourceNotFoundException("There are no saved recipes for this user", HttpStatus.NOT_FOUND.value());
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

    private List<PageRecipeDto> toListPageRecipeDto(List<Recipe> recipes, Long userId) {

        List<PageRecipeDto> recipeList = new ArrayList<>();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId, HttpStatus.NOT_FOUND.value()));

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