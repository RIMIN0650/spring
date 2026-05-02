package org.example.spring.orders;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.spring.common.BaseResponse;
import org.example.spring.orders.model.OrdersDto;
import org.example.spring.user.model.AuthUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/orders")
@RestController
public class OrdersController {

    private final OrdersService ordersService;

    @PostMapping("/create")
    public ResponseEntity<BaseResponse> create (
            @AuthenticationPrincipal AuthUserDetails authUserDetails,
            @Valid @RequestBody OrdersDto.OrdersReq dto) {
        OrdersDto.OrdersRes response = ordersService.create(authUserDetails, dto);

        return ResponseEntity.ok(BaseResponse.success(response));
    }




    @PostMapping("/verify")
    public ResponseEntity verify (
            @AuthenticationPrincipal AuthUserDetails authUserDetails,
            @RequestBody OrdersDto.VerifyReq verifyReq) {

        System.out.println(verifyReq.getPaymentId());

        ordersService.verify(authUserDetails, verifyReq);

        return ResponseEntity.ok("성공");
    }

}
