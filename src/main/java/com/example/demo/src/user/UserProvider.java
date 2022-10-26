package com.example.demo.src.user;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.config.Role;
import com.example.demo.config.Status;
import com.example.demo.src.bid.purchase.model.dto.GetUserBidPurchaseListRes;
import com.example.demo.src.bid.purchase.model.dto.GetUserBidPurchaseListResBid;
import com.example.demo.src.bid.purchase.model.entity.BidPurchaseRepository;
import com.example.demo.src.bid.sale.model.dto.GetUserBidSaleListRes;
import com.example.demo.src.bid.sale.model.dto.GetUserBidSaleListResBid;
import com.example.demo.src.bid.sale.model.entity.BidSaleRepository;
import com.example.demo.src.product.model.entity.*;
import com.example.demo.src.transaction.model.dto.WinningBidPrice;
import com.example.demo.src.transaction.model.entity.TransactionRepository;
import com.example.demo.src.user.model.dto.*;
import com.example.demo.src.user.model.entity.*;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserProvider {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserOwnRepository userOwnRepository;
    private final TransactionRepository transactionRepository;
    private final ProductRepository productRepository;
    private final ProductSizeRepository productSizeRepository;
    private final BidSaleRepository bidSaleRepository;
    private final BidPurchaseRepository bidPurchaseRepository;
    private final ProductLikeRepository productLikeRepository;
    private final AddressRepository addressRepository;
    private final AccountRepository accountRepository;
    private final CardRepository cardRepository;

    public GetUserLoginRes retrieveUserLogin(GetUserLoginReq requestDto) throws BaseException {
        User user = null;
        String password;
        user = userRepository.findByEmailAndStatus(requestDto.getEmail(), Status.ACTIVATED)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.FAILED_TO_LOGIN));

        AES128 aes = AES128.getInstance();

        try {
            password = aes.decrypt(user.getPassword());
        } catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.PASSWORD_DECRYPTION_ERROR);
        }
        if (password.equals(requestDto.getPassword())) {
            String jwt = jwtService.createAccessToken(user.getIdx(), Role.USER.name());
            return new GetUserLoginRes(user.getIdx(), jwt);
        } else {
            throw new BaseException(BaseResponseStatus.FAILED_TO_LOGIN);
        }
    }

    public PostUserFindEmailRes retrieveUserEmail(@Valid PostUserFindEmailReq requestDto) throws BaseException {
        User user;
        try {
            user = userRepository.findByPhoneAndStatus(requestDto.getPhone(), Status.ACTIVATED).orElseThrow(() -> new BaseException(BaseResponseStatus.USERS_FIND_NO_MATCH));
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
        return new PostUserFindEmailRes(user.getEmail());

    }

    public GetUserOwnRes retrieveUserOwnList(Long userIdx, boolean withProducts) throws BaseException {
        userRepository.findByIdxAndStatus(userIdx, Status.ACTIVATED)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USERS_WRONG_USER_IDX));


        Long totalValue;
        int count;
        Long totalPurchasePrice;
        Float totalValueIncreaseRate;
        Long totalValueIncreaseAmount;

        List<GetUserOwnProduct> productList = new ArrayList<>();
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        GetUserOwnRes getUserOwnRes = null;

        if (!withProducts) {
            List<UserOwn> userOwnList = userOwnRepository
                    .findByUserIdxAndStatus(userIdx, Status.ACTIVATED)
                    .stream().map(entity -> {
                        if (productSizeRepository.existsByIdxAndStatus(entity.getProductSizeIdx(), Status.ACTIVATED)) {
                            WinningBidPrice winningBid = transactionRepository
                                    .findTop1ByProductSizeIdxAndStatus(entity.getProductSizeIdx(), Status.DELIVERY_COMPLETED, sort);
                            Long buyPrice = entity.getBuyPrice();
                            if (winningBid != null) {
                                Long winningBidPrice = winningBid.getWinningBidPrice();
                                productList.add(new GetUserOwnProduct(
                                        null, null, null, null, buyPrice, winningBidPrice, null, (buyPrice - winningBidPrice)
                                ));
                            } else {
                                productList.add(new GetUserOwnProduct(
                                        null,
                                        null,
                                        null,
                                        null,
                                        buyPrice,
                                        null,
                                        null,
                                        null));
                            }
                        }
                        return entity;
                    }).collect(Collectors.toList());
            totalValue = productList.stream().mapToLong(entity -> {
                if (entity.getLatestTransactedPrice() != null)
                    return entity.getLatestTransactedPrice();
                else return entity.getPurchasePrice();
            }).sum();
            totalPurchasePrice = productList.stream().mapToLong(entity -> entity.getPurchasePrice()).sum();
            totalValueIncreaseAmount = totalValue - totalPurchasePrice;
            if (totalPurchasePrice == 0) {
                totalValueIncreaseRate = 0f;
            } else {
                totalValueIncreaseRate = (totalValueIncreaseAmount) * 1.0f / totalPurchasePrice;
            }
            count = userOwnList.size();

            getUserOwnRes = GetUserOwnRes.builder()
                    .userIdx(userIdx)
                    .totalValue(totalValue)
                    .count(count)
                    .totalPurchasePrice(totalPurchasePrice)
                    .totalValueIncreaseAmount(totalValueIncreaseAmount)
                    .totalValueIncreaseRate(totalValueIncreaseRate)
                    .build();
        } else if (withProducts) {
            List<UserOwn> userOwnList = userOwnRepository
                    .findByUserIdxAndStatus(userIdx, Status.ACTIVATED)
                    .stream().map(entity -> {
                        WinningBidPrice winningBid = transactionRepository
                                .findTop1ByProductSizeIdxAndStatus(entity.getProductSizeIdx(), Status.DELIVERY_COMPLETED, sort);
                        Long buyPrice = entity.getBuyPrice();
                        ProductSize productSize = productSizeRepository.findById(entity.getProductSizeIdx()).orElse(null);
                        if (winningBid != null) {
                            Long winningBidPrice = winningBid.getWinningBidPrice();
                            Long valueIncreaseAmount = winningBidPrice - buyPrice;
                            productList.add(new GetUserOwnProduct(
                                    productSize.getProduct().getName(),
                                    productSize.getProduct().getBrand().getName(),
                                    productSize.getProduct().getThumbnail(),
                                    productSize.getSize(),
                                    buyPrice,
                                    winningBidPrice,
                                    valueIncreaseAmount * 1.0f / buyPrice,
                                    valueIncreaseAmount
                            ));
                        } else {
                            productList.add(new GetUserOwnProduct(
                                    productSize.getProduct().getName(),
                                    productSize.getProduct().getBrand().getName(),
                                    productSize.getProduct().getThumbnail(),
                                    productSize.getSize(),
                                    buyPrice,
                                    null,
                                    null,
                                    null
                            ));
                        }
                        return entity;
                    }).collect(Collectors.toList());
            totalValue = productList.stream().mapToLong(entity -> {
                if (entity.getLatestTransactedPrice() != null)
                    return entity.getLatestTransactedPrice();
                else return entity.getPurchasePrice();
            }).sum();
            totalPurchasePrice = productList.stream().mapToLong(entity -> entity.getPurchasePrice()).sum();
            totalValueIncreaseAmount = totalValue - totalPurchasePrice;
            if (totalPurchasePrice == 0) {
                totalValueIncreaseRate = 0f;
            } else {
                totalValueIncreaseRate = (totalValueIncreaseAmount) * 1.0f / totalPurchasePrice;
            }
            count = userOwnList.size();
            getUserOwnRes = GetUserOwnRes.builder()
                    .userIdx(userIdx)
                    .totalValue(totalValue)
                    .count(count)
                    .totalPurchasePrice(totalPurchasePrice)
                    .totalValueIncreaseRate(totalValueIncreaseRate)
                    .totalValueIncreaseAmount(totalValueIncreaseAmount)
                    .productList(productList)
                    .build();

        }

        return getUserOwnRes;
    }

    public Boolean checkUserOwnExist(@Valid @NotNull Long userIdx, @Valid @NotNull Long productSizeIdx) throws BaseException {
        return userOwnRepository.existsByUserIdxAndProductSizeIdxAndStatus(userIdx, productSizeIdx, Status.ACTIVATED);
    }

    public GetUserRes retrieveUser(Long userIdx) throws BaseException {
        User user = userRepository
                .findByIdxAndStatus(userIdx, Status.ACTIVATED)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USERS_WRONG_USER_IDX));

        int productLikeCount = productLikeRepository.findByUserIdxAndStatus(userIdx, Status.ACTIVATED).size();

        GetUserOwnRes getUserOwnRes = retrieveUserOwnList(userIdx, false);
        Long totalValue = getUserOwnRes.getTotalValue();
        int ownProductCount = getUserOwnRes.getCount();
        Long totalPurchasePrice = getUserOwnRes.getTotalPurchasePrice();
        Float totalValueIncreaseRate = getUserOwnRes.getTotalValueIncreaseRate();
        Long totalValueIncreaseAmount = getUserOwnRes.getTotalValueIncreaseAmount();

        List<GetUserBidPurchaseListRes> purchaseListRes = retrieveUserBidPurchaseList(userIdx, false, null);
        long purchaseBiddingCount = purchaseListRes.get(0).getCount();
        long purchaseProceedingCount = purchaseListRes.get(1).getCount();
        long purchaseCompletedCount = purchaseListRes.get(2).getCount();

        List<GetUserBidSaleListRes> saleListRes = retrieveUserBidSaleList(userIdx, false, null);
        long saleBiddingCount = saleListRes.get(0).getCount();
        long saleProceedingCount = saleListRes.get(1).getCount();
        long saleCompletedCount = saleListRes.get(2).getCount();

        return new GetUserRes(
                user.getIdx(),
                user.getEmail(),
                user.getProfileImage(),
                user.getNickName(),
                user.getName(),
                user.getIntroduction(),
                user.getGrade(),
                user.getPoint(),
                productLikeCount,
                saleBiddingCount,
                saleProceedingCount,
                saleCompletedCount,
                purchaseBiddingCount,
                purchaseProceedingCount,
                purchaseCompletedCount,
                totalValue,
                ownProductCount,
                totalPurchasePrice,
                totalValueIncreaseRate,
                totalValueIncreaseAmount
        );
    }

    public List<GetUserBidPurchaseListRes> retrieveUserBidPurchaseList(@Valid @NotNull Long userIdx, boolean withProducts, Pageable pageable) throws BaseException {
        User user = userRepository
                .findByIdxAndStatus(userIdx, Status.ACTIVATED)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USERS_WRONG_USER_IDX));

        List<GetUserBidPurchaseListRes> bidPurchaseList = new ArrayList<>();
        List<GetUserBidPurchaseListResBid> bidPurchasesBidding;
        List<GetUserBidPurchaseListResBid> bidPurchasesCompleted;
        List<GetUserBidPurchaseListResBid> bidPurchasesProceeding;
        if (withProducts) {

            bidPurchasesBidding = bidPurchaseRepository
                    .findByUserIdxAndStatus(userIdx, Status.BIDDING, pageable)
                    .stream()
                    .map(entity -> {
                        Product product = entity.getProductSize().getProduct();
                        String status = entity.getDeadline().isBefore(LocalDate.now()) ? "EXPIRED" : "BIDDING";
                        return new GetUserBidPurchaseListResBid(
                                product.getIdx(),
                                product.getName(),
                                entity.getProductSize().getIdx(),
                                entity.getProductSize().getSize(),
                                entity.getBidPrice(),
                                entity.getDeadline(),
                                status
                        );
                    }).collect(Collectors.toList());
            bidPurchasesProceeding = bidPurchaseRepository
                    .findByUserIdxAndStatus(userIdx, Status.PROCEEDING, pageable)
                    .stream()
                    .map(entity -> {
                        Product product = entity.getProductSize().getProduct();
                        return new GetUserBidPurchaseListResBid(
                                product.getIdx(),
                                product.getName(),
                                entity.getProductSize().getIdx(),
                                entity.getProductSize().getSize(),
                                entity.getBidPrice(),
                                entity.getDeadline(),
                                null);
                    }).collect(Collectors.toList());
            bidPurchasesCompleted = bidPurchaseRepository
                    .findByUserIdxAndStatus(userIdx, Status.COMPLETED, pageable)
                    .stream()
                    .map(entity -> {
                        Product product = entity.getProductSize().getProduct();
                        return new GetUserBidPurchaseListResBid(
                                product.getIdx(),
                                product.getName(),
                                entity.getProductSize().getIdx(),
                                entity.getProductSize().getSize(),
                                entity.getBidPrice(),
                                entity.getDeadline(),
                                null

                        );

                    }).collect(Collectors.toList());
            bidPurchaseList.add(new GetUserBidPurchaseListRes("BIDDING", bidPurchasesBidding.size(), bidPurchasesBidding));
            bidPurchaseList.add(new GetUserBidPurchaseListRes("PROCEEDING", bidPurchasesProceeding.size(), bidPurchasesProceeding));
            bidPurchaseList.add(new GetUserBidPurchaseListRes("COMPLETED", bidPurchasesCompleted.size(), bidPurchasesCompleted));

        } else if (!withProducts) {
            int biddingCount = bidPurchaseRepository.findByUserIdxAndStatus(userIdx, Status.BIDDING, null).size();
            int proceedingCount = bidPurchaseRepository.findByUserIdxAndStatus(userIdx, Status.PROCEEDING, null).size();
            int completedCount = bidPurchaseRepository.findByUserIdxAndStatus(userIdx, Status.COMPLETED, null).size();

            bidPurchaseList.add(new GetUserBidPurchaseListRes("BIDDING", biddingCount, null));
            bidPurchaseList.add(new GetUserBidPurchaseListRes("PROCEEDING", proceedingCount, null));
            bidPurchaseList.add(new GetUserBidPurchaseListRes("COMPLETED", completedCount, null));

        }

        return bidPurchaseList;
    }

    public List<GetUserBidSaleListRes> retrieveUserBidSaleList(Long userIdx, boolean withProducts, Pageable pageable) throws BaseException {
        User user = userRepository
                .findByIdxAndStatus(userIdx, Status.ACTIVATED)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USERS_WRONG_USER_IDX));

        List<GetUserBidSaleListRes> bidSaleList = new ArrayList<>();
        List<GetUserBidSaleListResBid> bidSaleBidding;
        List<GetUserBidSaleListResBid> bidSaleProceeding;
        List<GetUserBidSaleListResBid> bidSaleCompleted;

        if (withProducts) {
            bidSaleBidding = bidSaleRepository
                    .findByUserIdxAndStatus(userIdx, Status.BIDDING, pageable)
                    .stream()
                    .map(entity -> {
                        Product product = entity.getProductSize().getProduct();
                        String status = entity.getDeadline().isBefore(LocalDate.now()) ? "EXPIRED" : "BIDDING";
                        return new GetUserBidSaleListResBid(
                                product.getIdx(),
                                product.getName(),
                                entity.getProductSize().getIdx(),
                                entity.getProductSize().getSize(),
                                entity.getBidPrice(),
                                entity.getDeadline(),
                                status
                        );
                    }).collect(Collectors.toList());
            bidSaleProceeding = bidSaleRepository
                    .findByUserIdxAndStatus(userIdx, Status.PROCEEDING, pageable)
                    .stream()
                    .map(entity -> {
                        Product product = entity.getProductSize().getProduct();
                        return new GetUserBidSaleListResBid(
                                product.getIdx(),
                                product.getName(),
                                entity.getProductSize().getIdx(),
                                entity.getProductSize().getSize(),
                                entity.getBidPrice(),
                                entity.getDeadline(),
                                null);
                    }).collect(Collectors.toList());
            bidSaleCompleted = bidPurchaseRepository
                    .findByUserIdxAndStatus(userIdx, Status.COMPLETED, pageable)
                    .stream()
                    .map(entity -> {
                        Product product = entity.getProductSize().getProduct();
                        return new GetUserBidSaleListResBid(
                                product.getIdx(),
                                product.getName(),
                                entity.getProductSize().getIdx(),
                                entity.getProductSize().getSize(),
                                entity.getBidPrice(),
                                entity.getDeadline(),
                                null

                        );

                    }).collect(Collectors.toList());
            bidSaleList.add(new GetUserBidSaleListRes("BIDDING", bidSaleBidding.size(), bidSaleBidding));
            bidSaleList.add(new GetUserBidSaleListRes("PROCEEDING", bidSaleProceeding.size(), bidSaleProceeding));
            bidSaleList.add(new GetUserBidSaleListRes("COMPLETED", bidSaleCompleted.size(), bidSaleCompleted));

        } else if (!withProducts) {
            int biddingCount = bidSaleRepository.findByUserIdxAndStatus(userIdx, Status.BIDDING, null).size();
            int proceedingCount = bidSaleRepository.findByUserIdxAndStatus(userIdx, Status.PROCEEDING, null).size();
            int completedCount = bidSaleRepository.findByUserIdxAndStatus(userIdx, Status.COMPLETED, null).size();

            bidSaleList.add(new GetUserBidSaleListRes("BIDDING", biddingCount, null));
            bidSaleList.add(new GetUserBidSaleListRes("PROCEEDING", proceedingCount, null));
            bidSaleList.add(new GetUserBidSaleListRes("COMPLETED", completedCount, null));

        }
        return bidSaleList;
    }


    public List<GetUserLikeRes> retrieveUserLike(Long userIdx, Sort sort) throws BaseException {
        userRepository.findByIdxAndStatus(userIdx, Status.ACTIVATED)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USERS_WRONG_USER_IDX));

        List<GetUserLikeRes> likeList = null;

        if (sort.stream().findFirst().get().getProperty().equals("createdAt")) {
            likeList = productLikeRepository.findByUserIdxAndStatus(
                    userIdx,
                    Status.ACTIVATED,
                    sort)
                    .stream()
                    .map(entity -> {
                        Product product = entity.getProductSize().getProduct();
                        ProductSize productSize = entity.getProductSize();
                        Long buyOutPrice = bidSaleRepository
                                .findMinBidPrice(productSize.getIdx(), Status.BIDDING);
                        return new GetUserLikeRes(
                                product.getBrand().getName(),
                                product.getBrand().getImage(),
                                product.getIdx(),
                                product.getThumbnail(),
                                product.getName(),
                                productSize.getIdx(),
                                productSize.getSize(),
                                buyOutPrice
                        );
                    }).collect(Collectors.toList());
        } else if  (sort.stream().findFirst().get().getProperty().equals("buyOutPrice")) {
            likeList = productLikeRepository.findByUserIdxAndStatus(
                    userIdx,
                    Status.ACTIVATED,
                    null)
                    .stream()
                    .map(entity -> {
                        Product product = entity.getProductSize().getProduct();
                        ProductSize productSize = entity.getProductSize();
                        Long buyOutPrice = bidSaleRepository
                                .findMinBidPrice(productSize.getIdx(), Status.BIDDING);
                        return new GetUserLikeRes(
                                product.getBrand().getName(),
                                product.getBrand().getImage(),
                                product.getIdx(),
                                product.getThumbnail(),
                                product.getName(),
                                productSize.getIdx(),
                                productSize.getSize(),
                                buyOutPrice
                        );
                    }).collect(Collectors.toList());
            likeList = likeList.stream().sorted((o1, o2) -> o1.getBuyOutPrice().compareTo(o2.getBuyOutPrice())).collect(Collectors.toList());
        }
        return likeList;
    }

    public List<GetUserAddressRes> retrieveUseAddress(Long userIdx) throws BaseException{
        userRepository.findByIdxAndStatus(userIdx, Status.ACTIVATED)
                .orElseThrow(()->new BaseException(BaseResponseStatus.USERS_WRONG_USER_IDX));
        List<GetUserAddressRes> addressList = addressRepository.findByUserIdxAndStatus(userIdx, Status.ACTIVATED)
                .stream().map(entity -> new GetUserAddressRes(
                        entity.getIdx(),
                        entity.getName(),
                        entity.getPhone(),
                        entity.getZipCode(),
                        entity.getAddress(),
                        entity.getAddressDetail(),
                        entity.getDefaultAddress()
                )).collect(Collectors.toList());
        return addressList;
    }

    public User getUserByIdxAndStatus(Long userIdx, Status status) throws BaseException{
        return userRepository.findByIdxAndStatus(userIdx,status)
                .orElseThrow(()->new BaseException(BaseResponseStatus.USERS_WRONG_USER_IDX));
    }

    public GetUserAccountRes retrieveUserAccounts(Long userIdx) throws BaseException {
        User user = getUserByIdxAndStatus(userIdx, Status.ACTIVATED);

        Account account =   accountRepository
                .findByUserIdxAndStatus(userIdx, Status.ACTIVATED);
        if(account != null ){
            return new GetUserAccountRes(account.getIdx(),account.getBank(),account.getAccount(),account.getAccountHolder());
        }else {
            return new GetUserAccountRes();
        }
    }

    public List<GetUserCardRes> retrieveUserCards(Long userIdx) throws BaseException{
        User user = getUserByIdxAndStatus(userIdx, Status.ACTIVATED);

        List<GetUserCardRes> cardList = cardRepository.findByUserIdxAndStatus(userIdx, Status.ACTIVATED)
                .stream()
                .map(entity-> new GetUserCardRes( entity.getIdx(),entity.getCompany(), entity.getBin(),entity.getDefaultCard()))
                .collect(Collectors.toList());
        return cardList;
    }
    public Address getAddressByIdxAndUserIdxAndStatus(Long idx, Long userIdx, Status status) throws BaseException{
       return addressRepository.findByIdxAndUserIdxAndStatus(idx,userIdx,status)
                .orElseThrow(()->new BaseException(BaseResponseStatus.USERS_WRONG_ADDRESS_IDX));
    }
    public Account getAccountByIdxAndUserIdxAndStatus(Long idx, Long userIdx, Status status) throws BaseException{
        return accountRepository.findByIdxAndUserIdxAndStatus(idx,userIdx,status)
                .orElseThrow(()->new BaseException(BaseResponseStatus.USERS_WRONG_ACCOUNT_IDX));
    }
    public Card getPaymentCardByIdxAndUserIdxAndStatus(Long idx, Long userIdx, Status status) throws BaseException{
        return cardRepository.findByIdxAndUserIdxAndStatus(idx,userIdx,status)
                .orElseThrow(()->new BaseException(BaseResponseStatus.USERS_WRONG_CARD_IDX));
    }
}
