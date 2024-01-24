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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public JwtTokenDto login(String email, String password) {
        //1.email로 사용자 정보 조회
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

        // 3. Authentication 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, member.getPassword());
        // 4. 실제 검증. authenticate() 메서드를 통해 요청된 Member 에 대한 검증 진행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // 5. 인증 정보를 기반으로 JWT 토큰 생성
        JwtTokenDto jwtTokenDto = jwtTokenProvider.generateToken(authentication);
        // 6. refreshToken 저장
        member.setRefreshToken(jwtTokenDto.getRefreshToken());

        return jwtTokenDto;
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
        joinDto.setNickname(randomNickname);

        log.info("회원가입 비밀번호: "+encodedPassword + ", 닉네임: "+randomNickname);

        return MemberDto.toDto(memberRepository.save(joinDto.toEntity()));
    }

    private String generateRandomNickname() {
        RandomNicknameGenerator nicknameGenerator = new RandomNicknameGenerator();
        return nicknameGenerator.generateRandomNickname();
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
