package com.longsan;

import org.springframework.beans.factory.annotation.Autowired;

public class HelloService {

    @Autowired
    private MyProperties properties;

    public String HelloWord(String userName) {
        return properties.getPrefix() + userName + properties.getSuffix();
    }
}
