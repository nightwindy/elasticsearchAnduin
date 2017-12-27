package com.anduin.api.service;

import com.anduin.api.constant.Apiconstants;
import com.anduin.api.dto.SearchConditionDto;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class Main {

    public static void main(String[] args){
        ApplicationContext context= new ClassPathXmlApplicationContext("applicationContext.xml");
        CompanyESearchService b2bESearchService= (CompanyESearchService ) context.getBean("b2bESearchService");
        SearchConditionDto searchCondition = new SearchConditionDto();
        searchCondition.setKeyWord("我的买买");
        searchCondition.setIndexName(Apiconstants.COMPANY_INDEX_ALIAS);
        b2bESearchService.commonSearch(searchCondition);
    }
}
