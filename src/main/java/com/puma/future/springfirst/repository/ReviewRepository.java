package com.puma.future.springfirst.repository;

import com.puma.future.springfirst.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByPokemonId(int pokemonId);
    List<Review> findByTitle(String title);
}
