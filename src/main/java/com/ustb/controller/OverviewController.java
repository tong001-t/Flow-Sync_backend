package com.ustb.controller;

import com.ustb.entity.Overview;
import com.ustb.service.OverviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author:Wangtao
 * @Data:2026/7/22 14:27
 */
@RestController
@RequestMapping("/overview/")
@CrossOrigin
public class OverviewController {
    @Autowired
    private OverviewService overviewService;

    @GetMapping("data")
    public Overview getOverviewData() {
        return overviewService.getOverviewData();
    }
}
