package kotlin_spring.board.domain.entity

import jakarta.persistence.*

@Entity
@Table(name = "posts")
class Post(
    @Embedded
    val authorInfo: AuthorInfo,

    @Column(nullable = false)
    var title: String,

    @Column(columnDefinition = "TEXT")
    var content: String = "",

    @Column(name = "view_count", nullable = false)
    var viewCount: Int = 0,

    @OneToMany(mappedBy = "post", cascade = [CascadeType.ALL], orphanRemoval = true)
    val comments: MutableList<Comment> = mutableListOf(),

): AuditEntity() {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}