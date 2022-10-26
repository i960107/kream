# 개발일지
## 2021-08-14
+ prod,dev ec2 서버 구축<br/>
  + prod : **ip 주소** *54.180.128.168* **domain** *prod.rp3-kream.shop* ->ssl 적용, 모든요청 9000(스프링부트)로 포트포워딩<br/>
  + dev : **ip 주소** *13.125.154.101* **domain** *dev.rp3-kream.shop*<br/>
  
+ RDS 구축<br/>
  **endpoint** rp3-risingtest.comborzrmicf.ap-northeast-2.rds.amazonaws.com(3306)
  
+ ERD 설계<br/>
  aquery 읽기 전용 url : https://aquerytool.com/aquerymain/index/?rurl=1d826c45-0bde-4964-90bf-f288f9021b8b <br/>패스워드 : 47r77w<br/>
  + 생성한 테이블<br/>
      1.user<br/>
      2.account<br/>
      3.address<br/>
      4.product<br/>
      5.product_size<br/>
      6.product_category<br/>
      7.product_image<br/>
      8.purchase_bid<br/>
      9.sale_bid<br/>
      10.product_own<br/>
      11.prodcut_like<br/>
      12.style<br/>
      13.style_comment<br/>
      14.style_image<br/>
      16.style_like<br/>
      17.watch<br/>
      18.watch_category<br/>
  
  + 개선되어야 할점<br/>
    관계 설정<br/>
    style_comment 테이블 댓글 대댓글관련 필드 수정하기 -> 댓글 및 대댓글 조회 순서 정할 수 있도록<br/>
  
## 2021-08-15
+ ERD설계 수정<br/>
    1) table 이름 수정 : product_own -> user_own <br/>
                       user가 소유한 상품에 대한 테이블이므로 user_own이 데이터를 더 잘 표현하는 이름.
                       user와 product가 1:N 관계. 이므로 1인 user가 중요.<br/>
    2) 추가된 테이블 : transaction,insepection, refund<br/>
                    낙찰되어 거래가 성사되면 제품이 결제되고 검수되고 구매자에게 배송되기까지의 과정을 담을 수 있어야 함   <br/>
    + 구매자는 현재 판매 입찰중인 가격중 가장 낮은 가격으로 즉시 구매할 수 있다. <br/>이때 purchase_bid 테이블에 데이터 추가하고 바로 transaction으로 이동되고 거래 진행.<br/>
    + 판매자는 현재 구매 입찰중인 가격 중 가장 높은 가격으로 즉시 판매할 수 있다.<br/>
    + 이때 sale_bid 테이블에 데이터 추가하고 바로 transactiond으로 이동됨.<br/>
    + 제품은 거래되고 난 후 검수를 진행하는데 이때 불합격하면 거래는 파기되고 구매자에게 환불이                       이루어진다.검수에 합격한 상품은 구매자의 단순변심에 의해 환불이 불가능하다. <br/>
    3) style_comment 댓글, 대댓글 관련 필드 추가 <br/>
    + 댓글, 댓글에 대한 대댓글, 대댓글에 대한 댓글등 가능<br/>
    + ref(댓글 그룹), level(댓글, 대댓글), step(순서), replyTo(누구에게 답변)로 정렬 가능 
                    
    4) 최종 데이터베이스 상태               
    + 유저 관련 : user, account, address, user_own <br/>
    + 상품 관련 : product, product_size, product_category, prodcut_image, product_like<br/>
    + 거래 관련 : sale_bid, purchase_bid, transaction, inspection, refund<br/>
    + 스타일 관련 : style, style_comment, style_image, style_like, 
    + watch 관련 : watch, watch_category
+ table 생성
+ user API 생성 및 서버 반영 완료- 회원가입, 로그인, 이메일찾기
    - nginx에서 port forwarding해주기
    
## 2021-08-16
+ 카테고리 ERD 수정 <br/>
     기존 depth1 depth2로 세부 카테고리 구분했지만 이렇게 하면-> 그룹핑해서 가져오기가 어려움<br/>
     카테고리 테이블 세부 카테고리 테이블로 나눠서->카테고리 전체 가져와서 하나씩 세부 카테고리 테이블 조회해서 가져오기<br/>
     다만, 이 방식은 데이서베이스 요청이 많아서 서비스 커질수록 안 좋음. 수정 필요.
     
