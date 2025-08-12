![header](https://capsule-render.vercel.app/api?type=slice&color=auto&height=300&section=header&text=zip.lab&desc=noodle.zip&fontSize=90&fontAlign=60&fontAlignY=25&descSize=40&descAlign=70&descAlignY=40&rotate=20) 

<p align="center">
  <a href="#누들집소개">🍜 서비스 소개</a> <br>
  <a href="#담당기능">📚 담당 기능</a> <br>
  <a href="#기술스택">🛠 기술 스택</a> <br>
  <a href="#시스템구조">⚙️ 시스템 구조</a> <br>
  <a href="#개발일정">⏳ 개발 일정</a> <br>
</p>

<h1 align="center" id="누들집소개">🍜 noodle.zip 서비스 소개</h1>

<div align="center">
"나에게 딱 맞는 라멘 맛집, 어떻게 찾을 수 있을까?"<br>
기존 맛집 서비스들이 제공하는 모호한 정보와 라멘 전문성에 대한 갈증에서 <b>noodle.zip</b>은 시작되었습니다.<br>
대한민국 라멘 애호가들이 위치 기반으로 주변 라멘집을 쉽게 찾고,<br>
영수증 인증을 통해 신뢰성 있는 리뷰를 작성하며,<br>
다양한 토핑 태그로 취향에 맞는 라멘을 탐색할 수 있는 전문 커뮤니티를 목표로 합니다.

<h1 align="center" id="담당기능">📚 담당 기능</h1>

## 🧑‍💻 유저 도메인

[소스 코드](https://github.com/innotation/final-noodle-zip/tree/main/src/main/java/noodlezip/user) 

이메일 기반의 회원가입을 통해 사용자에게 안전하고 신뢰성 있는 서비스를 제공합니다.<br>
사용자는 회원가입 페이지에서 정보를 입력하고, 전송된 이메일을 통해 계정을 활성화합니다.<br>
### 📄 회원가입 페이지
사용자는 회원가입 페이지에서 이메일 주소와 비밀번호를 입력하여 가입을 신청합니다. <br>
비밀번호는 보안을 위해 해시(Hash) 처리하여 데이터베이스에 저장합니다.<br>
가입 버튼을 누르면 입력된 정보가 서버로 전송되고, 이메일과 코드는 Redis에 저장됨과 동시에 인증 메일이 발송됩니다.<br>
<img width="2828" height="2342" alt="회원가입" src="https://github.com/user-attachments/assets/5db20416-623b-4d23-b345-203e1268b50b"/><br>
### 📨 이메일 인증
사용자가 가입 시 입력한 이메일로 인증 메일이 전송됩니다. 이메일에는 계정 활성화를 위한 '인증하기' 버튼이 포함되어 있습니다. <br>
사용자가 이 버튼을 클릭하면, redis 서버에서 이메일의 인증키를 확인하고 mysql 데이터베이스의 이메일 인증 속성을 '활성화' 상태로 변경합니다. <br>

<img width="381" height="330" alt="이메일 인증" src="https://github.com/user-attachments/assets/3fc25591-3bae-4cbb-a254-6e09fcb4b7b1" />

## 📝 커뮤니티 도메인

[소스 코드](https://github.com/innotation/final-noodle-zip/tree/main/src/main/java/noodlezip/community)

### 📌 게시판 리스트

게시판 리스트 화면을 통해 다양한 게시글을 한눈에 확인할 수 있습니다. <br>
사용자는 최신순, 좋아요순 등 원하는 정렬 방식으로 게시글을 탐색할 수 있으며, 검색 기능을 통해 특정 정보를 쉽게 찾을 수 있습니다. <br>

### ✍️ 게시글 작성

사용자는 에디터를 이용하여 자유롭게 새로운 게시글을 작성할 수 있습니다. <br>
게시글에는 제목, 내용, 이미지 등을 포함할 수 있습니다. <br>

<img width="828" height="724" alt="게시글작성" src="https://github.com/user-attachments/assets/11405f5d-e032-48cf-b815-62dfaed260a2" /><br>

### 💬 댓글 및 좋아요

게시글에 대한 다른 사용자들의 의견을 댓글로 확인할 수 있습니다. <br>
댓글을 통해 서로 소통하고 궁금한 점을 해결하며 커뮤니티를 활성화합니다. <br>
또한, 공감하는 게시글에 '좋아요'를 눌러 긍정적인 피드백을 전달할 수 있습니다. <br>

<img width="2828" height="4878" alt="게시글 상세보기" src="https://github.com/user-attachments/assets/ec52ecee-a409-42c2-bdb6-b20183898df8" /><br>

### 🔍 검색 및 필터링

사용자는 게시판 상단 검색창을 이용하여 원하는 키워드로 게시글을 검색할 수 있습니다. <br>
카테고리별로 필터링 및 카테고리 태그(예: 라멘)를 클릭시 해당 태그가 포함된 게시글만 모아볼 수 있어 정보 탐색의 효율성을 높입니다.<br>

### ⏰ 최근 본 게시물

사용자의 브라우저 쿠키를 기반으로 최근에 본 게시물 목록을 제공합니다. <br> 
이를 통해 사용자는 이전에 확인했던 게시물로 손쉽게 다시 접근할 수 있어 편리합니다.<br>

<img width="2828" height="4098" alt="게시판 리스트 화면" src="https://github.com/user-attachments/assets/b76e7b33-2feb-48e0-bc0d-abff8ef141a7"/>

## 🛠️ 공통 모듈

[소스 코드](https://github.com/innotation/final-noodle-zip/tree/main/src/main/java/noodlezip/common)

### 공통 모듈 작성 및 모듈화: 이미지 업로드, API 응답, 예외 처리 모델 등

### 주요 기술 및 기능:

#### Spring Security: 사용자 인증 및 권한 관리

#### DB 연결: 데이터베이스 연동

#### AOP: Aspect-Oriented Programming을 활용한 공통 로직 처리

#### 스케줄링: 배치 작업 및 주기적 작업 자동화

#### 메일: 이메일 발송 기능

#### Redis: 캐싱, 세션 관리 등

#### 쿠키: 사용자 정보 저장 및 관리

## 🖥️ 서버 구조 및 CI/CD

### 서버 구조 - NCP (Naver Cloud Platform) 클라우드 환경 구성

<img width="798" height="455" alt="서버구조도_impv" src="https://github.com/user-attachments/assets/1e59ebad-b904-4e7d-aed8-5c510931dca2" />

### CI/CD -  Github Actions 지속적 통합 및 배포 자동화

[소스 코드](https://github.com/innotation/final-noodle-zip/blob/main/.github/workflows/docker-CICD.yml)

### Docker -  컨테이너 기반 배포 환경 구축

[소스 코드](https://github.com/innotation/final-noodle-zip/blob/main/Dockerfile)

### 부하 테스트 - nGrinder 성능 및 부하 테스트 진행

<img width="416" height="360" alt="동접자30명1분" src="https://github.com/user-attachments/assets/e81d1ed2-bff6-4890-b2a6-4f65b989446c" />

<img width="437" height="364" alt="동접자100명1분" src="https://github.com/user-attachments/assets/1745899c-4976-4370-b3e8-f64efbf88143" />

<img width="424" height="378" alt="동접자1000명 1분" src="https://github.com/user-attachments/assets/10f73489-d65f-46c6-982e-3eca8333e3c3" />


<h1 id="기술스택">🛠 기술 스택</h1>
<table>
    <thead>
        <tr>
            <th>Category</th>
            <th>Stack</th>
        </tr>
    </thead>
    <tbody>
    <tr>
            <td>
                <p align=center>View</p>
            </td>
            <td>
                <img src="https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=Thymeleaf&logoColor=white">
                <img src="https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white">
                <img src="https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white">
                <img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=JavaScript&logoColor=white">
            </td>
        </tr>
        <tr>
            <td>
                <p align=center>Backend</p>
            </td>
            <td>
                <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white">
                <img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&logoColor=white">
                <img src="https://img.shields.io/badge/spring_boot-boot?style=for-the-badge&logo=springboot&logoColor=white&logoSize=auto&labelColor=6DB33F&color=6DB33F&cacheSeconds=3600">
                <img src="https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=Spring_Security&logoColor=white">
            </td>
        </tr>
        <tr>
            <td>
                <p align=center>DB</p>
            </td>
            <td>
                <img src="https://img.shields.io/badge/ncp%20S3-569A31?style=for-the-badge&logo=Amazon%20S3&logoColor=white">
                <img src="https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white">
                <img src="https://img.shields.io/badge/spring_data_jpa-spring?style=for-the-badge&logo=spring&logoColor=white&logoSize=auto&labelColor=6DB33F&color=6DB33F&cacheSeconds=3600">
                <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=Redis&logoColor=white">
            </td>
        </tr>
        <tr>
            <td>
                <p align=center>형상 관리</p>
            </td>
            <td>
                <img src="https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white">
            </td>
        </tr>
        <tr>
            <td>
                <p align=center>CI/CD</p>
            </td>
            <td>
                <img src="https://img.shields.io/badge/nginx-%23009639.svg?style=for-the-badge&logo=nginx&logoColor=white">
                <img src="https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white">
                <img src="https://img.shields.io/badge/GitHub_Actions-2088FF?style=for-the-badge&logo=github-actions&logoColor=white">
            </td>
        </tr>
        <tr>
            <td>
                <p align=center>API</p>
            </td>
            <td>
                <img src="https://img.shields.io/badge/naver_cloud_platform-ncp?style=for-the-badge&logo=naver&logoColor=green&logoSize=auto&labelColor=white&color=03C75A&cacheSeconds=3600">
            </td>
        </tr>
        <tr>
            <td>
                <p align=center>Test</p>
            </td>
            <td>
                <img src="https://img.shields.io/badge/junit5-test?style=for-the-badge&logo=junit5&logoColor=white&logoSize=auto&labelColor=25A162&color=DC524A&cacheSeconds=3600">
            </td>
        </tr>
        <tr>
            <td>
                <p align=center>Workspaces</p>
            </td>
            <td>
                <img src="https://img.shields.io/badge/IntelliJ_IDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white">
                <img src="https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white">
            </td>
        </tr>
        <tr>
            <td>
                <p align=center>협업</p>
            </td>
            <td>
                <img src="https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white">
                <img src="https://img.shields.io/badge/GIT-E44C30?style=for-the-badge&logo=git&logoColor=white">
                <img src="https://img.shields.io/badge/Notion-%23000000.svg?style=for-the-badge&logo=notion&logoColor=white">
            </td>
        </tr>
    </tbody>
</table>

<h1 id="시스템구조">⚙️ 시스템 구조</h1>

## 📄 [ERD 보기](https://www.erdcloud.com/d/3fXPuFYXD6sekZqTj)<br>
<img width="1352" height="666" alt="Image" src="https://github.com/user-attachments/assets/f698eca5-67fe-41c8-bd56-2bbeca4a8008"/>

<h1 id="개발일정">⏳ 개발 일정 (WBS 기반)</h1>
우리는 총 8주의 개발 일정으로 프로젝트를 진행하고 있어요.<br>
GitHub의 마일스톤으로 주차별 진행 상황을 관리하고, Issues와 Projects를 활용해 세부 작업을 분담하고 추적하고 있습니다.<br>
더 자세한 개발 일정은 아래 링크에서 확인하실 수 있어요!<br>
<a href="https://docs.google.com/spreadsheets/d/1pQlDkWOhMYdiOyYTpTDqrHLObNjFpZxG6JRY0jxXw2w/edit?usp=sharing">
➡️ 개발 일정 스프레드시트 보기
</a>
</div>




