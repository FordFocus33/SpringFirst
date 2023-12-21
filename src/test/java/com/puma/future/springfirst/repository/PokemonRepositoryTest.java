package com.puma.future.springfirst.repository;

import com.puma.future.springfirst.model.Pokemon;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PokemonRepositoryTest {

    @Autowired
    private PokemonRepository pokemonRepository;

    @Test
    public void PokemonRepository_SaveAll_ReturnSavedPokemon(){

        //Arrange
        Pokemon pokemon = Pokemon.builder()
                .name("pikachu")
                .type("electric")
                .build();
        //Act

        Pokemon savedPokemon = pokemonRepository.save(pokemon);

        //Assert

        Assertions.assertThat(savedPokemon).isNotNull();
        Assertions.assertThat(savedPokemon.getId()).isGreaterThan(0);
    }

    @Test
    public void PokemonRepository_FindById_ReturnPokemon(){
        Pokemon pokemon = Pokemon.builder()
                .name("pikachu")
                .type("electric")
                .build();

        pokemonRepository.save(pokemon);

        Pokemon pokemons = pokemonRepository.findById(pokemon.getId()).get();

        Assertions.assertThat(pokemons).isNotNull();
    }

    // Проверяем, что метод findByType реально возращает нам покемона по тайпу
    @Test
    public void PokemonRepository_FindByType_ReturnPokemonNotNull(){
        Pokemon pokemon = Pokemon.builder()
                .name("pikchu")
                .type("electric")
                .build();

        pokemonRepository.save(pokemon);

        Pokemon pokemons = pokemonRepository.findByType(pokemon.getType()).get();

        Assertions.assertThat(pokemons).isNotNull();
    }

    @Test
    public void PokemonRepository_UpdatePokemon_ReturnPokemonNotNull(){
        Pokemon pokemon = Pokemon.builder()
                .name("pikchu")
                .type("electric")
                .build();

        pokemonRepository.save(pokemon);

        Pokemon pokemons = pokemonRepository.findById(pokemon.getId()).get();
        pokemons.setType("Mechanic");
        pokemons.setName("Dodik");

        Pokemon updatedPokemon = pokemonRepository.save(pokemons);

        Assertions.assertThat(updatedPokemon.getName()).isNotNull();
        Assertions.assertThat(updatedPokemon.getType()).isNotNull();
    }

    @Test
    public void PokemonRepository_DeleteById_ReturnPokemonIsEmpty(){
        Pokemon pokemon = Pokemon.builder()
                .name("pikchu")
                .type("electric")
                .build();

        pokemonRepository.save(pokemon);

        pokemonRepository.deleteById(pokemon.getId());
        Optional<Pokemon> pokemonReturn = pokemonRepository.findById(pokemon.getId());

        Assertions.assertThat(pokemonReturn).isEmpty();
    }
}
