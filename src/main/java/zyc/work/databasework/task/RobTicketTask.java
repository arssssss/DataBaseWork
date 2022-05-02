package zyc.work.databasework.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import zyc.work.databasework.mapper.UserMapper;
import zyc.work.databasework.pojo.RobTicket;
import zyc.work.databasework.pojo.Ticket;

import java.util.List;

@Component
public class RobTicketTask {

    @Autowired
    public UserMapper userMapper;


    @Scheduled(cron = "1-2 * * * * ?")
    public void reportCurrentTimeWithCronExpression(){
        List<RobTicket> robTickets = userMapper.selectAllRobTicket();
        for(RobTicket robTicket:robTickets){
            if(userMapper.selectSeatState(robTicket.r_departure_id,robTicket.r_terminal_id,robTicket.tr_id,robTicket.r_seat)){
                Ticket ticket = new Ticket();
                ticket.p_id=robTicket.p_id;
                ticket.ti_departure_id=robTicket.r_departure_id;
                ticket.ti_terminal_id=robTicket.r_terminal_id;
                ticket.ti_seat=robTicket.r_seat;
                ticket.tr_id = robTicket.tr_id;
                ticket.u_id=robTicket.u_id;
                userMapper.addTicket(ticket);
                userMapper.deleteRobTicket(robTicket.rob_id);
            }
        }

    }


}
