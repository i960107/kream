package com.example.demo.src.banner;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.config.Status;
import com.example.demo.src.banner.model.BannerRepository;
import com.example.demo.src.banner.model.GetBannerRes;
import com.example.demo.utils.ValidationRegex;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BannerProvider {
    private final BannerRepository bannerRepository;

    public List<GetBannerRes> retrieveBannerList(String location) throws BaseException {
        if(!location.matches(ValidationRegex.location)) throw new BaseException(BaseResponseStatus.REQUEST_ERROR);
        List<GetBannerRes> bannerList = bannerRepository.findByLocationAndStatus(
                location,
                Status.ACTIVATED,
                Sort.by(Sort.Direction.ASC, "position"))
                .stream()
                .map(entity -> new GetBannerRes(entity.getImage(), entity.getPosition(),entity.getProductIdx()))
                .collect(Collectors.toList());
        return bannerList;
    }
}
