package kotlin_spring.board.service

import kotlin_spring.board.domain.entity.Post
import kotlin_spring.board.domain.repository.PostRepository
import kotlin_spring.board.dto.PostUpdateDto
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

    fun getPostWithoutViewIncrement(id: Long): Post {
        val post = postRepository.findById(id).orElseThrow {
            NoSuchElementException("게시글을 찾을 수 없습니다: $id")
        }
        return post
    }

    // 게시글 저장
    @Transactional
    fun savePost(post: Post): Post {
        return postRepository.save(post)
    }

    @Transactional
    fun updatePost(id: Long, postDto: PostUpdateDto): Post {
        val post = postRepository.findById(id).orElseThrow {
            NoSuchElementException("게시글을 찾을 수 없습니다: $id")
        }

        if (post.authorInfo.password != postDto.password) {
            throw IllegalArgumentException("비밀번호가 일치하지 않습니다.")
        }

        post.title = postDto.title
        post.content = postDto.content

        return post
    }

    @Transactional
    fun deletePost(id: Long, password: String) {
        val post = postRepository.findById(id).orElseThrow {
            NoSuchElementException("게시글을 찾을 수 없습니다: $id")
        }

        if (post.authorInfo.password != password) {
            throw IllegalArgumentException("비밀번호가 일치하지 않습니다.")
        }

        postRepository.deleteById(id)
    }
}