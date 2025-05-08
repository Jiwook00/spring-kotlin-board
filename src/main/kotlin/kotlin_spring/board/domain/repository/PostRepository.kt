package kotlin_spring.board.domain.repository

import kotlin_spring.board.domain.entity.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository : JpaRepository<Post, Long> {

    // 생성일 기준 최신순 정렬
    fun findAllByOrderByCreatedAtDesc(pageable: Pageable): Page<Post>

    // 조회수 기준 내림차순 정렬
    fun findAllByOrderByViewCountDesc(pageable: Pageable): Page<Post>

    // 제목에 특정 키워드가 포함된 게시글 검색 (최신순)
    fun findByTitleContainingOrderByCreatedAtDesc(title: String, pageable: Pageable): Page<Post>
}