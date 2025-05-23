package kotlin_spring.board

import jakarta.persistence.EntityManager
import kotlin_spring.board.domain.entity.AuthorInfo
import kotlin_spring.board.domain.entity.Post
import kotlin_spring.board.domain.repository.PostRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.springframework.data.domain.PageRequest

@DataJpaTest
class PostRepositoryTest {

    @Autowired
    private lateinit var postRepository: PostRepository

    @Autowired
    private lateinit var em: EntityManager

    @BeforeEach
    fun setup() {
        // 테스트 데이터
        val authorInfo1 = AuthorInfo("작성자1", "123")
        val authorInfo2 = AuthorInfo("작성자2", "123")
        val authorInfo3 = AuthorInfo("작성자3", "123")

        val post1 = Post(authorInfo1, "게시글1", "내용1")
        val post2 = Post(authorInfo2, "게시글2", "내용2")
        val post3 = Post(authorInfo3, "인기 게시글", "내용3")

        // post3 (인기 게시글) 조회수 증가
        post3.incrementViewCount()
        post3.incrementViewCount()

        postRepository.save(post1)
        postRepository.save(post2)
        postRepository.save(post3)

        em.flush()
        em.clear()

    }

    @Test
    fun `게시글 저장 테스트`() {
        // given
        val authorInfo = AuthorInfo("Danny", "password123")
        val post = Post(authorInfo, "테스트 제목", "테스트 내용")

        // when
        val savedPost = postRepository.save(post)
        em.flush()
        em.clear()

        // then
        val foundPost = postRepository.findById(savedPost.id!!).orElseThrow()
        assertThat(foundPost.title).isEqualTo("테스트 제목")
        assertThat(foundPost.content).isEqualTo("테스트 내용")
        assertThat(foundPost.authorInfo.nickname).isEqualTo("Danny")
        assertThat(foundPost.createdAt).isNotNull()
        assertThat(foundPost.updatedAt).isNotNull()
    }

    @Test
    fun `게시글 조회 테스트`() {
        // given
        val authorInfo = AuthorInfo("Danny", "password123")
        val post1 = Post(authorInfo, "게시글1", "내용1")
        val post2 = Post(authorInfo, "게시글2", "내용2")

        postRepository.save(post1)
        postRepository.save(post2)

        em.flush()
        em.clear()

        // when
        val posts = postRepository.findAll()

        // then
        assertThat(posts).hasSize(2)
        assertThat(posts.map { it.title }).containsExactlyInAnyOrder("게시글1", "게시글2")
    }

    @Test
    fun `게시글 수정 테스트`() {
        // given
        val authorInfo = AuthorInfo("Danny", "password123")
        val post = Post(authorInfo, "원본 제목", "원본 내용")

        val savedPost = postRepository.save(post)
        em.flush()
        em.clear()

        // when
        val foundPost = postRepository.findById(savedPost.id!!).orElseThrow()
        foundPost.title = "수정된 제목"
        foundPost.content = "수정된 내용"

        // postRepository.save(foundPost) 변경감지로 필요 없음!?
        em.flush()
        em.clear()

        // then
        val updatedPost = postRepository.findById(savedPost.id!!).orElseThrow()
        assertThat(updatedPost.title).isEqualTo("수정된 제목")
        assertThat(updatedPost.content).isEqualTo("수정된 내용")
        assertThat(updatedPost.updatedAt).isNotNull()
    }

    @Test
    fun `게시글 삭제 테스트`() {
        // given
        val authorInfo = AuthorInfo("Danny", "password123")
        val post = Post(authorInfo, "삭제될 게시글", "내용")

        val savedPost = postRepository.save(post)
        em.flush()
        em.clear()

        // when
        postRepository.deleteById(savedPost.id!!)
        em.flush()
        em.clear()

        // then
        val exists = postRepository.existsById(savedPost.id!!)
        assertThat(exists).isFalse()
    }

    @Test
    fun `최신순으로 게시글 조회 테스트`() {
        // when
        val pageable = PageRequest.of(0, 10)
        val result = postRepository.findAllByOrderByCreatedAtDesc(pageable)
        val posts = result.content

        // then
        assertThat(posts).hasSize(3)

        // 정렬 검증
        for (i in 0 until posts.size - 1) {
            assertThat(posts[i].createdAt).isAfterOrEqualTo(posts[i + 1].createdAt)
        }

        // 로깅 확인
        println("게시글 정렬 순서:")
        posts.forEachIndexed({ index, post ->
            println("$index: ${post.title}, 생성일: ${post.createdAt}")
        })
    }

    @Test
    fun `조회수 기준 인기 게시글 조회 테스트()`() {
        // when
        val pageable = PageRequest.of(0, 10)
        val result = postRepository.findAllByOrderByViewCountDesc(pageable)
        val posts = result.content

        // then
        assertThat(posts).hasSize(3)
        assertThat(posts[0].title).isEqualTo("인기 게시글")
        assertThat(posts[0].viewCount).isEqualTo(2)
    }

    @Test
    fun `게시글 제목 검색 테스트`() {
        // when
        val pageable = PageRequest.of(0, 10)
        val result = postRepository.findByTitleContainingOrderByCreatedAtDesc("인기", pageable)
        val posts = result.content

        // then
        assertThat(posts).hasSize(1)
        assertThat(posts[0].title).isEqualTo("인기 게시글")
    }
}