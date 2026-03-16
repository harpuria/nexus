package com.qwerty.nexus;

import com.qwerty.nexus.domain.auth.Provider;
import com.qwerty.nexus.domain.game.coupon.TimeLimitType;
import com.qwerty.nexus.domain.game.coupon.dto.request.CouponCreateRequestDto;
import com.qwerty.nexus.domain.game.coupon.service.CouponService;
import com.qwerty.nexus.domain.game.item.ItemType;
import com.qwerty.nexus.domain.game.item.dto.request.ItemCreateRequestDto;
import com.qwerty.nexus.domain.game.item.service.ItemService;
import com.qwerty.nexus.domain.game.mail.MailRecipientsType;
import com.qwerty.nexus.domain.game.mail.MailSendType;
import com.qwerty.nexus.domain.game.mail.dto.request.MailCreateRequestDto;
import com.qwerty.nexus.domain.game.mail.service.MailService;
import com.qwerty.nexus.domain.game.store.dto.request.ProductCreateRequestDto;
import com.qwerty.nexus.domain.game.store.dto.request.ShopCreateRequestDto;
import com.qwerty.nexus.domain.game.store.service.ProductService;
import com.qwerty.nexus.domain.game.store.service.ShopService;
import com.qwerty.nexus.domain.game.user.dto.request.GameUserCreateRequestDto;
import com.qwerty.nexus.domain.game.user.service.GameUserService;
import com.qwerty.nexus.domain.management.admin.AdminRole;
import com.qwerty.nexus.domain.management.admin.dto.request.AdminCreateRequestDto;
import com.qwerty.nexus.domain.management.admin.service.AdminService;
import com.qwerty.nexus.domain.management.game.dto.request.GameCreateRequestDto;
import com.qwerty.nexus.domain.management.game.repository.GameRepository;
import com.qwerty.nexus.domain.management.game.service.GameService;
import com.qwerty.nexus.domain.management.organization.repository.OrganizationRepository;
import com.qwerty.nexus.global.response.Result;
import org.jooq.JSONB;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@SpringBootTest
class DummyDataGenerationIntegrationTest {

    private final AdminService adminService;
    private final GameService gameService;
    private final GameUserService gameUserService;
    private final ItemService itemService;
    private final CouponService couponService;
    private final MailService mailService;
    private final ShopService shopService;
    private final ProductService productService;
    private final OrganizationRepository organizationRepository;
    private final GameRepository gameRepository;

    private final int ADMIN_MAX = 20;
    private final int GAME_MAX = 20;
    private final int USER_MAX = 20;
    private final int STACK_ITEM_MAX = 10;
    private final int INSTANCE_ITEM_MAX = 10;
    private final int COUPON_MAX = 10;


    @Autowired
    DummyDataGenerationIntegrationTest(
            AdminService adminService,
            GameService gameService,
            GameUserService gameUserService,
            ItemService itemService,
            CouponService couponService,
            MailService mailService,
            ShopService shopService,
            ProductService productService,
            OrganizationRepository organizationRepository,
            GameRepository gameRepository
    ) {
        this.adminService = adminService;
        this.gameService = gameService;
        this.gameUserService = gameUserService;
        this.itemService = itemService;
        this.couponService = couponService;
        this.mailService = mailService;
        this.shopService = shopService;
        this.productService = productService;
        this.organizationRepository = organizationRepository;
        this.gameRepository = gameRepository;
    }

