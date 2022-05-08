package zyc.work.databasework.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import zyc.work.databasework.Exception.ExecException;
import zyc.work.databasework.Exception.InvalidDataException;
import zyc.work.databasework.Exception.LoginFailException;

import zyc.work.databasework.enums.token.TokenType;
import zyc.work.databasework.mapper.ManagerMapper;
import zyc.work.databasework.pojo.*;
import zyc.work.databasework.util.TokenUtil;

import java.util.List;

@Service
public class ManagerService {
    @Autowired
    public ManagerMapper managerMapper;

    @Autowired
    public TokenUtil tokenUtil;

    @Transactional(rollbackFor = {Exception.class})
    public String login(String name, String password) throws Exception {
        if (!StringUtils.hasText(name) || !StringUtils.hasText(password))
            throw new InvalidDataException();
        String id = managerMapper.selectIdByNaAndPw(name, password);
        if (id == null)
            throw new LoginFailException();
        return tokenUtil.CreateToken(3600000, id, TokenType.Manager);
    }

    @Transactional(rollbackFor = {Exception.class})
    public PageInfo getStations(Integer startPage, Integer pageSize) {
        PageHelper.startPage(startPage, pageSize);
        return new PageInfo<>(managerMapper.selectStation(), startPage);
    }

    @Transactional(rollbackFor = {Exception.class})
    public void addStation(Station station) {
        managerMapper.addStation(station);
    }

    @Transactional(rollbackFor = {Exception.class})
    public void deleteStation(String s_id)  {
        managerMapper.deleteStation(s_id);
    }

    @Transactional(rollbackFor = {Exception.class})
    public PageInfo getTrains(Integer startPage, Integer pageSize) {
        PageHelper.startPage(startPage, pageSize);
        return new PageInfo<>(managerMapper.selectTrain(), startPage);
    }

    @Transactional(rollbackFor = {Exception.class})
    public void addTrain(Train train) throws Exception {
        if (managerMapper.addTrain(train) == 0)
            throw new ExecException();
    }

    @Transactional(rollbackFor = {Exception.class})
    public void deleteTrain(String tr_id){
        managerMapper.deleteTrain(tr_id);

    }

    @Transactional(rollbackFor = {Exception.class})
    public PageInfo getRouterTrains(Integer startPage, Integer pageSize) {
        PageHelper.startPage(startPage, pageSize);
        return new PageInfo<>(managerMapper.selectRouterTrain(), startPage);
    }

    @Transactional(rollbackFor = {Exception.class})
    public List<RouterTrain> getRouterTrainByTr_id(String tr_id) {
        return managerMapper.selectRouterTrainByTr_id(tr_id);
    }

    @Transactional(rollbackFor = {Exception.class})
    public void addRouterTrain(RouterTrain routerTrain) throws Exception {
        if (managerMapper.addRouterTrain(routerTrain) == 0)
            throw new ExecException();
    }

    @Transactional(rollbackFor = {Exception.class})
    public void deleteRouterTrain(String tr_id, String r_id) {
        managerMapper.deleteRouterTrain(tr_id, r_id);
    }

    @Transactional(rollbackFor = {Exception.class})
    public PageInfo getRouters(Integer startPage, Integer pageSize) {
        PageHelper.startPage(startPage, pageSize);
        return new PageInfo<>(managerMapper.selectRouter(), startPage);
    }

    @Transactional(rollbackFor = {Exception.class})
    public void addRouter(Router router) {
        managerMapper.addRouter(router);
    }

    @Transactional(rollbackFor = {Exception.class})
    public void deleteRouter(String r_id) {
        managerMapper.deleteRouter(r_id);
    }

    @Transactional(rollbackFor = {Exception.class})
    public PageInfo getVehicleModels(Integer startPage, Integer pageSize) {
        PageHelper.startPage(startPage, pageSize);
        return new PageInfo<>(managerMapper.selectVehicleModel(), startPage);
    }

    @Transactional(rollbackFor = {Exception.class})
    public void addVehicleModel(VehicleModel vehicleModel) throws Exception {
        if(managerMapper.addVehicleModel(vehicleModel)==0)
            throw new ExecException();
    }

    @Transactional(rollbackFor = {Exception.class})
    public void deleteVehicleModel(String vm_id) {
        managerMapper.deleteVehicleModel(vm_id);
    }
}
