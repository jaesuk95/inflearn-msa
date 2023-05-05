package com.example.userservice.impl;

import com.example.userservice.controller.response.ResponseOrder;
import com.example.userservice.feign.OrderServiceClient;
import com.example.userservice.model.user.UserDto;
import com.example.userservice.model.user.UserEntity;
import com.example.userservice.model.user.UserRepository;
import com.example.userservice.model.user.UserService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final Environment env;
//    private final RestTemplate restTemplate;
    private final OrderServiceClient orderServiceClient;
    private final CircuitBreakerFactory circuitBreakerFactory;

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = mapper.map(userDto, UserEntity.class);
        String bCryptPassword = bCryptPasswordEncoder.encode(userDto.getPwd());
        userEntity.setEncryptedPwd(bCryptPassword);
        userRepository.save(userEntity);
        log.info("new user has been saved = {}", userEntity.getId());

        UserDto returnUserDto = mapper.map(userEntity, UserDto.class);
        return returnUserDto;
    }

    @Override
    public Iterable<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found");
        }
        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

        /**
        // RestTemplate 사용 방식
        // 다른 마이크로스버스에서 호출해서 데이터 가져오기 ("http://127.0.0.1:8000/order-service/%s/orders")
        String orderUrl = String.format(env.getProperty("order_service.url"), userId);
        ResponseEntity<List<ResponseOrder>> orderListResponse = restTemplate.exchange(
                orderUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<ResponseOrder>>() {
                });

        // 우리가 필요한 데이터는 body 안에 담겨있다
        List<ResponseOrder> orderList = orderListResponse.getBody();
         */

        /**
         * FeignClient Method + Feign exception handling
         * */
//        try {
//            List<ResponseOrder> orderList = orderServiceClient.getOrders(userId);
//            userDto.setOrders(orderList);
//        } catch (FeignException e) {
//            log.error(e.getMessage());
//        }

        // errorDecoder 를 사용하기 때문에 try/catch 필요가 없다
        // errorDecoder 는 order-service 에서 URL 이 없을 때 발생하는 오류
//        List<ResponseOrder> orderList = orderServiceClient.getOrders(userId);
//        userDto.setOrders(orderList);

        // circuit breaker 방식
        // order-service 측에서 에러가 발생해도 무시하고 데이터를 출력한다. (errorDecoder 는 오류가 발생한다.)
        CircuitBreaker circuitbreaker = circuitBreakerFactory.create("circuitBreaker");
        List<ResponseOrder> orderList = circuitbreaker.run(() -> orderServiceClient.getOrders(userId),
                throwable -> new ArrayList<>());    // <- throwable -> new ArrayList<>() 이 코드의 뜻은, orderServiceClient.getOrders(id) 에서 오류가 발생하면 비어있는 arrayList[] 으로 반환한다는 뜻이다.
        userDto.setOrders(orderList);
        return userDto;
    }

    @Override
    public UserDto getUserDetailsByEmail(String username) {
        UserEntity userEntity = userRepository.findByEmail(username);

        if (userEntity == null) {
            throw new UsernameNotFoundException(String.format("User not found = %s", username));
        }

        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
        return userDto;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username);

        if (userEntity == null) {
            throw new UsernameNotFoundException(String.format("User not found = %s", username));
        }

        return new User(userEntity.getEmail(), userEntity.getEncryptedPwd(), true, true, true, true,
                new ArrayList<>()); // 현재 user 권한은 없기 때문에 arrayList 으로
    }
}
