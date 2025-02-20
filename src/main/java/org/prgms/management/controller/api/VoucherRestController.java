package org.prgms.management.controller.api;

import org.prgms.management.model.voucher.Voucher;
import org.prgms.management.service.voucher.VoucherService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
public class VoucherRestController {
    private final VoucherService voucherService;

    public VoucherRestController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    @GetMapping(value = "/api/v1/vouchers",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public List<Voucher> getVoucherList() {
        return voucherService.getAll();
    }

    @GetMapping(value = "/api/v1/vouchers/type",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public List<Voucher> getVoucherList(@RequestParam String voucherType) {
        return voucherService.getByType(voucherType);
    }

    @GetMapping(value = "/api/v1/vouchers/createdAt",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public List<Voucher> getVoucherList(@RequestParam LocalDateTime createdAt) {
        return voucherService.getByCreatedAt(createdAt);
    }

    @GetMapping(value = "/api/v1/voucher/voucherId",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public Voucher getVoucherById(@RequestParam UUID voucherId) {
        return voucherService.getById(voucherId);
    }

    @PutMapping("/api/v1/voucher")
    public Voucher createVoucher(@RequestParam String voucherName,
                                 @RequestParam String voucherType,
                                 @RequestParam int discountNum) {
        return voucherService.create(voucherName, voucherType, discountNum);
    }

    @DeleteMapping("/api/v1/voucher/delete")
    public Voucher deleteVoucher(@RequestParam UUID voucherId) {
        return voucherService.delete(voucherId);
    }
}
