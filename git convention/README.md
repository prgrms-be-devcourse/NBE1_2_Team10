# 🚣🏻‍♂️ Git - flow 전략

- **master** : 배포가 가능한 가장 메인이 되는 브랜치
- **hotfix** : 빠르게 버그를 수정해야 할 때 사용하는 브랜치
- **release** : 프로젝트 배포를 준비하기 위해 사용하는 브랜치
- **develop** : 개발 과정에서 사용하는 중심 브랜치
    - **feat** : 각 기능을 구현할 때 사용하는 브랜치
    
    ![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/0d46a6b4-394a-476f-8fe5-e8d0ca80a24f/4a5fb5f6-6fc4-4d84-b6d8-a7ce7974d5b4/image.png)
    

---

# ☄️ Commit 컨벤션

- pull request merge 시 모든 팀원의 코드 리뷰와 승인 시 가능

코드 리뷰 시 참고 자료

https://tech.kakao.com/posts/498

---

# 🎋 Branch 컨벤션

- 커밋 태그/기능명(Camel case / 최대한 간단히)
- 커밋 태그/이슈번호+기능명 ⇒ 이슈 트래커 브랜치 사용 시

like  `feat/#12userService`

---

# 💬 Commit Message 컨벤션

<aside>
❗

커밋 메시지 = 제목 + 본문 + 꼬리말

각 파트는 2번의 개행을 통해 구분 (= 하나의 빈 줄)

제목 = 커밋 태그 + 개조식 구문

like Feat : Fix UserDTO

⏬상세한 내용은 아래에서 확인

본문 = 상세히 무엇을 왜 변경했는지 작성 (어떻게는 포함 X)

⏬상세한 내용은 아래에서 확인

꼬리말 = issue 트래커 번호

⏬상세한 내용은 아래에서 확인

</aside>

### **Commit 메시지 예시**

![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/0d46a6b4-394a-476f-8fe5-e8d0ca80a24f/e09760ae-f55f-4ac2-a8b6-ec15e553790b/image.png)

**태그 종류**

![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/0d46a6b4-394a-476f-8fe5-e8d0ca80a24f/b498d440-a7cf-46c5-97e3-38d58cb0a6fd/image.png)

![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/0d46a6b4-394a-476f-8fe5-e8d0ca80a24f/ef597e61-9150-45a1-bdbe-37ea0d13461f/image.png)

![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/0d46a6b4-394a-476f-8fe5-e8d0ca80a24f/8e582f05-a303-4ff0-93c5-08c241ec41b2/image.png)

![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/0d46a6b4-394a-476f-8fe5-e8d0ca80a24f/13f1500b-1ba0-4fce-9847-c9e7f0d291e9/image.png)

참고 사이트

https://overcome-the-limits.tistory.com/entry/협업-협업을-위한-기본적인-git-커밋컨벤션-설정하기#본문은-어떻게-작성하는가

 참고하면 좋을만한 자료

https://guuumi.tistory.com/128

https://velog.io/@code-bebop/Github-Issue-Tracker와-Project-Board
