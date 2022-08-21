package ycw.hospital.hosp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ycw.hospital.common.model.hosp.HospitalSet;
import ycw.hospital.common.result.Result;
import ycw.hospital.hosp.service.HospitalSetService;
import ycw.hospital.common.utils.MD5;
import ycw.hospital.common.vo.hosp.HospitalSetQueryVo;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {

    @Autowired
    private HospitalSetService service;


    // 1 查询所有医院设置
    @ApiOperation(value = "获取所有医院设置")
    @GetMapping("/all")
    public Result getAllHospitalSet() {
        List<HospitalSet> list = service.list();
        return Result.ok(list);
    }


    // 5 根据id获取医院设置
    @ApiOperation(value = "根据id获取医院设置")
    @GetMapping("get/{id}")
    public Result getHospitalSet(@PathVariable Long id) {
        HospitalSet hospitalSet = service.getById(id);
        return Result.ok(hospitalSet);
    }


    // 2 逻辑删除医院设置
    @ApiOperation(value = "逻辑删除医院设置")
    @DeleteMapping("{id}")
    public Result removeHospitalSet(@PathVariable Long id) {
        boolean flag = service.removeById(id);
        if (flag) {
            return Result.ok();
        }
        return Result.fail();
    }


    @ApiOperation(value = "添加医院设置")
    @PostMapping("save")
    public Result saveHospitalSet(@RequestBody HospitalSet hospitalSet) {
        //设置状态 1 使用 0 不能使用
        hospitalSet.setStatus(1);
        //签名秘钥
        Random random = new Random();
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis() + "" + random.nextInt(1000)));
        //调用service
        boolean save = service.save(hospitalSet);
        if (save) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }


    // 3 条件查询带分页

    @GetMapping("page/{current}/{limit}")
    public Result findPageHospitalSet(@PathVariable long current,
                                  @PathVariable long limit,
                                  @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo){
        // 1) Page对象: 传递当前页和每页记录数
        Page<HospitalSet> page = new Page<>(current, limit);

        // 2) 构造条件
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        String hoscode = hospitalSetQueryVo.getHoscode();
        String hosname = hospitalSetQueryVo.getHosname();

        // 3) 判断条件是否可用
        if (!StringUtils.isEmpty(hoscode)) {
            wrapper.like("hosname", hospitalSetQueryVo.getHosname());
        }
        if (!StringUtils.isEmpty(hosname)) {
            wrapper.eq("hoscode", hospitalSetQueryVo.getHoscode());
        }

        // 4) 分页查询
        Page<HospitalSet> pageHospSet = service.page(page, wrapper);

        return Result.ok(pageHospSet);
    }


    // 6 修改医院设置
    @ApiOperation(value = "修改医院设置")
    @PostMapping("update")
    public Result updateHospitalSet(@RequestBody HospitalSet hospitalSet) {
        boolean flag = service.updateById(hospitalSet);
        if (flag) {
            return Result.ok();
        }
        return Result.fail();
    }


    // 7 批量删除医院设置
    @DeleteMapping("batchRemove")
    public Result batchRemoveHospSet(@RequestBody List<Long> idList) {
        boolean b = service.removeByIds(idList);
        return Result.ok();
    }


    // 8  医院设置锁定和解锁
    @PutMapping("lockHospitalSet/{id}/{status}")
    public Result lockHospitalSet(@PathVariable Long id,
                                  @PathVariable Integer status) {
        //根据id查询医院设置信息
        HospitalSet hospitalSet = service.getById(id);
        //设置状态
        hospitalSet.setStatus(status);
        //调用方法
        service.updateById(hospitalSet);
        return Result.ok();
    }

    // 9 发送签名秘钥
    @PutMapping("sendKey/{id}")
    public Result lockHospSet(@PathVariable long id) {
        HospitalSet hospitalSet = service.getById(id);
        String signKey = hospitalSet.getSignKey();
        String hoscode = hospitalSet.getHoscode();
        /**
         * TODO
         * 发送短信
         */
        return Result.ok();
    }


}
