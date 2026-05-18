package org.example.spring.billing;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.spring.billing.model.BillingDto;
import org.example.spring.billing.model.BillingSchedule;
import org.example.spring.billing.model.BillingScheduleRequest;
import org.example.spring.user.UserRepository;
import org.example.spring.user.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpHeaders;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class BillingService {

    private final UserRepository userRepository;


    private final BillingScheduleRepository billingScheduleRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;


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



    @Value("${PORTONE_SECRET}")
    private String portOneApiSecret;

    public BillingDto.BillingScheduleRes scheduleRecurringPayment(
            BillingScheduleRequest request,
            Long userId) {

        try {
            log.info("정기결제 예약 시작 - userId: {}, orderId: {}", userId, request.getOrderId());

            // 1. 포트원 API 호출
            String portOneResponse = callPortOneScheduleApi(
                    request.getOrderId(),
                    request.getBillingKey(),
                    request.getOrderName(),
                    request.getAmount(),
                    request.getTimeToPay()
            );

            log.info("포트원 API 응답: {}", portOneResponse);

            // 2. 응답 파싱
            Map<String, Object> responseMap = objectMapper.readValue(portOneResponse, Map.class);

            // 3. 결과 확인
            if (responseMap.containsKey("code") && responseMap.get("code") != null) {
                String errorMessage = (String) responseMap.getOrDefault("message", "Unknown error");
                log.error("포트원 API 에러: {}", errorMessage);
                return BillingDto.BillingScheduleRes.builder()
                        .success(false)
                        .message("결제 예약 실패: " + errorMessage)
                        .build();
            }

            // 4. DB에 예약 정보 저장
            BillingSchedule schedule = BillingSchedule.builder()
                    .userId(userId)
                    .billingKey(request.getBillingKey())
                    .orderId(request.getOrderId())
                    .orderName(request.getOrderName())
                    .amount(request.getAmount())
                    .scheduledTime(request.getTimeToPay())
                    .status("SCHEDULED")
                    .createdAt(LocalDateTime.now())
                    .build();

            billingScheduleRepository.save(schedule);

            log.info("정기결제 예약 완료 - orderId: {}", request.getOrderId());

            // 5. 성공 응답
            return BillingDto.BillingScheduleRes.builder()
                    .success(true)
                    .paymentId(request.getOrderId())
                    .scheduledTime(request.getTimeToPay())
                    .message("정기결제가 성공적으로 예약되었습니다.")
                    .build();

        } catch (Exception e) {
            log.error("정기결제 예약 중 에러 발생", e);
            return BillingDto.BillingScheduleRes.builder()
                    .success(false)
                    .message("서버 에러: " + e.getMessage())
                    .build();
        }
    }

    /**
     * 포트원 API 호출 - 결제 예약
     */
    private String callPortOneScheduleApi(
            String paymentId,
            String billingKey,
            String orderName,
            Integer amount,
            String timeToPay) {

        String url = "https://api.portone.io/payments/" + paymentId + "/schedule";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "PortOne " + portOneApiSecret);

        // 요청 바디 구성
        Map<String, Object> requestBody = new HashMap<>();

        Map<String, Object> payment = new HashMap<>();
        payment.put("billingKey", billingKey);
        payment.put("orderName", orderName);
        payment.put("amount", amount);
        payment.put("currency", "KRW");

        requestBody.put("payment", payment);
        requestBody.put("timeToPay", timeToPay); // ISO 8601 형식

        try {
            String jsonBody = objectMapper.writeValueAsString(requestBody);
            log.info("포트원 API 요청: {}", jsonBody);

            HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);
            String response = restTemplate.postForObject(url, entity, String.class);

            return response;
        } catch (Exception e) {
            log.error("포트원 API 호출 실패", e);
            throw new RuntimeException("포트원 API 호출 실패: " + e.getMessage());
        }
    }
}