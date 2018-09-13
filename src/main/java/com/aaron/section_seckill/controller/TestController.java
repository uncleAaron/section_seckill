package com.aaron.section_seckill.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * for section_seckill
 * </p>
 *
 * @author AaronHuang
 * @since 2018/9/12
 */
@RestController
@RequestMapping("/test")
public class TestController {

//    @Autowired
//    TakeService takeService;
//
//    @GetMapping("/decr")
//    public boolean decr(@RequestParam("sid") String sid) {
//        boolean a = takeService.tryTake("section1", "123123");
//        return a;
//    }

/*    @Autowired
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
    public ResultVO checkSuccess(@RequestParam("stuid") String stuid, @RequestParam("secid") String secid) {
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

    @PostMapping("/takes")
    public void contextLoads(@RequestParam("stuid") String stuid, @RequestParam("secid") String secid) {
        mqProvider.sendTakeMessage(stuid, secid);
    }

    @PostMapping("/take2")
    public void contextLoads(@RequestBody ArrayList<String> secids) {
        secids.stream().forEach(System.out::println);
    }
*/
}
