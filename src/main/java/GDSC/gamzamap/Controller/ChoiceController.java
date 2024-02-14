package GDSC.gamzamap.Controller;

import GDSC.gamzamap.Service.ChoiceService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/choice")
@Tag(name = "찜 관련 API")
public class ChoiceController {
    private final ChoiceService choiceService;

    @GetMapping("/{member_id}")
    @Operation(summary = "선택 목록 조회", description = "특정 회원의 선택 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(type = "array", implementation = Long.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public List<Long> choiceList(@PathVariable Long member_id){
        List<Long> choiceList = choiceService.choiceList(member_id);
        return choiceList;
    }


    @GetMapping("/{store_id}/add")
    @Operation(summary = "선택 추가", description = "가게 선택을 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "추가 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<HttpStatus> addChoice(@PathVariable Long store_id){
        log.info("가게아이디: "+store_id);
        choiceService.addChoice(store_id);
        log.info("추가 되었습니다.");

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{store_id}/delete")
    @Operation(summary = "선택 삭제", description = "가게 아이디에 해당하는 선택을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<HttpStatus> deleteChoice(@PathVariable Long store_id){
        choiceService.deleteChoice(store_id);
        log.info("삭제 되었습니다.");

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
