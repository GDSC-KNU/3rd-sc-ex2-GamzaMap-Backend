package GDSC.gamzamap.Controller;

import GDSC.gamzamap.Dto.*;
import GDSC.gamzamap.Service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "로그인 관련 API")
public class AuthController {
    private final AuthService authService;

    //Jwt 로그인
    @PostMapping("/login")
    @Operation(summary = "로그인 API", description = "로그인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(schema = @Schema(implementation = LoginDto.class))),
            @ApiResponse(responseCode = "201", description = "회원가입 성공",
                    content = @Content(schema = @Schema(implementation = LoginDto.class))),
            @ApiResponse(responseCode = "500", description = "내부 서버 오류",
                    content = @Content(schema = @Schema(implementation = HttpStatus.class)))
    })
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
    @Operation(summary = "Kakao 소셜 로그인 API", description = "Kakao 인증 코드를 이용하여 로그인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(schema = @Schema(implementation = JwtTokenDto.class))),
            @ApiResponse(responseCode = "500", description = "내부 서버 오류",
                    content = @Content(schema = @Schema(implementation = HttpStatus.class)))
    })
    public JwtTokenDto kakaoLogin(@RequestBody Map<String, String> requestBody){
        // 파싱하여 code 저장하기
        String code = requestBody.get("code");
        log.info("kakao 인증코드: {}", code);
        // code 이용하여 accessToken 발급 받기
        String accessToken = authService.getKakaoAccessToken(code);
        // accessToken 이용하여 사용자 조회하기
        HashMap<String, Object> userInfo = authService.getUserInfo(accessToken);
        log.info("kakao 사용자 정보: {}", userInfo);
        //userInfo에서 email 꺼내오기
        String email = (String)userInfo.get("email");

        return authService.kakaologin(email);
    }

    @PostMapping("/general/join")
    @Operation(summary = "일반 회원가입 API", description = "일반 회원가입")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공",
                    content = @Content(schema = @Schema(implementation = MemberDto.class))),
            @ApiResponse(responseCode = "500", description = "내부 서버 오류",
                    content = @Content(schema = @Schema(implementation = HttpStatus.class)))
    })
    public ResponseEntity<MemberDto> join(@RequestBody JoinDto joinDto){
        MemberDto savedMemberDto = authService.join(joinDto);
        log.info("회원가입 성공");
        return ResponseEntity.ok(savedMemberDto);
    }

    @PostMapping("/refresh")
    @Operation(summary = "토큰 재발급 API", description = "Refresh Token을 이용하여 Access Token을 재발급")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "토큰 재발급 성공",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "내부 서버 오류",
                    content = @Content(schema = @Schema(implementation = HttpStatus.class)))
    })
    public String accessTokenByRefreshToken(@RequestBody Map<String, String> requestBody) {
        String refreshToken = requestBody.get("refreshToken");
        JwtTokenDto newAccessTokenDto = authService.accessTokenByrefreshToken(refreshToken);
        log.info("새로운 accessToken: "+newAccessTokenDto.getAccessToken());

        return newAccessTokenDto.getAccessToken();
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃 API", description = "Refresh Token을 이용하여 로그아웃")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그아웃 성공",
                    content = @Content(schema = @Schema(implementation = HttpStatus.class))),
            @ApiResponse(responseCode = "500", description = "내부 서버 오류",
                    content = @Content(schema = @Schema(implementation = HttpStatus.class)))
    })
    public ResponseEntity<HttpStatus> logout(@RequestBody Map<String, String> requestBody){
        String refreshToken = requestBody.get("refreshToken");
        authService.logout(refreshToken);
        log.info("로그아웃 되었습니다.");

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/boss/join")
    @Operation(summary = "사장님 회원가입 API", description = "사장님 회원가입")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공",
                    content = @Content(schema = @Schema(implementation = BossDto.class))),
            @ApiResponse(responseCode = "500", description = "내부 서버 오류",
                    content = @Content(schema = @Schema(implementation = HttpStatus.class)))
    })
    public ResponseEntity<BossDto> bossjoin(@RequestBody BossDto bossDto){
        BossDto savedBossDto = authService.bossjoin(bossDto);
        return ResponseEntity.ok(savedBossDto);
    }
}
