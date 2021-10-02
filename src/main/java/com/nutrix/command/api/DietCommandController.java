package com.nutrix.command.api;

import com.nutrix.command.application.dtos.Recipe;
import com.nutrix.command.application.services.DietCommandService;
import com.nutrix.command.domain.Diet;
import com.nutrix.command.domain.DietRecipes;
import com.nutrix.command.infra.IDietRecipesRepository;
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

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/diet")
@Api(tags = "Diet", value = "Servicio Web RESTful de Diets")
public class DietCommandController {

    @Autowired
    private DietCommandService dietService;
    @Autowired
    private IDietRecipesRepository dietRecipesRepository;
    @Autowired
    private RestTemplate template;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Registro de Diets", notes = "Método para agregar un Diet")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Diet agregado"),
            @ApiResponse(code = 404, message = "Diet no fue agregado")
    })
    public ResponseEntity<Diet> insertDiet(@Valid @RequestBody Diet diet){
        try {
            Diet dietNew = dietService.save(diet);
            return ResponseEntity.status(HttpStatus.CREATED).body(dietNew);
        }catch (Exception e){
            return new ResponseEntity<Diet>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Actualizacion de un Diet", notes = "Método para actualizar un Diet")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Diet actualizado"),
            @ApiResponse(code = 404, message = "Diet no encontrado")
    })
    public ResponseEntity<Diet> updateDiet(@PathVariable("id") Integer id, @Valid @RequestBody Diet diet){
        try {
            Optional<Diet> dietOptional = dietService.getById(id);
            if(!dietOptional.isPresent())
                return new ResponseEntity<Diet>(HttpStatus.NOT_FOUND);
            diet.setId(id);
            dietService.save(diet);
            return new ResponseEntity<Diet>(diet,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<Diet>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Eliminación de un Diet", notes = "Método para eliminar un Diet")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Diet eliminado"),
            @ApiResponse(code = 404, message = "Diet no encontrado")
    })
    public ResponseEntity<Diet> deleteDiet(@PathVariable("id") Integer id){
        try {
            Optional<Diet> dietOptional = dietService.getById(id);
            if(!dietOptional.isPresent())
                return new ResponseEntity<Diet>(HttpStatus.NOT_FOUND);
            dietService.delete(id);
            return new ResponseEntity<Diet>(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<Diet>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/{recipe_id}/{diet_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Adición de Recipe a Diet", notes = "Método que añade una Recipe favorita a un Diet")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Recipe añadida a Diet"),
            @ApiResponse(code = 404, message = "Recipe o Diet no encontrado")
    })
    public ResponseEntity<DietRecipes> addRecipeToDiet(@PathVariable("recipe_id") Integer recipe_id,
                                                       @PathVariable("diet_id") Integer diet_id){
        try {
            Recipe foundRecipe = template.getForObject("http://localhost:8989/recipe/{recipe_id}", Recipe.class, recipe_id);
            Optional<Diet> foundDiet = dietService.getById(diet_id);
            if(foundRecipe == null)
                return new ResponseEntity<DietRecipes>(HttpStatus.NOT_FOUND);
            if(!foundDiet.isPresent())
                return new ResponseEntity<DietRecipes>(HttpStatus.NOT_FOUND);
            //DietRecipesFK newFKS = new DietRecipesFK(diet_id, recipe_id);
            DietRecipes dietRecipes = new DietRecipes();
            dietRecipes.setDiet(foundDiet.get());
            dietRecipes.setRecipeId(foundRecipe.getId());

            dietRecipesRepository.save(dietRecipes);
            return ResponseEntity.status(HttpStatus.CREATED).body(dietRecipes);
        }catch (Exception e){
            return new ResponseEntity<DietRecipes>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/{recipe_id}/{diet_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Eliminación de un Recipe de un Diet", notes = "Método para eliminar un Recipe de un Diet")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Recipe eliminado"),
            @ApiResponse(code = 404, message = "Recipe no encontrado")
    })
    public ResponseEntity<Diet> deleteRecipeFromDiet(@PathVariable("recipe_id") Integer recipe_id,
                                                     @PathVariable("diet_id") Integer diet_id)
    {
        try{
            //Optional<DietRecipes> dietRecipes = dietRecipesRepository.findById(newFKS);
            DietRecipes dietRecipes = new DietRecipes();

            Optional<Diet> foundDiet = dietService.getById(diet_id);
            dietRecipes.setDiet(foundDiet.get());

            if(!foundDiet.isPresent())
                return new ResponseEntity<Diet>(HttpStatus.NOT_FOUND);

            Recipe foundRecipe = template.getForObject("http://localhost:8989/recipe/{recipe_id}", Recipe.class, recipe_id);
            dietRecipes.setRecipeId(foundRecipe.getId());

            if(foundRecipe == null)
                return new ResponseEntity<Diet>(HttpStatus.NOT_FOUND);

            if(dietRecipes == null)
                return new ResponseEntity<Diet>(HttpStatus.NOT_FOUND);

            Integer dietRecipeToDeleteId = dietRecipesRepository.findByDietRecipe(dietRecipes.getRecipeId(), dietRecipes.getDiet().getId());

            dietRecipesRepository.deleteById(dietRecipeToDeleteId);
            return new ResponseEntity<Diet>(HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<Diet>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}