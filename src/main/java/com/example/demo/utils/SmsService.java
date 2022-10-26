package com.example.demo.utils;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.user.model.dto.PostSmsCertificationReq;
import com.example.demo.src.user.model.entity.UserRepository;
import lombok.RequiredArgsConstructor;
import net.nurigo.java_sdk.api.Message;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class SmsService {
    private final RedisService redisService;
    private final UserRepository userRepository;
    Message coolSms = new Message(Secret.COOL_SMS_API_KEY, Secret.COOL_SMS_API_SECRET);

    public void sendCertificationNum(String phone) throws BaseException {
        if (userRepository.existsByPhone(phone)) throw new BaseException(BaseResponseStatus.DUPLICATED_PHONE);

        String certificationNum = createRandomNumbers();

        HashMap<String, String> params = new HashMap<>();
        params.put("to", phone);
        params.put("from", Secret.COOL_SMS_FROM_PHONE);
        params.put("type", "SMS");
        params.put("text", "rp3-kream 인증번호는 [" + certificationNum + "]입니다.");
        params.put("app_version", "kream-dev");

        try {
            JSONObject result = coolSms.send(params);
            if (result.get("success_count").toString().equals(0)) {
                throw new BaseException(BaseResponseStatus.COOLSMS_FAIL);
            }
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.COOLSMS_FAIL);
        }

        redisService.setSmsCertification(phone, certificationNum);
    }

    public void verifyCertificationNum(PostSmsCertificationReq requestDto) throws BaseException {
        if (isVerified(requestDto)) redisService.removeSmsCertification(requestDto.getPhone());
    }

    private boolean isVerified(PostSmsCertificationReq requestDto) throws BaseException {
        if (!redisService.hasKey(requestDto.getPhone()))
            throw new BaseException(BaseResponseStatus.FAILED_TO_CERTIFICATE_DURATION_OUT);
        if (!redisService.getSmsCertification(requestDto.getPhone()).equals(requestDto.getCertificationNum()))
            throw new BaseException(BaseResponseStatus.FAILED_TO_CERTIFICATE_WRONG_NUMBER);
        return true;
    }

    public String createRandomNumbers() {
        Random random = new Random();
        String rnds = "";
        for (int i = 0; i < 5; i++) {
            String ran = Integer.toString(random.nextInt(10));
            rnds += ran;
        }
        return rnds;
    }

    public String sendTempPw(String phone) throws BaseException {
        String tempPw = createTempPw();

        HashMap<String, String> params = new HashMap<>();
        params.put("to", phone);
        params.put("from", Secret.COOL_SMS_FROM_PHONE);
        params.put("type", "SMS");
        params.put("text", "임시 비밀번호는 [" + tempPw + "]입니다.");
        params.put("app_version", "kream-dev");

        try{
            coolSms.send(params);
        }catch(Exception e){
            throw new BaseException(BaseResponseStatus.COOLSMS_FAIL);
        }

        return tempPw;
    }

    private String createTempPw() {
        StringBuffer temp = new StringBuffer();
        Random rnd = new Random();
        for (int i = 0; i < 10; i++) {
            int rIndex = rnd.nextInt(3);
            switch (rIndex) {
                case 0:
                    // a-z
                    temp.append((char) ((int) (rnd.nextInt(26)) + 97));
                    break;
                case 1:
                    // A-Z
                    temp.append((char) ((int) (rnd.nextInt(26)) + 65));
                    break;
                case 2:
                    // 0-9
                    temp.append((rnd.nextInt(10)));
                    break;
            }
        }
        return temp.toString();
    }

}
