package kotlin_spring.board.controller

import kotlin_spring.board.domain.entity.AuthorInfo
import kotlin_spring.board.domain.entity.Post
import kotlin_spring.board.dto.PostCreateDto
import kotlin_spring.board.dto.PostUpdateDto
import kotlin_spring.board.service.CommentService
import kotlin_spring.board.service.PostService
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("/posts")
class PostController(
    private val postService: PostService,
    private val commentService: CommentService,
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
    @GetMapping("/{id}")
    fun getPost(@PathVariable id: Long, model: Model): String {
        val post = postService.getPost(id)
        val comments = commentService.getCommentByPostId(id)

        model.addAttribute("post", post)
        model.addAttribute("comments", comments)

        return "posts/detail"
    }

    // 게시글 작성 폼
    @GetMapping("/new")
    fun newPostForm(model: Model): String {
        model.addAttribute("post", PostCreateDto("", "", "", ""))
        return "posts/form"
    }

    // 게시글 생성 처리
    @PostMapping
    fun createPost(
        @ModelAttribute postDto: PostCreateDto,
        redirectAttributes: RedirectAttributes,
    ): String {
        val post = Post(
            authorInfo = AuthorInfo(postDto.author, postDto.password),
            title = postDto.title,
            content = postDto.content
        )

        val savedPost = postService.savePost(post)
        redirectAttributes.addFlashAttribute("message", "게시글이 저장되었습니다.")

        return "redirect:/posts/${savedPost.id}"
    }

    // 게시글 수정 폼
    @GetMapping("/{id}/edit")
    fun editPostForm(@PathVariable id: Long, model: Model): String {
        val post = postService.getPostWithoutViewIncrement(id)

        model.addAttribute("post", PostUpdateDto(
            id = post.id ?: 0L,
            title = post.title,
            content = post.content,
            password = ""
        ))

        return "posts/edit"
    }

    // 게시글 수정 처리
    @PostMapping("/{id}")
    fun updatePost(
        @PathVariable id: Long,
        @ModelAttribute postDto: PostUpdateDto,
        redirectAttributes: RedirectAttributes,
    ): String {
        try {
            postService.updatePost(id, postDto)
            redirectAttributes.addFlashAttribute("message", "게시글이 수정되었습니다.")
        } catch (e: IllegalArgumentException) {
            redirectAttributes.addFlashAttribute("error", e.message)
            return "redirect:/posts/${id}"
        }

        return "redirect:/posts/${id}"
    }

    // 게시글 삭제
    @PostMapping("/{id}/delete")
    fun deletePost(
        @PathVariable id: Long,
        @RequestParam password: String,
        redirectAttributes: RedirectAttributes
    ): String {
        try {
            postService.deletePost(id, password)
            redirectAttributes.addFlashAttribute("message", "게시글이 삭제되었습니다.")
        } catch (e: IllegalArgumentException) {
            redirectAttributes.addFlashAttribute("error", e.message)
            return "redirect:/posts/${id}"
        }

        return "redirect:/posts"
    }
}