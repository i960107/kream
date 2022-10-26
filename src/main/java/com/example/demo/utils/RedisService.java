package com.example.demo.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate redisTemplate;
    private final int LIMIT_TIME = 3 * 60;
    private final String PREFIX = "sms";

    public void setHashValue(Long productIdx, List<Long> productSizeIdx) {
        redisTemplate.opsForSet().add(productIdx, productSizeIdx);
    }

    public List<Long> getProductSizeIdxList(Long productIdx) {
        return (List<Long>) redisTemplate.opsForSet().pop(productIdx);
    }

    public void setSmsCertification(String phone, String certificationNum) {
        if (!hasKey(phone)) {
            removeSmsCertification(phone);
        }
        redisTemplate.opsForValue().set(PREFIX + phone, certificationNum, Duration.ofSeconds(LIMIT_TIME));
    }

    public String getSmsCertification(String phone) {
        return (String) redisTemplate.opsForValue().get(PREFIX + phone);
    }

    public void removeSmsCertification(String phone) {
        redisTemplate.delete(PREFIX + phone);
    }

    public boolean hasKey(String phone) {
        return redisTemplate.hasKey(PREFIX + phone);
    }
}
