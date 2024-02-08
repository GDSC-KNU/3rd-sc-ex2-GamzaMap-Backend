package GDSC.gamzamap.Controller;

import GDSC.gamzamap.Service.ChoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/choice")

public class ChoiceController {
    private final ChoiceService choiceService;

    @GetMapping("/{member_id}")
    public List<Long> choiceList(@PathVariable Long member_id){
        List<Long> choiceList = choiceService.choiceList(member_id);
        return choiceList;
    }


    @GetMapping("/{store_id}/add")
    public ResponseEntity<HttpStatus> addChoice(@PathVariable Long store_id){
        log.info("가게아이디: "+store_id);
        choiceService.addChoice(store_id);
        log.info("추가 되었습니다.");

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{store_id}/delete")
    public ResponseEntity<HttpStatus> deleteChoice(@PathVariable Long store_id){
        choiceService.deleteChoice(store_id);
        log.info("삭제 되었습니다.");

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
