# JavaBird



<img src="https://img.shields.io/badge/Java-7F52FF?style=for-the-badge&Kotlin=Swift&logoColor=white"/><img src="https://img.shields.io/badge/MSA-757575?style=for-the-badge"/><img src="https://img.shields.io/badge/Spring-34A853?style=for-the-badge&logo=spring&logoColor=white"/><img src="https://img.shields.io/badge/MySQL-FFCA28?style=for-the-badge&logo=MySQL&logoColor=black"/>


> 개발 기간: 2024.05 ~ 2024.06



<br>

# 📖 프로젝트 소개
> SNS의 기능들을 만드는 백엔드 프로젝트입니다.



# ✨ 마이크로 서버 별 주요 기능
## eureka-service
- 서비스 등록, 회복 등
## gateway-service
- JWT 토큰 인증
  - gateway filter를 이용하여 원하는 요청에 필터를 적용하여 인증을 할수 있습니다.
- 라우팅
- 글로벌 에러 핸들링
## config-service
1. 설정 정보의 중앙 집중화 관리
   - Spring Cloud Config를 통해 모든 서비스의 설정 정보를 중앙 저장소(Git 등)에서 관리함으로써 일관성을 유지할 수 있습니다.
2. 동적 구성 업데이트
   - Spring Cloud Bus와 RabbitMQ를 활용하여 설정 변경 사항을 실시간으로 모든 서비스에 전달할 수 있습니다. /busrefresh 엔드포인트를 Post방식으로 호출하여 모든 변경된 설정을 전달하고 업데이트가 가능합니다.
## user-service
- 회원가입
   - 데이터베이스에 유저 정보 저장
- 로그인(JWT 토큰방식)
   - 스프링 시큐리티의 필터를 이용해서 login 엔드포인트에만 인증하도록 했습니다.
- 모든 사용자
- 사용자 1명 상세보기
   - FeignClient를 이용해서 사용자의 게시물 정보도 가져옵니다.
## post-service
- 게시물 등록, 삭제, 일기
- 게시물에 좋아요 기능
   - 동시성 문제를 해결하기 위한 Kafka 사용했습니다.

# 트러블 슈팅
- [게시물에 좋아요 기능 동시성 문제](https://flashdragon.tistory.com/159)

</br>


