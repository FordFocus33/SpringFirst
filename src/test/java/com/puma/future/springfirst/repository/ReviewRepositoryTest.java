package com.puma.future.springfirst.repository;

import com.puma.future.springfirst.model.Review;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ReviewRepositoryTest {

    private ReviewRepository reviewRepository;

    @Autowired
    public ReviewRepositoryTest(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Test
    public void ReviewRepositoryTest_SaveAll_ReturnsSaveReview(){
        Review review = Review.builder()
                .title("First review")
                .content("Describe my opinion")
                .stars(5)
                .build();

        Review saveReview = reviewRepository.save(review);

        Assertions.assertThat(saveReview).isNotNull();
        Assertions.assertThat(saveReview.getId()).isGreaterThan(0);
    }

    @Test
    public void ReviewRepository_GetAll_ReturnsMoreThenOneReview(){
        Review review = Review.builder()
                .title("First review")
                .content("Describe my opinion")
                .stars(5)
                .build();

        Review review2 = Review.builder()
                .title("Second review")
                .content("Describe my opinion in second time")
                .stars(4)
                .build();

        reviewRepository.save(review);
        reviewRepository.save(review2);

        List<Review> reviews = reviewRepository.findAll();

        Assertions.assertThat(reviews).isNotNull();
        Assertions.assertThat(reviews.size()).isEqualTo(2);
    }

    @Test
    public void ReviewRepositoryTest_FindById_ReturnsSaveReview(){
        Review review = Review.builder()
                .title("First review")
                .content("Describe my opinion")
                .stars(5)
                .build();

        Review saveReview = reviewRepository.save(review);

        Review reviewReturn = reviewRepository.findById(review.getId()).get();

        Assertions.assertThat(reviewReturn).isNotNull();
    }

    @Test
    public void ReviewRepositoryTest_UpdateReview_ReturnsReview(){
        Review review = Review.builder()
                .title("First review")
                .content("Describe my opinion")
                .stars(5)
                .build();

        reviewRepository.save(review);

        Review reviewSave = reviewRepository.findById(review.getId()).get();
        reviewSave.setTitle("title");
        reviewSave.setContent("Content");

        Review updatedPokemon = reviewRepository.save(reviewSave);

        Assertions.assertThat(updatedPokemon.getTitle()).isNotNull();
        Assertions.assertThat(updatedPokemon.getContent()).isNotNull();
    }

    @Test
    public void ReviewRepositoryTest_DeleteReview_ReturnsReviewIsEmpty(){
        Review review = Review.builder()
                .title("First review")
                .content("Describe my opinion")
                .stars(5)
                .build();

        reviewRepository.save(review);
        reviewRepository.deleteById(review.getId());

        Optional<Review> deletePokemon = reviewRepository.findById(review.getId());

        Assertions.assertThat(deletePokemon).isEmpty();
    }

}
