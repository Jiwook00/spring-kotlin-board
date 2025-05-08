package kotlin_spring.board.domain.entity

import jakarta.persistence.*

@Entity
@Table(name = "comments")
class Comment (
    @Embedded
    val author: AuthorInfo,

    @Column(nullable = false)
    var content: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    val post: Post

): AuditEntity() {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}
