package GDSC.gamzamap.Service;

import GDSC.gamzamap.Dto.BossDto;
import GDSC.gamzamap.Dto.JoinDto;
import GDSC.gamzamap.Dto.JwtTokenDto;
import GDSC.gamzamap.Dto.MemberDto;
import GDSC.gamzamap.Entity.Member;
import GDSC.gamzamap.Jwt.JwtTokenProvider;
import GDSC.gamzamap.Repository.BossRepository;
import GDSC.gamzamap.Repository.MemberRepository;
import GDSC.gamzamap.Util.RandomNicknameGenerator;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

@Service
@Transactional(readOnly = true)
@Slf4j

public class AuthService {
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final BossRepository bossRepository;

    public AuthService(MemberRepository memberRepository, AuthenticationManagerBuilder authenticationManagerBuilder, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder, BossRepository bossRepository) {
        this.memberRepository = memberRepository;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.bossRepository = bossRepository;
    }

    @Transactional
    public JwtTokenDto login(String email) {
        // 1. email로 사용자 정보 조회
        Member member = memberRepository.findByEmail(email).orElse(null);
        // 2. Authentication 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, member.getPassword());
        // 3. 실제 검증. authenticate() 메서드를 통해 요청된 Member 에 대한 검증 진행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // 4. 인증 정보를 기반으로 JWT 토큰 생성
        JwtTokenDto jwtTokenDto = jwtTokenProvider.generateToken(authentication);
        // 5. refreshToken 저장
        member.setRefreshToken(jwtTokenDto.getRefreshToken());

        return jwtTokenDto;
    }

