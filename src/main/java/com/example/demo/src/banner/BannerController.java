package com.example.demo.src.banner;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.banner.model.GetBannerRes;
import com.example.demo.utils.ValidationRegex;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BannerController {
    private final BannerProvider bannerProvider;

    @GetMapping("/api/banners")
    public BaseResponse<List<GetBannerRes>> retrieveBannerList(
             @RequestParam(name = "location", required = true) String location
    ) throws BaseException {
        List<GetBannerRes> responseDto = bannerProvider.retrieveBannerList(location);
        return new BaseResponse<List<GetBannerRes>>(responseDto);
    }


}
