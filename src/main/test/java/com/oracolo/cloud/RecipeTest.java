package com.oracolo.cloud;

import com.oracolo.cloud.exception.ErrorCode;
import com.oracolo.cloud.rest.dto.ErrorDto;
import com.oracolo.cloud.rest.dto.IngredientDto;
import com.oracolo.cloud.rest.dto.RecipeDto;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class RecipeTest {

    public static final String RECIPE_NAME = "ricetta a caso";


    private static final String[] INGREDIENTS = {"carote", "piselli", "gallina", "cipolla"};


    public RecipeDto createRecipe() throws IOException {
        List<IngredientDto> ingredientDtos = createIngredients(INGREDIENTS);
        String imageName = "softkitten.jpg";
        File image = Assertions.assertDoesNotThrow(()->new File(getClass().getClassLoader().getResource(imageName).getFile()));
        Response createdRecipe = given()
                .accept(MediaType.APPLICATION_JSON)
                .multiPart("ingredients",ingredientDtos)
                .multiPart("name",RECIPE_NAME)
                .multiPart("image",image)
                .when().post("/recipes");
        Assertions.assertEquals(201, createdRecipe.getStatusCode());
        RecipeDto recipeDto = Assertions.assertDoesNotThrow(() -> Json.decodeValue(Buffer.buffer(createdRecipe.body().asByteArray()), RecipeDto.class));
        Assertions.assertNotNull(recipeDto.id);
        Assertions.assertEquals(RECIPE_NAME, recipeDto.name);
        Assertions.assertFalse(recipeDto.ingredients.isEmpty());
        Assertions.assertTrue(recipeDto.ingredients.containsAll(ingredientDtos));
        return recipeDto;
    }
    @Test
    @DisplayName("Create recipe without name")
    public RecipeDto createRecipeWithoutName() throws IOException {
        List<IngredientDto> ingredientDtos = createIngredients(INGREDIENTS);
        String imageName = "softkitten.jpg";
        File image = Assertions.assertDoesNotThrow(()->new File(getClass().getClassLoader().getResource(imageName).getFile()));
        Response createdRecipe = given()
                .accept(MediaType.APPLICATION_JSON)
                .multiPart("ingredients",ingredientDtos)
                .multiPart("image",image)
                .when().post("/recipes");
        Assertions.assertEquals(201, createdRecipe.getStatusCode());
        RecipeDto recipeDto = Assertions.assertDoesNotThrow(() -> Json.decodeValue(Buffer.buffer(createdRecipe.body().asByteArray()), RecipeDto.class));
        Assertions.assertNotNull(recipeDto.id);
        Assertions.assertFalse(recipeDto.ingredients.isEmpty());
        Assertions.assertTrue(recipeDto.ingredients.containsAll(ingredientDtos));
        return recipeDto;
    }

    @Test
    @DisplayName("Successfully create a recipe without ingredients")
    public void createRecipeWithoutIngredients() throws IOException {
        String imageName = "softkitten.jpg";
        File image = Assertions.assertDoesNotThrow(()->new File(getClass().getClassLoader().getResource(imageName).getFile()));
        Response createdRecipe = given()
                .accept(MediaType.APPLICATION_JSON)
                .multiPart("name",RECIPE_NAME)
                .multiPart("image",image)
                .when().post("/recipes");
        Assertions.assertEquals(201, createdRecipe.getStatusCode());
        RecipeDto recipeDto = Assertions.assertDoesNotThrow(() -> Json.decodeValue(Buffer.buffer(createdRecipe.body().asByteArray()), RecipeDto.class));
        Assertions.assertNotNull(recipeDto.id);
        Assertions.assertEquals(RECIPE_NAME, recipeDto.name);
        Assertions.assertTrue(recipeDto.ingredients.isEmpty());
    }



    @Test
    @DisplayName("Image too big exception")
    public void imageTooBigTest() throws IOException {
        List<IngredientDto> ingredientDtos = createIngredients();
        String imageName = "devops/curltests/imagetoobig.jpg";
        File image = Assertions.assertDoesNotThrow(()->new File(getClass().getClassLoader().getResource(imageName).getFile()));
        Response createdRecipe = given()
                .accept(MediaType.APPLICATION_JSON)
                .multiPart("ingredients",ingredientDtos)
                .multiPart("name", RECIPE_NAME)
                .multiPart("image",image)
                .when().post("/recipes");
        Assertions.assertEquals(400, createdRecipe.getStatusCode());
        ErrorDto errorDto = Assertions.assertDoesNotThrow(() -> Json.decodeValue(Buffer.buffer(createdRecipe.body().asByteArray()), ErrorDto.class));
        Assertions.assertEquals(ErrorCode.IMAGE_TOO_BIG,errorDto.errorCode);
    }

    @Test
    @DisplayName("Too many ingredients")
    public void tooManyIngredientsTest() throws IOException {
        List<IngredientDto> ingredientDtos = IntStream.range(0,200).mapToObj(index-> new IngredientDto(UUID.randomUUID().toString())).collect(Collectors.toUnmodifiableList());
        String imageName = "softkitten.jpg";
        File image = Assertions.assertDoesNotThrow(()->new File(getClass().getClassLoader().getResource(imageName).getFile()));
        Response createdRecipe = given()
                .accept(MediaType.APPLICATION_JSON)
                .multiPart("ingredients",ingredientDtos,MediaType.APPLICATION_JSON)
                .multiPart("name", RECIPE_NAME)
                .multiPart("image",image)
                .when().post("/recipes");
        Assertions.assertEquals(400, createdRecipe.getStatusCode());
        ErrorDto errorDto = Assertions.assertDoesNotThrow(() -> Json.decodeValue(Buffer.buffer(createdRecipe.body().asByteArray()), ErrorDto.class));
        Assertions.assertEquals(ErrorCode.TOO_MANY_INGREDIENTS,errorDto.errorCode);
    }

    @Test
    @DisplayName("Get recipe by id")
    public void getById() throws IOException {
        RecipeDto recipeDto = createRecipe();
        Response createdRecipe = given()
                .accept(MediaType.APPLICATION_JSON)
                .pathParam("recipeId",recipeDto.id)
                .when()
                    .get("/recipes/{recipeId}");
        Assertions.assertEquals(200, createdRecipe.getStatusCode());
        RecipeDto recipeDtoById = Assertions.assertDoesNotThrow(() -> Json.decodeValue(Buffer.buffer(createdRecipe.body().asByteArray()), RecipeDto.class));
        Assertions.assertEquals(recipeDto.imageUrl,recipeDtoById.imageUrl);
        Assertions.assertEquals(recipeDto.ingredients,recipeDtoById.ingredients);
        Assertions.assertEquals(recipeDto.name,recipeDtoById.name);
        Assertions.assertEquals(recipeDto.id,recipeDtoById.id);
    }

    @Test
    @DisplayName("Get recipe image by id")
    public void getRecipeImageById() throws IOException {
        RecipeDto recipeDto = createRecipe();
        Response recipeImage = given()
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .when()
                .get(recipeDto.imageUrl);
        Assertions.assertEquals(200, recipeImage.getStatusCode());
        String imageName = "softkitten.jpg";
        File image = Assertions.assertDoesNotThrow(()->new File(getClass().getClassLoader().getResource(imageName).getFile()));
        Assertions.assertEquals(recipeImage.asByteArray().length,Files.readAllBytes(image.toPath()).length);
    }

    @Test
    @DisplayName("Get recipe by query")
    public void getRecipeByQuery() throws IOException {
        int range = 10;
        IntStream.range(0,range).forEach(value -> {
            try {
                createRecipe();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        Response recipes = given()
                .accept(MediaType.APPLICATION_JSON)
                .param("recipeName",RECIPE_NAME)
                .param("ingredients",List.of(INGREDIENTS))
                .when()
                .get("/recipes");
        Assertions.assertEquals(200, recipes.getStatusCode());
        List<RecipeDto> recipeDtos = new JsonArray(Buffer.buffer(recipes.asByteArray())).stream()
                .map(o->Json.decodeValue(JsonObject.mapFrom(o).encode(),RecipeDto.class)).collect(Collectors.toUnmodifiableList());
        Assertions.assertTrue(recipeDtos.stream().allMatch(recipeDto -> recipeDto.ingredients.stream().map(ingredientDto -> ingredientDto.name).collect(Collectors.toUnmodifiableList()).containsAll(List.of(INGREDIENTS))));
        Assertions.assertTrue(recipeDtos.stream().allMatch(recipeDto -> recipeDto.name.equals(RECIPE_NAME)));
    }

    @Test
    @DisplayName("Delete by id")
    public void deleteByIdTest() throws IOException {
        RecipeDto recipeDto = createRecipe();
        Response recipe = given()
                .pathParam("recipeId",recipeDto.id)
                .when()
                .delete("/recipes/{recipeId}");
        Assertions.assertEquals(204, recipe.getStatusCode());
        Response deleteResponse = given()
                .pathParam("recipeId",12839)
                .when()
                .get("/recipes/{recipeId}");
        Assertions.assertEquals(404,deleteResponse.getStatusCode());

    }

    @Test
    @DisplayName("Modify recipe ingredients")
    public void modifyRecipeIngredient() throws IOException {
        RecipeDto recipeDto = createRecipe();
        List<IngredientDto> ingredientDtos = createIngredients("borraccia","righello");
        Response modifiedRecipe = given()
                .pathParam("recipeId",recipeDto.id)
                .body(ingredientDtos)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .when()
                .put("/recipes/{recipeId}");
        Assertions.assertEquals(200,modifiedRecipe.getStatusCode());
        RecipeDto modified = Assertions.assertDoesNotThrow(()->Json.decodeValue(Buffer.buffer(modifiedRecipe.asByteArray()),RecipeDto.class));
        Assertions.assertTrue(modified.ingredients.containsAll(ingredientDtos));
        Assertions.assertFalse(modified.ingredients.containsAll(createIngredients(INGREDIENTS)));
        Assertions.assertEquals(modified.name,recipeDto.name);
        Assertions.assertEquals(modified.imageUrl, recipeDto.imageUrl);
    }


    private static List<IngredientDto> createIngredients(String... ingredients) {
        return Arrays.stream(ingredients).map(IngredientDto::new).collect(Collectors.toUnmodifiableList());
    }


}
