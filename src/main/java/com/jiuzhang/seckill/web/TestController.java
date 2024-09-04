package com.jiuzhang.seckill.web;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
public class TestController {

    @ResponseBody
    @RequestMapping("hello")
    public String hello(){
        String result;
        // Resource names can use any string with business semantics, such as method names,
        // names, or other uniquely identifiable strings.
        try (Entry entry = SphU.entry("HelloResource")){
            // Protected Business Logic
            result  = "Hello Sentinel";
            return result;
        }catch (BlockException ex) {
            // Resource access blocked, restricted or degraded
            // Perform the appropriate processing actions here
            log.error(ex.toString());
            result = "System is busy. Please try again later.";
            return  result;
        }
    }

    /**
     * Define rate-limiting rules
     * 1. Create a collection that holds rate-limiting rules
     * 2. Create a rate-limiting rule
     * 3. Place rate-limiting rules in the collection
     * 4. Load the rate-limiting rule
     * @PostConstruct Executed after the constructor of the current class is executed.
     */
    @PostConstruct
    public void seckillsFlow(){
        //1.Create a collection that holds rate-limiting rules
        List<FlowRule> rules = new ArrayList<>();
        //2.Creating a rate-limiting rule
        FlowRule rule = new FlowRule();
        //Defines the resource for which the sentinel will take effect.
        rule.setResource("seckills");
        //Define the type of rate-limiting rule, QPS type
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        //Define QPS number of requests passed per second
        rule.setCount(1);

        FlowRule rule2 = new FlowRule();
        rule2.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule2.setCount(2);
        rule2.setResource("HelloResource");
        //3. Add rate-limiting rules into collections
        rules.add(rule);
        rules.add(rule2);
        //4. Load rate-limiting rules
        FlowRuleManager.loadRules(rules);
    }
}