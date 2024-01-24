package GDSC.gamzamap.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor

public class JwtTokenDto {
    private String grantType; //JWT에 대한 인증방식 (Bearer 사용)
    private String accessToken;
    private String refreshToken;
}
