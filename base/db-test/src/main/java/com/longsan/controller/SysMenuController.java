package com.longsan.controller;


import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 菜单权限表 前端控制器
 * </p>
 *
 * @author longhao
 * @since 2023-04-07
 */
@RestController
@RequestMapping("/sysMenu")
public class SysMenuController {

    @GetMapping("/get")
    public String testGet(String message) {
        return message + "get";
    }

    @PostMapping("post")
    public PostDo testPost(@RequestBody PostDo postDo) {
        postDo.setName(postDo.getName() + "post");
        TestList testList = postDo.getLists().get(0);
        testList.setSex("bigSex");
        postDo.getLists().set(0, testList);
        return postDo;
    }


    @Data
    static class PostDo{
        private String name;
        private Integer age;
        private List<TestList> lists;
    }

    @Data
    static class TestList{
        private String sex;
        private Integer big;
    }

}

