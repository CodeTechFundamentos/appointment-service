package com.nutrix.query.api;

import com.nutrix.command.dtos.Recipe;
import com.nutrix.command.domain.Diet;
import com.nutrix.command.infra.IDietRecipesRepository;
import com.nutrix.query.application.services.DietQueryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/diet")
@Api(tags = "Diet", value = "Servicio Web RESTful de Diets")
public class DietQueryController {

    @Autowired
    private DietQueryService dietService;
    @Autowired
    private IDietRecipesRepository dietRecipesRepository;
    @Autowired
    private RestTemplate template;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Buscar todos los Diets", notes = "Método para encontrar todos los Diets")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Diets encontrados"),
            @ApiResponse(code = 404, message = "Diets no encontrados")
    })
    public ResponseEntity<List<Diet>>findAll(){
        try {
            List<Diet> diets = new ArrayList<>();
            diets = dietService.getAll();
            return new ResponseEntity<List<Diet>>(diets, HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity<List<Diet>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Buscar Diets por Id", notes = "Método para encontrar Diets por Id")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Diets encontrados"),
            @ApiResponse(code = 404, message = "Diets no encontrados")
    })
    public ResponseEntity<Diet> findById(@PathVariable("id") Integer id){
        try {
            Optional<Diet> diet = dietService.getById(id);
            if(!diet.isPresent())
                return new ResponseEntity<Diet>(HttpStatus.NOT_FOUND);
            return new ResponseEntity<Diet>(diet.get(),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<Diet>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/findDietRecipes/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Buscar Recipes de un Diet", notes = "Método para listar Recipes de un Diet")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Recipes encontrados"),
            @ApiResponse(code = 404, message = "Recipes no encontrados")
    })
    public ResponseEntity<Recipe[]> findDietRecipes(@PathVariable("id") Integer id)
    {
        try {
            List<Integer> recipes = dietRecipesRepository.findByDiet(id);
            if(recipes.isEmpty())
                return new ResponseEntity<Recipe[]>(HttpStatus.NOT_FOUND);

            Recipe[] recipesList = template.getForObject("http://localhost:8989/recipe/getRecipesByList", Recipe[].class, recipes);

            return new ResponseEntity<Recipe[]>(recipesList, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<Recipe[]>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}