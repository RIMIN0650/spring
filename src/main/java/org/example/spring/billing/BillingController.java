package org.example.spring.billing;

import lombok.RequiredArgsConstructor;
import org.example.spring.billing.model.BillingDto;
import org.example.spring.user.model.AuthUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/billing")
@RequiredArgsConstructor
public class BillingController {

    private final BillingService billingService;

    @PostMapping("/register")
    public void getBilling(@AuthenticationPrincipal AuthUserDetails authUserDetails,
                 @RequestBody BillingDto.BillingKeyDto dto) {
        System.out.println(authUserDetails.getIdx());
        billingService.registerBillingKey(authUserDetails.getIdx(), dto.getBillingKey());
    }

}