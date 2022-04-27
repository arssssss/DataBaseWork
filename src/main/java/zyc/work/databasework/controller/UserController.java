package zyc.work.databasework.controller;

import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import zyc.work.databasework.ResultType.ResponseResult;
import zyc.work.databasework.annotation.toekn.LoginToken;
import zyc.work.databasework.annotation.toekn.TokenCheck;
import zyc.work.databasework.enums.result.ResultCode;
import zyc.work.databasework.enums.token.TokenType;
import zyc.work.databasework.interceptor.token.TokenInterceptor;
import zyc.work.databasework.pojo.RobTicket;
import zyc.work.databasework.pojo.Ticket;
import zyc.work.databasework.pojo.User;
import zyc.work.databasework.service.ManagerService;
import zyc.work.databasework.service.UserService;
import zyc.work.databasework.util.TokenUtil;

import java.time.LocalDate;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    public UserService userService;
    @Autowired
    public ManagerService managerService;

    @Autowired
    public TokenUtil tokenUtil;

    /**
     * 用户注册
     * @param user
     * @return
     */
    @PostMapping("/userregister")
    public ResponseResult register(User user){
        boolean register = userService.register(user);
        if(register)
            return new ResponseResult(ResultCode.OK.getValue(), "注册成功");
        return new ResponseResult(ResultCode.ERROR.getValue(), "注册失败");
    }

    /**
     * 用户登录
     * @param phone
     * @param password
     * @return
     */
    @LoginToken
    @PostMapping(value = "/userlogin")
    public ResponseResult userlogin(@RequestParam @Nullable String phone, @RequestParam @Nullable String password){
        if(NumberUtils.isDigits(phone) && StringUtils.hasText(password)){
            String id= userService.login(phone,password);
            if(id!=null){
                return new ResponseResult(ResultCode.OK.getValue(),tokenUtil.CreateToken(3600000, id, TokenType.User));
            }
        }
        return new ResponseResult(ResultCode.ERROR.getValue(),null);
    }

    /**
     * 查询用户信息
     * @return
     */
    @TokenCheck
    @GetMapping( "/userInformation")
    public ResponseResult getUserInformation(){
        return new ResponseResult(ResultCode.OK.getValue(), userService.getUser(TokenInterceptor.Id.get()));
    }

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    @TokenCheck
    @PutMapping("/userInformation")
    public ResponseResult updateUserInformation(User user){
        user.u_id=TokenInterceptor.Id.get();
        return new ResponseResult(ResultCode.OK.getValue(), userService.updateUser(user));
    }

    /**
     * 查询用户已购票(分页)
     * @param startPage
     * @param pageSize
     * @return
     */
    @TokenCheck
    @GetMapping("/ticket")
    public ResponseResult queryTicket(@RequestParam(value = "startPage")Integer startPage,@RequestParam(value = "pageSize")Integer pageSize){
        PageInfo tickets = userService.getTickets(TokenInterceptor.Id.get(), startPage, pageSize);
        if(tickets==null)
            return new ResponseResult(ResultCode.ERROR.getValue(), "查询失败");
        return new ResponseResult(ResultCode.OK.getValue(),tickets);
    }

    /**
     * 查询路线
     * @param start_station 起始站的站点名称
     * @param end_station 终点站的站点名称
     * @param s_date 出发时间
     * @param transit 限制换乘次数
     * @return
     */
    @TokenCheck
    @GetMapping("/path")
    public ResponseResult queryPath(@RequestParam(value = "start_station") String start_station,
                            @RequestParam(value = "end_station") String end_station,
                            @RequestParam(value = "s_date")LocalDate s_date,
                            @RequestParam(value = "transit") Integer transit){
        return  new ResponseResult(ResultCode.OK.getValue(),userService.getPath(start_station,end_station,s_date,transit));
    }

    /**
     * 用户购票
     * @return
     */
    @TokenCheck
    @PostMapping("/ticket")
    public ResponseResult buyTicket(Ticket ticket){
        ticket.setU_id(TokenInterceptor.Id.get());

        if(userService.buyTicket(ticket))
            return new ResponseResult(ResultCode.OK.getValue(),"购买成功");
        return new ResponseResult(ResultCode.ERROR.getValue(), "购买失败");
    }

    /**
     * 查看相关车次的座位情况
     * @param start_station 出发站站点名称
     * @param end_station 终点站站点名称
     * @param tr_name 车次名
     * @return
     */
    @GetMapping("/seatInformation")
    public ResponseResult seatInformation(@RequestParam(value = "start_station") String start_station,
                                          @RequestParam(value = "end_station") String end_station,
                                          @RequestParam(value = "tr_name")String tr_name){
        List<Boolean> booleans = userService.seatInformation(start_station, end_station, tr_name);
        if(booleans==null)
            return new ResponseResult(ResultCode.ERROR.getValue(), "查询失败");
        return new ResponseResult(ResultCode.OK.getValue(), booleans);
    }

    /**
     * 用户退票
     * @param ti_id 票的Id
     * @return
     */
    @TokenCheck
    @PutMapping("/refund")
    public ResponseResult refund(@RequestParam(value = "ti_id") String ti_id){
        if(userService.refund(ti_id,TokenInterceptor.Id.get()))
            return new ResponseResult(ResultCode.OK.getValue(), "退票成功");
        return new ResponseResult(ResultCode.ERROR.getValue(), "退票失败");
    }

    /**
     * 补票
     * @param ticket 原有的票
     * @param extend_station 额外的终点站点名
     * @return
     */
    @TokenCheck
    @PostMapping("/ticketExtension")
    public ResponseResult ticketExtension(Ticket ticket,@RequestParam(value = "extend_station")String extend_station){
        if(!userService.addExtendTicket(ticket,extend_station,TokenInterceptor.Id.get()))
            return new ResponseResult(ResultCode.ERROR.getValue(), "补票失败");
        return new ResponseResult(ResultCode.OK.getValue(), "补票成功");
    }

    /**
     * 添加抢票
     * @param robTicket
     * @return
     */
    @TokenCheck
    @PostMapping("/addRobTicket")
    public ResponseResult addRobTicket(RobTicket robTicket){
        if(userService.addRobTicket(robTicket,TokenInterceptor.Id.get()))
            return new ResponseResult(ResultCode.OK.getValue(), "等待抢票中");
        return new ResponseResult(ResultCode.ERROR.getValue(), "抢票失败");
    }
}
