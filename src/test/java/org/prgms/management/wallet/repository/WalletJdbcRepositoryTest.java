package org.prgms.management.wallet.repository;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.*;
import org.prgms.management.customer.entity.Customer;
import org.prgms.management.customer.repository.CustomerJdbcRepository;
import org.prgms.management.voucher.entity.Voucher;
import org.prgms.management.voucher.entity.VoucherCreator;
import org.prgms.management.voucher.repository.VoucherJdbcRepository;
import org.prgms.management.wallet.vo.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@ActiveProfiles(value = "test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WalletJdbcRepositoryTest {
    @Autowired
    WalletJdbcRepository walletJdbcRepository;

    @Autowired
    CustomerJdbcRepository customerJdbcRepository;

    @Autowired
    VoucherJdbcRepository voucherJdbcRepository;

    List<Wallet> newWallets = new ArrayList<>();
    List<Customer> newCustomers = new ArrayList<>();
    List<Voucher> newVouchers = new ArrayList<>();

    @BeforeAll
    void setup() {
        customerJdbcRepository.deleteAll();
        newCustomers.add(Customer.getCustomer(UUID.randomUUID(), "test-user1", LocalDateTime.now()));
        newCustomers.add(Customer.getCustomer(UUID.randomUUID(), "test-user2", LocalDateTime.now()));
        newCustomers.add(Customer.getCustomer(UUID.randomUUID(), "test-user3", LocalDateTime.now()));
        newCustomers.add(Customer.getCustomer(UUID.randomUUID(), "test-user4", LocalDateTime.now()));
        newCustomers.forEach(v -> customerJdbcRepository.insert(v));

        voucherJdbcRepository.deleteAll();
        newVouchers.add(VoucherCreator.createVoucher(UUID.randomUUID(), 1000, "voucher-test1", "fixed", LocalDateTime.now()).orElse(null));
        newVouchers.add(VoucherCreator.createVoucher(UUID.randomUUID(), 500, "voucher-test2", "fixed", LocalDateTime.now()).orElse(null));
        newVouchers.add(VoucherCreator.createVoucher(UUID.randomUUID(), 99, "voucher-test3", "percent", LocalDateTime.now()).orElse(null));
        newVouchers.add(VoucherCreator.createVoucher(UUID.randomUUID(), 50, "voucher-test4", "percent", LocalDateTime.now()).orElse(null));
        newVouchers.forEach(v -> voucherJdbcRepository.insert(v));

        newCustomers.forEach(c -> {
            newVouchers.forEach(v -> {
                newWallets.add(Wallet.getWallet(
                        UUID.randomUUID(),
                        c.getCustomerId(),
                        v,
                        LocalDateTime.now())
                );
            });
        });
    }

    @Test
    @Order(1)
    @DisplayName("지갑을 추가 할 수 있다.")
    void insert() {
        newWallets.forEach(v -> {
            var wallet = walletJdbcRepository.insert(v);
            assertThat(wallet == null, is(false));
            assertThat(wallet.getWalletId(), is(v.getWalletId()));
        });
    }

    @Test
    @Order(2)
    @DisplayName("중복된 지갑은 추가 할 수 없다.")
    void insertDuplicateWallet() {
        newWallets.forEach(v -> {
            var wallet = walletJdbcRepository.insert(v);
            assertThat(wallet == null, is(true));
        });
    }

    @Test
    @Order(3)
    @DisplayName("전체 지갑을 조회 할 수 있다.")
    void findAll() {
        var wallets = walletJdbcRepository.findAll();
        assertThat(wallets.size(), is(newVouchers.size() * newCustomers.size()));
    }

    @Test
    @Order(4)
    @DisplayName("아이디로 지갑을 조회 할 수 있다.")
    void findById() {
        newWallets.forEach(v -> {
            var wallet = walletJdbcRepository.findById(v.getWalletId());
            assertThat(wallet == null, is(false));
        });
    }

    @Test
    @Order(5)
    @DisplayName("고객 아이디로 지갑을 조회 할 수 있다.")
    void findByCustomerId() {
        newWallets.forEach(v -> {
            var wallet = walletJdbcRepository.findByCustomerId(
                    v.getCustomerId());
            assertThat(wallet == null, is(false));
        });
    }

    @Test
    @Order(6)
    @DisplayName("지갑을 삭제 할 수 있다.")
    void delete() {
        var wallet = walletJdbcRepository.delete(newWallets.get(0));
        assertThat(wallet == null, is(false));
    }

    @Test
    @Order(7)
    @DisplayName("모든 지갑을 삭제 할 수 있다.")
    void deleteAll() {
        walletJdbcRepository.deleteAll();
        customerJdbcRepository.deleteAll();
        voucherJdbcRepository.deleteAll();
        var wallets = walletJdbcRepository.findAll();
        assertThat(wallets == null, is(true));
    }

    @Test
    @Order(8)
    @DisplayName("삭제한 지갑은 검색 할 수 없다.")
    void findDeleted() {
        newWallets.forEach(w -> {
            var wallets = walletJdbcRepository.findById(w.getWalletId());
            assertThat(wallets == null, is(true));
            walletJdbcRepository.findByCustomerId(w.getCustomerId());
            assertThat(wallets == null, is(true));
        });
    }
}