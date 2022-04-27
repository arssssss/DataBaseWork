package zyc.work.databasework.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import zyc.work.databasework.mapper.UserMapper;
import zyc.work.databasework.pojo.RobTicket;
import zyc.work.databasework.pojo.Ticket;
import zyc.work.databasework.pojo.User;
import zyc.work.databasework.pojo.QueryPath;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;


@Service
public class UserService {

    @Autowired
    public UserMapper userMapper;

    public String login(String phone, String password) {
        return userMapper.selectIdByPhAndPw(phone,password);
    }

    public boolean register(User user){
        if(!NumberUtils.isDigits(user.u_phone))
            return false;
        if(userMapper.selectIdCountByPh(user.u_phone)>1)
            return false;
        int count=userMapper.addUser(user);
        return count==1;
    }

    public User getUser(String id){
        return userMapper.selectUserById(id);
    }

    public boolean updateUser(User user){
        return userMapper.updateUser(user)==1;
    }

    public PageInfo getTickets(String u_id,Integer startPage,Integer pageSize){
        PageHelper.startPage(startPage,pageSize);
       return new PageInfo<>(userMapper.selectTicketById(u_id),startPage);
    }

    public boolean buyTicket(Ticket ticket){
        if(userMapper.addTicket(ticket)==1)
            return true;
        return false;
    }

    public List<QueryPath> getPath(String start_station,String end_station, LocalDate s_date, Integer transit){
        return userMapper.selectPath(start_station,end_station,s_date,transit);
    }

    public List<Boolean> seatInformation(String start_station, String end_station, String tr_id){
        Integer count = userMapper.selectSeatCount(tr_id);
        if(count==null)return null;
        return userMapper.selectSeatStates(start_station,end_station,tr_id,count);
    }

    public boolean refund(String ti_id,String u_id){
        if(userMapper.refundTicket(ti_id,u_id)==1)
            return true;
        return false;
    }

    public boolean addExtendTicket(Ticket ticket,String extend_station_name,String u_id){
        String extend_station = userMapper.selectStationIdByName(extend_station_name);
        if(extend_station==null)
            return false;
        if(ticket.u_id.equals(u_id))
            return false;
        if(!userMapper.existTrain(ticket.tr_id))
            return false;
        if(!userMapper.existStation(ticket.tr_id,ticket.ti_terminal_id))
            return false;
        if(!userMapper.existStation(ticket.tr_id,extend_station))
            return false;
        LocalDate end_Date = userMapper.arrivalDate(ticket.tr_id, ticket.ti_terminal_id);
        LocalDate extend_Date = userMapper.arrivalDate(ticket.tr_id, extend_station);
        LocalTime end_Time = userMapper.arrivalTime(ticket.tr_id, ticket.ti_terminal_id);
        LocalTime extend_Time = userMapper.arrivalTime(ticket.tr_id, extend_station);
        if(end_Date.isAfter(extend_Date))
            return false;
        if(end_Date.isEqual(extend_Date)&&end_Time.isAfter(extend_Time))
            return false;
        ticket.ti_departure_id=ticket.ti_terminal_id;
        ticket.ti_terminal_id=extend_station;
        ticket.ti_seat=null;
        userMapper.addTicket(ticket);
        return true;
    }

    public Boolean addRobTicket(RobTicket robTicket,String u_id){
        robTicket.u_id=u_id;
        if(userMapper.addRobTicket(robTicket)>0)
            return true;
        return false;
    }
}
