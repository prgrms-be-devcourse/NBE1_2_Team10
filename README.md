# NBE1_2_Team10

## 0️⃣프로젝트 소개
---
### 프로젝트명
CineVerse(영화 소개 및 평론 사이트)
 
### 프로젝트 참고 자료
* [노션](https://www.notion.so/10-73b6a79c52864a1180dd000f85fafa0d)

### 개발 기간
* 2024/09/23~2024/10/10

## 1️⃣기획서
---
* [기획서](https://www.notion.so/10-35c8a466484541f59a01e0f2697663cb#722fc23c3df7433991eb46ab01c74900)

## 2️⃣개발툴
---
### 개발 도구

[![stackticon](https://firebasestorage.googleapis.com/v0/b/stackticon-81399.appspot.com/o/images%2F1728451050577?alt=media&token=0321c463-3a38-4c0d-b830-717d959179fa)](https://github.com/msdio/stackticon)

### 백엔드 기술 스택
| 소프트웨어           | 비고       |
|-----------------|----------|
| Spring Boot     | 3.0.3    |
| mybatis         | 1.5.2    |
| Swagger         | 2.2.0    |
| JWT             | 0.12.5   |
| KMDB API    | Open API |
| Spring Security | 6.3.3        |

### 백엔드 dependencies
| 라이브러리 명                                 | 세부 사양(버전) |
|-----------------------------------------|-----------|
| spring-boot-starter-validation          | 3.0.2     |
| spring-boot-starter-web                 | 3.3.4     |
| spring-boot-starter-webflux             | 3.3.4     |
| mysql-connector-j                       | 8.3.0     |
| Lombok                                  | 1.18.34   |
| spring-boot-starter-test                | 3.3.4     |
| spring-cloud-gcp-starte                 | 5.6.1     |
| spring-cloud-gcp-starter-storage        | 5.6.1     |
| spring-boot-starter-cache               | 3.3.4     |
| org.json:json:20211205                  | 2.53.0    |
| spring-boot-starter-data-redis          | 3.3.4     |
| spring-boot-starter-data-redis-reactive | 3.3.4     |
| spring-boot-starter-webflux             | 6.1.13    |
| junit-platform-launcher                 | 5.10.3    |


### 프론트 기술 스택
| 소프트웨어     | 세부 사양(버전) |
|-----------|-----------|
| React     | 미정        |
| Axios     | 미정        |
| Bootstrap | 미정        |

### 프론트 dependencies


## 3️⃣프로젝트 협업 규칙
---
* [Git 컨벤션](https://github.com/prgrms-be-devcourse/NBE1_2_Team10/blob/main/git%20convention/README.md)


## 4️⃣프로젝트 구현 사항
---

### 1. 주요 기능 요약
### 2. 데이터 베이스 캐스팅
* MySQL: 실제 운영 데이터베이스로 사용. 회원, 영화, 찜, 평론, 리뷰, 댓글 데이터 저장.
* H2 Database: 테스트 용도로 사용. 개발 및 테스트 환경에서 사용.

### 3. Spring Security
* 간단한 인증 및 인가 구현: 회원 정보 보호를 위한 기본적인 인증 로직 사용.

### 4. 테스트
* JUnit을 이용한 유닛 테스트: 각 서비스 로직 및 컨트롤러에 대한 테스트 구현.

### 5. Swagger API 문서화
* SpringDoc을 활용하여 API 문서 자동 생성.
* [링크 추가 예정] (건하님 추가 예정)

### 6. API 명세
* [API 명세서](https://www.notion.so/API-09eb6088fc2143f6928fe70135e73405)

### 7. Sequence Diagram
* [Sequence Diagram](https://www.notion.so/11990d1ee4be810593e0f3a71943910b)

### 8. ERD
![image-3](https://github.com/user-attachments/assets/e0dc7923-1ba3-4389-bc5d-325a5fd1536b)


### 9. 프로젝트 패키지 구성
<details><summary> 패키지 구성 </summary>
    application

    ├─main
    │  ├─java
    │  │  └─core
    │  │      └─application
    │  │          │  Application.java
    │  │          │
    
    │  │          ├─config
    │  │          │  │  PrivateConfig.java
    │  │          │  │  SwaggerConfig.java
    │  │          │  │  WebClientConfig.java
    │  │          │  │
    
    │  │          │  └─mybatis
    │  │          │          MyBatisConfig.java
    │  │          │          UUIDTypeHandler.java
    │  │          │
    
    │  │          ├─movies
    │  │          │  ├─constant
    │  │          │  │      CommentSort.java
    │  │          │  │      Genre.java
    │  │          │  │      MovieSearch.java
    │  │          │  │
    
    │  │          │  ├─controller
    │  │          │  │      CommentController.java
    │  │          │  │      GlobalExceptionHandler.java
    │  │          │  │      MovieController.java
    │  │          │  │      MovieExceptionAdvice.java
    │  │          │  │
    
    │  │          │  ├─exception
    │  │          │  │      ExceptionResult.java
    │  │          │  │      InvalidReactionException.java
    │  │          │  │      NoMovieException.java
    │  │          │  │      NotFoundCommentException.java
    │  │          │  │      WrongAccessException.java
    │  │          │  │      WrongWriteCommentException.java
    │  │          │  │
    
    │  │          │  ├─models
    │  │          │  │  ├─dto
    │  │          │  │  │      CommentReactionRespDTO.java
    │  │          │  │  │      CommentRespDTO.java
    │  │          │  │  │      CommentWriteReqDTO.java
    │  │          │  │  │      MainPageMovieRespDTO.java
    │  │          │  │  │      MainPageMoviesRespDTO.java
    │  │          │  │  │      MovieDetailRespDTO.java
    │  │          │  │  │      MovieSearchRespDTO.java
    │  │          │  │  │
    
    │  │          │  │  └─entities
    │  │          │  │          CachedMovieEntity.java
    │  │          │  │          CommentEntity.java
    │  │          │  │
    
    │  │          │  ├─repositories
    │  │          │  │  │  CachedMovieRepository.java
    │  │          │  │  │  CachedMovieRepositoryImpl.java
    │  │          │  │  │  CommentDislikeRepository.java
    │  │          │  │  │  CommentLikeRepository.java
    │  │          │  │  │  CommentRepository.java
    │  │          │  │  │  CommentRepositoryImpl.java
    │  │          │  │  │
    
    │  │          │  │  └─mapper
    │  │          │  │          CachedMovieMapper.java
    │  │          │  │          CommentDislikeMapper.java
    │  │          │  │          CommentLikeMapper.java
    │  │          │  │          CommentMapper.java
    │  │          │  │
    
    │  │          │  └─service
    │  │          │          CommentService.java
    │  │          │          MovieService.java
    │  │          │          MovieServiceImpl.java
    │  │          │
    
    │  │          ├─reviews
    │  │          │  │  ReviewExceptionHandler.java
    │  │          │  │
    
    │  │          │  ├─controllers
    │  │          │  │      ReviewCommentController.java
    │  │          │  │
    
    │  │          │  ├─exceptions
    │  │          │  │      InvalidCommentContentException.java
    │  │          │  │      NoReviewCommentFoundException.java
    │  │          │  │      NoReviewFoundException.java
    │  │          │  │      NotCommentOwnerException.java
    │  │          │  │
    
    │  │          │  ├─models
    │  │          │  │  ├─dto
    │  │          │  │  │  ├─request
    │  │          │  │  │  │      CommonCommentReqDTO.java
    │  │          │  │  │  │      CreateCommentReqDTO.java
    │  │          │  │  │  │      EditCommentReqDTO.java
    │  │          │  │  │  │
    
    │  │          │  │  │  └─response
    │  │          │  │  │          CommonCommentRespDTO.java
    │  │          │  │  │          CreateCommentRespDTO.java
    │  │          │  │  │          EditCommentRespDTO.java
    │  │          │  │  │          MessageRespDTO.java
    │  │          │  │  │          ShowCommentsRespDTO.java
    │  │          │  │  │
    
    │  │          │  │  └─entities
    │  │          │  │          ReviewCommentEntity.java
    │  │          │  │          ReviewEntity.java
    │  │          │  │
    
    │  │          │  ├─repositories
    │  │          │  │  │  ReviewCommentRepository.java
    │  │          │  │  │  ReviewRepository.java
    │  │          │  │  │
    
    │  │          │  │  ├─mapper
    │  │          │  │  │      ReviewMapper.java
    │  │          │  │  │
    
    │  │          │  │  └─mybatis
    │  │          │  │          MyBatisReviewCommentRepository.java
    │  │          │  │          ReviewCommentMapperProvider.java
    │  │          │  │          ReviewMapperProvider.java
    │  │          │  │
    
    │  │          │  └─services
    │  │          │          ReviewCommentService.java
    │  │          │          ReviewCommentServiceImpl.java
    │  │          │          ReviewCommentSortOrder.java
    │  │          │          ReviewService.java
    │  │          │          ReviewSortOrder.java
    │  │          │
    
    │  │          └─users
    │  │              ├─mapper
    │  │              │      DibMapper.java
    │  │              │      UserMapper.java
    │  │              │
    
    │  │              ├─models
    │  │              │  ├─dto
    │  │              │  │      DibDetailRespDTO.java
    │  │              │  │      DibRespDTO.java
    │  │              │  │      MessageResponseDTO.java
    │  │              │  │      MyPageRespDTO.java
    │  │              │  │      UserDTO.java
    │  │              │  │
    
    │  │              │  └─entities
    │  │              │          DibEntity.java
    │  │              │          UserEntity.java
    │  │              │          UserRole.java
    │  │              │
    
    │  │              ├─repositories
    │  │              │      DibRepository.java
    │  │              │      DibRepositoryImpl.java
    │  │              │      UserRepository.java
    │  │              │      UserRepositoryImpl.java
    │  │              │
    
    │  │              └─service
    │  │                      DibService.java
    │  │                      DibServiceImpl.java
    │  │                      MyPageService.java
    │  │                      MyPageServiceImpl.java
    │  │                      UserService.java
    │  │
    
    │  └─resources
    │      │  application.properties
    │      │
    
    │      ├─mappers
    │      │  ├─movies
    │      │  │      CacheMovieMapper.xml
    │      │  │      CommentDislikeMapper.xml
    │      │  │      CommentLikeMapper.xml
    │      │  │      CommentMapper.xml
    │      │  │
    
    │      │  ├─review
    │      │  │      ReviewMapper.xml
    │      │  │
    
    │      │  └─users
    │      │          DibMapper.xml
    │      │          UserMapper.xml
    │      │
    
    │      └─privates
    │              private.properties
    │
    
    └─test
        └─java
            └─core
                └─application
                │  ApplicationTests.java
                │
    
                ├─movies
                │  ├─contoller
                │  │      MovieContollerTest.java
                │  │
    
                │  ├─repository
                │  │      CommentRepositoryTest.java
                │  │      MovieRepositoryTest.java
                │  │
    
                │  └─service
                │          CommentServiceTest.java
                │          MovieServiceTest.java
                │
    
                ├─reviews
                │  ├─repositories
                │  │      ReviewCommentRepositoryTest.java
                │  │
    
                │  └─services
                │          ReviewCommentServiceImplTest.java
                │
    
                └─users
                    ├─repositories
                    │      DibRepositoryImplTest.java
                    │      UserRepositoryImplTest.java
                    │
    
                    └─service
                            DibServiceImplTest.java
                            MyPageServiceImplTest.java

</details>

---

## 5️⃣트러블 슈팅


---
개발 인원
|이름|역할|프로젝트 담당 기능|
|------|---|---|
|민성훈|팀원|영화 상세 조회, 프론트|
|이민정|팀원|테스트3|
|정소은|팀원|프론트|
|정준상|팀장|테스트3|
|황건하|팀원|영화 검색,영화 한줄평|
