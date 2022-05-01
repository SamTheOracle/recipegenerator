package com.oracolo.cloud.rest.dto;


import org.jboss.resteasy.reactive.PartType;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RecipeFormDto {

    @FormParam("id")
    @PartType(MediaType.TEXT_PLAIN)
    public Integer id;

    @FormParam("name")
    @PartType(MediaType.TEXT_PLAIN)
    public String name;

    @FormParam("ingredients")
    @PartType(MediaType.APPLICATION_JSON)
    public List<IngredientDto> ingredients;

    @FormParam("image")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    public File image;

    public RecipeFormDto() {
        ingredients = new ArrayList<>();
    }

}
