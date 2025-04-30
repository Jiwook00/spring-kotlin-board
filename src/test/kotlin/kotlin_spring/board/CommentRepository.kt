package kotlin_spring.board

import kotlin_spring.board.domain.entity.AuthorInfo
import kotlin_spring.board.domain.entity.Comment
import kotlin_spring.board.domain.entity.Post
import kotlin_spring.board.domain.repository.CommentRepository
import kotlin_spring.board.domain.repository.PostRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DataJpaTest
class CommentRepository {

    @Autowired
    private lateinit var commentRepository: CommentRepository

    @Autowired
    private lateinit var postRepository: PostRepository

    @Autowired
    private lateinit var em: TestEntityManager

    @Test
    fun `댓글 저장 테스트`() {
        // given
        val authorInfo = AuthorInfo("Danny", "password123")
        val post = Post(authorInfo, "게시글 제목", "게시글 내용")
        val savedPost = postRepository.save(post)

        val commentAuthor = AuthorInfo("Danny", "password123")
        val comment = Comment(commentAuthor, "댓글 내용", savedPost)

        // when
        val savedComment = commentRepository.save(comment)
        em.flush()
        em.clear()

        // then
        val foundComment = commentRepository.findById(savedComment.id!!).orElseThrow()
        assertThat(foundComment.content).isEqualTo("댓글 내용")
        assertThat(foundComment.author.nickname).isEqualTo("Danny")
        assertThat(foundComment.post.id).isEqualTo(savedPost.id)
        assertThat(foundComment.createdAt).isNotNull()
    }

    @Test
    fun `findByPostId 테스트`() {
        // given
        val postAuthor = AuthorInfo("게시자", "password123")
        val post1 = Post(postAuthor, "게시글1", "내용1")
        val post2 = Post(postAuthor, "게시글2", "내용2")

        val savedPost1 = postRepository.save(post1)
        val savedPost2 = postRepository.save(post2)

        val commentAuthor = AuthorInfo("댓글러", "password123")

        // post1에 댓글 2개 추가
        commentRepository.save(Comment(commentAuthor, "게시글1의 댓글1", savedPost1))
        commentRepository.save(Comment(commentAuthor, "게시글1의 댓글2", savedPost1))

        // post2에 댓글 1개 추가
        commentRepository.save(Comment(commentAuthor, "게시글2의 댓글1", savedPost2))

        em.flush()
        em.clear()

        // when
        val post1Comments = commentRepository.findByPostId(savedPost1.id!!)
        val post2Comments = commentRepository.findByPostId(savedPost2.id!!)

        // then
        // post1 테스트
        assertThat(post1Comments).hasSize(2)
        assertThat(post1Comments.map { it.content }).containsExactlyInAnyOrder("게시글1의 댓글1", "게시글1의 댓글2")

        // post2 테스트
        assertThat(post2Comments).hasSize(1)
        assertThat(post2Comments[0].content).isEqualTo("게시글2의 댓글1")
    }

    @Test
    fun `댓글 삭제 테스트`() {
        // given
        val postAuthor = AuthorInfo("게시자", "password123")
        val post = Post(postAuthor, "게시글 제목", "게시글 내용")
        val savedPost = postRepository.save(post)

        val commentAuthor = AuthorInfo("댓글작성자", "comment123")
        val comment = Comment(commentAuthor, "댓글 내용", savedPost)

        val savedComment = commentRepository.save(comment)

        // when
        commentRepository.deleteById(comment.id!!)
        em.flush()
        em.clear()

        // then
        val commentExists = commentRepository.existsById(savedComment.id!!)
        assertThat(commentExists).isFalse()

        // 게시글 존재 여부 확인
        val postExists = postRepository.existsById(savedPost.id!!)
        assertThat(postExists).isTrue()
    }

}