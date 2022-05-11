package zyc.work.databasework.controller;

import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import zyc.work.databasework.Exception.LoginFailException;
import zyc.work.databasework.ResultType.ResponseResult;
import zyc.work.databasework.annotation.toekn.LoginToken;
import zyc.work.databasework.annotation.toekn.TokenCheck;
import zyc.work.databasework.enums.result.ResultCode;
import zyc.work.databasework.enums.token.TokenType;
import zyc.work.databasework.interceptor.token.TokenInterceptor;
import zyc.work.databasework.pojo.*;
import zyc.work.databasework.service.ManagerService;
import zyc.work.databasework.service.UserService;
import zyc.work.databasework.util.TokenUtil;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    public UserService userService;
    @Autowired
    public ManagerService managerService;

    @Autowired
    public TokenUtil tokenUtil;

    /**
     * 测试连通情况
     *
     * @return
     */
    @RequestMapping("/")
    public ResponseResult<String> test() {
        return new ResponseResult<>(ResultCode.OK.getValue(), "/连接成功");
    }

    /**
     * 用户注册
     *
     * @param user
     * @return
     */
    @PostMapping("/register")
    public ResponseResult<String> register(User user) {
        try {
            userService.register(user);
            return new ResponseResult<String>(ResultCode.OK.getValue(), "注册成功");
        } catch (Exception e) {
            return new ResponseResult<String>(ResultCode.ERROR.getValue(), e.toString());
        }
    }

    /**
     * 用户登录
     *
     * @param phone
     * @param password
     * @return
     */
    @LoginToken
    @PostMapping(value = "/login")
    public ResponseResult<String> userlogin(@RequestParam(value = "phone") String phone, @RequestParam(value = "password") String password) {
        try {
            String token = userService.login(phone, password);
            return new ResponseResult<String>(ResultCode.OK.getValue(), token);
        } catch (Exception e) {
            return new ResponseResult<String>(ResultCode.ERROR.getValue(), e.toString());
        }
    }

    /**
     * 查询用户信息
     *
     * @return
     */
    @TokenCheck
    @GetMapping("/Information")
    public ResponseResult getUserInformation() {
        try {
            return new ResponseResult<User>(ResultCode.OK.getValue(), userService.getUser(TokenInterceptor.Id.get()));
        } catch (Exception e) {
            return new ResponseResult<String>(ResultCode.ERROR.getValue(), e.toString());
        }
    }

    /**
     * 更新用户信息
     *
     * @param user
     * @return
     */
    @TokenCheck
    @PutMapping("/Information")
    public ResponseResult<String> updateUserInformation(User user) {
        try {
            user.u_id = TokenInterceptor.Id.get();
            userService.updateUser(user);
            return new ResponseResult<String>(ResultCode.OK.getValue(), "更新成功");
        } catch (Exception e) {
            return new ResponseResult<String>(ResultCode.ERROR.getValue(), e.toString());
        }
    }

    /**
     * 查询用户已购票(分页)
     *
     * @param startPage
     * @param pageSize
     * @return
     */
    @TokenCheck
    @GetMapping("/ticket")
    public ResponseResult queryTicket(@RequestParam(value = "startPage") Integer startPage,
                                      @RequestParam(value = "pageSize") Integer pageSize) {
        try {
            PageInfo<Ticket> tickets = userService.getTickets(TokenInterceptor.Id.get(), startPage, pageSize);
            return new ResponseResult<PageInfo<Ticket>>(ResultCode.OK.getValue(), tickets);
        } catch (Exception e) {
            return new ResponseResult<String>(ResultCode.ERROR.getValue(), "查询失败");
        }
    }

    /**
     * 查询路线
     *
     * @param start_station 起始站的站点名称
     * @param end_station   终点站的站点名称
     * @param s_date        出发时间
     * @param transit       限制换乘次数
     * @return
     */
    @TokenCheck
    @GetMapping("/path")
    public ResponseResult queryPath(@RequestParam(value = "start_station") String start_station,
                                    @RequestParam(value = "end_station") String end_station,
                                    @RequestParam(value = "s_date") LocalDate s_date,
                                    @RequestParam(value = "transit") Integer transit) {
        try {
            return new ResponseResult<List<QueryPath>>(ResultCode.OK.getValue(), userService.getPath(start_station, end_station, s_date, transit));
        } catch (Exception e) {
            return new ResponseResult<String>(ResultCode.ERROR.getValue(), e.toString());
        }

    }

    /**
     * 用户购票
     *
     * @return
     */
    @TokenCheck
    @PostMapping("/ticket")
    public ResponseResult buyTicket(Ticket ticket) {
        try {
            ticket.setU_id(TokenInterceptor.Id.get());
            userService.buyTicket(ticket);
            return new ResponseResult<String>(ResultCode.OK.getValue(), "购买成功");
        } catch (Exception e) {
            return new ResponseResult<String>(ResultCode.ERROR.getValue(), e.toString());
        }
    }

    /**
     * 查看相关车次的座位情况
     *
     * @param start_station 出发站站点名称
     * @param end_station   终点站站点名称
     * @param tr_name       车次名
     * @return
     */
    @TokenCheck
    @GetMapping("/seatInformation")
    public ResponseResult seatInformation(@RequestParam(value = "start_station") String start_station,
                                          @RequestParam(value = "end_station") String end_station,
                                          @RequestParam(value = "tr_name") String tr_name) {
        try {
            SeatInfo seatInfo = userService.seatInformation(start_station, end_station, tr_name);
            return new ResponseResult<SeatInfo>(ResultCode.OK.getValue(), seatInfo);
        } catch (Exception e) {
            return new ResponseResult<String>(ResultCode.ERROR.getValue(), e.toString());
        }
    }

    /**
     * 用户退票
     *
     * @param ti_id 票的Id
     * @return
     */
    @TokenCheck
    @PutMapping("/refund")
    public ResponseResult<String> refund(@RequestParam(value = "ti_id") String ti_id) {
        try {
            userService.refund(ti_id, TokenInterceptor.Id.get());
            return new ResponseResult<String>(ResultCode.OK.getValue(), "退票成功");
        } catch (Exception e) {
            return new ResponseResult<String>(ResultCode.ERROR.getValue(), e.toString());
        }
    }

    /**
     * 补票
     *
     * @param ticket         原有的票
     * @param extend_station 额外的终点站点名
     * @return
     */
    @TokenCheck
    @PostMapping("/ticketExtension")
    public ResponseResult<String> ticketExtension(Ticket ticket, @RequestParam(value = "extend_station") String extend_station) {
        try {
            userService.addExtendTicket(ticket, extend_station, TokenInterceptor.Id.get());
            return new ResponseResult<String>(ResultCode.OK.getValue(), "补票成功");
        } catch (Exception e) {
            return new ResponseResult<String>(ResultCode.ERROR.getValue(), e.toString());
        }
    }

    /**
     * 添加抢票
     *
     * @param robTicket
     * @return
     */
    @TokenCheck
    @PostMapping("/addRobTicket")
    public ResponseResult<String> addRobTicket(RobTicket robTicket) {
        try {
            userService.addRobTicket(robTicket, TokenInterceptor.Id.get());
            return new ResponseResult<String>(ResultCode.OK.getValue(), "等待抢票中");
        } catch (Exception e) {
            return new ResponseResult<String>(ResultCode.ERROR.getValue(), e.toString());
        }
    }

    /**
     * 根据rob_id取消抢票
     *
     * @param
     * @return
     */
    @TokenCheck
    @DeleteMapping("/RobTicket")
    public ResponseResult<String> deleteRobTicket(@RequestParam(value = "rob_id")String rob_id) {
        try {
            userService.deleteRobTicket(rob_id, TokenInterceptor.Id.get());
            return new ResponseResult<String>(ResultCode.OK.getValue(), "取消成功");
        } catch (Exception e) {
            return new ResponseResult<String>(ResultCode.ERROR.getValue(), e.toString());
        }
    }

    /**
     * 查看当前用户抢票信息
     *
     * @param
     * @return
     */
    @TokenCheck
    @PostMapping("/RobTicket")
    public ResponseResult RobTicket(@RequestParam(value = "startPage") Integer startPage,
                                            @RequestParam(value = "pageSize") Integer pageSize) {
        try{
            PageInfo<RobTicket> tickets = userService.getRobTickets(TokenInterceptor.Id.get(), startPage, pageSize);
            return new ResponseResult<PageInfo<RobTicket>>(ResultCode.OK.getValue(), tickets);
        } catch (Exception e) {
            return new ResponseResult<String>(ResultCode.ERROR.getValue(), "查询失败");
        }
    }

    /**
     * 通过车次名称获取车次信息
     *
     * @param tr_name
     * @return
     */
    @TokenCheck
    @GetMapping("/train")
    public ResponseResult getTrain(@RequestParam(value = "tr_name") String tr_name) {
        try {
            return new ResponseResult<Train>(ResultCode.OK.getValue(), userService.getTrain(tr_name));
        } catch (Exception e) {
            return new ResponseResult<String>(ResultCode.ERROR.getValue(), e.toString());
        }
    }

    /**
     * 通过车次id获取车次信息
     *
     * @param tr_id
     * @return
     */
    @TokenCheck
    @GetMapping("/train/{tr_id}")
    public ResponseResult getTrainById(@PathVariable(value = "tr_id")String tr_id) {
        try {
            return new ResponseResult<List<Train>>(ResultCode.OK.getValue(), userService.getTrainById(tr_id));
        } catch (Exception e) {
            return new ResponseResult<String>(ResultCode.ERROR.getValue(), e.toString());
        }
    }

    /**
     * 通过站点名称获取站点信息
     *
     * @param s_name
     * @return
     */
    @TokenCheck
    @GetMapping("/station")
    public ResponseResult getStation(@RequestParam(value = "s_name") String s_name) {
        try {
            return new ResponseResult<Station>(ResultCode.OK.getValue(), userService.getStation(s_name));
        } catch (Exception e) {
            return new ResponseResult<String>(ResultCode.ERROR.getValue(), e.toString());
        }
    }

    /**
     * 通过站点Id获取站点信息
     *
     * @param s_id
     * @return
     */
    @TokenCheck
    @GetMapping("/station/{s_id}")
    public ResponseResult getStationById(@PathVariable(value = "s_id") String s_id) {
        try {
            return new ResponseResult<List<Station>>(ResultCode.OK.getValue(), userService.getStationById(s_id));
        } catch (Exception e) {
            return new ResponseResult<String>(ResultCode.ERROR.getValue(), e.toString());
        }
    }

    /**
     * 获取票价的信息
     *
     * @return
     */
    @TokenCheck
    @GetMapping("/price")
    public ResponseResult getPrice(@RequestParam(value = "vm_id") String vm_id,
                                   @RequestParam(value = "p_seat_style") String p_seat_style,
                                   @RequestParam(value = "date") LocalDate date) {
        try {
            return new ResponseResult<List<Price>>(ResultCode.OK.getValue(), userService.getPrice(vm_id, p_seat_style, date));
        } catch (Exception e) {
            return new ResponseResult<String>(ResultCode.ERROR.getValue(), e.toString());
        }
    }

    /**
     * 指定车次路线信息
     * @param tr_name
     * @param s_station_name
     * @param e_station_name
     * @return
     */
    @TokenCheck
    @GetMapping("/pathInfo")
    public ResponseResult pathInfo(@RequestParam(value = "tr_name") String tr_name,
                                    @RequestParam(value = "s_station_name")String s_station_name,
                                   @RequestParam(value = "e_station_name")String e_station_name){
        try {
            return new ResponseResult(ResultCode.OK.getValue(),userService.getPathInfo(tr_name,s_station_name,e_station_name));
        }catch (Exception e){
            return new ResponseResult(ResultCode.ERROR.getValue(), e.toString());
        }
    }
}

