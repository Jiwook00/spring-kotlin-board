package kotlin_spring.board.service

import kotlin_spring.board.domain.entity.Comment
import kotlin_spring.board.domain.repository.CommentRepository
import kotlin_spring.board.dto.CommentDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CommentService(private val commentRepository: CommentRepository) {

    fun getCommentByPostId(postId: Long): List<CommentDto> {
        return commentRepository.findByPostId(postId).map { CommentDto.from(it) }
    }

    @Transactional
    fun saveComment(comment: Comment): Comment {
        return commentRepository.save(comment)
    }

    @Transactional
    fun deleteComment(id: Long, password: String) {
        val comment = commentRepository.findById(id).orElseThrow {
            NoSuchElementException("댓글을 찾을 수 없습니다: $id")
        }

        if (comment.author.password != password) {
            throw IllegalArgumentException("비밀번호가 일치하지 않습니다.")
        }

        commentRepository.deleteById(id)
    }
}