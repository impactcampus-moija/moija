package impact.moija.repository;

import impact.moija.domain.community.Category;
import impact.moija.domain.community.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("select p from Post p " +
            "where (:category is null or p.category = :category) " +
            "and (:keyword is null or p.title like %:keyword% or p.content like %:keyword%)")
    Page<Post> findAllByFilters(
            @Param("category") Category category,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    @Query("select count(p) from Post p " +
            "join PostRecommendation pr " +
            "on p = pr.post " +
            "where p.id = :postId")
    long countRecommendation(@Param("postId") Long postId);
}
