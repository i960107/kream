package com.example.demo.src.product.model;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.config.Status;
import com.example.demo.src.bid.purchase.model.entity.BidPurchaseRepository;
import com.example.demo.src.bid.sale.model.entity.BidSaleRepository;
import com.example.demo.src.product.model.dto.*;
import com.example.demo.src.product.model.entity.*;
import com.example.demo.src.transaction.model.entity.Transaction;
import com.example.demo.src.transaction.model.entity.TransactionRepository;
import com.example.demo.src.user.model.entity.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductProvider {
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductCategoryDetailRepository productCategoryDetailRepository;
    private final ProductRepository productRepository;
    private final ProductSizeRepository productSizeRepository;
    private final TransactionRepository transactionRepository;
    private final BidSaleRepository bidSaleRepository;
    private final BidPurchaseRepository bidPurchaseRepository;
    private final BrandRepository brandRepository;
    private final ProductLikeRepository productLikeRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductCategoryMapRepository productCategoryMapRepository;
    private final UserRepository userRepository;
    @PersistenceUnit
    private EntityManagerFactory emf;

    public GetProductCategoryDetailListRes retrieveProductCategoryList(Boolean exposure) throws BaseException {
        GetProductCategoryListRes productCategoryList = null;

        Sort sort = Sort.by(Sort.Direction.ASC, "position");

        List<ProductCategory> categories;
        categories = productCategoryRepository.findByPositionGreaterThanEqualAndStatus((byte) 0, Status.ACTIVATED, sort);

        List<GetProductCategoryListRes> categoryList = new ArrayList<>();

        for (ProductCategory category : categories) {
            List<GetProductCategoryRes> detailCategories = productCategoryDetailRepository
                    .findByProductCategoryIdxAndExposureAndStatus(category.getIdx(), exposure.toString().toUpperCase(Locale.ROOT), Status.ACTIVATED, sort)
                    .stream().map(entity -> new GetProductCategoryRes(entity.getIdx(), entity.getName(), entity.getImage(), entity.getPosition()))
                    .collect(Collectors.toList());

            categoryList.add(new GetProductCategoryListRes(category.getIdx(), category.getName(), category.getPosition(), detailCategories));
        }
        return new GetProductCategoryDetailListRes(categoryList);
    }

    public GetBrandListRes retrieveBrandList() throws BaseException {
        List<GetBrandRes> brandList = brandRepository
                .findByStatus(Status.ACTIVATED, Sort.by(Sort.Direction.ASC, "name"))
                .stream().map(brand -> new GetBrandRes(brand.getIdx(), brand.getName()))
                .collect(Collectors.toList());
        return new GetBrandListRes(brandList);
    }

    public GetProductListRes retrieveProductList(Long categoryIdx,
                                                 Long categoryDetailIdx,
                                                 String luxury,
                                                 List<String> brands,
                                                 Byte sneakersSize,
                                                 Long minPrice,
                                                 Long maxPrice,
                                                 String keyword
    ) throws BaseException {

        List<Product> productList = productRepository.findByStatus(Status.ACTIVATED);

        List<GetProductListProductRes> productResList = new ArrayList<>();

        for (Product product : productList) {
            List<Long> productSizeList = product.getProductSizes().stream().map(entity -> entity.getIdx()).collect(Collectors.toList());
            Long minBid = bidSaleRepository.findMinBidPrice(productSizeList, Status.BIDDING);
            if (minBid != null) {
                productResList.add(new GetProductListProductRes(product.getIdx(), product.getThumbnail(), product.getBrand().getName(), product.getBrand().getImage(), product.getName(), product.getDescription(), minBid, 0L, 0L));
            }
        }
        return new GetProductListRes(productResList);
    }

    public GetProductRes retrieveProduct(Long productIdx) throws BaseException {
        Product product = productRepository.findByIdxAndStatus(productIdx, Status.ACTIVATED).orElseThrow(() -> new BaseException(BaseResponseStatus.PRODUCT_WRONG_IDX));
        List<Long> productSizeList = product.getProductSizes().stream().map(entity -> entity.getIdx()).collect(Collectors.toList());

        List<GetImageRes> productImages = new ArrayList<>();
        product.getProductImages()
                .stream()
                .forEach(entity -> {
                    if (entity.getPosition() != -1) {
                        productImages.add(new GetImageRes(entity.getImage(), entity.getPosition()));
                    }
                });

        List<Transaction> top2Transactions = transactionRepository.findTop2ByProductSizeIdxInAndStatus(productSizeList, Status.DELIVERY_COMPLETED, Sort.by(Sort.Direction.DESC, "createdAt"));
        Long latestTransactedPrice;
        Long secondLatestTransactedPrice;
        Long priceIncreaseAmount;
        Float priceIncreaseRate;
        if (top2Transactions.size() == 2) {
            latestTransactedPrice = top2Transactions.get(0).getWinningBidPrice();
            secondLatestTransactedPrice = top2Transactions.get(1).getWinningBidPrice();
            priceIncreaseAmount = latestTransactedPrice - secondLatestTransactedPrice;
            priceIncreaseRate = priceIncreaseAmount.floatValue() / (secondLatestTransactedPrice);
        } else if (top2Transactions.size() == 1) {
            latestTransactedPrice = top2Transactions.get(0).getWinningBidPrice();
            priceIncreaseAmount = null;
            priceIncreaseRate = null;
        } else {
            latestTransactedPrice = null;
            priceIncreaseAmount = null;
            priceIncreaseRate = null;
        }

        Long minSaleBid = bidSaleRepository.findMinBidPrice(productSizeList, Status.BIDDING);
        Long maxPurchaseBid = bidPurchaseRepository.findMaxBidPrice(productSizeList, Status.BIDDING);

        Long liked = productLikeRepository.findCountByProductIdxInAndStatus(productSizeList, Status.ACTIVATED);
//liked추가필요
        return GetProductRes.builder()
                .name(product.getName())
                .brandName(product.getBrand().getName())
                .description(product.getDescription())
                .modelNo(product.getModelNo())
                .color(product.getColor())
                .releaseDate(product.getReleaseDate())
                .releasePrice(product.getReleasePrice())
                .latestTransactedPrice(latestTransactedPrice)
                .liked(liked)
                .priceIncreaseAmount(priceIncreaseAmount)
                .productImages(productImages)
                .priceIncreaseRate(priceIncreaseRate)
                .buyPrice(minSaleBid)
                .sellPrice(maxPurchaseBid)
                .build();
    }

    public GetProductSizeListRes retrieveProductSizeList(Long productIdx) throws BaseException {
        if (!productRepository.existsByIdxAndStatus(productIdx, Status.ACTIVATED))
            throw new BaseException(BaseResponseStatus.PRODUCT_WRONG_IDX);
        List<GetProductSizeRes> sizeList = productSizeRepository.findByProductIdxAndStatus(
                        productIdx,
                        Status.ACTIVATED,
                        Sort.by(Sort.Direction.ASC, "size")
                ).stream()
                .map(entity ->
                        new GetProductSizeRes(
                                entity.getIdx(),
                                entity.getSize()))
                .collect(Collectors.toList());
        return new GetProductSizeListRes(sizeList);
    }

    public GetProductOthersListRes retrieveProductOthersList(Long productIdx, Pageable pageable) throws BaseException {
        Product product = productRepository.findByIdxAndStatus(productIdx, Status.ACTIVATED)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.PRODUCT_WRONG_IDX));

        Long brandIdx = product.getBrand().getIdx();
        String brandName = product.getBrand().getName();

        List<Long> categoryDetailIdxList = productCategoryMapRepository
                .findProductCategoryDetailIdxListByProductIdxAndStatus(
                        productIdx,
                        Status.ACTIVATED,
                        Status.ACTIVATED
                );

        List<GetProductOthersRes> othersList = new ArrayList<>();
        List<Product> sameBrandProductList = productCategoryMapRepository
                .findByBrandIdxAndProductCategoryDetailIdxAndStatus(
                        brandIdx,
                        categoryDetailIdxList,
                        Status.ACTIVATED,
                        pageable
                );
        for (Product sameBrandProduct : sameBrandProductList) {
            List<Long> productSizeList = sameBrandProduct
                    .getProductSizes()
                    .stream()
                    .map(entity -> entity.getIdx())
                    .collect(Collectors.toList());
            Long buyPrice = bidSaleRepository.findMinBidPrice(productSizeList, Status.BIDDING);
            othersList.add(
                    new GetProductOthersRes(
                            sameBrandProduct.getIdx(),
                            sameBrandProduct.getBrand().getImage(),
                            sameBrandProduct.getBrand().getName(),
                            sameBrandProduct.getName(),
                            sameBrandProduct.getThumbnail(),
                            buyPrice));
        }
        return new GetProductOthersListRes(brandName, othersList);
    }


    public List<GetProductRecommendRes> retrieveProductRecommend() throws BaseException {
        List<Byte> productCategories = productCategoryRepository
                .findByPositionLessThanAndStatus((byte) 0, Status.ACTIVATED)
                .stream()
                .map(entity -> entity.getIdx())
                .collect(Collectors.toList());

        List<ProductCategoryDetail> productCategoryDetail = productCategoryDetailRepository
                .findByProductCategoryIdxInAndExposureAndPositionGreaterThanAndStatus(
                        productCategories,
                        "TRUE",
                        (byte) 0,
                        Status.ACTIVATED,
                        Sort.by(Sort.Direction.ASC, "position"));

        List<GetProductRecommendRes> recommendResList = new ArrayList<>();

        for (ProductCategoryDetail category : productCategoryDetail) {
            List<Product> recommendProductList = productCategoryMapRepository
                    .findByProductCategoryDetailIdxAndStatus(
                            category.getIdx(),
                            Status.ACTIVATED)
                    .stream()
                    .map(entity -> entity.getProduct())
                    .collect(Collectors.toList());


            List<GetProductRecommendResProduct> productList = new ArrayList<>();
            for (Product product : recommendProductList) {
                List<Long> productSizeList = productSizeRepository
                        .findByProductIdxAndStatus(product.getIdx(), Status.ACTIVATED, null)
                        .stream()
                        .map(entity -> entity.getIdx()).collect(Collectors.toList());

                Long minBidSalePrice = bidSaleRepository.findMinBidPrice(productSizeList, Status.BIDDING);
                productList.add(
                        new GetProductRecommendResProduct(
                                product.getIdx(),
                                product.getThumbnail(),
                                product.getBrand().getName(),
                                product.getBrand().getImage(),
                                product.getName(),
                                minBidSalePrice));
            }
            recommendResList.add(new GetProductRecommendRes(
                    category.getName(),
                    category.getDescription(),
                    category.getPosition(),
                    productList));
        }

        return recommendResList;
    }

    public List<GetProductAdsRes> retrieveProductAds() {
        List<Byte> productCategories = productCategoryRepository
                .findByPositionLessThanAndStatus((byte) 0, Status.ACTIVATED)
                .stream()
                .map(entity -> entity.getIdx())
                .collect(Collectors.toList());

        List<GetProductAdsRes> adsList = new ArrayList<>();

        productCategoryDetailRepository
                .findByProductCategoryIdxInAndExposureAndPositionGreaterThanAndStatus(
                        productCategories,
                        "FALSE",
                        (byte) 0,
                        Status.ACTIVATED,
                        Sort.by(Sort.Direction.ASC, "position"))
                .stream()
                .forEach(entity -> {
                            List<ProductCategoryMap> productCategoryMaps = productCategoryMapRepository.findByProductCategoryDetailIdxAndStatus(entity.getIdx(), Status.ACTIVATED);
                            if (productCategoryMaps.size() != 0) {
                                Product adsProduct = productCategoryMaps.get(0).getProduct();
                                String image = productImageRepository
                                        .findByProductIdxPositionLessThanAndStatus(
                                                adsProduct.getIdx(),
                                                (byte) 0,
                                                Status.ACTIVATED);
                                adsList.add(new GetProductAdsRes(adsProduct.getIdx(), image, adsProduct.getName(), adsProduct.getDescription(), entity.getPosition()));
                            }
                        }
                );
        return adsList;
    }

    public Boolean checkProductSizeExist(@Valid @NotNull Long productIdx, @Valid @NotNull Long productSizeIdx) throws BaseException {
        return productSizeRepository.existsByProductIdxAndIdxAndStatus(productIdx, productSizeIdx, Status.ACTIVATED);
    }

    public Product getProductByIdxAndStatus(Long productIdx, Status status) throws BaseException {
        return productRepository.findByIdxAndStatus(productIdx, Status.ACTIVATED)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.PRODUCT_WRONG_IDX));
    }

    public ProductSize getProductSizeByIdxAndProductIdxStatus(Long productSizeIdx, Long productIdx, Status status) throws BaseException {
        return productSizeRepository.findByIdxAndProductIdxAndStatus(productSizeIdx, productIdx, Status.ACTIVATED)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.PRODUCT_SIZE_WRONG_IDX));
    }
}
