package zyc.work.databasework.controller;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import zyc.work.databasework.ResultType.ResponseResult;
import zyc.work.databasework.annotation.toekn.LoginToken;
import zyc.work.databasework.annotation.toekn.TokenCheck;
import zyc.work.databasework.enums.result.ResultCode;
import zyc.work.databasework.enums.token.TokenType;
import zyc.work.databasework.pojo.*;
import zyc.work.databasework.service.ManagerService;

@RestController
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    public ManagerService managerService;


    /**
     * 管理员登陆
     * @param name
     * @param password
     * @return
     */
    @LoginToken
    @PostMapping(value = "/login")
    public ResponseResult managerlogin(@RequestParam @Nullable String name, @RequestParam @Nullable String password){
        try {
            return new ResponseResult(ResultCode.OK.getValue(),managerService.login(name,password));
        }catch (Exception e){
            return new ResponseResult(ResultCode.ERROR.getValue(),e.toString());
        }
    }

    /**
     * 获取车站信息(分页）
     * @param startPage
     * @param pageSize
     * @return
     */
    @TokenCheck(TYPE = TokenType.Manager)
    @GetMapping(value = "/station")
    public ResponseResult getStations(@RequestParam(value = "startPage")Integer startPage,@RequestParam(value = "pageSize")Integer pageSize){
        try {
            PageInfo stations = managerService.getStations(startPage, pageSize);
            return new ResponseResult(ResultCode.OK.getValue(), stations);
        }catch (Exception e){
            return new ResponseResult(ResultCode.ERROR.getValue(), "查询失败");
        }
    }

    /**
     * 添加车站信息
     * @param station
     * @return
     */
    @TokenCheck(TYPE = TokenType.Manager)
    @PostMapping(value = "/station")
    public ResponseResult addStation(Station station){
        try {
            managerService.addStation(station);
            return new ResponseResult(ResultCode.OK.getValue(), "加入成功");
        }catch (Exception e){
            return new ResponseResult(ResultCode.ERROR.getValue(), e.toString());
        }
    }

    /**
     * 删除车站信息
     * @param s_id
     * @return
     */
    @TokenCheck(TYPE = TokenType.Manager)
    @DeleteMapping(value = "/station")
    public ResponseResult deleteStation(@RequestParam(value = "s_id")String s_id){
        try {
            managerService.deleteStation(s_id);
            return new ResponseResult(ResultCode.OK.getValue(), "删除成功");
        }catch (Exception e){
            return new ResponseResult(ResultCode.ERROR.getValue(), e.toString());
        }
    }

    /**
     * 获取车次信息(分页）
     * @param startPage
     * @param pageSize
     * @return
     */
    @TokenCheck(TYPE = TokenType.Manager)
    @GetMapping("/train")
    public ResponseResult getTrains(@RequestParam(value = "startPage")Integer startPage,@RequestParam(value = "pageSize")Integer pageSize){
        try {
            PageInfo trains = managerService.getTrains(startPage, pageSize);
            return new ResponseResult(ResultCode.OK.getValue(), trains);
        }catch (Exception e){
            return new ResponseResult(ResultCode.ERROR.getValue(), e.toString());
        }
    }

    /**
     * 添加车次信息
     * @param train
     * @return
     */
    @TokenCheck(TYPE = TokenType.Manager)
    @PostMapping(value = "/train")
    public ResponseResult addTrain(Train train){
        try {
            managerService.addTrain(train);
            return new ResponseResult(ResultCode.OK.getValue(), "加入成功");
        }catch (Exception e){
            return new ResponseResult(ResultCode.ERROR.getValue(), e.toString());
        }
    }

    /**
     * 删除车次信息
     * @param tr_id
     * @return
     */
    @TokenCheck(TYPE = TokenType.Manager)
    @DeleteMapping(value = "/train")
    public ResponseResult deleteTrain(@RequestParam(value = "tr_id")String tr_id){
        try {
            managerService.deleteTrain(tr_id);
            return new ResponseResult(ResultCode.OK.getValue(), "删除成功");
        }catch (Exception e){
            return new ResponseResult(ResultCode.ERROR.getValue(), e.toString());
        }
    }

    /**
     * 获取车次路线信息(分页）
     * @param startPage
     * @param pageSize
     * @return
     */
    @TokenCheck(TYPE = TokenType.Manager)
    @GetMapping("/routerTrain")
    public ResponseResult getRouterTrains(@RequestParam(value = "startPage")Integer startPage,@RequestParam(value = "pageSize")Integer pageSize){
        try {
            PageInfo trains = managerService.getRouterTrains(startPage, pageSize);
            return new ResponseResult(ResultCode.OK.getValue(), trains);
        }catch (Exception e){
            return new ResponseResult(ResultCode.ERROR.getValue(), e.toString());
        }
    }

    /**
     * 通过tr_id获取相应车次的路线信息
     * @param tr_id
     * @return
     */
    @TokenCheck(TYPE = TokenType.Manager)
    @GetMapping("/routerTrain/{tr_id}")
    public ResponseResult getRouterTrainByTr_id(@PathVariable(value = "tr_id")String tr_id){
        try {
            return new ResponseResult(ResultCode.OK.getValue(), managerService.getRouterTrainByTr_id(tr_id));
        }catch (Exception e){
            return new ResponseResult(ResultCode.ERROR.getValue(), e.toString());
        }
    }

    /**
     * 添加车次路线信息
     * @param routertrain
     * @return
     */
    @TokenCheck(TYPE = TokenType.Manager)
    @PostMapping(value = "/routerTrain")
    public ResponseResult addRouterTrain(RouterTrain routertrain){
        try {
            managerService.addRouterTrain(routertrain);
            return new ResponseResult(ResultCode.OK.getValue(), "加入成功");
        }catch (Exception e){
            return new ResponseResult(ResultCode.ERROR.getValue(), e.toString());
        }
    }

    /**
     * 删除车次路线信息
     * @param tr_id
     * @param r_id
     * @return
     */
    @TokenCheck(TYPE = TokenType.Manager)
    @DeleteMapping(value = "/routerTrain")
    public ResponseResult deleteRouterTrain(@RequestParam(value = "tr_id")String tr_id,@RequestParam(value = "r_id")String r_id){
        try {
            managerService.deleteRouterTrain(tr_id,r_id);
            return new ResponseResult(ResultCode.OK.getValue(), "删除成功");
        }catch (Exception e){
            return new ResponseResult(ResultCode.ERROR.getValue(), e.toString());
        }
    }

    /**
     * 获取路线信息(分页）
     * @param startPage
     * @param pageSize
     * @return
     */
    @TokenCheck(TYPE = TokenType.Manager)
    @GetMapping("/router")
    public ResponseResult getRouters(@RequestParam(value = "startPage")Integer startPage,@RequestParam(value = "pageSize")Integer pageSize){
        try {
            PageInfo trains = managerService.getRouters(startPage, pageSize);
            return new ResponseResult(ResultCode.OK.getValue(), trains);
        }catch (Exception e){
            return new ResponseResult(ResultCode.ERROR.getValue(), e.toString());
        }
    }

    /**
     * 添加路线信息
     * @param router
     * @return
     */
    @TokenCheck(TYPE = TokenType.Manager)
    @PostMapping(value = "/router")
    public ResponseResult addRouter(Router router){
        try {
            managerService.addRouter(router);
            return new ResponseResult(ResultCode.OK.getValue(), "加入成功");
        }catch (Exception e){
            return new ResponseResult(ResultCode.ERROR.getValue(), e.toString());
        }
    }

    /**
     * 删除路线信息
     * @param r_id
     * @return
     */
    @TokenCheck(TYPE = TokenType.Manager)
    @DeleteMapping(value = "/router")
    public ResponseResult deleteRouter(@RequestParam(value = "r_id")String r_id){
        try {
            managerService.deleteRouter(r_id);
            return new ResponseResult(ResultCode.OK.getValue(), "删除成功");
        }catch (Exception e){
            return new ResponseResult(ResultCode.ERROR.getValue(), e.toString());
        }
    }

    /**
     * 获取车型信息(分页）
     * @param startPage
     * @param pageSize
     * @return
     */
    @TokenCheck(TYPE = TokenType.Manager)
    @GetMapping("/vehicleModel")
    public ResponseResult getVehicleModel(@RequestParam(value = "startPage")Integer startPage,@RequestParam(value = "pageSize")Integer pageSize){
        try {
            PageInfo trains = managerService.getVehicleModels(startPage, pageSize);
            return new ResponseResult(ResultCode.OK.getValue(), trains);
        }catch (Exception e){
            return new ResponseResult(ResultCode.ERROR.getValue(), e.toString());
        }
    }

    /**
     * 添加车型信息
     * @param vehicleModel
     * @return
     */
    @TokenCheck(TYPE = TokenType.Manager)
    @PostMapping(value = "/vehicleModel")
    public ResponseResult addVehicleModel(VehicleModel vehicleModel){
        try {
            managerService.addVehicleModel(vehicleModel);
            return new ResponseResult(ResultCode.OK.getValue(), "加入成功");
        }catch (Exception e){
            return new ResponseResult(ResultCode.ERROR.getValue(), e.toString());
        }
    }

    /**
     * 删除车型信息
     * @param vm_id
     * @return
     */
    @TokenCheck(TYPE = TokenType.Manager)
    @DeleteMapping(value = "/vehicleModel")
    public ResponseResult deleteVehicleModel(@RequestParam(value = "r_id")String vm_id){
        try {
            managerService.deleteVehicleModel(vm_id);
            return new ResponseResult(ResultCode.OK.getValue(), "删除成功");
        }catch (Exception e){
            return new ResponseResult(ResultCode.ERROR.getValue(), e.toString());
        }
    }

}
