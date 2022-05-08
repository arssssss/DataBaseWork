package zyc.work.databasework.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.apache.commons.lang3.math.NumberUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import zyc.work.databasework.Exception.*;
import zyc.work.databasework.ResultType.ResponseResult;
import zyc.work.databasework.enums.result.ResultCode;
import zyc.work.databasework.enums.token.TokenType;
import zyc.work.databasework.mapper.UserMapper;
import zyc.work.databasework.pojo.*;
import zyc.work.databasework.util.TokenUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@Service
public class UserService {

    @Autowired
    public UserMapper userMapper;

    @Autowired
    public TokenUtil tokenUtil;

    @Transactional(rollbackFor = {Exception.class})
    public String login(String phone, String password) throws Exception {
        if(!NumberUtils.isDigits(phone) ||! StringUtils.hasText(password))
            throw new InvalidDataException();
        String id= userMapper.selectIdByPhAndPw(phone, password);
        if (id == null)
            throw new LoginFailException();
        return  tokenUtil.CreateToken(3600, id, TokenType.User);
    }

    @Transactional(rollbackFor = {Exception.class})
    public void register(User user) throws Exception {
        if(!NumberUtils.isDigits(user.u_phone))
            throw new MissingPhoneException();
        if(userMapper.selectIdCountByPh(user.u_phone)>0)
            throw new UserExistException();
        userMapper.addUser(user);
    }

    @Transactional(rollbackFor = {Exception.class})
    public User getUser(String id) throws Exception {
        User user = userMapper.selectUserById(id);
        if(user==null)
            throw new UserNotExistException();
            return user;
    }

    @Transactional(rollbackFor = {Exception.class})
    public void updateUser(User user){
        userMapper.updateUser(user);
    }

    @Transactional(rollbackFor = {Exception.class})
    public PageInfo<Ticket> getTickets(String u_id,Integer startPage,Integer pageSize){
        PageHelper.startPage(startPage,pageSize);
       return new PageInfo<>(userMapper.selectTicketById(u_id),startPage);
    }

    @Transactional(rollbackFor = {Exception.class})
    public void buyTicket(Ticket ticket) throws Exception {
        if(userMapper.addTicket(ticket)!=1)
            throw new AddTicketException();
    }

    @Transactional(rollbackFor = {Exception.class})
    public List<QueryPath> getPath(String start_station,String end_station, LocalDate s_date, Integer transit){
        return userMapper.selectPath(start_station,end_station,s_date,transit);
    }

    @Transactional(rollbackFor = {Exception.class})
    public SeatInfo seatInformation(String start_station, String end_station, String tr_name) throws Exception {
        SeatInfo seatInfo = new SeatInfo();
        String tr_id=userMapper.selectTrainIdByName(tr_name);
        Integer count = userMapper.selectSeatCount(tr_id);
        if(count==null)
            throw new TrainNotExistException();
        seatInfo.seatSum=count;
        seatInfo.booleans=userMapper.selectSeatStates(start_station,end_station,tr_id,count);
        seatInfo.havingSeat=false;
        for(Boolean b:seatInfo.booleans){
            if(b){
                seatInfo.havingSeat=true;
                break;
            }
        }
        return seatInfo;
    }

    @Transactional(rollbackFor = {Exception.class})
    public void refund(String ti_id,String u_id) throws Exception {
        if(userMapper.refundTicket(ti_id,u_id)!=1)
            throw new TicketNotExistException();
    }

    @Transactional(rollbackFor = {Exception.class})
    public void addExtendTicket(Ticket ticket,String extend_station_name,String u_id) throws Exception {
        String extend_station = userMapper.selectStationIdByName(extend_station_name);
        if(extend_station==null)
            throw new StationNotExistException("补票终点站不存在");
        if(ticket.u_id.equals(u_id))
            throw new InvalidDataException();
        if(!userMapper.existTrain(ticket.tr_id))
            throw new TrainNotExistException();
        if(!userMapper.existStation(ticket.tr_id,ticket.ti_terminal_id))
            throw new StationNotExistException("该车次不存在该终点站");
        if(!userMapper.existStation(ticket.tr_id,extend_station))
            throw new StationNotExistException("该车次不存在该补票终点站");
        LocalDate end_Date = userMapper.arrivalDate(ticket.tr_id, ticket.ti_terminal_id);
        LocalDate extend_Date = userMapper.arrivalDate(ticket.tr_id, extend_station);
        LocalTime end_Time = userMapper.arrivalTime(ticket.tr_id, ticket.ti_terminal_id);
        LocalTime extend_Time = userMapper.arrivalTime(ticket.tr_id, extend_station);
        if(end_Date.isAfter(extend_Date))
            throw new TimeOutException("补票站在预期终点站之前");
        if(end_Date.isEqual(extend_Date)&&end_Time.isAfter(extend_Time))
            throw new TimeOutException("补票站在预期终点站之前");
        ticket.ti_departure_id=ticket.ti_terminal_id;
        ticket.ti_terminal_id=extend_station;
        ticket.ti_seat=null;
        userMapper.addTicket(ticket);
    }

    @Transactional(rollbackFor = {Exception.class})
    public void addRobTicket(RobTicket robTicket,String u_id) throws Exception {
        robTicket.u_id=u_id;
        if(userMapper.addRobTicket(robTicket)==0)
            throw new ExecException();
    }
}
