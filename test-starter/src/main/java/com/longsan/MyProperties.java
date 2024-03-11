package com.longsan;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("long.name")
public class MyProperties {

    private String prefix;
    private String suffix;
}
