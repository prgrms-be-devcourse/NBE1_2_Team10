# 🚣🏻‍♂️ Git - flow 전략

- **master** : 배포가 가능한 가장 메인이 되는 브랜치
- **hotfix** : 빠르게 버그를 수정해야 할 때 사용하는 브랜치
- **release** : 프로젝트 배포를 준비하기 위해 사용하는 브랜치
- **develop** : 개발 과정에서 사용하는 중심 브랜치
    - **feat** : 각 기능을 구현할 때 사용하는 브랜치
    
<p align="center"> <img src="https://github.com/user-attachments/assets/956518a4-c18f-428e-8f4a-284a35b85a85" width="600"/> </p>
    

---
<br>

# ☄️ Commit 컨벤션

- pull request merge 시 모든 팀원의 코드 리뷰와 승인 시 가능
- merge 시 rebase를 진행함
<br>

#### 코드 리뷰 시 참고 자료

https://tech.kakao.com/posts/498

---
<br>

# 🎋 Branch 컨벤션

- 커밋 태그/이슈번호+기능명 ⇒ 이슈 트래커 브랜치 사용 시

like  `feat/userService_#12`

---
<br>

# 💬 Commit Message 컨벤션

<aside>
❗커밋 메시지 = 제목 + 본문 + 꼬리말

각 파트는 2번의 개행을 통해 구분 (= 하나의 빈 줄)

<br>
제목 = 커밋 태그 + 개조식 구문

like `Feat : Fix UserDTO`

⏬상세한 내용은 아래에서 확인

<br>
본문 = 상세히 무엇을 왜 변경했는지 작성 (어떻게는 포함 X)

⏬상세한 내용은 아래에서 확인

<br>
꼬리말 = issue 트래커 번호

⏬상세한 내용은 아래에서 확인

<br>

### **Commit 메시지 예시**

<p align="center"> <img src="https://github.com/user-attachments/assets/36a33143-02dd-4c33-b757-e049337d2658" width="550"/> </p>

**태그 종류**

<p align="center"> <img src="https://github.com/user-attachments/assets/fab20542-708f-44e5-aacb-6296b4ace830" width="550"/> </p>

<p align="center"> <img src="https://github.com/user-attachments/assets/40d93962-89e6-43df-930d-32d9b3afcbfc" width="550"/> </p>

<p align="center"> <img src="https://github.com/user-attachments/assets/f188b740-97bf-47aa-9771-7d9d09fe82c8" width="550"/> </p>

<p align="center"> <img src="https://github.com/user-attachments/assets/52cff5b3-e81f-4311-8017-f4cb0556447c" width="550"/> </p>

<br>

#### 참고한 사이트

https://overcome-the-limits.tistory.com/entry/협업-협업을-위한-기본적인-git-커밋컨벤션-설정하기#본문은-어떻게-작성하는가

<br>

#### 참고하면 좋을만한 자료

https://guuumi.tistory.com/128

https://velog.io/@code-bebop/Github-Issue-Tracker와-Project-Board
