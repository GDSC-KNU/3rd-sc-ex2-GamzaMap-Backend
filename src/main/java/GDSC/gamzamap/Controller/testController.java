package GDSC.gamzamap.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class testController {
    @GetMapping("/test")
    public ResponseEntity<String> test(){
        log.info("서버 연결 테스트 입니다. 성공!");

        String message = "서버 연결 완료!";
        return ResponseEntity.ok().body(message);
    }
}
