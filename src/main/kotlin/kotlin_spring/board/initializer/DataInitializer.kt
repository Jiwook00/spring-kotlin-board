package kotlin_spring.board.initializer

import kotlin_spring.board.domain.entity.AuthorInfo
import kotlin_spring.board.domain.entity.Post
import kotlin_spring.board.domain.repository.PostRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("dev")
class DataInitializer {

    @Bean
    fun init(postRepository: PostRepository): CommandLineRunner {
        return CommandLineRunner {
            // 초기 더미 데이터 삽입
            val posts = listOf(
                Post(
                    authorInfo = AuthorInfo("홍길동", "password123"),
                    title = "안녕하세요, 첫 번째 게시글입니다.",
                    content = "게시판 첫 글로 인사드립니다.\n\n이 게시판은 다양한 정보와 소식을 공유하는 공간입니다. 많은 참여 부탁드립니다.\n\n앞으로도 좋은 정보와 소식을 전해드리겠습니다. 감사합니다!",
                    viewCount = 42
                ),
                Post(
                    authorInfo = AuthorInfo("관리자", "admin123"),
                    title = "게시판 사용 안내입니다.",
                    content = "안녕하세요, 관리자입니다.\n\n게시판 사용에 관한 안내 말씀 드립니다.\n\n1. 서로 존중하는 문화를 만들어주세요.\n2. 불법적인 내용이나 광고는 삭제될 수 있습니다.\n3. 게시글 작성 시 입력한 비밀번호는 수정 및 삭제에 사용됩니다.\n\n즐거운 커뮤니티 활동 되시길 바랍니다.",
                    viewCount = 156
                ),
                Post(
                    authorInfo = AuthorInfo("김소식", "news123"),
                    title = "오늘의 소식을 전해드립니다.",
                    content = "오늘의 주요 소식입니다.\n\n- 지역 행사 안내: 다음 주말 시민 공원에서 플리마켓이 열립니다.\n- 새로운 지역 맛집: 역 앞에 새로 오픈한 카페가 인기를 끌고 있습니다.\n- 교통 정보: 이번 주말 도로 공사로 우회가 필요합니다.\n\n더 자세한 정보는 추후 업데이트하겠습니다.",
                    viewCount = 89
                ),
                Post(
                    authorInfo = AuthorInfo("이질문", "question123"),
                    title = "질문이 있습니다.",
                    content = "안녕하세요, 커뮤니티 여러분.\n\n이번에 새로 이사왔는데 지역 정보에 대해 알고 싶습니다.\n\n1. 근처에 좋은 병원이 있을까요?\n2. 주말에 가족과 함께 갈만한 곳이 있나요?\n3. 지역 주민들이 자주 이용하는 시설이 궁금합니다.\n\n답변 부탁드립니다. 감사합니다!",
                    viewCount = 23
                ),
                Post(
                    authorInfo = AuthorInfo("박정보", "info123"),
                    title = "정보 공유합니다.",
                    content = "유용한 정보를 공유합니다.\n\n1. 무료 온라인 강의 사이트: www.example.com에서 다양한 무료 강의를 제공하고 있습니다.\n2. 전자책 할인 정보: 이번 달 말까지 50% 할인 이벤트 중입니다.\n3. 유용한 앱 추천: 일정 관리에 도움이 되는 앱을 소개합니다.\n\n도움이 되셨으면 좋겠습니다.",
                    viewCount = 67
                ),
                Post(
                    authorInfo = AuthorInfo("관리자", "admin123"),
                    title = "게시판 업데이트 관련 안내",
                    content = "안녕하세요, 관리자입니다.\n\n게시판 기능 업데이트 안내드립니다.\n\n1. 댓글 기능이 추가되었습니다.\n2. 게시글 검색 기능이 개선되었습니다.\n3. 모바일 화면 최적화가 적용되었습니다.\n\n더 나은 서비스를 제공하기 위해 노력하겠습니다. 감사합니다.",
                    viewCount = 112
                ),
                Post(
                    authorInfo = AuthorInfo("최인사", "hello123"),
                    title = "안녕하세요~ 반갑습니다.",
                    content = "커뮤니티 여러분 안녕하세요!\n\n처음 가입했습니다. 앞으로 잘 부탁드립니다.\n\n다양한 정보 공유와 소통을 통해 즐거운 시간 보내고 싶습니다.\n\n잘 부탁드립니다!",
                    viewCount = 34
                ),
                Post(
                    authorInfo = AuthorInfo("정자료", "data123"),
                    title = "자료 요청합니다.",
                    content = "안녕하세요, 커뮤니티 여러분.\n\n프로젝트 진행에 필요한 자료를 찾고 있습니다.\n\n1. 웹 개발 관련 추천 도서가 있을까요?\n2. 초보자가 배우기 좋은 프로그래밍 언어는 무엇인가요?\n3. 온라인 스터디 그룹에 대한 정보도 있으면 좋겠습니다.\n\n도움 주시면 감사하겠습니다.",
                    viewCount = 45
                ),
                Post(
                    authorInfo = AuthorInfo("관리자", "admin123"),
                    title = "이벤트 안내",
                    content = "커뮤니티 회원 여러분들을 위한 이벤트를 안내드립니다.\n\n1. 활발한 활동을 하는 회원을 위한 소정의 선물을 준비했습니다.\n2. 다음 달부터 월간 베스트 게시글을 선정하여 상품을 드립니다.\n3. 온라인 만남의 날을 계획 중입니다.\n\n많은 참여 부탁드립니다!",
                    viewCount = 201
                ),
                Post(
                    authorInfo = AuthorInfo("강첫글", "first123"),
                    title = "첫 게시글입니다.",
                    content = "안녕하세요! 처음으로 게시글을 작성해봅니다.\n\n이 커뮤니티에서 많은 것을 배우고 공유하고 싶습니다.\n\n앞으로 자주 찾아뵙겠습니다. 잘 부탁드립니다!",
                    viewCount = 321
                ),
                Post(
                    authorInfo = AuthorInfo("테스트", "first123"),
                    title = "테스트 게시글입니다.",
                    content = "안녕하세요! 처음으로 게시글을 작성해봅니다.\n\n이 커뮤니티에서 많은 것을 배우고 공유하고 싶습니다.\n\n앞으로 자주 찾아뵙겠습니다. 잘 부탁드립니다!",
                    viewCount = 321
                )
            )

            postRepository.saveAll(posts)
        }
    }
}