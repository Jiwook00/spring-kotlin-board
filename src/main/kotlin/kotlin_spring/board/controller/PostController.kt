package kotlin_spring.board.controller

import kotlin_spring.board.service.PostService
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/posts")
class PostController(
    private val postService: PostService,
) {

    // 게시글 목록 페이지
    @GetMapping
    fun listPosts(
        model: Model,
        @RequestParam(required = false) searchKeyword: String?,
        @RequestParam(required = false, defaultValue = "created_at") sortBy: String,
        @PageableDefault(size = 10, sort = ["createdAt"], direction = Sort.Direction.DESC) pageable: Pageable
    ): String {
        val posts = when {
            !searchKeyword.isNullOrBlank() -> postService.searchPostsByTitle(searchKeyword, pageable)
            sortBy == "views" -> postService.getPopularPosts(pageable)
            else -> postService.getPosts(pageable)
        }

        model.addAttribute("posts", posts)
        model.addAttribute("searchKeyword", searchKeyword ?: "")
        model.addAttribute("sortOption", sortBy)
        model.addAttribute("currentPage", posts.number + 1)
        model.addAttribute("totalPages", posts.totalPages)
        model.addAttribute("totalElements", posts.totalElements)

        return "posts/list"
    }

    // 게시글 상세 페이지

    // 게시글 작성 폼

    // 게시글 생성 처리

    // 게시글 수정 폼

    // 게시글 수정 처리

    // 게시글 삭제

}