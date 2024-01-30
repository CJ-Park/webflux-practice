package com.example.webflux;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final TestService testService;

    @GetMapping("/hi")
    Mono<String> test() {
        return Mono.just("hi~");
    }

    @GetMapping("/id")
    Mono<IdDto> getId(@RequestParam(name = "nickname") String nickname) {
        return testService.getId(nickname).log();
    }

    @GetMapping("/info")
    Mono<InfoDto> getInfo(@RequestParam(name = "ocid") String ocid) {
        return testService.getInfo(ocid).log();
    }

    @GetMapping("/users")
    Flux<User> getUserRank() {
        return testService.getRankUser();
    }

    @PostMapping("/ranker")
    Mono<String> saveUser() {
        return testService.saveId()
                .then(Mono.just("성공"))
                .onErrorReturn("실패");
    }

    @PostMapping("/ocid")
    Mono<User> updateOcid(@RequestBody NicknameDto dto) {
        return testService.saveOcid(dto.getNickname());
    }
}
