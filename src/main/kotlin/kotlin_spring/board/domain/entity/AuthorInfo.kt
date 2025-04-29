package kotlin_spring.board.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class AuthorInfo(
    @Column(nullable = false)
    var nickname: String,

    @Column(nullable = false)
    var password: String,
) {
    constructor() : this("", "") // JPA 요구사항
}