+ 카테고리 get API 생성 및 서버 반영<br/>
     카테고리-세부카테고리리스트 map으로 제공하려 했지만 이렇게 되면 서버에서 정렬해서 map은 순서가 보장되지 않는다. arraylist안 arraylist(카테고리 및 세부카테고리리스트 포함)로 보냄.
### 2021-08-17
+ erd피드백<br/>
  + 댓글,대댓글 관련 필드 parentIdx하나만 있으면 됨 : 댓글인 경우 -1 대댓글인겨우 관련 댓글 혹은 대댓글의 idx, 정렬은 createdAt으로 가능<br/>
  + 홈 화연 피드는 따른 테이블이 아니라 카테고리로 관리. 카테고리의 범위가 늘어남. 카테고리의 노출되지는 않지만 홈 화면 같은 그룹은 같은 카테고리 가짐. 즉 하나의 상품은 여러개의 카테고리 가질 수 있음->상품-카테고리 테이블 필요<br/>
  + 빠른 조회를 위한 필드는 없는게 좋을듯. 그때그때 최신 데이터 가져오기. insert할때마다 관련된 테이블 update시켜주는 것보다 select해오는게 더 빠름
+ api명세서 피드백<br/>
  + json값은 number, string,boolean,array지만 클라를 위해서 어떤 데이터타입인지 써주는게 좋음<br/>
  + 하나의 객체인 경우 Object타입으로 바꿔주기
  + 캡쳐화면 놓치지 말기
  + request validation error는 필드별로 따로 보내주는게 클라에게 좋음. 각 필드별로 다른 에러를 뱉는 것이 좋다는 조언을 받았지만 한번에 map 필드-필요한 형식ㅇ르 ㅗ보내주기로 함 <br/>
  그렇지 않으면 필드 하나씩 요청보내서 에러 확인해야 하므로 하나의 에러의 모든 필드 검사 결과 보내주기로 선택<br/>
  
  ## 2021-08-18
  ### 힌일
  ### 고민
     +  빠른 조회를 위한 필드 : 서비스가 작을때는 update보다 select가 훨씬 빠름. <br/>
                           서비스가 커질 경우 row가 많아져서 select db 부하 심해짐. <br/>
                           최신 데이터가 아니어도 상대적으로 괜찮은 data의 경우 field를 두고, 5분에 한번 정도씩 field update 시켜주는게 나을수도.<br/>
                           reddis 같은 inmemory db사용해서 count같은 값 저장할 수도 있음.<br/>
     +  query string으로 TRUE 혹은 FALSE값 받고 싶을때 String 보다 Boolean이 좋지 않을까? NO.<br/>
        현재 db에는 FALSE/TRUE로 저장되어있는데 Boolean으로 받을 경우 true,false로 받아져서 다시 db조회할때는 uppercase 변환 필요.<br/>
      
  ## 2021-08-19
  ### 한 일
  + 상품 관련 API 구현 및 서버 반영 완료 :<br/>
    1) 사이즈별 즉시 구매가능 가격 조회<br/>
    2) 전체/및 사이즈별 거래 내역/판매 입찰/ 구매 입찰 내역 조회<br/>
        pageable사용하여 필드별로 정렬할 수 있도록 구현.<br/>
        거래내역 조회는 duration사용하여 시세 조회에도 사용할 수 있도록 구현<br/>
    3) 비슷한 상품 추천하기<br/>
  + 카테고리 조회 API 수정 :<br>
    클라 아무것도 선택하지 않았을때 나타나는 카테고리 내려달라는 요청<br/>
    category 테이블에 mainPosition이라는 필드를 추가해서 main에 노출될 카테고리의 경우 position으로 순서 나타내고,<br/>
    노출되지 않는 카테고리의 경우 -1의 값을 가지도록<br/>
    실제 세부카테고리로 나타나는 카테고리 이미지랑 메인에 노출되는 이미지가 다를경우 <br/>
    카테고리 하나더 생성<br/>
  
  ### 고민<br/>
    + 판매 입찰 내역 조회시 : 가격이 같은 판매 입찰의 경우 따로 보여주지 않고 수량으로 보여주는데 사이즈별 판매입찰 내역 db에서 조회한 후<br/>
                         다시 서버에서 가공해서 count해야하나 -> 그건 아님. 판매입찰내역순, 사이즈순, 수량순으로 정렬이 가능해야 하는데 서버에서는 정렬이 어려움.<br/>
                         -> 서버에서 groupby로 count계산 후 dto로 가져온다. Jpa에서 entity아닌 primitive 혹은 dto반환 가능<br/>
    + 추천 product 불러오기 : <br/>
                1)product의 brand는 하나지만 category는 여러개.<br/>
                 예를 들어 조회하는 상품의 카테고리가 스니커즈,jordan이라면  현재 API는 상품 중 스니커즈카테고리 모두, jordan카테고리 모두 조회해서 5개만 가져옴. idx 낮은순.<br/>
                 가장 비슷한 것 추천해주려면 더 고도화 필요..-> category를 상품카테고리, 세분류카테고리를 나눠서 세분류 카테고리가 같은 것을 추천해 줄 수 있도록.<br/>
                 2)product와 category는 m:n관계인데 카테고리가 같은 상품을 조회하기 위해 product테이블을 기준으로 조회하니 어려움.
                   join해주는 product_category 테이블을 기준으로 카테고리가 같은 상품의 idx를 가져온 후 product조회하기.
                   product의 즉시 구매 가능가 조회하기 위해서는 다시, product 하나씩 sale_bid테이블 조회해서 가장 낮은 가격 가져오는 작업 필요 <br/>

    + 응답 형식 : 이때까지 list로 돌려주는 경우 list로 감싸는 dto하나더 만들어서 그 값으로 보내주었는데 이건 잘못되었음<br/>
                예시) getProductRes를 list로 반환하고 싶을때 GetProductResList라는 List<GetProductRes>담는 객체를 하나 더 만듬<br/>
                필요없는 list이름까지 반환되고 list에서 다시 list조회하는 수고로움.<br/>
                base response result에 List<GetProductRes>로 반환할 수 있음.<br/>
                ->이때까지 구현한 API는 이미 클라에 반영되었기 때문에 수정하기 어렵고, 앞으로는 형식 주의하도록!
             
