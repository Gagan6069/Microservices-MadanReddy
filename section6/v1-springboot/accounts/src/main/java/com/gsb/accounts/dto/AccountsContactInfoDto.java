package com.gsb.accounts.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

// 6.71: Reading configurations using @Configuration properties
// record class provides getter method by default for all fields and constructor is initiailized behind the scenes.
// There is no setter method. This helps when we only require data and need not change it.
@ConfigurationProperties(prefix = "accounts")
public record AccountsContactInfoDto(String message, Map<String, String> contactDetails, List<String> onCallSupport) {

}