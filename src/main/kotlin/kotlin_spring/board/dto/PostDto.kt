package kotlin_spring.board.dto

data class PostCreateDto(
    val title: String,
    val content: String,
    val author: String,
    val password: String
)