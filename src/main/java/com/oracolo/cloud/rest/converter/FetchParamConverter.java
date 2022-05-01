package com.oracolo.cloud.rest.converter;

import com.oracolo.cloud.bl.helpers.FetchParam;

import javax.enterprise.context.ApplicationScoped;
import java.util.*;

@ApplicationScoped
public class FetchParamConverter {

    public Map<FetchParam,List<String>> from(String recipeName, Collection<String> ingredients){
        if((recipeName==null || recipeName.isBlank()) && ingredients.size()==0){
            return Collections.emptyMap();
        }
        Map<FetchParam,List<String>> fetchMap = new HashMap<>();
        fetchMap.computeIfAbsent(FetchParam.RECIPE_NAME,fetchParam -> new ArrayList<>()).add(recipeName);
        fetchMap.computeIfAbsent(FetchParam.INGREDIENT_NAME,fetchParam -> new ArrayList<>()).addAll(ingredients);
        return fetchMap;
    }
}
