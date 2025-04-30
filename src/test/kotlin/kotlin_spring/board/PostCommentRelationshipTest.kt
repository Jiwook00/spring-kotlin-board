package kotlin_spring.board

import jakarta.persistence.EntityManager
import kotlin_spring.board.domain.entity.AuthorInfo
import kotlin_spring.board.domain.entity.Comment
import kotlin_spring.board.domain.entity.Post
import kotlin_spring.board.domain.repository.PostRepository
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.junit.jupiter.api.Test

@DataJpaTest
class PostCommentRelationshipTest {

    @Autowired
    private lateinit var postRepository: PostRepository

    @Autowired
    private lateinit var em: EntityManager

    @Test
    fun `저장된 게시글에 댓글 추가 테스트`() {
        // given
        val postAuthor = AuthorInfo("게시자", "password123")
        val post = Post(postAuthor, "게시글", "게시글 내용")

        val savedPost = postRepository.save(post)
        em.flush()
        em.clear()

        // when
        // 저장된 게시글 조회
        val foundPost = postRepository.findById(savedPost.id!!).orElseThrow()

        // 댓글 생성 및 추가
        val commentAuthor = AuthorInfo("댓글작성자", "comment123")
        val comment = Comment(commentAuthor, "댓글 내용", post)

        // 연관관계 편의 메서드 사용
        foundPost.addComment(comment)

        em.flush()
        em.clear()

        // then
        val updatedPost = postRepository.findById(savedPost.id!!).orElseThrow()
        assertThat(updatedPost.comments).hasSize(1)
        assertThat(updatedPost.comments[0].content).isEqualTo("댓글 내용")

    }
}