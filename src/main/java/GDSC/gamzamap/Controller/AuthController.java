package GDSC.gamzamap.Controller;

import GDSC.gamzamap.Dto.*;
import GDSC.gamzamap.Service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")

public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public JwtTokenDto login(@RequestBody LoginDto loginDto){
        String email = loginDto.getEmail();
        String password = loginDto.getPassword();
        JwtTokenDto jwtTokenDto = authService.login(email, password);
        log.info("request email = {}, password = {}", email, password);
        log.info("jwtToken accessToken = {}, refreshToken = {}", jwtTokenDto
                .getAccessToken(), jwtTokenDto.getRefreshToken());
        log.info("로그인 성공");
        return jwtTokenDto;
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
