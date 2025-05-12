package kotlin_spring.board.service

import kotlin_spring.board.domain.entity.Post
import kotlin_spring.board.domain.repository.PostRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PostService(private val postRepository: PostRepository) {

    // 페이징 처리된 게시글 목록 조회 (최신순)
    fun getPosts(pageable: Pageable): Page<Post> {
        return postRepository.findAllByOrderByCreatedAtDesc(pageable)
    }

    // 조회수 기준 인기 게시글 조회
    fun getPopularPosts(pageable: Pageable): Page<Post> {
        return postRepository.findAllByOrderByViewCountDesc(pageable)
    }

    // 제목 키워드로 게시글 검색
    fun searchPostsByTitle(title: String, pageable: Pageable): Page<Post> {
        return postRepository.findByTitleContainingOrderByCreatedAtDesc(title, pageable)
    }

    // 게시글 조회 (조회수 증가)
    @Transactional
    fun getPost(id: Long): Post {
        val post = postRepository.findById(id).orElseThrow {
            NoSuchElementException("게시글을 찾을 수 없습니다: $id")
        }
        post.incrementViewCount()

        return post
    }
}