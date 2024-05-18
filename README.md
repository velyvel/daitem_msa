<h2> 💎 프로젝트 명 : DAITEM</h2>
<p>
    <img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"
    alt="스프링">
    <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white"
    alt="MySQL">
    <img src="https://img.shields.io/badge/redis-%23DD0031.svg?&style=for-the-badge&logo=redis&logoColor=white"
    alt="레디스">
<img src="https://img.shields.io/badge/Apache%20Kafka-000?style=for-the-badge&logo=apachekafka"
    alt="카프카"><br>
악세서리 e-commerce 플랫폼을 구축하였습니다. <br>
기존 monolithic 서비스를 분산 환경으로 전환하여 대용량 트래픽을 효과적으로 처리할 수 있도록 설계하였습니다.
</p>

## 📋 목차 <br>
1. [⚒️ 설계](#-설계) <br>
2. [📍 실행 방법](#-실행-방법)<br>
3. [📝 API 명세서](#-API-명세서)<br>
4. [🧐 기술적 의사결정](#-기술적-의사결정)<br>
5. [💫 트러블 슈팅](#-트러블-슈팅)<br>
6. [🖊️ 코드컨벤션 & 협업 전략](#-코드컨벤션-&-협업-전략)<br>
7. <a href="https://devdevleyy.tistory.com/" target="_blank">😍 회고록</a><br>
contact here 👉🏻 <a href="mailto:eathergs012@gmail.com"> 📨 send me</a><br>

## ⚒️ 설계
<img width="724" alt="스크린샷 2024-05-17 오전 7 04 25" src="https://github.com/velyvel/daitem_msa/assets/110210134/d6437a07-77e4-4136-bb8c-9327372d462c">

## 📍 실행 방법
<p>
1. Docker가 설치되어 있어야 합니다. <br>
2. Docker에 mySQL, Redis, kafka 이미지가 설치되어 있어야 합니다<br>
3. 로컬 실행 환경 : git fork 하여 프로젝트 root 위치로 가 아래의 명령어를 입력해 주세요<br>
<pre>
    <code>
        docker-compose up
    </code>
</pre>
4. 다음 명령어로 로그를 확인해 주세요
<pre>
    <code>
        docker-compose logs
    </code>
</pre>
5. 테스트 종료 후 메모리 절약을 위해 컨테이너를 삭제 해 주세요
<pre>
    <code>
        docker-compose down --rmi all
    </code>
</pre>
<br>
4. 로컬에서 실행 후 
<a href="http://localhost:8083/swagger-ui/index.html">http://localhost:8083/swagger-ui/index.html</a> 로 접속하면 테스트 할 수 있는 환경이 제공됩니다.
</p>

## 📝 API 명세서
<a href="">링크를 확인해 주세요</a>
<img width="500" alt="스크린샷 2024-05-01 오전 11 39 16" src="https://github.com/velyvel/daitem_msa/assets/110210134/adfeabce-c31b-43b2-a6a0-5c18d04b63bb">

## 🧐 기술적 의사결정
<p> 
    <a href="https://devdevleyy.tistory.com/" target="_blank">
        [로그인] Refresh Token과 Access Token은 같이 응답값으로 내려 주어야 하는가? 그렇다면 토큰에는 어떤 정보를 담아야 할까?
    </a><br>
    <a href="https://devdevleyy.tistory.com/" target="_blank">
        [아키텍쳐]왜 MSA로 전환했을까?
    </a><br>
    <a href="https://devdevleyy.tistory.com/" target="_blank">
        [동시성 처리] lock에서 messsage 방식으로, kafka까지 적용해보기
    </a><br>
</p>

## 💫 트러블 슈팅

<details>
    <summary><span style="background-color:#FFE6E6">[동시성 처리]</span></summary>
    문제 상황 : @Transactional 레벨에서의 동시성 처리, 레이스 컨디션으로 인한 초과
    해결 방법 : 다양한 lock 적용 -> 이벤트 방식으로 바꿈 -> kafka 적용 <br>
    <a href="https://devdevleyy.tistory.com/" target="_blank">자세히 보기</a>
</details>

<details>
    <summary><span style="background-color:#FFE6E6">[데이터 일관성 유지]</span></summary>
    문제 상황 : 레디스를 사용하여 재고 조회를 하는데 레디스가 사라졌다?
    해결 방법 : read through + write around와 read through + write throw 비교 <br>
    <a href="https://devdevleyy.tistory.com/" target="_blank">자세히 보기</a>
</details>

<details>
    <summary><span style="background-color:#FFE6E6">[kafka 적용 후 이벤트가 작동이 안된다?]</span></summary>
    문제 상황 : 구매 취소 이벤트가 발생하지 않았다 
    해결 방법 : 토픽을 지정해 주었더니 해결했다 <br>
    <a href="https://devdevleyy.tistory.com/" target="_blank">자세히 보기</a>
</details>

## 🖊️코드컨벤션 & 협업 전략
<p>
    코드컨벤션은 <a href="https://github.com/google/styleguide" target="_blank">구글 자바 컨벤션</a>을 적용하였습니다.
    다른 점은 아래 조건과 같습니다.
    <br>
    1. 기존 tab size, indent(들여쓰기)가 2로 설정 되어 있어 4로 수정하였습니다.<br>
    2. try 문 괄호 이후 새로운 줄 안에 코드블럭이 생깁니다.
    <br><br>
    협업 전략은 다음과 같습니다. swagger-ui를 사용하여 가독성 있는 API 명세서를 만들었고, 
    <br>
    @SecurityRequirement를 활용하여 로그인이 필요한 상황을 파악하고 토큰을 넣어 간단하게 테스트 할 수 있도록 하였습니다.
    @Schema를 활용하여 dto 필드명과 특징을 볼 수 있게 구현하였습니다.
</p>
<hr>
