package com.oracolo.cloud.rest;


import com.oracolo.cloud.bl.RecipeService;
import com.oracolo.cloud.entities.Recipe;
import com.oracolo.cloud.exception.RecipeNotFoundException;
import com.oracolo.cloud.rest.converter.FetchParamConverter;
import com.oracolo.cloud.rest.converter.IngredientConverter;
import com.oracolo.cloud.rest.converter.RecipeConverter;
import com.oracolo.cloud.rest.dto.IngredientDto;
import com.oracolo.cloud.rest.dto.RecipeDto;
import com.oracolo.cloud.rest.dto.RecipeFormDto;
import org.jboss.resteasy.reactive.MultipartForm;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/recipes")
public class RecipeRest {

    @Inject
    RecipeService recipeService;

    @Inject
    RecipeConverter recipeConverter;

    @Inject
    FetchParamConverter fetchParamConverter;

    @Inject
    IngredientConverter ingredientConverter;


    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createRecipe(@MultipartForm RecipeFormDto recipeFormDto) {
        return Response.status(Response.Status.CREATED)
                .entity(recipeConverter.toRecipe(recipeService.checkedInsert(recipeConverter.fromRecipe(recipeFormDto),ingredientConverter.fromIngredients(recipeFormDto.ingredients))))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @GET
    @Path("{recipeId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public RecipeDto getById(@PathParam("recipeId") Integer recipeId){
        return recipeConverter.toRecipe(recipeService.getById(recipeId).orElseThrow(()->new NotFoundException("Recipe not found")));
    }

    @GET
    @Path("{recipeId}/image")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Transactional
    public byte[] getRecipeImage(@PathParam("recipeId") Integer recipeId){
        return recipeService.getById(recipeId).map(Recipe::getImage).orElseThrow(()->new NotFoundException("Recipe not found"));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<RecipeDto> getRecipes(
            @QueryParam("recipeName")
            String recipeName,
            @QueryParam("ingredientNames")
            List<String> ingredientNames
    ){
        return recipeConverter.toRecipes(recipeService.fetchRecipes(fetchParamConverter.from(recipeName,ingredientNames)));
    }

    @DELETE
    @Path("{recipeId}")
    @Transactional
    public void deleteById(@PathParam("recipeId") Integer recipeId){
        recipeService.delete(recipeService.getById(recipeId).orElseThrow(RecipeNotFoundException::new));
    }

    @PUT
    @Path("{recipeId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public RecipeDto modifyRecipeIngredients(@PathParam("recipeId") Integer recipeId, List<IngredientDto> ingredientDtos){
        return recipeConverter.toRecipe(recipeService.update(recipeService.getById(recipeId).orElseThrow(RecipeNotFoundException::new),ingredientConverter.fromIngredients(ingredientDtos)));
    }
}
