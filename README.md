![header](https://capsule-render.vercel.app/api?type=slice&color=auto&height=300&section=header&text=zip.lab&desc=noodle.zip&fontSize=90&fontAlign=60&fontAlignY=25&descSize=40&descAlign=70&descAlignY=40&rotate=20) 

<p align="center">
  <a href="#누들집소개">🍜 서비스 소개</a> <br>
  <a href="#기술스택">🛠 기술 스택</a> <br>
  <a href="#시스템구조">⚙️ 시스템 구조</a> <br>
  <a href="#팀원소개">👥 팀 구성 및 역할</a> <br>
  <a href="#개발일정">⏳ 개발 일정</a> <br>
</p>

<h1 align="center" id="누들집소개">🍜 noodle.zip 서비스 소개</h1>

<div align="center">
"나에게 딱 맞는 라멘 맛집, 어떻게 찾을 수 있을까?"<br>
기존 맛집 서비스들이 제공하는 모호한 정보와 라멘 전문성에 대한 갈증에서 <b>noodle.zip</b>은 시작되었습니다.<br>
대한민국 라멘 애호가들이 위치 기반으로 주변 라멘집을 쉽게 찾고,<br>
영수증 인증을 통해 신뢰성 있는 리뷰를 작성하며,<br>
다양한 토핑 태그로 취향에 맞는 라멘을 탐색할 수 있는 전문 커뮤니티를 목표로 합니다.

## 📍 지도 기반 라멘 검색
현재 위치를 기반으로 주변 라멘집을 찾고,지역, 토핑, 운영시간 등<br>
상세한 조건으로 필터링하여 원하는 라멘집을 쉽게 발견할 수 있어요.<br>
![화면 기록 2025-07-24 오후 5 17 04](https://github.com/user-attachments/assets/b8cdb62e-9aa1-4dd2-8683-cfbf88b03231)<br>
## 🧾 영수증 OCR 리뷰 등록 
영수증 자동 인식(OCR) 기능을 통해 간편하게 리뷰를 작성하고,<br>
신뢰성 있는 정보를 공유할 수 있습니다.<br>
![영수증리뷰](https://github.com/user-attachments/assets/65c94177-d80d-4a9a-b864-1aa93b83b78b)<br>
## 🏆 뱃지 시스템
활발한 활동을 통해 다양한 뱃지를 획득하고, <br>
라멘 전문가로 성장하는 재미를 느껴보세요.<br>
![배지시스템](https://github.com/user-attachments/assets/a2d1e973-7b8a-4c3c-a49b-a9542314347d)<br>
## 💬 커뮤니티 기능
리뷰, 질문, 자유로운 소통을 위한 커뮤니티 탭에서 다른 라멘 애호가들과 정보를 교환하고 교류할 수 있어요.<br>
![커뮤니티](https://github.com/user-attachments/assets/8f257ae1-3159-4d76-bafa-6e6ffd1c2e57)<br>
## 🧑 마이페이지
내가 작성한 글, 저장한 매장, 구독 정보, 좋아요 등을 한눈에 관리할 수 있습니다.<br>
![마이페이지](https://github.com/user-attachments/assets/caea5ef2-8abb-4780-a3dc-de7d052223c4)<br>
## 🧑‍💼 관리자 페이지
매장 승인, 사용자 신고 제재 등 서비스 운영에 필요한 다양한 관리 기능을 제공합니다.<br>
![어드민](https://github.com/user-attachments/assets/fac20db7-d3ef-4462-a7ca-e7ba5a5669c0)<br>
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

📄 [ERD 보기](https://www.erdcloud.com/d/3fXPuFYXD6sekZqTj)<br>
<img width="1352" height="666" alt="Image" src="https://github.com/user-attachments/assets/f698eca5-67fe-41c8-bd56-2bbeca4a8008"/>
🧩 [아키텍처 다이어그램 보기]<!--(./docs/architecture.svg)-->
<img width="1446" height="931" alt="서버구조도" src="https://github.com/user-attachments/assets/f87f0002-c3bf-4e23-9593-102cd7fbd332"/>

<hr>

<h1 id="팀원소개">👥 팀 구성 및 역할</h1>


|                                               [@kakaba-hub](https://github.com/kakaba-hub)                                                |                                                  [@sotogito](https://github.com/sotogito)                                                  |                                               [@innotation](https://github.com/innotation)                                                |                                                   [@ssb7779](https://github.com/ssb7779)                                                   |                                                   [@podoseee](https://github.com/podoseee)                                                   |
| :--------------------------------------------------------------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------------------------------------------------------------------: |
| <img src="https://github.com/kakaba-hub.png" width="80"> | <img src="https://github.com/sotogito.png" width="80"> | <img src="https://github.com/innotation.png" width="80"> | <img src="https://github.com/ssb7779.png" width="80"> | <img src="https://github.com/podoseee.png" width="80"> |
|                                                                 **라멘 검색 페이지, 매장 상세 페이지 개발**                                                                 |                                                                    **마이페이지, 배지, 저장 매장 기능**                                                                     |                                                                    **게시판, 유저 도메인, 공통 모듈  개발**                                                                     |                                                                    **관리자 기능, 헤더, 검색, OCR 리뷰 기능**                                                                     |                                                                    **매장 등록 페이지**                                                                     |
<hr>
<h1 id="개발일정">⏳ 개발 일정 (WBS 기반)</h1>
우리는 총 8주의 개발 일정으로 프로젝트를 진행하고 있어요.<br>
GitHub의 마일스톤으로 주차별 진행 상황을 관리하고, Issues와 Projects를 활용해 세부 작업을 분담하고 추적하고 있습니다.<br>
더 자세한 개발 일정은 아래 링크에서 확인하실 수 있어요!<br>
<a href="https://docs.google.com/spreadsheets/d/1pQlDkWOhMYdiOyYTpTDqrHLObNjFpZxG6JRY0jxXw2w/edit?usp=sharing">
➡️ 개발 일정 스프레드시트 보기
</a>
</div>

