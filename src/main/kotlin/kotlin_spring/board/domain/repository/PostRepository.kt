package kotlin_spring.board.domain.repository

import kotlin_spring.board.domain.entity.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository : JpaRepository<Post, Long> {
}