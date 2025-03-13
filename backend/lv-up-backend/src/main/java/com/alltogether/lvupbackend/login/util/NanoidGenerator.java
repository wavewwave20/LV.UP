package com.alltogether.lvupbackend.login.util;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import org.springframework.stereotype.Component;

@Component
public class NanoidGenerator {
    public String generateNanoid() {
        return NanoIdUtils.randomNanoId();
    }
}
