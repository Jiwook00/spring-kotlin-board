package kotlin_spring.board

import kotlin_spring.board.domain.entity.AuthorInfo
import kotlin_spring.board.domain.entity.Post
import kotlin_spring.board.domain.repository.PostRepository
import kotlin_spring.board.service.PostService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.test.util.ReflectionTestUtils
import java.util.*
import kotlin.NoSuchElementException
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class PostServiceTest {

    @Mock
    private lateinit var postRepository: PostRepository

    @InjectMocks
    private lateinit var postService: PostService

    @Test
    fun `게시글 목록 조회 테스트`() {
        // given
        val pageable: Pageable = PageRequest.of(0, 10)
        val postAuthor = AuthorInfo("게시자", "password123")

        val post1 = Post(postAuthor, "게시글1", "게시글 내용1")
        ReflectionTestUtils.setField(post1, "id", 1L)

        val post2 = Post(postAuthor, "게시글2", "게시글 내용2")
        ReflectionTestUtils.setField(post2, "id", 2L)

        // 게시글 목록 생성
        val posts = listOf(post1, post2)

        // Page 객체 생성, Repository가 반환할 결과
        val postsPage = PageImpl<Post>(posts, pageable, posts.size.toLong())

        // Mock 객체 동작 설정
        `when`(postRepository.findAllByOrderByCreatedAtDesc(pageable)).thenReturn(postsPage)

        // when
        val result = postService.getPosts(pageable)

        // then
        assertEquals(2, result.content.size)
        assertEquals("게시글1", result.content[0].title)
        assertEquals("게시글2", result.content[1].title)
        verify(postRepository).findAllByOrderByCreatedAtDesc(pageable)
    }

    @Test
    fun `게시글 조회 및 조회수 증가 테스트`() {
        // given
        val postAuthor = AuthorInfo("게시자", "password123")
        val post = Post(postAuthor, "게시글", "게시글 내용")
        val initialViewCount = post.viewCount

        ReflectionTestUtils.setField(post, "id", 1L)

        `when`(postRepository.findById(1L)).thenReturn(Optional.of(post))

        // when
        val result = postService.getPost(1L)

        // then
        assertEquals(initialViewCount + 1, result.viewCount)
        verify(postRepository).findById(1L)
    }

    @Test
    fun `존재하지 않는 게시글 조회시 예외 테스트`() {
        // given
        `when`(postRepository.findById(99L)).thenReturn(Optional.empty())

        // then
        assertThrows<NoSuchElementException> { postService.getPost(99L) }
    }

    @Test
    fun `조회수 기준 인기 게시글 조회 테스트`() {
        // given
        val pageable: Pageable = PageRequest.of(0, 10)
        val postAuthor = AuthorInfo("게시자", "password123")

        // 인기 조회수 게시글 설정
        val popularPost = Post(postAuthor, "인기 게시글", "게시글 내용1")
        ReflectionTestUtils.setField(popularPost, "id", 1L)
        repeat(10) { popularPost.incrementViewCount() }

        // 일반 조회수 게시글 설정
        val normalPost = Post(postAuthor, "일반 게시글", "게시글 내용2")
        ReflectionTestUtils.setField(normalPost, "id", 2L)
        repeat(5) { normalPost.incrementViewCount() }

        // 조회수 높은 순으로 정렬된 리스트
        val posts = listOf(popularPost, normalPost)
        val postsPage = PageImpl<Post>(posts, pageable, posts.size.toLong())

        // Mock 설정
        `when`(postRepository.findAllByOrderByViewCountDesc(pageable)).thenReturn(postsPage)

        // when
        val result = postService.getPopularPosts(pageable)

        // then
        assertEquals(2, result.content.size)
        assertEquals("인기 게시글", result.content[0].title)
        assertEquals(10, result.content[0].viewCount)
        assertEquals("일반 게시글", result.content[1].title)
        assertEquals(5, result.content[1].viewCount)
        verify(postRepository).findAllByOrderByViewCountDesc(pageable)
    }

    @Test
    fun `제목 키워드로 게시글 검색 테스트`() {
        // given
        val pageable: Pageable = PageRequest.of(0, 10)
        val postAuthor = AuthorInfo("게시자", "password123")
        val keyword = "게시글"

        // 검색 키워드 포함된 게시글
        val matchingPost1 = Post(postAuthor, "게시글 테스트1", "내용1")
        ReflectionTestUtils.setField(matchingPost1, "id", 1L)

        val matchingPost2 = Post(postAuthor, "게시글 테스트2", "내용2")
        ReflectionTestUtils.setField(matchingPost2, "id", 2L)

        val searchResults = listOf(matchingPost1, matchingPost2)
        val resultPage = PageImpl<Post>(searchResults, pageable, searchResults.size.toLong())


        // Mock 설정
        `when`(postRepository.findByTitleContainingOrderByCreatedAtDesc(keyword, pageable)).thenReturn(resultPage)


        // when
        val result = postService.searchPostsByTitle(keyword, pageable)

        // then
        assertEquals(2, result.content.size)
        assertEquals("게시글 테스트1", result.content[0].title)
        assertEquals("게시글 테스트2", result.content[1].title)
        verify(postRepository).findByTitleContainingOrderByCreatedAtDesc(keyword, pageable)

    }
}