package kotlin_spring.board.controller

import kotlin_spring.board.domain.entity.AuthorInfo
import kotlin_spring.board.domain.entity.Comment
import kotlin_spring.board.dto.CommentCreateDto
import kotlin_spring.board.service.CommentService
import kotlin_spring.board.service.PostService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("/comments")
class CommentController(
    private val commentService: CommentService,
    private val postService: PostService,
) {

    @PostMapping
    fun addComment(
        @ModelAttribute commentDto: CommentCreateDto,
        redirectAttributes: RedirectAttributes,
    ): String {
        try {
            val post = postService.getPostWithoutViewIncrement(commentDto.postId)

            val comment = Comment(
                author = AuthorInfo(commentDto.author, commentDto.password),
                content = commentDto.content,
                post = post
            )

            commentService.saveComment(comment)
            redirectAttributes.addFlashAttribute("message", "댓글이 등록되었습니다.")
        } catch (e: Exception) {
            redirectAttributes.addFlashAttribute("error", "댓글 등록에 실패했습니다.")
        }

        return "redirect:/posts/${commentDto.postId}"
    }
}