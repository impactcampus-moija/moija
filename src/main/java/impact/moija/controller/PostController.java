package impact.moija.controller;

import impact.moija.api.BaseResponse;
import impact.moija.dto.common.PageResponse;
import impact.moija.dto.common.PkResponseDto;
import impact.moija.dto.common.RecommendationResponseDto;
import impact.moija.dto.community.PostDetailResponseDto;
import impact.moija.dto.community.PostPageResponseDto;
import impact.moija.dto.community.PostRequestDto;
import impact.moija.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@PreAuthorize("hasAnyRole('USER')")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public BaseResponse<PkResponseDto> createPost(@RequestBody PostRequestDto postRequestDto) {
        PkResponseDto postId = postService.createPost(postRequestDto);
        return BaseResponse.created(postId);
    }

    @GetMapping
    public BaseResponse<PageResponse<PostPageResponseDto>> getAllPosts(
            @RequestParam(required = false, defaultValue = "") String categoryFilter,
            @RequestParam(required = false) String keywordFilter,
            @PageableDefault(size = 12) Pageable pageable
    ) {
        return BaseResponse.ok(postService.getAllPosts(categoryFilter, keywordFilter, pageable));
    }

    @GetMapping("/{postId}")
    public BaseResponse<PostDetailResponseDto> getPostById(@PathVariable Long postId) {
        PostDetailResponseDto postDetail = postService.getPostById(postId);
        return BaseResponse.ok(postDetail);
    }

    @PutMapping("/{postId}")
    public BaseResponse<PkResponseDto> updatePost(
            @PathVariable Long postId,
            @RequestBody PostRequestDto postRequestDto
    ) {
        PkResponseDto updatedPostId = postService.updatePost(postId, postRequestDto);
        return BaseResponse.ok(updatedPostId);
    }

    @DeleteMapping("/{postId}")
    public BaseResponse<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return BaseResponse.ok();
    }

    @PostMapping("{postId}/recommendations")
    public BaseResponse<RecommendationResponseDto> likePost(@PathVariable Long postId) {
        return BaseResponse.ok(postService.likePost(postId));
    }
}
