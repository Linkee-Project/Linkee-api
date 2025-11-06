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
  WBS를 자세히 보려면 [여기](https://www.notion.so/25b36d2af8f580fab019e6d2211cd7a5?v=25b36d2af8f5802b830b000cb9f5aefe&source=copy_link)를 클릭하세요요



- ### 📚 요구사항 명세서

  요구사항 명세서를 자세히 보려면 [여기](https://docs.google.com/spreadsheets/d/1gjJ8mYfv-hq05CkPhD09bkD4a0_2K8PejZSjfwREuMI/edit?gid=1507918672#gid=1507918672)를 클릭하세요
  
  
- ### 🖼️ UML **(Unified Modeling Language)**

  UML을 자세히 보려면 [여기](https://www.canva.com/design/DAGw914rlWc/A6U_UKm-gqUP5vZRQ2hHxA/edit)를 클릭하세요



- ### 🗺️ ERD **(Entity Relationship Diagram)**

  ERD를 자세히 보려면 [여기](https://www.erdcloud.com/d/cTij9aNCYr9CxJZnf)를 클릭하세요

  
- ### 🗞️ 테스트 케이스 작성 및 테스트
  테스트 코드를 자세히 보려면 db > domain 을 확인해주세요
  
- ### 🔁 통합테스트 시나리오 및 코드
  통합테스트 시나리오를 자세히 보려면 [여기](https://www.notion.so/26136d2af8f5802e914afbc54cf37e47?source=copy_link)를 클릭하세요  
  
---
## ⚠️ 5. Trouble Shooting



---

# 🍺 6. 프로젝트 회고록

