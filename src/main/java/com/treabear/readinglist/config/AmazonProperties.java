package com.treabear.readinglist.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * AmazonProperties
 * 
 * 아마존에 특화된 구성 프로퍼티
 */
@Component
@ConfigurationProperties("amazon")  // 프로퍼티 주입
@Setter @Getter
public class AmazonProperties {

    private String associateId;

}