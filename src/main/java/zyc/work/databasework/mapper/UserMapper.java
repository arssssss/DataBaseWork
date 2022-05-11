package zyc.work.databasework.mapper;

import org.apache.catalina.LifecycleState;
import org.apache.ibatis.annotations.Mapper;
import sun.text.normalizer.UBiDiProps;
import zyc.work.databasework.pojo.*;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Mapper
public interface UserMapper {

    public String selectIdByPhAndPw(String phone,String password);

    public Integer addUser(User user);

    public Integer selectIdCountByPh(String phone);

    public User selectUserById(String id);

    public Integer updateUser(User user);

    public List<Ticket> selectTicketById(String id);

    public Integer addTicket(Ticket ticket);

    public List<QueryPath> selectPath(String start_station, String end_station, LocalDate s_date, Integer transit);

    public String selectTrainIdByName(String tr_name);

    public VehicleModel selectSeatVehicleModel(String tr_id);

    public Boolean selectSeatState(String start_station,String end_station,String tr_id,Integer seat_id);

    public List<Boolean> selectSeatStates(String start_station,String end_station,String tr_id,Integer seatCount);

    public Integer refundTicket(String ti_id,String u_id);

    public Boolean existTrain(String tr_id);

    public Boolean existStation(String tr_id,String station);

    public LocalDate arrivalDate(String tr_id,String station);

    public LocalTime arrivalTime(String tr_id, String station);

    public Integer addRobTicket(RobTicket robTicket);

    public List<RobTicket> selectAllRobTicket();

    public Integer deleteRobTicket(String rob_id);

    public String selectStationIdByName(String s_name);

    public Train selectTrainByName(String tr_name);

    public List<Train> selectTrainById(String tr_id);

    public Station selectStationByName(String s_name);

    public List<Station> selectStationById(String s_id);

    public List<Price> selectPrice(String vm_id,String p_seat_style,boolean isHoliday);

    public Boolean isHoliday(LocalDate localDate);

    public RouterTrainDetail getStartStationInfo(String tr_name,String s_station_name);

    public RouterTrainDetail getEndStationInfo(String tr_name,String e_station_name);

    public Double getDistance(String tr_id,String s_station_name,String e_station_name);

    public List<RobTicket> selectRobTicketUById(String u_id);

    public RobTicket selectRobTicketById(String rob_id);
}
