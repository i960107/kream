package com.example.demo.src.user;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.config.Status;
import com.example.demo.src.product.model.ProductProvider;
import com.example.demo.src.product.model.dto.PostUserOwnRes;
import com.example.demo.src.user.model.dto.*;
import com.example.demo.src.user.model.entity.*;
import com.example.demo.utils.AES128;
import com.example.demo.utils.S3Service;
import com.example.demo.utils.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Random;

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private AES128 aes128 = AES128.getInstance();
    private final SmsService smsService;
    private final UserOwnRepository userOwnRepository;
    private final UserProvider userProvider;
    private final ProductProvider productProvider;
    private final S3Service s3Service;
    private final AddressRepository addressRepository;
    private final AccountRepository accountRepository;
    private final CardRepository cardRepository;

    public PostUserRes createUser(@Valid PostUserReq requestDto) throws BaseException {
        User user2 = userRepository.findByEmailAndStatus(requestDto.getEmail(), Status.ACTIVATED).orElse(null);
        if (userRepository.findByEmailAndStatus(requestDto.getEmail(), Status.ACTIVATED).isPresent())
            throw new BaseException(BaseResponseStatus.DUPLICATED_EMAIL);
        if (userRepository.findByPhoneAndStatus(requestDto.getPhone(), Status.ACTIVATED).isPresent())
            throw new BaseException(BaseResponseStatus.DUPLICATED_PHONE);

        String password;
        try {
            password = aes128.encrypt(requestDto.getPassword());
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.PASSWORD_DECRYPTION_ERROR);
        }

        Random rnd = new Random();
        String randomNickName = "";
        for (int i = 0; i < 5; i++) {
            randomNickName += String.valueOf((char) ((int) (rnd.nextInt(26)) + 97));
        }

        User user = User.builder()
                .email(requestDto.getEmail())
                .nickName(randomNickName)
                .name(requestDto.getName())
                .sneakersSize(requestDto.getSneakersSize())
                .grade(requestDto.getGrade())
                .point(requestDto.getPoint())
                .introduction(requestDto.getIntroduction())
                .password(password)
                .profileImage(requestDto.getProfileImage())
                .phone(requestDto.getPhone())
                .status(requestDto.getStatus())
                .build();
        try {
            user = userRepository.save(user);
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
        return new PostUserRes(user.getEmail());
    }

    public void createTempPassword(PostUserFindPasswordReq requestDto) throws BaseException {
        User user = userRepository.findByPhoneAndEmailAndStatus(
                        requestDto.getPhone(),
                        requestDto.getEmail(),
                        Status.ACTIVATED)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USERS_FIND_NO_MATCH));

        String tempPw = smsService.sendTempPw(requestDto.getPhone());

        try {
            tempPw = aes128.encrypt(tempPw);
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.PASSWORD_ENCRYPTION_ERROR);
        }

        user.updateTempPw(tempPw);
        userRepository.save(user);
    }

    public PostUserOwnRes createUserOwn(Long userIdx, PostUserOwnReq requestDto) throws BaseException {
        userRepository
                .findByIdxAndStatus(userIdx, Status.ACTIVATED)
                .orElseThrow((() -> new BaseException(BaseResponseStatus.USERS_WRONG_USER_IDX)));

        if (!productProvider.checkProductSizeExist(requestDto.getProductIdx(), requestDto.getProductSizeIdx()))
            throw new BaseException(BaseResponseStatus.PRODUCT_SIZE_WRONG_IDX);

        UserOwn userOwn = userOwnRepository.save(UserOwn.builder()
                .userIdx(userIdx)
                .productSizeIdx(requestDto.getProductSizeIdx())
                .buyPrice(requestDto.getBuyPrice())
                .status(Status.ACTIVATED)
                .build());
        return new PostUserOwnRes(userOwn.getIdx(), requestDto.getProductIdx(), userOwn.getProductSizeIdx());
    }

    public PatchUserRes updateUser(Long userIdx, PatchUserReq requestDto) throws BaseException {
        User user = userRepository
                .findByIdxAndStatus(userIdx, Status.ACTIVATED)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USERS_WRONG_USER_IDX));


        user.update(
                requestDto.getName(),
                requestDto.getNickName(),
                requestDto.getIntroduction());

        userRepository.save(user);

        return new PatchUserRes(
                user.getIdx(),
                user.getEmail(),
                user.getProfileImage(),
                user.getNickName(),
                user.getName(),
                user.getIntroduction(),
                user.getGrade(),
                user.getPoint());
    }

    public PatchUserRes updateUserProfileImage(Long userIdx, MultipartFile profileImage) throws BaseException {
        User user = userRepository
                .findByIdxAndStatus(userIdx, Status.ACTIVATED)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USERS_WRONG_USER_IDX));

        String profileImageUrl = s3Service.upload(profileImage, "static/users");
        user.updateProfileImage(profileImageUrl);
        userRepository.save(user);
        return new PatchUserRes(
                user.getIdx(),
                user.getEmail(),
                user.getProfileImage(),
                user.getNickName(),
                user.getName(),
                user.getIntroduction(),
                user.getGrade(),
                user.getPoint());
    }

    public GetUserAddressRes createUserAddress(Long userIdx, PostUserAddressReq postUserAddressReq) throws BaseException {
        User user = userProvider.getUserByIdxAndStatus(userIdx, Status.ACTIVATED);

        if (postUserAddressReq.getDefaultAddress().equals("TRUE")) {
            Address originalDefaultAddress = addressRepository
                    .findByUserIdxAndDefaultAddressAndStatus(userIdx, "TRUE", Status.ACTIVATED);
            if (originalDefaultAddress != null) {
                addressRepository.save(originalDefaultAddress.deleteFromDefaultAddress());
            }
        } else {
            if (addressRepository
                    .findByUserIdxAndDefaultAddressAndStatus(userIdx, "TRUE", Status.ACTIVATED) == null)
                throw new BaseException(BaseResponseStatus.USER_ADDRESS_NO_DEFAULT_ADDRESS);
        }

        Address address = addressRepository.save(Address.builder()
                .userIdx(userIdx)
                .name(postUserAddressReq.getName())
                .phone(postUserAddressReq.getPhone())
                .zipCode(postUserAddressReq.getZipCode())
                .address(postUserAddressReq.getAddress())
                .addressDetail(postUserAddressReq.getAddressDetail())
                .defaultAddress(postUserAddressReq.getDefaultAddress())
                .status(Status.ACTIVATED)
                .build());

        return new GetUserAddressRes(
                address.getIdx(),
                address.getName(),
                address.getPhone(),
                address.getZipCode(),
                address.getAddress(),
                address.getAddressDetail(),
                address.getDefaultAddress()
        );
    }

    public GetUserAccountRes createUserAccount(
            Long userIdx,
            PostUserAccountReq postUserAccountReq
    ) throws BaseException {
        User user = userProvider.getUserByIdxAndStatus(userIdx, Status.ACTIVATED);
        Account originalAccount = accountRepository.findByUserIdxAndStatus(userIdx, Status.ACTIVATED);
        if (originalAccount != null) {
            if (originalAccount.getAccount().equals(postUserAccountReq.getAccount())) {
                throw new BaseException(BaseResponseStatus.DUPLICATED_ACCOUNT);
            } else {
                accountRepository.save(originalAccount.delete());
            }
        }
        Account account = accountRepository.save(
                Account.builder()
                        .userIdx(userIdx)
                        .bank(postUserAccountReq.getBank())
                        .account(postUserAccountReq.getAccount())
                        .accountHolder(postUserAccountReq.getAccountHolder())
                        .status(Status.ACTIVATED)
                        .build()
        );

        return new GetUserAccountRes(
                account.getIdx(),
                account.getBank(),
                account.getAccount(),
                account.getAccountHolder()
        );

    }

    public GetUserAddressRes updateUserAddress(Long userIdx, PatchUserAddressReq patchUserAddressReq) throws BaseException {
        User user = userProvider.getUserByIdxAndStatus(userIdx, Status.ACTIVATED);
        Address address = addressRepository
                .findByIdxAndUserIdxAndStatus(patchUserAddressReq.getAddressIdx(), userIdx, Status.ACTIVATED)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USERS_WRONG_ADDRESS_IDX));


        Address originalDefaultAddress = addressRepository
                .findByUserIdxAndDefaultAddressAndStatus(userIdx, "TRUE", Status.ACTIVATED);

        if (patchUserAddressReq.getDefaultAddress().equals("TRUE")) {
            if (originalDefaultAddress != null && originalDefaultAddress.getIdx() != address.getIdx()) {
                addressRepository.save(originalDefaultAddress.deleteFromDefaultAddress());
                address.updateAsDefaultAddress();
            }
        } else {
            if (addressRepository
                    .findByUserIdxAndDefaultAddressAndStatus(userIdx, "TRUE", Status.ACTIVATED) == null)
                throw new BaseException(BaseResponseStatus.USER_ADDRESS_NO_DEFAULT_ADDRESS);
            address.deleteFromDefaultAddress();
        }
        address.updateAddress(patchUserAddressReq);
        addressRepository.save(address);
        return new GetUserAddressRes(
                address.getIdx(),
                address.getName(),
                address.getPhone(),
                address.getZipCode(),
                address.getAddress(),
                address.getAddressDetail(),
                address.getDefaultAddress()
        );
    }

    public GetUserCardRes createUserCard(Long userIdx, PostUserCardReq postUserCardReq) throws BaseException {
        User user = userProvider.getUserByIdxAndStatus(userIdx, Status.ACTIVATED);
        if (cardRepository.existsByBinAndStatus(postUserCardReq.getBin(), Status.ACTIVATED)) {
            throw new BaseException(BaseResponseStatus.DUPLICATED_PAYMENT_CARD);
        }
        if (postUserCardReq.getDefaultCard().equals("TRUE")) {
            Card originalDefaultCard = cardRepository
                    .findByUserIdxAndDefaultCardAndStatus(userIdx, "TRUE", Status.ACTIVATED);
            if (originalDefaultCard != null) {
                cardRepository.save(originalDefaultCard.deleteFromDefaultCard());
            }
        } else {
            Card originalDefaultCard = cardRepository
                    .findByUserIdxAndDefaultCardAndStatus(userIdx, "TRUE", Status.ACTIVATED);

            if (originalDefaultCard == null)
                throw new BaseException(BaseResponseStatus.USER_CARD_NO_DEFAULT_CARD);

        }
        Card paymentCard = cardRepository.save(
                Card.builder()
                        .userIdx(userIdx)
                        .company(postUserCardReq.getCompany())
                        .bin(postUserCardReq.getBin())
                        .defaultCard(postUserCardReq.getDefaultCard())
                        .status(Status.ACTIVATED)
                        .build()
        );
        return new GetUserCardRes(
                paymentCard.getIdx(),
                paymentCard.getCompany(),
                paymentCard.getBin(),
                paymentCard.getDefaultCard()
        );
    }

    public Long useAllPoint(User user, Long paymentAmount) throws BaseException {
        Long pointToUse = (user.getPoint() < paymentAmount)? user.getPoint():paymentAmount;
           userRepository.save(user.usePoint(pointToUse));
           return pointToUse;
    }

}
