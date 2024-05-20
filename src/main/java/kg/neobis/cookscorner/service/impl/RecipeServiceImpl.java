package kg.neobis.cookscorner.service.impl;

import kg.neobis.cookscorner.dto.RecipeDto;
import kg.neobis.cookscorner.dto.RecipeMainPageDto;
import kg.neobis.cookscorner.entity.Image;
import kg.neobis.cookscorner.entity.Ingredient;
import kg.neobis.cookscorner.entity.Recipe;
import kg.neobis.cookscorner.entity.User;
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

@Slf4j
@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RecipeServiceImpl implements RecipeService {

    CloudinaryService cloudinaryService;
    ImageRepository imageRepository;
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

        return RecipeMapper.toDto(savedRecipe);
    }


    @Override
    public List<RecipeMainPageDto> getRecipesByCategory(Category category, Long currentUserId) {

        List<Recipe> recipes = recipeRepository.findByCategory(category);
        List<RecipeMainPageDto> recipeList = new ArrayList<>();

        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + currentUserId, HttpStatus.NOT_FOUND.value()));

        for (Recipe recipe : recipes) {
            RecipeMainPageDto dto = new RecipeMainPageDto();
            dto.setId(recipe.getId());
            dto.setName(recipe.getName());
            dto.setImageUrl(recipe.getImage().getUrl());
            dto.setUserName(recipe.getUser().getUsername());
            dto.setLikeCount(likedRecipeRepository.countByRecipe(recipe));
            dto.setSaveCount(savedRecipeRepository.countByRecipe(recipe));
            dto.setIsLikedByCurrentUser(likedRecipeRepository.findByUserAndRecipe(user, recipe).isPresent());
            dto.setIsSavedByCurrentUser(savedRecipeRepository.findByUserAndRecipe(user, recipe).isPresent());
            recipeList.add(dto);
        }
        if (recipeList.isEmpty()) {
            throw new ResourceNotFoundException("Recipes with category '" + category + "' not found", HttpStatus.NOT_FOUND.value());
        }
        return recipeList;
    }
}