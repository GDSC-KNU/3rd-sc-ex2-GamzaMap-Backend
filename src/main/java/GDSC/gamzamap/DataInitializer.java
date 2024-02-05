package GDSC.gamzamap;

import GDSC.gamzamap.Service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {
    private final StoreService storeService;

    public DataInitializer(StoreService storeService) {
        this.storeService = storeService;
    }

    @Override
    public void run(String... args) throws Exception {
        storeService.chatroomBystore();
    }
}
