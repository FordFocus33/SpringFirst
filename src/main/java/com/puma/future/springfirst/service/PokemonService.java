package com.puma.future.springfirst.service;

import com.puma.future.springfirst.dto.PokemonDto;
import com.puma.future.springfirst.dto.PokemonResponse;

public interface PokemonService {
    PokemonDto createPokemon(PokemonDto pokemonDto);
    PokemonResponse getAllPokemon(int page, int size);
    PokemonDto getPokemonById(int id);
    PokemonDto updatePokemon(PokemonDto pokemonDto, int id);

    void deletePokemon(int id);
}
