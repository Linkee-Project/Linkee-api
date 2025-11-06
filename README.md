# Linkee-api

---

# 🌟링키 Linkee
<div align="center">
<img width="600" height="600" alt="ChatGPT Image 2025년 10월 15일 오후 05_09_11 (1) (1)" src="https://github.com/user-attachments/assets/ab453c50-5f3a-4732-baf8-25057305de24" />
</div>


---
## 목차 (Table of Contents)

1. [👩‍👧‍👦 1. 멤버 소개](#-1-멤버-소개)


## 📂 프로젝트 파일 구조

```
charly-market/
│
├── .github/
│   └── ISSUE_TEMPLATE/         # 이슈 템플릿 관련 파일들
│
├── Docs/                       # 문서 및 기획 관련 내용
│
├── Integration Test/           # 통합 테스트 관련 코드
│
├── db/                         # 데이터베이스 관련 파일들
│  └── domain                   # 도메인별 테스트 코드
|  └── init                     # 데이터베이스 생성 코드 및 더미 데이터
|
├── flowchart/                  # 플로우차트 이미지 또는 문서
│
├── README.md                   # 현재 README 파일
│
└── 찰리 프로젝트 기획서.md     # 프로젝트 기획서 한국어 문서
```

---

## 👩‍👧‍👦 1. 멤버 소개


<div align="center">

| 유한세 | 김상재 | 김 진 |
|--------|--------|--------|
|<img width="150" height="150" alt="image (15)" src="https://github.com/user-attachments/assets/2c38376b-4b67-43fc-ba88-4fbf2c5d49e3" />| <img width="150" height="150" alt="image (16)" src="https://github.com/user-attachments/assets/0fc0dbfc-5954-4056-9fad-46b75c9224c8" /> | <img width="150" height="150" alt="IMG_7128" src="https://github.com/user-attachments/assets/2aef33c7-7883-4932-af31-dac933230542" />|

| 김명진 | 정동욱 |
|--------|--------|
|<img width="150" height="150" alt="image (17)" src="https://github.com/user-attachments/assets/bfe7f4bf-1f85-4cad-9354-525063a424de" /> | <img width="150" height="150" alt="KakaoTalk_20251105_144003874" src="https://github.com/user-attachments/assets/4e307545-a820-4551-9773-094f203fc07a" />|


</div>

---
## ⚒️ 2. 서비스 소개
Linkee는 사용자가 CS 관련 퀴즈에 참여하고 문제를 풀며 다른 사용자들과 소통할 수 있는 플랫폼입니다. 퀴즈방을 만들거나 참가하고, 문제를 등록하며 다양한 기능을 이용할 수 있습니다.

### 🔑 **회원가입 및 로그인**
- **회원가입**: 사용자는 이메일 인증을 통해 회원가입을 하거나 소셜 로그인("네이버")를 통해 쉽게 가입할 수 있습니다.
- **이메일 인증**: 회원가입 시 이메일 인증을 통해 계정을 활성화할 수 있습니다.
- **로그인**: 아이디와 비밀번호 또는 소셜 계정을 통해 로그인할 수 있습니다.
- **회원 정보 관리**: 자신의 프로필을 수정하거나 비밀번호를 변경할 수 있습니다.

### 📚 **문제 게시판**
- **문제 등록**: 사용자는 DB, 네트워크, 운영체제 등 다양한 카테고리의 CS 관련 문제를 등록할 수 있습니다.
- **문제 검증**: 관리자들이 인기 있는 문제를 검토하여 명확성, 정답 타당성 등을 확인합니다.
- **시스템 문제로 등록**: 검토가 완료된 문제는 퀴즈방 문제로 등록 되어집니다.

### 🎮 **퀴즈**
- **퀴즈방 만들기**: 주제, 모드(연습/다함께), 인원 수 등을 설정하여 퀴즈방을 만들 수 있습니다.
- **빠른 시작**: '빠른시작' 기능을 통해 모집 중인 공개 방에 자동으로 참여할 수 있습니다.
- **문제 출제**: 시스템이 주제에 맞는 문제를 자동으로 선택하여 출제합니다.
- **결과 확인**: 퀴즈가 종료된 후 점수와 순위를 확인할 수 있습니다.

### 🎲 **자율방 기능**
- **자율방 만들기**: 사용자는 친구들과 자율적으로 방을 생성할 수 있습니다. 방을 만들 때, 주제, 인원 수, 공개/비공개 여부 등을 설정할 수 있습니다.
- **그룹 채팅**: 자율방 내에서는 그룹원들 간에 자유롭게 채팅을 할 수 있습니다.
- **문제 등록**: 방의 참가자 중 한 명이 문제를 직접 등록할 수 있으며, 다른 참가자들은 해당 문제에 대한 답을 맞춰서 제출할 수 있습니다.
- **문제 목록**: 각자가 제출한 문제는 채팅방 옆에 문제 목록으로 등록됩니다. 이를 통해 시스템에서 출제된 문제들과는 중복되지 않으며, 자유롭게 다양한 문제를 경험할 수 있습니다.

### 👥 **소셜 기능**
- **친구 추가**: 닉네임으로 친구를 검색하고, 친구 요청을 보낼 수 있습니다.
- **채팅**: 친구와 1:1 또는 그룹 채팅을 통해 소통할 수 있습니다.
- **친구 프로필**: 친구의 프로필을 상세히 확인하고, 방에 초대할 수 있습니다.

### ⚖️ **신고 및 관리**
- **신고**: 부적절한 콘텐츠나 사용자를 신고할 수 있습니다.
- **관리자 관리**: 관리자는 신고 사항을 처리하고, 비활성화 또는 경고를 할 수 있습니다.

### 📢 **알림 기능**
- **알림 템플릿**: 관리자들은 알림 템플릿을 등록하고 수정할 수 있습니다.
- **알림 발송**: 시스템은 등록된 템플릿에 따라 알림을 사용자에게 발송합니다.
- **알림 조회**: 사용자는 발송된 알림 목록을 조회할 수 있습니다.

---

## 🚀 3. 기술 스택

### 🚀 Stacks
<p>
  <img src="https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white"/>
  <img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"/>
  <img src="https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=spring&logoColor=white"/>
  <img src="https://img.shields.io/badge/OAuth2-000000?style=for-the-badge&logo=oauth&logoColor=white"/>
  <img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=json-web-tokens&logoColor=white"/>
  <img src="https://img.shields.io/badge/Thymeleaf-003300?style=for-the-badge&logo=thymeleaf&logoColor=white"/>
  <img src="https://img.shields.io/badge/MyBatis-9B1D20?style=for-the-badge&logo=mybatis&logoColor=white"/>
  <img src="https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=mariadb&logoColor=white"/>
  <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white"/>
  <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white"/>
</p>


### ⚒️ Tools
<p>
<img src="https://img.shields.io/badge/HeidiSQL-b0fb0c?style=for-the-badge&logo=heidisql&logoColor=white"/>
<img src="https://img.shields.io/badge/ERDCloud-72099f?style=for-the-badge&logo=erdcloud&logoColor=white"/>
<img src="https://img.shields.io/badge/Canva-00C4CC?style=for-the-badge&logo=canva&logoColor=white"/>
<img src="https://img.shields.io/badge/DBeaver-382923?style=for-the-badge&logo=dbeaver&logoColor=white"/>
<img src="https://img.shields.io/badge/IntelliJ_IDEA-000000?style=for-the-badge&logo=intellij-idea&logoColor=white"/>
<img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white"/>
</p>

### 👥  Collaboration      

<p>
<img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white"/>
<img src="https://img.shields.io/badge/discord-5865F2?style=for-the-badge&logo=discord&logoColor=white"/>
<img src="https://img.shields.io/badge/notion-000000?style=for-the-badge&logo=notion&logoColor=white"/>
</p>
---

## 🗂️ 4. 프로젝트 산출물

- ### 🕖 WBS **(Work Breakdown Structure)**
  WBS를 자세히 보려면 [여기](https://www.notion.so/25b36d2af8f580fab019e6d2211cd7a5?v=25b36d2af8f5802b830b000cb9f5aefe&source=copy_link)를 클릭하세요

  <details> <summary> WBS 이미지</summary>
  <img width="1400" height="800" alt="WBS" src="https://github.com/user-attachments/assets/770e5cfa-048a-4ac9-abba-0cd393c1c334" />
  </details>
  



- ### 📚 요구사항 명세서

  요구사항 명세서를 자세히 보려면 [여기](https://docs.google.com/spreadsheets/d/1m9Jk_0A6LhK9aZ2RiVEwYnF6yCltFJS01lFYrovXV8I/edit?gid=680046902#gid=680046902)를 클릭하세요


  <details> 
  <summary> 요구사항 명세서 이미지</summary>
  <img width="1157" height="800" alt="Image" src="https://github.com/user-attachments/assets/68e3f008-a803-4412-9934-433ad9d06171" />
  <img width="1161" height="557" alt="Image" src="https://github.com/user-attachments/assets/a4fd564b-979a-492e-83a2-09e888a51592" />
  </details>

  
  
- ### 🖼️ DDD **(Domain Driven Design)**

  DDD을 자세히 보려면 [여기](https://miro.com/app/board/uXjVJ4xhIGY=/)를 클릭하세요

  <details> 
  <summary> DDD 이미지</summary>
  <img width="852" height="876" alt="Image" src="https://github.com/user-attachments/assets/4c1e3e07-882a-413a-b0ae-209e2ced15ab" />
  </details>




- ### 🗺️ ERD **(Entity Relationship Diagram)**

  ERD를 자세히 보려면 [여기](https://www.erdcloud.com/d/uT2o7h2jb9BhZ62L2)를 클릭하세요

  <details> 
  <summary> ERD 이미지</summary>
  <img width="4720" height="2032" alt="Image" src="https://github.com/user-attachments/assets/5eabab43-8f1d-4e2d-9bd9-8ca4bc9c34ef" />
  </details>

- ### 📋 API 명세서 

  <details> 
  <summary> API 명세서 이미지</summary>
  <img width="897" height="702" alt="Image" src="https://github.com/user-attachments/assets/d830db0b-158d-471a-86fe-fa28e1a9a009" />
  <img width="895" height="426" alt="Image" src="https://github.com/user-attachments/assets/5cbee805-7c38-4d6f-aae2-665f89b93496" />
  <img width="596" height="715" alt="Image" src="https://github.com/user-attachments/assets/5d3d8c48-14e4-4b8f-a7c1-e3571b8d30d8" />
  <img width="895" height="691" alt="Image" src="https://github.com/user-attachments/assets/6eb77749-d5f4-4334-a36f-5aeb25c794da" />
  <img width="892" height="881" alt="Image" src="https://github.com/user-attachments/assets/016d5b14-8fb4-4a8c-ab70-7816dd011258" />
  <img width="897" height="941" alt="Image" src="https://github.com/user-attachments/assets/ef71d0a8-814d-4043-b941-f3191bd1b9f9" />
  </details>

  
- ### 🗞️ 테스트 케이스 작성 및 테스트
  테스트 코드를 자세히 보려면 db > domain 을 확인해주세요
  
- ### 🔁 통합테스트 시나리오 및 코드
  통합테스트 시나리오를 자세히 보려면 [여기](https://www.notion.so/26136d2af8f5802e914afbc54cf37e47?source=copy_link)를 클릭하세요  
  
---
## ⚠️ 5. Trouble Shooting

- 김상재
  <details>

  <summary>Builder 초기값 문제</summary>

  ```java
  @Builder
  public class User {
    private Status userStatus = Status.Y; // 경고 발생
    }
  ```

  @Builder로 객체를 생성하면 userStatus = Status.Y 이 초기값은 적용되지 않는다
  
  만약 Builder에서도 기본값으로 적용하고 싶으면 이렇게 바꿔야 한다

  ```java
  @Builder
  public class User {
    @Builder.Default
    private Status userStatus = Status.Y;
    }
  ```

  </details> 

  <details>
  <summary>record 를 mybatis로 사용 할 경우 String값</summary>

  mybatis 사용할때 레코드 형식으로 받아오면 String 값이

  ```java
  "templateId": 2,
  "templateContent": "{\r\n    \"content\" : \"새로운 내용1\"\r\n}",
  "createdAt": "2025-10-29T23:56:47.096377",
  "updatedAt": "2025-10-29T23:56:47.096377"
  ```

  이런식으로 JSON 형식으로 넘어갈때 Jackson은 문자열 안의 줄바꿈과 쌍따옴표를 이스케이프 처리한다 그래서 레코드 를 사용하면서 String 값을 제대로 확인하고싶으면

  ```java
  Long templateId,
  @JsonRawValue // 이 어노테이션 추가 하면 제대로 된 String 내용만 확인할 수 있다
  String templateContent,
  LocalDateTime createdAt,
  LocalDateTime updatedAt
  ```

  </details>
  <details>
  <summary>xml mapping 문제</summary>

  - SpringBoot 는 기본적으로 Application 과 같은 레벨이나 하위 패키지의 bean을 어노테이션에 따라 자동 스캔한다
  - 하지만 이걸 직접적으로

  ```java
  @MapperScan("com.linkee.linkeeapi.inquiry.mapper")
  @EnableJpaAuditing
  @SpringBootApplication
  public class LinkeeApiApplication {

      public static void main(String[] args) {
          SpringApplication.run(LinkeeApiApplication.class, args);
      }
  }
  ```

  이렇게 매핑해버리면 mapper 스캔을 저 디렉토리 안에서만 하게된다

  그래서 다른 xml이 생기면 찾지를 못한다

  - 폴더 구조가 application 보다 상위로 올라올 경우나
  - 특별한 경우는 빼고는 MapperScan 을 직접적으로 하지않는게 좋을 거 같다

  </details>
  <details>
  <summary>IntelliJ 테스트 문제</summary>

  - 인텔리J 그래들 환경에서 테스트를 진행하는데
  - xml, yml , gradle , 파일 경로 설정 까지 다 맞지만
  - 계속 ClassNotFoundException 발생
  org.gradle.internal.UncheckedException: java.lang.ClassNotFoundException: com.example.ApplicationTests
  - 이 원인은 다음과 같다.
  - 인텔리제이 한글깨짐을 방지하려고 vm옵션에 다음과 같이 설정해두었다.

  ```
  Dfile.encoding=UTF-8
  Dconsole.encoding=UTF-8
  ```

  - 해당 옵션을 사용하면 스프링 프로젝트가 위치한 폴더,   상위폴더에 한글이 있으면 오류가 발생한다.
  - 따라서 한글설정을 하고 싶은경우
  - vm옵션을 지정하고, 폴더경로에 한글 폴더가 없게한다.
  
  인텔리제이 설정 →
![](https://velog.velcdn.com/images/dhkdwlsgod/post/d6781320-b39d-41bf-b14a-b248a1e6bb50/image.png)
![](https://velog.velcdn.com/images/dhkdwlsgod/post/e85ddcd9-8c70-4d46-b91a-003ca9f0148b/image.png)
  - 다음과 같이 설정해주면 된다.
  - 어지간하면 프로젝트는 한글이름이들어간 경로로 설정하지말자….
  </details>

  <details>
  <summary>requestDTO 같을 못받아오는 문제</summary>

  이런식으로 request 객체를 직접 받아와서 썼는데
  
  ```java
  @GetMapping
      public PageResponse<ChatMessageResponse> selectAllChatMessage(ChatMessageSearchRequest request){

  //        System.out.println("================================\n컨트롤러 " + request.getPage() + " " +request.getSize());
          return service.selectAllChatMessage(request);
      }
  ```

  ```java
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public class ChatMessageSearchRequest {

      private String keyword;
      private Integer page;
      private Integer size;
      private Integer offset;
      private Long chatRoomId;
      private Long senderId;
  }
  ```

  아무리 url 에 맞게 작성해서 넘겨도
  null 값이 넘어온다
  변수명 매핑 정확히 일치하지만 값이 controller 부터 들어오지 않았다
  — 
  이건 간단 한거였는데
  request로 받아올때
  @Setter  가 존재하지않아서
  받아와도 값을 저장하지 못해서 계속해서 null 값이 들어오는 것

  ```java
  @Setter
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public class ChatMessageSearchRequest {

      private String keyword;
      private Integer page;
      private Integer size;
      private Integer offset;
      private Long chatRoomId;
      private Long senderId;
  }
  @Setter 추가하면 값이 제대로 저장되서 넘어오는걸 확인할 수 있다
  ```
  </details>

  <details>
    <summary>redis 가 동작은하지만 값을 확인못하는 문제</summary>
    도커로 레디스를 동작시켜서 값을 저장하거나 불러오는게 잘 동작하는걸 확인 했다 하지만 cmd keys *  를 통해 값이 확인이 안되고 어떤 select 1 ~15 db에서도 확인이 안된다 포트도 기본포트 , 따로 설정한 db 값도 없고 id값도 없지만 확인이 안된다 127.0.0.1:6379> keys * (empty array)

  해결방안
  - 동작은 문제없이 돌아간다 하지만
  - 아무리해도 값을 확인을 못하는건
  - 다른 레디스를 바라보고 있던 것

  ```java
  netstat -ano | findstr :6379 // 현재 6379 포트를 쓰고있는걸 확인

  netstat -ano | findstr :6379
    TCP    0.0.0.0:6379           0.0.0.0:0              LISTENING       4636
    TCP    127.0.0.1:6379         127.0.0.1:55997        ESTABLISHED     4636
    TCP    127.0.0.1:6379         127.0.0.1:56029        ESTABLISHED     4636
    TCP    127.0.0.1:55997        127.0.0.1:6379         ESTABLISHED     21392
    TCP    127.0.0.1:56029        127.0.0.1:6379         ESTABLISHED     8200
    TCP    [::]:6379              [::]:0                 LISTENING       4636
    PS C:\WINDOWS\system32> tasklist | findstr 4636
    redis-server.exe              4636 Services                   0      4,472 K
    Notion.exe                   14636    Console                    1     45,436 K

    도커가아닌 윈도우 로컬 환경에서 redis가 동작하고 있는것이     확인된다
    그래서 지금 값이 도커 레디스가 아닌
    윈도우 로컬에 실행중인 레디스에 값이 저장되던 것

    kill -9 4636

    해당 프로세스 강제종료 후
    도커로 실행 하면 값이 제대로 잘 들어가는 것을 알 수 있다

    127.0.0.1:6379> keys *
    1) "TEST_KEY"
    2) "refresh:rrr223@ssdds.com"
    ```

  </details>

  <details>
    <summary>spring User 객체를 Custom으로 사용할때 @Builder 를 사용못하는 문제</summary>
    스프링 시큐리티를 사용하면서 UserDetails 객체를 커스텀해서 사용하기 위해 User 를 상속받는 CustomUser 객체를 선언했다 하지만 lombok의 @Bulider 를 사용하려고 하니
  
    ```java
    @Builder
    ^
    return type CustomUserBuilder is not compatible with UserBuilder
    ```
    라는 오류 발생

    찾아보니 이건
    User 라는 스프링에서 제공하는 클래스를 상속받지만
    User 클래스에는 @Builder를 사용하지 않아서
    하위 클래스에서도 인식을 못하는 문제가 발생하는 것

    해결방법
  
    ```java
    return CustomUser.builder()
                .userId(user.getUserId())
                .username(user.getUserEmail())
                .password(user.getUserPassword())
                .authorities(user.getUserRole().name()) // role 그대로
                .build();
                
                
    이렇게 빌더를 사용하는 대신


  return new CustomUser(
                user.getUserId(),
                user.getUserEmail(),
                user.getUserPassword(),
                user.getUserRole().name()
        );
    원래의 방식대로 생성한다
  ```
  </details>

  <details>
  <summary>일반 유저와 소셜로그인을 같이 사용할 때 필요 컬럼값이 달라 초기화 오류</summary>
  
  일반유저와 소셜로그인을 같이 구현할 때 가져오는 api에 따라 필요값이 달라진다
  - 일반유저 → 로그인아이디를 email로 사용 , pw : 값 필수
  - 소셜유저(네이버) → pw : 초기값이 들어가면 x , loginId 값이 따로 들어가야함

  이런식으로 필요한 값들이 조금씩 달라서 유저객체를 생성자를 통해 생성할 때 오류가 발생한다 이를 해결하기위해 생성자가 아닌 메서드 형태로 일반 , 소셜 로그인을 따로 만들어서 값을 초기화 시킨다

  ```java
  Entity 에 생성자가 아닌 static 메서드 형태로 정의

  // 일반 회원가입용
  public static User createNormalUser(String email, String password, String nickname) {
    return User.builder()
            .userEmail(email)
            .userPassword(password)
            .userNickname(nickname)
            .userRole(Role.USER)
            .userStatus(Status.Y)
            .build();
  }

  // 소셜 로그인용
  public static User createSocialUser(String loginId, String email, String nickname) {
    return User.builder()
            .userLoginId(loginId)
            .userEmail(email)
            .userNickname(nickname)
            .userPassword("")  // 소셜 로그인은 비밀번호 없음
            .userRole(Role.USER)
            .userStatus(Status.Y)
            .build();
  }

  // ///////////////////////
  호출
  new User() 가 아니고

  일반유저
  User user = User.createNormalUser(request.getUserEmail(), encodedPassword, request.getUserNickname());

  소셜유저
  User user = userRepository.findByUserEmail(email)
  .orElseGet(() -> userRepository.save(User.createSocialUser(naverId, email, name)));
  ```
  </details>

<br>

 - 유한세
    <details>
    <summary>로그인 및 JWT인증 관련 문제</summary>

    - 문제
      - JWT 토큰을 사용한 로그인 후, 인증되지 않은 사용자 API에 접근할 수 있는 문제
    - 원인
      - 로그인 후 accessToken이 제대로 localStorage에 저장되지 않음
      - 토큰을 헤더에 포함시켜 요청하는 과정이 누락됨
    - 해결
      - 로그인 후 JWT 토큰 저장

    ```java
    window.onload = () => {
            //URL 파라미터에서 토큰/이메일 추출
            const params = new URLSearchParams(window.location.search);
            const token = params.get('token');
            const email = params.get('email');
            if (token && email) {
                localStorage.setItem('accessToken', token);
                localStorage.setItem('email', email);
                // URL에서 쿼리 제거
                window.history.replaceState({}, document.title, "/");
            }
            checkLogin();

    ```

    - API  요청 시 JWT 토큰 포함
    
    ```java
    
    // 요청을 보낼 때 Authorization 헤더에 토큰을 포함시킴
    const token = localStorage.getItem('accessToken');
    fetch('https://example.com/api/data', {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
    .then(response => response.json())
    .then(data => console.log(data));

    ```

    - 서버 측 JWT인증 필터

    ```java
    // JWT 인증 필터 (Spring Security)
    public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtTokenProvider.resolveToken(request);
        if (token != null && jwtTokenProvider.validateToken(token)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
          }
          filterChain.doFilter(request, response);
        }
    }

    ```

     - JWT 토큰 제공
     ```java
     // JWT 토큰 생성
      public String createToken(String username) {
          return Jwts.builder()
                  .setSubject(username)
                  .setExpiration(new Date(System.currentTimeMillis() + 864_000_000)) // 만료시간 10일
                  .signWith(SignatureAlgorithm.HS256, secretKey)
                  .compact();
      }

      ```


    </details>

    <details>
    <summary>소셜 로그인 (OAuth2)설정 오류</summary>
  
    -  문제
        - OAuth2 로그인 후 네이버 인증이 제대로 동작하지 않거나,  리 디렉션이 잘못되는 문제
    
    - 해결방법
      - OauthController에서 `OAUTH2_AUTHORIZATION_BASE_URI + "/" + registration.getRegistrationId()` 수정

      ```java
      @GetMapping("/login")
      public String getLoginPage(Model model) {
          Map<String, String> oauth2AuthenticationUrls = new HashMap<>();

          if (clientRegistrationRepository instanceof Iterable<?>) {
              Iterable<ClientRegistration> clientRegistrations =
                      (Iterable<ClientRegistration>) clientRegistrationRepository;

              System.out.println("✅ [DEBUG] 등록된 OAuth2 클라이언트 목록:");
              clientRegistrations.forEach(registration -> {
                  System.out.println(" - " + registration.getClientName() + " (" + registration.getRegistrationId() + ")");
                  oauth2AuthenticationUrls.put(
                          registration.getClientName(),
                          OAUTH2_AUTHORIZATION_BASE_URI + "/" + registration.getRegistrationId()
                  );
              });
          } else {
              System.out.println("❌ [DEBUG] clientRegistrationRepository 가 Iterable 이 아닙니다!");
          }

          model.addAttribute("oauth2AuthenticationUrls", oauth2AuthenticationUrls);
          return "login";
      }
      ```

    - 네이버 Application에서 Callback-uri 재설정
    - redirect-uri OAuth설정에 맞게 수정정
    </details>

    <br>

  - 김진
    <details>
    <summary>순환참조 관련 오류</summary>

    - 문제
    
    **빈(Bean) 순환 참조(circular dependency)** 로 인해 스프링 컨테이너가 애플리케이션을 구동하지 못함
        

    - 원인
        
        QuizRoomCommandController
        ↓ 의존
        QuizRoomCommandServiceImpl
        ↓ 의존
        QuizGameAdvanceScheduler
        ↓ 다시 의존
        QuizRoomCommandServiceImpl (또는 Controller)
        

    - 해결 방안
        
        **@Lazy 사용** 
        
        `@Lazy`로 순환 참조 지연 로딩 처리 가능
        
        ```java
        @Service
        public class QuizRoomCommandServiceImpl {
        private final QuizGameAdvanceScheduler scheduler;
        public QuizRoomCommandServiceImpl(@Lazy QuizGameAdvanceScheduler scheduler) {
        this.scheduler = scheduler;
        }
        }
        ```
        
        QuizGameAdvanceScheduler 빈이 생성될 때 QuizRoomCommandService의 실제 인스턴스를 즉시 생성하여 주입하는 대신, QuizRoomCommandService의 프록시(Proxy) 객체를 먼저
        주입합니다. 그리고 QuizGameAdvanceScheduler 내에서 quizRoomCommandService 필드가 실제로 사용되는 시점(즉, quizRoomCommandService.someMethod()와 같이 메서드가
        호출될 때)에 비로소 QuizRoomCommandService의 실제 인스턴스가 생성되고 프록시가 이를 통해 호출을 위임하게 됩니다.
    </details>

    <br>

  - 김명진
    <details>
    <summary>-</summary>
    </details>

    <br>

  - 정동욱
    <details>
    <summary>알림 발송 관련 단방향 및 양방향 통신</summary>
      
      - 문제

        -  알림을 양방향 통신으로 할 경우 서버에 부하가 심해질 것으로 예상

    - 해결

      - 처음에 redis를 사용해서 양방향 통신으로 구현하였지만 팀원들과 이야기를 해보니 서버에 부하가 심할 것 같다고 판단되어 단방향 통신으로 SSE로 구현하여 수정
    </details>

    <details>
    <summary>친구 요청 시 중복 데이터 </summary>

    - 문제

      - 예를 들어 1번 유저가 2번 유저에게 친구를 보내고 반대로 2번 유저가 1번 유저에게 친구를 보내면 DB에 같이 저장이 되는 문제를 발견

    - 해결

      - 1, 2 or 2, 1 이렇게 DB에 저장되어있는 값을 확인하고 만약 있다면 해당 값으로 업데이트 진행하는 방식으로 해결

      ```java
      // 두 사용자 간의 관계가 이미 존재하는지 확인
        Optional<Relation> optionalRelation = relationRepository.findByUsers(requester, receiver);

        if (optionalRelation.isPresent()) {
            // 관계가 이미 존재하면 , 재활용
            Relation existingRelation = optionalRelation.get();

            switch (existingRelation.getRelationStatus()) {
                case A:
                    throw new BusinessException(ErrorCode.RELATION_ALREADY_EXISTS);
                case P:
                    throw new BusinessException(ErrorCode.RELATION_REQUEST_ALREADY_EXISTS);
                case R:
                    // 거절된 상태에서 다시 요청하는 경우, 기존 데이터를 업데이트
                    existingRelation.reRequest(requester, receiver);
                    relationRepository.save(existingRelation);
                    break;
            }
        } else {
            Relation relation = Relation.builder()
                    .requester(requester)
                    .receiver(receiver)
                    .build();

            relationRepository.save(relation);
        }
      ```
    </details>









  











---

## 🍺 6. 프로젝트 회고록

