<h2>💎 프로젝트 명 : DAITEM</h2>
<p>
악세서리 e-commerce 플랫폼을 구축하였습니다. <br>
기존 monolothic 서비스를 분산 환경으로 전환하여 대용량 트래픽을 효과적으로 처리할 수 있도록 설계하였습니다.
</p>

<h3> ⚒️ 설계</h3>
<img width="724" alt="스크린샷 2024-05-17 오전 7 04 25" src="https://github.com/velyvel/daitem_msa/assets/110210134/d6437a07-77e4-4136-bb8c-9327372d462c">

<h3>📍 실행 방법 </h3>
<p>
1. Docker가 설치되어 있어야 합니다. <br>
2. Docker에 mySQL, Redis, kafka 이미지가 설치되어 있어야 합니다<br>
3. 로컬 실행 환경 : git fork 하여 프로젝트 root 위치로 가 Docker compose up 명령어 입력해 주세요
4. 로컬에서 실행 후 http://localhost:8083/swagger-ui/index.html 로 접속하면 테스트 할 수 있는 환경이 제공됩니다.
</p>

<h3> 📝 API 명세서 </h3>
<img >
<a>링크를 확인해 주세요</a>

<h3> 기술적 의사결정</h3>
<p> 
[로그인] Refresh Token과 Access Token은 같이 응답값으로 내려 주어야 하는가?<br>
그렇다면 토큰에는 무슨 정보가 담겨야 할까?<br>
[아키텍쳐]왜 MSA로 전환했을까?<br>
[동시성 처리] lock에서 messsage 방식으로, kafka까지 적용해보기<br>

<h3> 트러블 슈팅 </h3>
[동시성 처리]
문제 상황 : @Transactional 레벨에서의 동시성 처리, 레이스 컨디션으로 인한 초과
해결 방법 : 다양한 lock 적용 -> 이벤트 방식으로 바꿈 -> kafka 적용

[데이터 일관성 유지]
문제 상황 : 재고관리 서비스 중
해결 방법 : 

[성능 향상]


[일시적 장애 해결]
문제상황 :
해결 방법 :

<hr>
회고록 보기
