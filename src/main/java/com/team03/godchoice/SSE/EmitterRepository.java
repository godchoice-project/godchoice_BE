//package com.team03.godchoice.repository;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//
//import java.util.Map;
//
//public interface EmitterRepository{
//    SseEmitter save(String emitterId, SseEmitter sseEmitter);
//    void saveEventCache(String emitterId, Object event);
//    Map<String, SseEmitter> findAllStartWithById(Long postId);
//    Map<String, Object> findAllEventCacheStartWithId(String memberId);
//    void deleteById(String id);
//    void deleteAllEmitterStartWithId(String memberId);
//    void deleteAllEventCacheStartWithId(String memberId);
//
//}
