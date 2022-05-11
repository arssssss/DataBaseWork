package zyc.work.databasework.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.apache.commons.lang3.math.NumberUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import zyc.work.databasework.Exception.*;
import zyc.work.databasework.ResultType.ResponseResult;
import zyc.work.databasework.annotation.toekn.TokenCheck;
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
        if (!NumberUtils.isDigits(phone) || !StringUtils.hasText(password))
            throw new InvalidDataException();
        String id = userMapper.selectIdByPhAndPw(phone, password);
        if (id == null)
            throw new LoginFailException();
        return tokenUtil.CreateToken(1000 * 3600 * 24, id, TokenType.User);
    }

    @Transactional(rollbackFor = {Exception.class})
    public void register(User user) throws Exception {
        if (!NumberUtils.isDigits(user.u_phone))
            throw new MissingPhoneException();
        if (userMapper.selectIdCountByPh(user.u_phone) > 0)
            throw new UserExistException();
        userMapper.addUser(user);
    }

    @Transactional(rollbackFor = {Exception.class})
    public User getUser(String id) throws Exception {
        User user = userMapper.selectUserById(id);
        if (user == null)
            throw new UserNotExistException();
        return user;
    }

    @Transactional(rollbackFor = {Exception.class})
    public void updateUser(User user) {
        userMapper.updateUser(user);
    }

    @Transactional(rollbackFor = {Exception.class})
    public PageInfo<Ticket> getTickets(String u_id, Integer startPage, Integer pageSize) {
        PageHelper.startPage(startPage, pageSize);
        return new PageInfo<>(userMapper.selectTicketById(u_id), startPage);
    }

    @Transactional(rollbackFor = {Exception.class})
    public void buyTicket(Ticket ticket) throws Exception {
        if (userMapper.addTicket(ticket) != 1)
            throw new AddTicketException();
    }

    @Transactional(rollbackFor = {Exception.class})
    public List<QueryPath> getPath(String start_station, String end_station, LocalDate s_date, Integer transit) {
        return userMapper.selectPath(start_station, end_station, s_date, transit);
    }

    @Transactional(rollbackFor = {Exception.class})
    public SeatInfo seatInformation(String start_station, String end_station, String tr_name) throws Exception {
        SeatInfo seatInfo = new SeatInfo();
        String tr_id = userMapper.selectTrainIdByName(tr_name);
        VehicleModel vehicleModel = userMapper.selectSeatVehicleModel(tr_id);
        if (vehicleModel.vm_total == null)
            throw new TrainNotExistException();
        seatInfo.seatSum = vehicleModel.vm_total;
        seatInfo.firstClassNum = vehicleModel.vm_first_class;
        seatInfo.secondClassNum = vehicleModel.vm_second_class;
        seatInfo.vm_id = vehicleModel.vm_id;
        seatInfo.distance=userMapper.getDistance(tr_id,start_station,end_station);
        seatInfo.booleans = userMapper.selectSeatStates(start_station, end_station, tr_id, vehicleModel.vm_total);
        seatInfo.havingSeat = false;

        for (Boolean b : seatInfo.booleans) {
            if (b) {
                seatInfo.havingSeat = true;
                break;
            }
        }
        return seatInfo;
    }

    @Transactional(rollbackFor = {Exception.class})
    public void refund(String ti_id, String u_id) throws Exception {
        if (userMapper.refundTicket(ti_id, u_id) != 1)
            throw new TicketNotExistException();
    }

    @Transactional(rollbackFor = {Exception.class})
    public void addExtendTicket(Ticket ticket, String extend_station_name, String u_id) throws Exception {
        String extend_station = userMapper.selectStationIdByName(extend_station_name);
        if (extend_station == null)
            throw new StationNotExistException("补票终点站不存在");
        if (ticket.u_id.equals(u_id))
            throw new InvalidDataException();
        if (!userMapper.existTrain(ticket.tr_id))
            throw new TrainNotExistException();
        if (!userMapper.existStation(ticket.tr_id, ticket.ti_terminal_id))
            throw new StationNotExistException("该车次不存在该终点站");
        if (!userMapper.existStation(ticket.tr_id, extend_station))
            throw new StationNotExistException("该车次不存在该补票终点站");
        LocalDate end_Date = userMapper.arrivalDate(ticket.tr_id, ticket.ti_terminal_id);
        LocalDate extend_Date = userMapper.arrivalDate(ticket.tr_id, extend_station);
        LocalTime end_Time = userMapper.arrivalTime(ticket.tr_id, ticket.ti_terminal_id);
        LocalTime extend_Time = userMapper.arrivalTime(ticket.tr_id, extend_station);
        if (end_Date.isAfter(extend_Date))
            throw new TimeOutException("补票站在预期终点站之前");
        if (end_Date.isEqual(extend_Date) && end_Time.isAfter(extend_Time))
            throw new TimeOutException("补票站在预期终点站之前");
        ticket.ti_departure_id = ticket.ti_terminal_id;
        ticket.ti_terminal_id = extend_station;
        ticket.ti_seat = null;
        userMapper.addTicket(ticket);
    }

    @Transactional(rollbackFor = {Exception.class})
    public void addRobTicket(RobTicket robTicket, String u_id) throws Exception {
        robTicket.u_id = u_id;
        if (userMapper.addRobTicket(robTicket) == 0)
            throw new ExecException();
    }

    @Transactional(rollbackFor = {Exception.class})
    public Train getTrain(String tr_name) {
        return userMapper.selectTrainByName(tr_name);
    }

    @Transactional(rollbackFor = {Exception.class})
    public List<Train> getTrainById(String tr_id){
        return userMapper.selectTrainById(tr_id);
    }

    @Transactional(rollbackFor = {Exception.class})
    public Station getStation(String s_name) {
        return userMapper.selectStationByName(s_name);
    }

    @Transactional(rollbackFor = {Exception.class})
    public List<Station> getStationById(String s_id) {
        return userMapper.selectStationById(s_id);
    }

    @Transactional(rollbackFor = {Exception.class})
    public List<Price> getPrice(String vm_id, String p_seat_style, LocalDate date) {
        boolean isHoliday = userMapper.isHoliday(date);
        return userMapper.selectPrice(vm_id, p_seat_style, isHoliday);
    }

    @Transactional(rollbackFor = {Exception.class})
    public PathInfo getPathInfo(String tr_name, String s_station_name, String e_station_name){
        PathInfo pathInfo = new PathInfo();
        pathInfo.tr_name=tr_name;
        pathInfo.s_station_name=s_station_name;
        pathInfo.e_station_name=e_station_name;
        pathInfo.date=userMapper.getStartStationInfo(tr_name,s_station_name).rtr_date;
        pathInfo.s_time=userMapper.getStartStationInfo(tr_name,s_station_name).rtr_departure_time;
        pathInfo.e_time=userMapper.getEndStationInfo(tr_name,e_station_name).rtr_arrival_time;
        return pathInfo;
    }

    @Transactional(rollbackFor = {Exception.class})
    public PageInfo<RobTicket> getRobTickets(String u_id, Integer startPage, Integer pageSize) {
        PageHelper.startPage(startPage, pageSize);
        return new PageInfo(userMapper.selectRobTicketUById(u_id), startPage);
    }

    @Transactional(rollbackFor = {Exception.class})
    public void deleteRobTicket(String rob_id, String u_id) throws Exception {
        if (userMapper.selectRobTicketById(rob_id).u_id!=u_id)
            throw new InvalidDataException();
        else
            userMapper.deleteRobTicket(rob_id);
    }
}
