package org.example.spring.billing;

import lombok.RequiredArgsConstructor;
import org.example.spring.billing.model.BillingDto;
import org.example.spring.user.UserRepository;
import org.example.spring.user.model.User;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class BillingService {

    private final UserRepository userRepository;

    public BillingDto.BillingKeyDtoRes registerBillingKey(Long userIdx, String billingKey) {
        User user = userRepository.findById(userIdx).orElse(null);
        user.setBillingKey(billingKey);
        userRepository.save(user);
        BillingDto.BillingKeyDtoRes dto = BillingDto.BillingKeyDtoRes.builder()
                .userIdx(userIdx)
                .billingKey(billingKey)
                .build();
        return dto;
    }
}