##2021-08-20
    ### 힌일
    ### 고민<br/>
##2021-08-21
    ### 힌일
    ### 고민<br/>
##2021-08-22
    ### 힌일
  1.uri RESTful하게 수정. :- bids→ 삭제
- profile써주는게 좋냐?
- api/bids/sale:productsizeidx
- entity 뒤에 번호가 와주는게 좋음
- 동사와 entity구분해주기
    ### 고민<br/>
##2021-08-23
    ### 힌일 
  1.유저 배송지, 결제카드, 정산용 계좌 관련 api 구현: 추가, 수정
  2.판매 입찰하기 구현 : 로직은 판매 입찰이든 즉시 판매이든 bidSale idx를 가지고 transaction에 추가되는 것이지만 판매 입찰일때와 즉시 판매일때 서버에서 받는 값이 다르므로 api분리 결정.
   ### 고민<br/>
  + account(판매 정산용 계좌)는 유저마다 하나씩 밖에 안 가지는데도 따른 테이블로 관리해야하나?<br/>
   유저 테이블의 칼럼으로 관리한다면 수정된 기록을 알 수 없음. 판매 정산용 계좌는 계속 바뀔 수 있고 어떤 거래에서 어떤 계좌로 정산받았는지 알 수 있어야 하므로 따른 테이블로 관리하는것이 좋음<br/>
  + 계좌 실명 인증용 api 사용하려면 주민번호 앞자리 필요. 현재 프론트에서 앞자리 받고 있지만 서버로 넘기고 있지는 않은 상황. -> 우선순위가 낮으므로 일단 중요한 작업부터 하고 남는 시간에 하기로.<br/>
  + responseDto 도 validation해주어야할까? 해주는게 좋음 넘어갈때 null이 넘어가지 않도록. 다만 @validation 붙여줘야함. 어디서?
  + db TRUE/FALSE를 java에서 boolean으로 받을 수 있지만 넣을때 변환하는것이 힘드므로 string으로 관리하기로 결정