    @Test
    @DisplayName("더미 데이터 통합 생성 테스트")
    void createDummyDataSet() {
        Assertions.assertNotNull(organizationRepository.findByOrgId(1), "orgId=1 단체가 먼저 생성되어 있어야 합니다.");
        Assertions.assertNotNull(gameRepository.findByGameId(1), "gameId=1 게임이 먼저 생성되어 있어야 합니다.");

        String batchKey = UUID.randomUUID().toString().substring(0, 8);
        List<AdminRole> adminRoles = List.of(AdminRole.ADMIN, AdminRole.NONE);

        for (int i = 1; i <= ADMIN_MAX; i++) {
            AdminCreateRequestDto dto = new AdminCreateRequestDto();
            dto.setLoginId("dummy_admin_" + batchKey + "_" + i);
            dto.setLoginPw("admin_pw_" + i);
            dto.setAdminEmail("dummy_admin_" + batchKey + "_" + i + "@nexus.test");
            dto.setAdminNm("더미관리자" + i);
            dto.setAdminRole(adminRoles.get(ThreadLocalRandom.current().nextInt(adminRoles.size())));
            dto.setOrgId(1);

            Result<Void> result = adminService.createAdmin(dto);
            Assertions.assertInstanceOf(Result.Success.class, result, "관리자 생성 실패 index=" + i);
        }

        for (int i = 1; i <= GAME_MAX; i++) {
            GameCreateRequestDto dto = new GameCreateRequestDto();
            dto.setOrgId(1);
            dto.setName("더미게임_" + batchKey + "_" + i);
            dto.setCreatedBy("dummy-seeder");
            dto.setVersion("1.0." + i);

            Result<Void> result = gameService.createGame(dto);
            Assertions.assertInstanceOf(Result.Success.class, result, "게임 생성 실패 index=" + i);
        }

        for (int i = 1; i <= USER_MAX; i++) {
            GameUserCreateRequestDto dto = new GameUserCreateRequestDto();
            dto.setGameId(1);
            dto.setUserLId("dummy_user_" + batchKey + "_" + i);
            dto.setUserLPw("user_pw_" + i);
            dto.setNickname("더미유저" + i);
            dto.setSocialId("dummy_social_" + batchKey + "_" + i);
            dto.setProvider(Provider.GOOGLE);
            dto.setDevice("ANDROID");
            dto.setCreatedBy("dummy-seeder");

            Result<Void> result = gameUserService.createGameUser(dto);
            Assertions.assertInstanceOf(Result.Success.class, result, "유저 생성 실패 index=" + i);
        }

        for (int i = 1; i <= STACK_ITEM_MAX; i++) {
            ItemCreateRequestDto dto = new ItemCreateRequestDto();
            dto.setGameId(1);
            dto.setItemCode("STACK_ITEM_" + batchKey + "_" + i);
            dto.setName("스택형 아이템" + i);
            dto.setDesc("스택형 더미 아이템");
            dto.setItemType(ItemType.CURRENCY);
            dto.setIsStackable("Y");
            dto.setDefaultStack(100L);
            dto.setMaxStack(999999L);
            dto.setRarity("COMMON");
            dto.setCreatedBy("dummy-seeder");
            dto.setMetaJson(JSONB.valueOf("{\"type\":\"stack\"}"));

            Result<Void> result = itemService.createItem(dto);
            Assertions.assertInstanceOf(Result.Success.class, result, "스택형 아이템 생성 실패 index=" + i);
        }

        for (int i = 1; i <= INSTANCE_ITEM_MAX; i++) {
            ItemCreateRequestDto dto = new ItemCreateRequestDto();
            dto.setGameId(1);
            dto.setItemCode("INSTANCE_ITEM_" + batchKey + "_" + i);
            dto.setName("인스턴스형 아이템" + i);
            dto.setDesc("인스턴스형 더미 아이템");
            dto.setItemType(ItemType.EQUIP);
            dto.setIsStackable("N");
            dto.setDefaultStack(1L);
            dto.setMaxStack(1L);
            dto.setRarity("RARE");
            dto.setCreatedBy("dummy-seeder");
            dto.setMetaJson(JSONB.valueOf("{\"type\":\"instance\"}"));

            Result<Void> result = itemService.createItem(dto);
            Assertions.assertInstanceOf(Result.Success.class, result, "인스턴스형 아이템 생성 실패 index=" + i);
        }

        for (int i = 1; i <= COUPON_MAX; i++) {
            CouponCreateRequestDto dto = new CouponCreateRequestDto();
            dto.setGameId(1);
            dto.setName("더미 쿠폰 " + i);
            dto.setDesc("통합 테스트 더미 쿠폰");
            dto.setCode("DUMMYCP" + batchKey.toUpperCase() + i);
            dto.setRewards(JSONB.valueOf("[{\"itemCode\":\"STACK_ITEM\",\"amount\":1000}]"));
            dto.setTimeLimitType(TimeLimitType.LIMITED);
            dto.setUseStartDate(OffsetDateTime.now().minusDays(1));
            dto.setUseEndDate(OffsetDateTime.now().plusDays(30));
            dto.setMaxIssueCount(10000L);
            dto.setUseLimitPerUser(1);
            dto.setCreatedBy("dummy-seeder");
            dto.setUpdatedBy("dummy-seeder");

            Result<Void> result = couponService.createCoupon(dto);
            Assertions.assertInstanceOf(Result.Success.class, result, "쿠폰 생성 실패 index=" + i);
        }

        for (int i = 1; i <= 5; i++) {
            MailCreateRequestDto dto = new MailCreateRequestDto();
            dto.setGameId(1);
            dto.setTitle("더미 우편(보상) " + i);
            dto.setContent("보상형 더미 우편입니다.");
            dto.setRewards(JSONB.valueOf("[{\"itemCode\":\"STACK_ITEM\",\"amount\":500}]"));
            dto.setSendType(MailSendType.IMMEDIATE);
            dto.setRecipientsType(MailRecipientsType.ALL);
            dto.setExpireAt(OffsetDateTime.now().plusDays(14));
            dto.setCreatedBy("dummy-seeder");
            dto.setUpdatedBy("dummy-seeder");

            Result<Void> result = mailService.createMail(dto);
            Assertions.assertInstanceOf(Result.Success.class, result, "보상형 우편 생성 실패 index=" + i);
        }

        for (int i = 1; i <= 5; i++) {
            MailCreateRequestDto dto = new MailCreateRequestDto();
            dto.setGameId(1);
            dto.setTitle("더미 우편(읽기전용) " + i);
            dto.setContent("읽기 전용 더미 우편입니다.");
            dto.setRewards(JSONB.valueOf("[]"));
            dto.setSendType(MailSendType.IMMEDIATE);
            dto.setRecipientsType(MailRecipientsType.ALL);
            dto.setExpireAt(OffsetDateTime.now().plusDays(14));
            dto.setCreatedBy("dummy-seeder");
            dto.setUpdatedBy("dummy-seeder");

            Result<Void> result = mailService.createMail(dto);
            Assertions.assertInstanceOf(Result.Success.class, result, "읽기전용 우편 생성 실패 index=" + i);
        }

        for (int i = 1; i <= 10; i++) {
            ShopCreateRequestDto dto = new ShopCreateRequestDto();
            dto.setGameId(1);
            dto.setShopCode("DUMMY_SHOP_" + batchKey + "_" + i);
            dto.setName("더미 상점 " + i);
            dto.setDesc("통합 테스트 더미 상점");
            dto.setShopType("NORMAL");
            dto.setTimeLimitType("UNLIMITED");
            dto.setOpenAt(LocalDateTime.now().minusDays(1));
            dto.setCloseAt(LocalDateTime.now().plusYears(1));
            dto.setSortOrder(i);
            dto.setIsVisible("Y");
            dto.setCreatedBy("dummy-seeder");

            Result<Void> result = shopService.createShop(dto);
            Assertions.assertInstanceOf(Result.Success.class, result, "상점 생성 실패 index=" + i);
        }

        for (int i = 1; i <= 100; i++) {
            ProductCreateRequestDto dto = new ProductCreateRequestDto();
            dto.setGameId(1);
            dto.setProductCode("DUMMY_PRODUCT_" + batchKey + "_" + i);
            dto.setName("더미 상품 " + i);
            dto.setDesc("통합 테스트 더미 상품");
            dto.setImageUrl("https://dummy.nexus/images/" + i + ".png");
            dto.setProductType("PACKAGE");
            dto.setRewards(JSONB.valueOf("[{\"itemCode\":\"STACK_ITEM\",\"qty\":100}]"));
            dto.setCreatedBy("dummy-seeder");
            dto.setUpdatedBy("dummy-seeder");

            Result<Void> result = productService.createProduct(dto);
            Assertions.assertInstanceOf(Result.Success.class, result, "상품 생성 실패 index=" + i);
        }
    }
}
