package kotlin_spring.board

import kotlin_spring.board.domain.entity.AuthorInfo
import kotlin_spring.board.domain.entity.Comment
import kotlin_spring.board.domain.entity.Post
import kotlin_spring.board.domain.repository.CommentRepository
import kotlin_spring.board.service.CommentService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.test.util.ReflectionTestUtils
import java.util.*
import kotlin.NoSuchElementException
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExtendWith(MockitoExtension::class)
class CommentServiceTest {

    @Mock
    private lateinit var commentRepository: CommentRepository

    @InjectMocks
    private lateinit var commentService: CommentService

    private lateinit var authorInfo: AuthorInfo
    private lateinit var post: Post
    private lateinit var comment: Comment

    @BeforeEach
    fun setUp() {
        authorInfo = AuthorInfo("testUser", "password123")

        post = Post(authorInfo, "Test Post", "Test Content")
        ReflectionTestUtils.setField(post, "id", 1L)

        comment = Comment(authorInfo, "Test Comment", post)
        ReflectionTestUtils.setField(comment, "id", 10L)
    }

    @Test
    fun `포스트 ID로 댓글 목록 조회 테스트`() {
        // given
        val postId = 1L
        val comments = listOf(comment)

        `when`(commentRepository.findByPostId(postId)).thenReturn(comments)

        // when
        val result = commentService.getCommentByPostId(postId)

        // then
        assertEquals(1, result.size)
        assertEquals(10L, result[0].id)
        assertEquals("testUser", result[0].author)
        assertEquals("Test Comment", result[0].content)
        verify(commentRepository, times(1)).findByPostId(postId)
    }

    @Test
    fun `댓글이 없는 게시글은 빈 리스트를 반환`() {
        // given
        val postId = 2L
        `when`(commentRepository.findByPostId(postId)).thenReturn(emptyList())

        // when
        val result = commentService.getCommentByPostId(postId)

        // then
        assertTrue(result.isEmpty())
        verify(commentRepository, times(1)).findByPostId(postId)
    }

    @Test
    fun `댓글 저장 테스트`() {
        // given
        `when`(commentRepository.save(comment)).thenAnswer { invocation -> invocation.arguments[0] as Comment }

        // when
        val savedComment = commentService.saveComment(comment)

        // then
        assertEquals(comment, savedComment)

        // ArgumentCaptor를 사용해 실제 저장된 객체 검증
        val commentCaptor = ArgumentCaptor.forClass(Comment::class.java)
        verify(commentRepository).save(commentCaptor.capture())

        val capturedComment = commentCaptor.value
        assertEquals(comment.id, capturedComment.id)
        assertEquals(comment.content, capturedComment.content)
        assertEquals(comment.author.nickname, capturedComment.author.nickname)
    }

    @Test
    fun `댓글 삭제 시 올바른 비밀번호를 입력한다`() {
        // given
        val commentId = 10L
        val password = "password123"

        `when`(commentRepository.findById(commentId)).thenReturn(Optional.of(comment))

        // when
        commentService.deleteComment(commentId, password)

        // then
        verify(commentRepository, times(1)).findById(commentId)
        verify(commentRepository, times(1)).deleteById(commentId)
    }

    @Test
    fun `잘못된 비밀번호로 댓글 삭제 시 예외를 발생시킨다`() {
        // given
        val commentId = 10L
        val wrongPassword = "wrongPassword123"

        `when`(commentRepository.findById(commentId)).thenReturn(Optional.of(comment))

        // when
        val exception = assertThrows<IllegalArgumentException> {
            commentService.deleteComment(commentId, wrongPassword)
        }

        // then
        assertEquals("비밀번호가 일치하지 않습니다.", exception.message)
        verify(commentRepository, times(1)).findById(commentId)
        verify(commentRepository, never()).deleteById(commentId)
    }

    @Test
    fun `존재하지 않는 댓글 ID로 삭제 시 예외를 발생시킨다`() {
        // given
        val wrongCommentId = 789L
        val password = "password123"

        `when`(commentRepository.findById(wrongCommentId)).thenReturn(Optional.empty())

        // when
        val exception = assertThrows<NoSuchElementException> {
            commentService.deleteComment(wrongCommentId, password)
        }

        // then
        assertEquals("댓글을 찾을 수 없습니다: $wrongCommentId", exception.message)
        verify(commentRepository, times(1)).findById(wrongCommentId)
        verify(commentRepository, never()).deleteById(wrongCommentId)
    }
}