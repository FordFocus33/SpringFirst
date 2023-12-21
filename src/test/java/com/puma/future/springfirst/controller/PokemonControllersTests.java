package com.puma.future.springfirst.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.puma.future.springfirst.dto.PokemonDto;
import com.puma.future.springfirst.dto.PokemonResponse;
import com.puma.future.springfirst.dto.ReviewDto;
import com.puma.future.springfirst.model.Pokemon;
import com.puma.future.springfirst.model.Review;
import com.puma.future.springfirst.service.PokemonService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = MainController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class PokemonControllersTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PokemonService pokemonService;

    @Autowired
    private ObjectMapper objectMapper;

    private PokemonDto pokemonDto;
    private Pokemon pokemon;
    private ReviewDto reviewDto;
    private Review review;

    @BeforeEach
    public void init(){
        pokemon = Pokemon.builder()
                .name("Pokemon Name")
                .type("Pokemon Type")
                .build();

        pokemonDto = PokemonDto.builder()
                .name("PokemonDTO Name")
                .type("PokemonDTO Type")
                .build();

        review = Review.builder()
                .title("Review Title")
                .content("Review content")
                .stars(5)
                .build();

        reviewDto = ReviewDto.builder()
                .title("ReviewDto title")
                .content("ReviewDto content")
                .stars(5)
                .build();
    }

    //Этот код представляет тестовый метод для проверки функциональности создания покемона в контроллере.
    //
    //Первая строка кода использует метод given из библиотеки Mockito для настройки поведения мок-объекта pokemonService.
    // Он указывает, что при вызове метода createPokemon с любым аргументом, мок-объект должен вернуть этот аргумент.
    // Это позволяет имитировать создание покемона в сервисе и контролировать возвращаемое значение в тесте.
    //
    //Во второй строке кода выполняется запрос к контроллеру методом post на эндпоинт /api/pokemon/create.
    // В запросе указывается тип контента MediaType.APPLICATION_JSON и содержимое запроса, которое представляет собой сериализованный в JSON объект pokemonDto.
    //
    //Затем выполняется проверка результата запроса с помощью метода andExpect из библиотеки Spring MVC Test.
    // В данном случае проверяется, что статус ответа является HTTP 201 Created.
    //
    //Таким образом, данный код тестирует функциональность создания покемона в контроллере, используя мок-объект для имитации работы сервиса.

    @Test
    public void pokemonController_CreatePokemon_ReturnCreated() throws Exception{
        given(pokemonService.createPokemon(ArgumentMatchers.any())).willAnswer((invocation -> invocation.getArgument(0)));

        ResultActions response = mockMvc.perform(post("/api/pokemon/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pokemonDto)));
        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(pokemonDto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type", CoreMatchers.is(pokemonDto.getType())));
                //.andDo(MockMvcResultHandlers.print());
    }

    //Данный код представляет тестовый метод для проверки функциональности получения списка покемонов в контроллере.
    //
    //Первая строка кода создает объект responseDto, который представляет собой ответ сервера в формате PokemonResponse.
    // В данном случае, объект responseDto содержит информацию о странице (page), размере (size) и содержимом (content) списка покемонов.
    // Затем, с помощью метода when из библиотеки Mockito, настраивается поведение мок-объекта pokemonService.
    // Он указывает, что при вызове метода getAllPokemon с аргументами 1 и 10, мок-объект должен вернуть объект responseDto.
    // Это позволяет имитировать получение списка покемонов из сервиса и контролировать возвращаемое значение в тесте.
    //
    //Во второй строке кода выполняется запрос к контроллеру методом get на эндпоинт /api/pokemon.
    // В запросе указывается тип контента MediaType.APPLICATION_JSON и параметры page и size, которые указывают на номер страницы и размер страницы соответственно.
    //
    //Затем выполняется проверка результата запроса с помощью метода andExpect из библиотеки Spring MVC Test.
    // В данном случае проверяется, что статус ответа является HTTP 200 OK и количество элементов в поле content объекта JSON
    // соответствует размеру списка покемонов в объекте responseDto.
    //
    //Таким образом, данный код тестирует функциональность получения списка покемонов в контроллере,
    // используя мок-объект для имитации работы сервиса и проверку результата запроса.

    @Test
    public void PokemonController_GetAllPokemon_ReturnResponseDto() throws Exception{
        PokemonResponse responseDto = PokemonResponse.builder()
                .size(10).last(true)
                .page(1).content(Arrays.asList(pokemonDto))
                .build();
        when(pokemonService.getAllPokemon(1, 10)).thenReturn(responseDto);

        ResultActions response = mockMvc.perform(get("/api/pokemon")
                        .contentType(MediaType.APPLICATION_JSON)
                .param("page", "1")
                .param("size", "10"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.size()", CoreMatchers.is(responseDto.getContent().size())));

    }

    @Test
    public void PokemonController_PokemonDetail_ReturnPokemonDto() throws Exception {
        int pokemonId = 1;
        when(pokemonService.getPokemonById(1)).thenReturn(pokemonDto);

        ResultActions response = mockMvc.perform(get("/api/pokemon/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pokemonDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(pokemonDto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type", CoreMatchers.is(pokemonDto.getType())));

    }


    @Test
    public void PokemonController_PokemonUpdate_ReturnPokemonDto() throws Exception {
        int pokemonId = 1;
        when(pokemonService.updatePokemon(pokemonDto, pokemonId)).thenReturn(pokemonDto);

        ResultActions response = mockMvc.perform(put("/api/pokemon/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pokemonDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(pokemonDto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type", CoreMatchers.is(pokemonDto.getType())));

    }


    @Test
    public void PokemonController_DeletePokemon_ReturnString() throws Exception {
        int pokemonId = 1;
        doNothing().when(pokemonService).deletePokemon(pokemonId);

        ResultActions response = mockMvc.perform(delete("/api/pokemon/delete/1")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk());

    }
}
