package com.aaron.section_seckill.controller;

import com.aaron.section_seckill.VO.ResultVO;
import com.aaron.section_seckill.constant.QueueConstants;
import com.aaron.section_seckill.constant.SessionConstants;
import com.aaron.section_seckill.entity.Section;
import com.aaron.section_seckill.entity.Takes;
import com.aaron.section_seckill.service.SectionService;
import com.aaron.section_seckill.service.TakeService;
import com.aaron.section_seckill.utils.MQProvider;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * for section_manage
 * </p>
 *
 * @author AaronHuang
 * @since 2018/9/7
 */
@RestController
@RequestMapping("/section")
public class TakesController {

    @Autowired
    TakeService takeService;
    @Autowired
    SectionService sectionService;
    @Autowired
    MQProvider mqProvider;

    @GetMapping("/list")
    public ResultVO getSectionList(@RequestParam("page") int page, @RequestParam("size") int size) {
        List<Section> sections = sectionService.findSectionByPage(page, size);
        return ResultVO.success("成功", sections);
    }

    @PostMapping("/success")
    public ResultVO checkSuccess(@RequestParam("secid") String secid, HttpSession session) {
        String stuid = (String) session.getAttribute(SessionConstants.SESSION_KEY);
        int ok = takeService.checkSuccess(stuid, secid);
        if (ok > 0) {
            return ResultVO.success("成功", ok);
        } else if (ok == 0) {
            return ResultVO.failed("处理未完成", ok);
        } else {
            return ResultVO.failed("失败", ok);
        }
    }

    @GetMapping("/cantake")
    public boolean getStatus() {
        return takeService.getTakeStatus();
    }

    @GetMapping("/switch")
    public void changeStatus() {
        takeService.saveOrSetTakeStatus(!takeService.getTakeStatus());
    }

//    @PostMapping("/takes")
//    public void contextLoads(@RequestParam("stuid") String stuid, @RequestParam("secid") String secid) {
//        mqProvider.sendTakeMessage(stuid, secid);
//    }

    // 接收数组json类似["section1", "section2"]
    @PostMapping(value = "/take", produces = "application/json")
    public ResultVO contextLoads(@RequestBody List<String> secids, HttpSession session) {
        String stuid = (String) session.getAttribute(SessionConstants.SESSION_KEY);
//        secids.stream().forEach(System.out :: println);
        for (String secid : secids) {
            takeService.tryTake(secid, stuid);
        }
        return ResultVO.success("请求已提交，请等待处理", "");
    }

    @GetMapping("/time")
    public ResultVO getTime() {
        long time =  new Date().getTime() / 1000;
        return ResultVO.success("成功", time);
    }

    @RabbitListener(queues = QueueConstants.TAKES_QUEUE)
    public void test(Takes takes) {
        takeService.takeSectionToDB(takes);
    }

}
