package com.miniproject.cafe.Emitter;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SseEmitterStore {

    // 매장별 관리자에게 메시지를 보내기 위해 key = storeName
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter addEmitter(String storeName, SseEmitter emitter) {
        emitters.put(storeName, emitter);
        emitter.onCompletion(() -> emitters.remove(storeName));
        emitter.onTimeout(() -> emitters.remove(storeName));
        return emitter;
    }

    public SseEmitter getEmitter(String storeName) {
        return emitters.get(storeName);
    }

    public void sendToStore(String storeName, Object data) {
        SseEmitter emitter = emitters.get(storeName);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("order")
                        .data(data)
                        .id(String.valueOf(System.currentTimeMillis())));
            } catch (Exception e) {
                emitters.remove(storeName);
            }
        }
    }
}