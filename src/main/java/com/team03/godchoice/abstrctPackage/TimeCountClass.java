package com.team03.godchoice.abstrctPackage;

import java.time.*;
import java.time.format.DateTimeFormatter;

public abstract class TimeCountClass {

    public static String countTime(LocalDateTime localDateTime) {
        String countTime = "";

        LocalDateTime now = LocalDateTime.now();//현재날짜 시간
        LocalDate nowDate = now.toLocalDate();//현재 날짜
        LocalTime nowTime = now.toLocalTime();//현재 시간

        LocalDate postDate = localDateTime.toLocalDate();//포스팅 날짜
        LocalTime postTime = localDateTime.toLocalTime();//포스팅 시간

        Period period = Period.between(postDate, nowDate);

        Duration duration = Duration.between(postTime, nowTime);
        long betweenTime = duration.getSeconds();  //두시간 차이(초)

        if (period.getDays() < 1) {
            if (betweenTime <= 60) {
                countTime = "약 1분 전";
            } else if (betweenTime <= 3600) {
                countTime = "약 "+(betweenTime / 60) + "분 전";
            } else if (betweenTime <= 86400) {
                countTime = "약 "+(betweenTime / 60 / 60) + "시간 전";
            }
        } else if (period.getDays() < 7) {
            countTime = "약 "+period.getDays() + "일 전";
        } else {
            countTime = localDateTime.format(DateTimeFormatter.ofPattern("MM월 dd일 HH시 mm분"));
        }

        return countTime;
    }
}