    @Transactional
    public JwtTokenDto jwtlogin(String email, String password){
        // 1. email로 사용자 정보 조회
        Member member = memberRepository.findByEmail(email).orElse(null);
        if (member == null) {
            log.info("이메일이 없습니다.");
            throw new UsernameNotFoundException("이메일이 없습니다.");
        }
        log.info("입력한 비번: "+password);
        log.info("db저장 비번: "+member.getPassword());

        //2. 입력한 비번을 암호화하여 db에 저장된 비번과 비교
        if (!passwordEncoder.matches(password, member.getPassword())){
            log.info("비밀번호가 일치하지 않습니다.");
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        return login(email);
    }

    @Transactional
    public JwtTokenDto accessTokenByrefreshToken(String refreshToken) {
        Member member = memberRepository.findByRefreshToken(refreshToken).orElse(null);
        String sub = member.getEmail();
        String auth = member.getRole();

        JwtTokenDto newAccessTokenDto = jwtTokenProvider.accessTokenByrefreshToken(refreshToken, sub, "ROLE_"+auth);
        if (newAccessTokenDto == null) {
            log.info("재발급 실패");
            throw new RuntimeException("Failed to refresh access token");
        }
        return newAccessTokenDto;
    }

    @Transactional
    public String getKakaoAccessToken(String code){
        String accessToken = "";
        String requestURL = "https://kauth.kakao.com/oauth/token";
        String RestApiKey = "b8201d590e870a0295523da6db288aee";
        String RedirectUri = "http://localhost:8080/auth/login/kakao";

        try {
            URL url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            // setDoOutput()은 OutputStream으로 POST 데이터를 넘겨 주겠다는 옵션
            // POST 요청을 수행하려면 setDoOutput()을 true로 설정
            conn.setDoOutput(true);

            // POST 요청에서 필요한 파라미터를 OutputStream을 통해 전송
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            String sb = "grant_type=authorization_code" +
                    "&client_id=" + RestApiKey + // REST_API_KEY
                    "&redirect_uri=" + RedirectUri + // REDIRECT_URI
                    "&code=" + code;
            bufferedWriter.write(sb);
            bufferedWriter.flush();

            int responseCode = conn.getResponseCode();
            log.info("responseCode : " + responseCode);

            // 요청을 통해 얻은 데이터를 InputStreamReader을 통해 읽어 오기
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            StringBuilder result = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
            log.info("response body : " + result);

            JsonElement element = JsonParser.parseString(result.toString());

            accessToken = element.getAsJsonObject().get("access_token").getAsString();

            log.info("accessToken : " + accessToken);

            bufferedReader.close();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return accessToken;
    }


    @Transactional
    public HashMap<String, Object> getUserInfo(String accessToken){
        HashMap<String, Object> userInfo = new HashMap<>();
        String postURL = "https://kapi.kakao.com/v2/user/me";
        try {
            URL url = new URL(postURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = conn.getResponseCode();
            log.info("responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            StringBuilder result = new StringBuilder();

            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            log.info("response body : " + result);

            JsonElement element = JsonParser.parseString(result.toString());
            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
            JsonObject kakaoAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject();
            //String nickname = properties.getAsJsonObject().get("nickname").getAsString();
            //log.info("닉네임은 잘됩니다");
            String email = kakaoAccount.getAsJsonObject().get("email").getAsString();
            log.info("이메일도 잘됩니다.");

            //userInfo.put("nickname", nickname);
            userInfo.put("email", email);

        } catch (IOException exception) {
            exception.printStackTrace();
        }
        log.info("리턴~");
        return userInfo;
    }


    @Transactional
    public JwtTokenDto kakaologin(String email) {
        // email로 사용자 정보 조회
        Member member = memberRepository.findByEmail(email).orElse(null);

        if (member == null) {
            log.info("이메일이 없습니다. 가입을 진행합니다.");
            String password = email.substring(0,10)+"!"+generateRandomNum();
            log.info("비번: "+password);
            // 새로운 JoinDto 생성
            JoinDto joinDto = new JoinDto();
            joinDto.setEmail(email);
            joinDto.setPassword(password);
            // 회원가입
            join(joinDto);
        }
        return login(email);
    }


    //password 뒤 랜덤 숫자 생성
    private String generateRandomNum() {
        RandomNicknameGenerator nicknameGenerator = new RandomNicknameGenerator();
        return nicknameGenerator.generateRandomNum();
    }


    @Transactional
    public MemberDto join(JoinDto joinDto){
        if (memberRepository.existsByEmail(joinDto.getEmail())){
            throw new IllegalArgumentException("이미 사용 중인 메일입니다.");
        }

        if (!joinDto.getPassword().matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$")) {
            log.info("영문, 숫자, 특수 문자 포함 8~20자리");
            throw new IllegalArgumentException("영문, 숫자, 특수 문자 포함 8~20자리 입력 바랍니다.");
        }

        // Password 암호화
        String encodedPassword = passwordEncoder.encode(joinDto.getPassword());
        joinDto.setPassword(encodedPassword);

        // 랜덤 닉네임 생성
        String randomNickname = generateRandomNickname();
        while (memberRepository.existsByNickname(randomNickname)){
            randomNickname = generateRandomNickname();
        }
        joinDto.setNickname(randomNickname);

        log.info("회원가입 비밀번호: "+encodedPassword + ", 닉네임: "+randomNickname);

        return MemberDto.toDto(memberRepository.save(joinDto.toEntity()));
    }


    private String generateRandomNickname() {
        RandomNicknameGenerator nicknameGenerator = new RandomNicknameGenerator();
        return nicknameGenerator.generateRandomNickname();
    }


    @Transactional
    public ResponseEntity<HttpStatus> logout(String refreshToken){
        Member member = memberRepository.findByRefreshToken(refreshToken).orElse(null);
        member.setRefreshToken(null);
        log.info("null 세팅 완료");
        memberRepository.save(member);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @Transactional
    public BossDto bossjoin(BossDto bossDto) {
        log.info("서비스 들어옴");
        if (bossRepository.existsByStoreName(bossDto.getStoreName())){
            throw new IllegalArgumentException("이미 존재하는 가게명입니다.");
        }
        if (bossRepository.existsByStoreNum(bossDto.getStoreNum())){
            throw new IllegalArgumentException("이미 존재하는 사업자번호 입니다.");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Member member = memberRepository.findByEmail(email).orElse(null);


        bossDto.setMemberId(member.getId());
        log.info("멤버아이디: "+bossDto.getMemberId());


        return BossDto.toDto(bossRepository.save(bossDto.toEntity()));
    }
}
