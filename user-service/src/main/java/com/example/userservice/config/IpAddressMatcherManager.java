package com.example.userservice.config;

import lombok.Getter;
import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class IpAddressMatcherManager {
    @Getter
    private final List<IpAddressMatcher> ipAddressMatchers;

    public IpAddressMatcherManager() {
        this.ipAddressMatchers = Arrays.asList(
                new IpAddressMatcher("192.168.1.0/24"),
                new IpAddressMatcher("192.168.2.0/24")
        );
    }

    public boolean isAccessible(String ipAddress) {
        //List<IpAddressMatcher> ipAddressMatchers = this.getIpAddressMatchers();
        return ipAddressMatchers.stream().anyMatch(matcher -> matcher.matches(ipAddress));
    }
}
