package GDSC.gamzamap.Service;

import GDSC.gamzamap.Entity.ChatRoom;
import GDSC.gamzamap.Entity.Store;
import GDSC.gamzamap.Repository.ChatRoomRepository;
import GDSC.gamzamap.Repository.StoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j

public class StoreService {
    private final StoreRepository storeRepository;
    private final ChatRoomRepository chatRoomRepository;

    public StoreService(StoreRepository storeRepository, ChatRoomRepository chatRoomRepository) {
        this.storeRepository = storeRepository;
        this.chatRoomRepository = chatRoomRepository;
    }

    public void chatroomBystore(){
        // 가게 생성 시 채팅방도 자동 생성
        List<Store> storeList = storeRepository.findAll();
        for (Store store : storeList){
            ChatRoom chatRoom = new ChatRoom();
            chatRoom.setStoreName(store.getStoreName());
            chatRoomRepository.save(chatRoom);
            store.setChatRoom(chatRoom);
            storeRepository.save(store);
        }
    }
}
