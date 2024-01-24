package GDSC.gamzamap.Util;

import java.security.SecureRandom;
import java.util.Random;

public class RandomNicknameGenerator {
    private static final String[] PREDEFINED_NICKNAMES = {"어피치", "라이언", "무지", "콘",
            "제이지", "프로도", "네오", "춘식이", "튜브"};

    private final Random random = new SecureRandom();

    public String generateRandomNickname() {
        int randomIndex = random.nextInt(PREDEFINED_NICKNAMES.length);
        int randomNumber = random.nextInt(100) + 1;
        return PREDEFINED_NICKNAMES[randomIndex] + randomNumber;
    }
}
