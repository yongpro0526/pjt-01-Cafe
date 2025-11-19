package com.miniproject.cafe.Controller;

import com.miniproject.cafe.Emitter.SseEmitterStore;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class SseController {

    private final SseEmitterStore emitterStore;

    @GetMapping("/sse/subscribe/{storeName}")
    public SseEmitter subscribe(@PathVariable String storeName) {

        SseEmitter emitter = new SseEmitter(1000L * 60 * 30); // 30분 타임아웃

        emitterStore.addEmitter(storeName, emitter);

        try {
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("connected"));
        } catch (Exception e) {
            emitter.complete();
        }

        return emitter;
    }
}
