package com.puma.future.springfirst.sevice;

import com.puma.future.springfirst.dto.PokemonDto;
import com.puma.future.springfirst.dto.ReviewDto;
import com.puma.future.springfirst.model.Pokemon;
import com.puma.future.springfirst.model.Review;
import com.puma.future.springfirst.repository.PokemonRepository;
import com.puma.future.springfirst.repository.ReviewRepository;
import com.puma.future.springfirst.service.ReviewServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private PokemonRepository pokemonRepository;
    @InjectMocks
    private ReviewServiceImpl reviewService;

    private Pokemon pokemon;
    private Review review;
    private ReviewDto reviewDto;
    private PokemonDto pokemonDto;

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

    @Test
    public void ReviewService_CreateReview_ReturnsDto(){
        when(pokemonRepository.findById(pokemon.getId())).thenReturn(Optional.of(pokemon));
        when(reviewRepository.save(Mockito.any(Review.class))).thenReturn(review);

        ReviewDto savedReview = reviewService.createReview(pokemonDto.getId(), reviewDto);

        Assertions.assertThat(savedReview).isNotNull();
    }

    @Test
    public void ReviewService_FindById_ReturnReviewDto(){
        int reviewId = 1;

        when(reviewRepository.findByPokemonId(reviewId)).thenReturn(Arrays.asList(review));

        List<ReviewDto> pokemonReturn = reviewService.getReviewsByPokemonId(reviewId);

        Assertions.assertThat(pokemonReturn).isNotNull();
    }

    @Test
    public void ReviewService_GetReviewById_ReturnReviewDTo(){
        int reviewId = 1;
        int pokemonId = 1;

        review.setPokemon(pokemon);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(pokemonRepository.findById(pokemonId)).thenReturn(Optional.of(pokemon));

        ReviewDto reviewReturn = reviewService.getReviewById(reviewId, pokemonId);

        Assertions.assertThat(reviewReturn).isNotNull();
    }

    @Test
    public void ReviewService_UpdatePokemon_ReturnReviewDto(){
        int pokemonId = 1;
        int reviewId = 1;

        pokemon.setReviews(Arrays.asList(review));
        review.setPokemon(pokemon);

        when(pokemonRepository.findById(pokemonId)).thenReturn(Optional.of(pokemon));
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(reviewRepository.save(review)).thenReturn(review);

        ReviewDto updateReturn = reviewService.updateReview(pokemonId, reviewId, reviewDto);

        Assertions.assertThat(updateReturn).isNotNull();
    }

    @Test
    public void ReviewService_DeletePokemonById_ReturnVoid(){
        int pokemonId = 1;
        int reviewId = 1;

        pokemon.setReviews(Arrays.asList(review));
        review.setPokemon(pokemon);

        when(pokemonRepository.findById(pokemonId)).thenReturn(Optional.of(pokemon));
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        assertAll(() -> reviewService.deleteReview(pokemonId, reviewId));

    }
}
