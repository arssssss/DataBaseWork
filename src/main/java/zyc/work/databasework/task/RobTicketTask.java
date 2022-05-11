package zyc.work.databasework.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import zyc.work.databasework.mapper.UserMapper;
import zyc.work.databasework.pojo.RobTicket;
import zyc.work.databasework.pojo.Station;
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
            List<Station> startStations = userMapper.selectStationById(robTicket.r_departure_id);
            List<Station> endStations = userMapper.selectStationById(robTicket.r_terminal_id);
            if(userMapper.selectSeatState(startStations.get(0).s_name,endStations.get(0).s_name,robTicket.tr_id,robTicket.r_seat)){
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
