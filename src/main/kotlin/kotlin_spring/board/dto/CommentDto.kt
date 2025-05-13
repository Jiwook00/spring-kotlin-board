package kotlin_spring.board.dto

import kotlin_spring.board.domain.entity.Comment
import java.time.format.DateTimeFormatter

data class CommentDto(
    val id: Long,
    val content: String,
    val author: String,
    val createdAt: String
) {
    companion object {
        fun from(comment: Comment): CommentDto {
            return CommentDto(
                id = comment.id ?: 0L,
                content = comment.content,
                author = comment.author.nickname,
                createdAt = comment.createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            )
        }
    }
}

data class CommentCreateDto(
    val postId: Long,
    val content: String,
    val author: String,
    val password: String
)