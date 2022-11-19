package com.team03.godchoice.interfacepackage;

import com.team03.godchoice.dto.GlobalResDto;

public interface PostInterface {
    default GlobalResDto<?> createPost() {
        return null;
    }

    default void putPost() {
    }

    default void deletePost() {
    }

    default void getOnePost(){
    }
}
