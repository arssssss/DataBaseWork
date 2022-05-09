package zyc.work.databasework.controller;

import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
     * @return
     */
    @RequestMapping("/")
    public ResponseResult<String> test(){
        return new ResponseResult<>(ResultCode.OK.getValue(), "/连接成功");
    }
    /**
     * 测试连通情况
     * @return
     */
    @RequestMapping("/api")
    public ResponseResult<String> test2(){
        return new ResponseResult<>(ResultCode.OK.getValue(), "/api连接成功");
    }
    /**
     * 测试连通情况
     * @return
     */
    @RequestMapping("/api/")
    public ResponseResult<String> test3(){
        return new ResponseResult<>(ResultCode.OK.getValue(), "/api/连接成功");
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
    public ResponseResult<String> userlogin(@RequestParam String phone, @RequestParam String password) {
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
    @GetMapping("/seatInformation")
    public ResponseResult seatInformation(@RequestParam(value = "start_station") String start_station,
                                          @RequestParam(value = "end_station") String end_station,
                                          @RequestParam(value = "tr_name") String tr_name){
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
}
