package com.example.demo.src.user;

import com.example.demo.config.Auth;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.Role;
import com.example.demo.src.bid.BidProvider;
import com.example.demo.src.bid.purchase.model.dto.GetUserBidPurchaseListRes;
import com.example.demo.src.bid.sale.model.dto.GetUserBidSaleListRes;
import com.example.demo.src.product.model.dto.PostUserOwnRes;
import com.example.demo.src.user.model.dto.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserProvider userProvider;
    private final JwtService jwtService;
    private final SmsService smsService;
    private final BidProvider bidProvider;

    @PostMapping("/api/users/sign-in")
    public BaseResponse<GetUserLoginRes> retrieveUserLogin(@Valid @RequestBody GetUserLoginReq requestDto) throws BaseException {
        GetUserLoginRes responseDto = userProvider.retrieveUserLogin(requestDto);
        return new BaseResponse<GetUserLoginRes>(responseDto);
    }

    @PostMapping("/api/users/sign-up")
    public BaseResponse<PostUserRes> createUserSignUp(@RequestBody @Valid PostUserReq requestDto) throws BaseException {
        PostUserRes responseDto = userService.createUser(requestDto);
        return new BaseResponse<PostUserRes>(responseDto);
    }

    @PostMapping("/api/users/find-email")
    public BaseResponse<PostUserFindEmailRes> retrieveUserEmail(@RequestBody @Valid PostUserFindEmailReq requestDto) throws BaseException {
        PostUserFindEmailRes responseDto = userProvider.retrieveUserEmail(requestDto);
        return new BaseResponse<PostUserFindEmailRes>(responseDto);
    }

    @PostMapping("/api/users/certificate-send")
    public BaseResponse<String> sendSms(@RequestBody @Valid PostCertificationNumberReq requestDto) throws BaseException {
        smsService.sendCertificationNum(requestDto.getPhone());
        return new BaseResponse<String>("문자 발송 완료");
    }

    @PostMapping("/api/users/certificate-verify")
    public BaseResponse<String> verifyCertificationCode(@RequestBody @Valid PostSmsCertificationReq requestDto) throws BaseException {
        smsService.verifyCertificationNum(requestDto);
        return new BaseResponse<String>("인증 완료");
    }

    @PostMapping("/api/users/find-pw")
    public BaseResponse<String> createTempPassword(@RequestBody @Valid PostUserFindPasswordReq requestDto) throws BaseException {
        userService.createTempPassword(requestDto);
        return new BaseResponse<String>("임시 비밀번호 발송 완료");
    }

    @Auth(role = Role.USER)
    @GetMapping("/api/users/{userIdx}/own")
    public BaseResponse<GetUserOwnRes> retrieveUserOwnList(
            @PathVariable("userIdx") Long userIdx,
            @RequestParam(name = "withProducts", required = false, defaultValue = "false") Boolean withProducts
    ) throws BaseException {
        GetUserOwnRes responseDto = userProvider.retrieveUserOwnList(userIdx, withProducts);
        return new BaseResponse<>(responseDto);
    }

    @Auth(role = Role.USER)
    @PostMapping("/api/users/{userIdx}/own")
    public BaseResponse<PostUserOwnRes> createUserOwn(
            @PathVariable("userIdx") Long userIdx,
            @Valid @RequestBody PostUserOwnReq requestDto
    ) throws BaseException {
        PostUserOwnRes responseDto = userService.createUserOwn(userIdx, requestDto);
        return new BaseResponse<PostUserOwnRes>(responseDto);
    }

    @Auth(role = Role.USER)
    @GetMapping("/api/users/{userIdx}")
    public BaseResponse<GetUserRes> retrieveUser(
            @PathVariable("userIdx") Long userIdx
    ) throws BaseException {
        GetUserRes responseDto = userProvider.retrieveUser(userIdx);
        return new BaseResponse<GetUserRes>(responseDto);
    }

    @Auth(role = Role.USER)
    @PatchMapping("/api/users/{userIdx}")
    public BaseResponse<PatchUserRes> updateUser(
            @PathVariable("userIdx") Long userIdx,
            @Valid @RequestBody PatchUserReq requestDto
    ) throws BaseException {
        PatchUserRes responseDto = userService.updateUser(userIdx, requestDto);
        return new BaseResponse<>(responseDto);
    }

    @Auth(role = Role.USER)
    @PatchMapping("/api/users/{userIdx}/profile-image")
    public BaseResponse<PatchUserRes> updateUserProfileImage(
            @PathVariable("userIdx") Long userIdx,
            @RequestParam("profileImage") MultipartFile profileImage
    ) throws BaseException {
        PatchUserRes responseDto = userService.updateUserProfileImage(userIdx, profileImage);
        return new BaseResponse<PatchUserRes>(responseDto);
    }

    @Auth(role = Role.USER)
    @GetMapping("/api/users/{userIdx}/purchases")
    public BaseResponse<List<GetUserBidPurchaseListRes>> retrieveUserBidPurchaseList(
            @PathVariable("userIdx") Long userIdx,
            @RequestParam(name = "withProducts", defaultValue = "true", required = false) boolean withProducts,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable
    ) throws BaseException {
        List<GetUserBidPurchaseListRes> responseDto = userProvider.retrieveUserBidPurchaseList(userIdx, withProducts, pageable);
        return new BaseResponse<List<GetUserBidPurchaseListRes>>(responseDto);
    }

    @Auth(role = Role.USER)
    @GetMapping("/api/users/{userIdx}/sales")
    public BaseResponse<List<GetUserBidSaleListRes>> retrieveUserBidSaleList(
            @PathVariable("userIdx") Long userIdx,
            @RequestParam(name = "withProducts", defaultValue = "true", required = false) boolean withProducts,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable
    ) throws BaseException {
        List<GetUserBidSaleListRes> responseDto = userProvider.retrieveUserBidSaleList(userIdx, withProducts, pageable);
        return new BaseResponse<>(responseDto);
    }

    @Auth(role = Role.USER)
    @GetMapping("/api/users/{userIdx}/likes")
    public BaseResponse<List<GetUserLikeRes>> retrieveUserLike(
            @PathVariable("userIdx") Long userIdx,
            @SortDefault(sort = "createdAt", direction = Sort.Direction.ASC) Sort sort
    ) throws BaseException {
        List<GetUserLikeRes> responseDto = userProvider.retrieveUserLike(userIdx, sort);
        return new BaseResponse<>(responseDto);
    }

    @Auth(role = Role.USER)
    @GetMapping("/api/users/{userIdx}/addresses")
    public BaseResponse<List<GetUserAddressRes>> retrieveUserAddress(
            @PathVariable("userIdx") Long userIdx
    ) throws BaseException {
        List<GetUserAddressRes> responseDto = userProvider.retrieveUseAddress(userIdx);
        return new BaseResponse<>(responseDto);
    }
    @Auth(role = Role.USER)
    @PostMapping("/api/users/{userIdx}/addresses")
    public BaseResponse<GetUserAddressRes> createUserAddress(
            @PathVariable("userIdx") Long userIdx,
            @Valid @RequestBody PostUserAddressReq postUserAddressReq
    )throws BaseException {
        GetUserAddressRes responseDto = userService.createUserAddress(userIdx, postUserAddressReq);
        return new BaseResponse<GetUserAddressRes>(responseDto);
    }
    @Auth(role = Role.USER)
    @PostMapping("/api/users/{userIdx}/accounts")
    public BaseResponse<GetUserAccountRes> createUserAccount(
            @PathVariable("userIdx") Long userIdx,
            @Valid @RequestBody PostUserAccountReq postUserAccountReq
    )throws BaseException {
        GetUserAccountRes responseDto = userService.createUserAccount(userIdx, postUserAccountReq);
        return new BaseResponse<>(responseDto);
    }
    @Auth(role = Role.USER)
    @GetMapping("/api/users/{userIdx}/accounts")
    public BaseResponse<GetUserAccountRes> retrieveUserAccounts(
            @PathVariable("userIdx") Long userIdx
    )throws BaseException {
        GetUserAccountRes responseDto = userProvider.retrieveUserAccounts(userIdx);
        return new BaseResponse<>(responseDto);
    }
    @Auth(role = Role.USER)
    @PatchMapping("/api/users/{userIdx}/addresses")
    public BaseResponse<GetUserAddressRes> updateUserAddress(
            @PathVariable("userIdx") Long userIdx,
            @Valid @RequestBody PatchUserAddressReq patchUserAddressReq
    )throws BaseException {
        GetUserAddressRes responseDto = userService.updateUserAddress(userIdx, patchUserAddressReq);
        return new BaseResponse<>(responseDto);
    }

    @Auth(role = Role.USER)
    @PostMapping("/api/users/{userIdx}/cards")
    public BaseResponse<GetUserCardRes> createUserCard(
            @PathVariable("userIdx") Long userIdx,
            @Valid @RequestBody PostUserCardReq postUserCardReq
    )throws BaseException {
        GetUserCardRes responseDto = userService.createUserCard(userIdx, postUserCardReq);
        return new BaseResponse<>(responseDto);
    }

    @Auth(role = Role.USER)
    @GetMapping("/api/users/{userIdx}/cards")
    public BaseResponse<List<GetUserCardRes>> retrieveUserCard(
            @PathVariable("userIdx") Long userIdx
    )throws BaseException {
        List<GetUserCardRes> responseDto = userProvider.retrieveUserCards(userIdx);
        return new BaseResponse<>(responseDto);
    }
}

