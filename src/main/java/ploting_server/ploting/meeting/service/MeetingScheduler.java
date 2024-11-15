package ploting_server.ploting.meeting.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeetingScheduler {

    private final MeetingLeaderService meetingLeaderService;

    /**
     * 만료된 모임의 activeStatus를 매일 자정에 비활성화로 변경합니다.
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void runDeactivateExpiredMeetings() {
        meetingLeaderService.deactivateExpiredMeetings();
    }
}
