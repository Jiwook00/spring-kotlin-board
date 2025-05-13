package kotlin_spring.board.dto

data class PostCreateDto(
    val title: String,
    val content: String,
    val author: String,
    val password: String
)

data class PostUpdateDto(
    val id: Long,
    val title: String,
    val content: String,
    val password: String
)