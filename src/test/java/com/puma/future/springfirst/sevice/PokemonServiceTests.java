package com.puma.future.springfirst.sevice;

import com.puma.future.springfirst.dto.PokemonDto;
import com.puma.future.springfirst.dto.PokemonResponse;
import com.puma.future.springfirst.model.Pokemon;
import com.puma.future.springfirst.repository.PokemonRepository;
import com.puma.future.springfirst.service.PokemonServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PokemonServiceTests {

    //  аннотация, которая создает заглушку (mock) для зависимости. В данном случае, аннотация @Mock создает мок-объект для зависимости PokemonRepository.
    //  Мок-объект используется для имитации поведения реального объекта и позволяет настроить его поведение в тестах.
    @Mock
    private PokemonRepository pokemonRepository;

    // @InjectMocks - аннотация, которая создает экземпляр класса и автоматически внедряет в него все созданные моки.
    // В данном случае, аннотация @InjectMocks создает экземпляр класса PokemonServiceImpl и автоматически внедряет в него мок-объект PokemonRepository.
    @InjectMocks
    private PokemonServiceImpl pokemonService;

    @Test
    public void PokemonService_CreatePokemon_ReturnsPokemonDto(){
        Pokemon pokemon = Pokemon.builder()
                .name("Name")
                .type("Type")
                .build();

        PokemonDto pokemonDto = PokemonDto.builder()
                .name("Name")
                .type("Type")
                .build();

        // Настройка мок-объекта при помощи when() осуществляется с помощью статического метода when() из класса Mockito.
        // Метод when() позволяет настроить поведение мок-объекта при вызове определенного метода с определенными аргументами.
        // В данном случае, вызов метода when(pokemonRepository.save(Mockito.any(Pokemon.class))).thenReturn(pokemon) указывает,
        // что при вызове метода save у мок-объекта pokemonRepository с любым аргументом типа Pokemon, он должен вернуть созданный объект Pokemon.
        // Это позволяет имитировать сохранение объекта в репозиторий и контролировать возвращаемое значение в тесте.
        when(pokemonRepository.save(Mockito.any(Pokemon.class))).thenReturn(pokemon);

        PokemonDto savedPokemon = pokemonService.createPokemon(pokemonDto);

        Assertions.assertThat(savedPokemon).isNotNull();
    }

    @Test
    public void PokemonService_GetAllPokemons_ReturnResponseDto(){
        Page<Pokemon> pokemons = Mockito.mock(Page.class);

        when(pokemonRepository.findAll(Mockito.any(Pageable.class))).thenReturn(pokemons);

        PokemonResponse savePokemon = pokemonService.getAllPokemon(1, 10);

        Assertions.assertThat(savePokemon).isNotNull();
    }

    @Test
    public void PokemonService_GetPokemonById_ReturnPokemonDto(){
        Pokemon pokemon = Pokemon.builder()
                .name("Name")
                .type("Type")
                .build();

        when(pokemonRepository.findById(1)).thenReturn(Optional.ofNullable(pokemon));

        PokemonDto savedPokemon = pokemonService.getPokemonById(1);

        Assertions.assertThat(savedPokemon).isNotNull();
    }

    @Test
    public void PokemonService_UpdatePokemon_ReturnPokemonDto(){
        Pokemon pokemon = Pokemon.builder()
                .name("Name")
                .type("Type")
                .build();
        PokemonDto pokemonDto = PokemonDto.builder()
                .name("Name")
                .type("Type")
                .build();

        // ofNullable Значит, что если мы вернем null, метод не выкинет исключение
        when(pokemonRepository.findById(1)).thenReturn(Optional.ofNullable(pokemon));
        when(pokemonRepository.save(Mockito.any(Pokemon.class))).thenReturn(pokemon);

        PokemonDto savedPokemon = pokemonService.updatePokemon(pokemonDto, 1);

        Assertions.assertThat(savedPokemon).isNotNull();

    }

    //В этом коде написан тест для метода `deletePokemon` в классе `PokemonService`.
    //
    //1. Сначала создается объект `pokemon` с именем "Name" и типом "Type".
    //2. Затем используется метод `when` из библиотеки Mockito для создания заглушки репозитория.
    // Мы говорим, что если метод `findById` репозитория вызывается с аргументом 1, то должен быть возвращен Optional, содержащий объект `pokemon`.
    //
    //3. Затем мы используем `assertAll` из библиотеки `JUnit`, чтобы проверить, что метод `deletePokemon` вызывается без ошибок и без возвращаемого значения.
    //
    //В целом, этот тест проверяет, что метод `deletePokemon` правильно вызывает метод `findById` репозитория и успешно выполняется без ошибок

    @Test
    public void PokemonService_DeletePokemonById_ReturnPokemonDto(){
        Pokemon pokemon = Pokemon.builder()
                .name("Name")
                .type("Type")
                .build();

        when(pokemonRepository.findById(1)).thenReturn(Optional.ofNullable(pokemon));

        assertAll(() -> pokemonService.deletePokemon(1));
    }
}
