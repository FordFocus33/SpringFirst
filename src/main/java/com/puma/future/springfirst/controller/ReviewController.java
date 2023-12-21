package com.puma.future.springfirst.controller;

import com.puma.future.springfirst.dto.ReviewDto;
import com.puma.future.springfirst.service.ReviewServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ReviewController {

    private ReviewServiceImpl reviewService;

    @Autowired
    public ReviewController(ReviewServiceImpl reviewServiceImpl) {
        this.reviewService = reviewServiceImpl;
    }

    @PostMapping("/pokemon/{id}/review")
    public ResponseEntity<ReviewDto> createReview(@PathVariable(value = "id") int id,
                                                  @RequestBody ReviewDto reviewDto){
        return new ResponseEntity<>(reviewService.createReview(id, reviewDto), HttpStatus.CREATED);
    }

    @GetMapping("/pokemon/{id}/reviews")
    public List<ReviewDto> getReviewsByPokemonId(@PathVariable(value = "id") int id){
        return reviewService.getReviewsByPokemonId(id);
    }

    @GetMapping("/pokemon/{id}/reviews/{reviewId}")
    public ResponseEntity<ReviewDto> getReviewById(@PathVariable(value = "id") int id, @PathVariable(value = "reviewId") int reviewId){
        ReviewDto reviewDto = reviewService.getReviewById(reviewId, id);
        return new ResponseEntity<>(reviewDto, HttpStatus.OK);
    }

    @PutMapping("/pokemon/{pokemonId}/reviews/{reviewId}")
    private ResponseEntity<ReviewDto> updatedReview(@PathVariable(value = "pokemonId") int pokemonId,
                                                    @PathVariable(value = "reviewId") int reviewId,
                                                    @RequestBody ReviewDto reviewDto){
        ReviewDto updatedReviewDto = reviewService.updateReview(pokemonId, reviewId, reviewDto);
        return new ResponseEntity<>(updatedReviewDto, HttpStatus.OK);
    }

    @DeleteMapping("/pokemon/{pokemonId}/reviews/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable(value = "pokemonId") int pokemonId, @PathVariable(value = "reviewId") int reviewId){
        reviewService.deleteReview(pokemonId, reviewId);
        return new ResponseEntity<>("Ревью c ид " + reviewId + " у покемона с ид " + pokemonId + " удалено!", HttpStatus.OK);
    }
}
