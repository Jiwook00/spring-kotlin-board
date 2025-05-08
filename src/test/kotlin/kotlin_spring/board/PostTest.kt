package kotlin_spring.board

import kotlin_spring.board.domain.entity.AuthorInfo
import kotlin_spring.board.domain.entity.Comment
import kotlin_spring.board.domain.entity.Post
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PostTest {

    @Test
    fun `댓글 추가 시 양방향 연관관계가 설정되어야 한다`() {
        // given
        val postAuthor = AuthorInfo("게시자", "password123")
        val post = Post(postAuthor, "게시글", "게시글 내용")

        val commentAuthor = AuthorInfo("댓글작성자", "comment123")
        val comment = Comment(commentAuthor, "댓글 내용", post)

        // when
        post.comments.add(comment)

        // then
        assertEquals(1, post.comments.size)
        assertTrue(post.comments.contains(comment))
        assertEquals(post, comment.post)
    }

    @Test
    @DisplayName("조회수가 증가되어야 한다")
    fun incrementViewCountShouldIncreaseViewCount() {
        // given
        val postAuthor = AuthorInfo("게시자", "password123")
        val post = Post(postAuthor, "게시글", "게시글 내용")
        val initialViewCount = post.viewCount

        // when
        post.incrementViewCount()

        // then
        assertEquals(initialViewCount + 1, post.viewCount )
    }

    @Test
    fun `조회수 증가 여러번 호출 시 호출 횟수만큼 증가해야 한다`() {
        // given
        val postAuthor = AuthorInfo("게시자", "password123")
        val post = Post(postAuthor, "게시글", "게시글 내용")
        val initialViewCount = post.viewCount
        var postView = 0

        // when
        while (postView < 6) {
            post.incrementViewCount()
            postView++
        }

        // then
        assertEquals(initialViewCount + postView, post.viewCount)
    }

}