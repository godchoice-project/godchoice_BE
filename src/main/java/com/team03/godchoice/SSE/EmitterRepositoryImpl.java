package com.team03.godchoice.SSE;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class EmitterRepositoryImpl implements EmitterRepository{


    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<String,Object> eventCache = new ConcurrentHashMap<>();

    @Override // Emitter를 저장
    public SseEmitter save(String emitterId, SseEmitter sseEmitter) {
        emitters.put(emitterId,sseEmitter);
        return sseEmitter;
    }

    @Override //이벤트를 저장
    public void saveEventCache(String eventCacheId, Object event) {
        eventCache.put(eventCacheId,event);

    }

    @Override // 구분자를 회원 ID를 사용하기에 StartWith를 사용 - 회원과 관련된 모든 Emitter를 찾는다.
    public Map<String, SseEmitter> findAllEmitterStartWithByUserId(String memberId) {
        return emitters.entrySet().stream() // key / value entry 리턴
                .filter(entry -> entry.getKey().startsWith(memberId))
                // 해당 userId 로 시작하는 키값을 필터 key, value 리턴
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override // 회원에게 수신된 모든 이벤트를 찾는다.
    public Map<String, Object> findAllEventCacheStartWithByUserId(String memberId) {
        return eventCache.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(memberId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    // pk 를 통해 Emitter를 제거
    @Override
    public void deleteById(String id) {
        emitters.remove(id);

    }
    // id와 관련된 Emitter를 모두 제거
    @Override
    public void deleteAllEmitterStartWithId(String memberId) {
        emitters.forEach(
                (key,emitter) -> {
                    if(key.startsWith(memberId)){
                        emitters.remove(key);
                    }
                }
        );
    }
    // id와 관련된 모든 이벤트 캐시를 삭제
    @Override
    public void deleteAllEventCacheStartWithId(String memberId) {
        eventCache.forEach(
                (key,emitter) -> {
                    if(key.startsWith(memberId)){
                        eventCache.remove(key);
                    }
                }
        );

    }
}

