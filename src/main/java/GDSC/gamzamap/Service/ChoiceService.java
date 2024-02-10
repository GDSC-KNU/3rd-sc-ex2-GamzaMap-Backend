package GDSC.gamzamap.Service;

import GDSC.gamzamap.Entity.Choice;
import GDSC.gamzamap.Entity.Embeded.ChoiceRelationship;
import GDSC.gamzamap.Entity.Member;
import GDSC.gamzamap.Entity.Store;
import GDSC.gamzamap.Repository.ChatRoomRepository;
import GDSC.gamzamap.Repository.ChoiceRepository;
import GDSC.gamzamap.Repository.MemberRepository;
import GDSC.gamzamap.Repository.StoreRepository;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j

public class ChoiceService {
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;
    private final ChoiceRepository choiceRepository;

    public ChoiceService(MemberRepository memberRepository, StoreRepository storeRepository, ChoiceRepository choiceRepository) {
        this.memberRepository = memberRepository;
        this.storeRepository = storeRepository;
        this.choiceRepository = choiceRepository;
    }


    public List<Long> choiceList(Long member_id){
        // 찜 목록 불러오기
        List<Choice> choiceList = choiceRepository.findAllByChoiceRelationshipMemberId(member_id);

        return choiceList.stream()
                .map(choice -> choice.getChoiceRelationship().getStore().getId())
                .collect(Collectors.toList());
    }


    public ResponseEntity<HttpStatus> addChoice(Long store_id){
        // 가게 찜 등록
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Member member = memberRepository.findByEmail(email).orElse(null);
        Store store = storeRepository.findById(store_id).orElse(null);

        ChoiceRelationship choiceRelationship = new ChoiceRelationship(member, store);
        Choice choice = new Choice();
        choice.setChoiceRelationship(choiceRelationship);
        choiceRepository.save(choice);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<HttpStatus> deleteChoice(Long store_id){
        // 가게 찜 취소
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Member member = memberRepository.findByEmail(email).orElse(null);
        Store store = storeRepository.findById(store_id).orElse(null);

        Choice choice = choiceRepository.findByChoiceRelationshipMemberAndChoiceRelationshipStore(member, store).orElse(null);
        if (choice != null) {
            choiceRepository.delete(choice);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
