package com.example.webflux;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestService {
    private final UserRepository userRepository;
    private final WebClient webClient;
    private final String date = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    public Mono<IdDto> getId(String nickname) {
        return webClient.get().uri("/id?character_name=" + nickname)
                .retrieve().bodyToMono(IdDto.class)
                .onErrorResume(e -> Mono.empty());
    }

    public Mono<InfoDto> getInfo(String id) {
        String req = String.format("/character/basic?ocid=%s&date=%s", id, date);

        return webClient.get().uri(req)
                .retrieve().bodyToMono(InfoDto.class)
                .onErrorResume(e -> Mono.empty());
    }

    public Flux<User> getRankUser() {
        return userRepository.findAll().log("저장된 모든 유저 조회");
    }

    // 리액티브 스트림 구독을 위해 서비스 코드에서는 subscribe 호출 또는 리액티브 객체 타입의 반환이 필수적임
    // 리액티브 스트림의 구독 이전의 작업은 lazy 로 진행
    public Flux<User> saveId() {
        Flux<User> map = webClient.get().uri("/ranking/overall?date=" + date)
                .retrieve()
                .bodyToFlux(InfoResponseDto.class)
                .flatMap(infoResponseDto -> Flux.fromIterable(infoResponseDto.getRanking()))
                .map(RankInfo::toEntity);

        return userRepository.saveAll(map)
                .log("랭킹 200위 까지 저장").onErrorComplete();


        // 이렇게 하면 saveAll(map) 작업이 구독 이전에 lazy 로 작동하고
        // 비동기 방식이므로 Mono.just("성공")이 먼저 반환되며 구독되므로 save 작업이 완료되지 않음
//        return userRepository.saveAll(map).log("랭킹 200위까지 저장")
//                .then(Mono.just("성공"))
//                .onErrorReturn("실패");
    }

    // 비동기 스트림 작업 내에서 블로킹 작업은 못함 => 스트림 형식의 함수 작성 불가
    // subscribe() 구독 방법
    // 1. 실제 호출
    // 2. return Mono / Flux 시 호출한 측에서 구독 가능
    // 구독은 실제로 데이터를 사용하는 위치에서 해야 됨 => 즉 서비스 단에서 데이터가 필요하면 return Mono / Flux 타입을 맞춰줘야 됨
    public Mono<User> saveOcid(String nickname) {
        return getId(nickname).flatMap(idDto -> userRepository.findByNickname(nickname)
                .flatMap(user -> {
                    user.setOcid(idDto.getOcid());
                    return userRepository.save(user).doOnSuccess(saveUser ->
                            log.info("저장된 유저 : {}", saveUser));
                })
                .switchIfEmpty(
                        webClient.get().uri(String.format("/ranking/overall?date=%s&ocid=%s", date, idDto.getOcid()))
                                .retrieve()
                                .bodyToMono(InfoResponseDto.class)
                                .flatMap(infoResponseDto -> Mono.just(infoResponseDto.getRanking().get(0)))
                                .flatMap(rankInfo -> {
                                    User u = rankInfo.toEntity();
                                    u.setOcid(idDto.getOcid());
                                    return userRepository.save(u).doOnSuccess(saveUser ->
                                            log.info("저장된 유저 : {}", saveUser));
                                }).onErrorResume(e -> {
                                    log.error("외부 API 호출 에러", e);
                                    return Mono.empty();
                                }))).log("유저 닉네임 받아서 랭킹정보 DB 저장");
    }
}