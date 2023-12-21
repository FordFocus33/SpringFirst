package com.puma.future.springfirst.controller;

import com.puma.future.springfirst.dto.PokemonDto;
import com.puma.future.springfirst.dto.PokemonResponse;
import com.puma.future.springfirst.service.PokemonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MainController {

    private PokemonService pokemonService;

    @Autowired
    public MainController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @GetMapping("/pokemon")
    public ResponseEntity<PokemonResponse> getPokemon(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size
    ){

        return new ResponseEntity<>(pokemonService.getAllPokemon(page, size), HttpStatus.OK);
    }

    @GetMapping("/pokemon/{id}")
    public ResponseEntity<PokemonDto> pokemonDetail(@PathVariable int id){

        return ResponseEntity.ok(pokemonService.getPokemonById(id));
    }

    @PostMapping("/pokemon/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PokemonDto> createPokemon(@RequestBody PokemonDto pokemonDto){
        return new ResponseEntity<>(pokemonService.createPokemon(pokemonDto), HttpStatus.CREATED);
    }

    @PutMapping("/pokemon/update/{id}")
    public ResponseEntity<PokemonDto> updatePokemon(@RequestBody PokemonDto pokemonDto, @PathVariable("id") int id){
        PokemonDto response = pokemonService.updatePokemon(pokemonDto, id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/pokemon/delete/{id}")
    public ResponseEntity<String> deletePokemon(@PathVariable("id") int id){
        pokemonService.deletePokemon(id);

        return new ResponseEntity<>("Pokemon delete", HttpStatus.OK);
    }
}
