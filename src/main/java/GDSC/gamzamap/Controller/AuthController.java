package GDSC.gamzamap.Controller;

import GDSC.gamzamap.Dto.*;
import GDSC.gamzamap.Service.AuthService;
import com.ibm.icu.text.Transliterator;
import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")

public class AuthController {
    private final AuthService authService;

    //Jwt 로그인
    @PostMapping("/login")
    public JwtTokenDto login(@RequestBody LoginDto loginDto){
        String email = loginDto.getEmail();
        String password = loginDto.getPassword();
        JwtTokenDto jwtTokenDto = authService.jwtlogin(email, password);
        log.info("request email = {}, password = {}", email, password);
        log.info("jwtToken accessToken = {}, refreshToken = {}", jwtTokenDto
                .getAccessToken(), jwtTokenDto.getRefreshToken());
        log.info("로그인 성공");
        return jwtTokenDto;
    }

    //Kakao 소셜 로그인
    @PostMapping("/login/kakao")
    public JwtTokenDto kakaoLogin(@RequestBody Map<String, String> requestBody){
        // 파싱하여 accessToken 저장하기
        String accessToken = requestBody.get("accessToken");
        log.info("kakao 엑세스 토큰: {}", accessToken);
        // 사용자 조회하기
        HashMap<String, Object> userInfo = authService.getUserInfo(accessToken);
        log.info("kakao 사용자 정보: {}", userInfo);
        //userInfo에서 nickname 꺼내오기
        String nickname = (String)userInfo.get("nickname");

        return authService.kakaologin(nickname);
    }

    @PostMapping("/general/join")
    public ResponseEntity<MemberDto> join(@RequestBody JoinDto joinDto){
        MemberDto savedMemberDto = authService.join(joinDto);
        log.info("회원가입 성공");
        return ResponseEntity.ok(savedMemberDto);
    }

    @PostMapping("/boss/join")
    public ResponseEntity<BossDto> bossjoin(@RequestBody BossDto bossDto){
        BossDto savedBossDto = authService.bossjoin(bossDto);
        log.info("사장님 등록 성공");
        return ResponseEntity.ok(savedBossDto);
    }
}
