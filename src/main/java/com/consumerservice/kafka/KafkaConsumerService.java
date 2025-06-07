package com.consumerservice.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import com.consumerservice.dtos.ProductUserDTO;
import com.consumerservice.dtos.UserDTO;
import com.consumerservice.dtos.ProductDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.consumerservice.model_views.User;

@Service
public class KafkaConsumerService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String USER_API_URL = "http://localhost:8080/users";
    private static final String PRODUCT_API_URL = "http://localhost:8081/products";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "product-user-topic", groupId = "consumer-product-user-service-group")
    public void listen(ConsumerRecord<String, String> record) {
        String payload = record.value();
        System.out.println("Mensagem recebida do Kafka: " + payload);
        try {
            // 1. Desserializar para ProductUserDTO
            ProductUserDTO productUserDTO = objectMapper.readValue(payload, ProductUserDTO.class);

            // 2. Montar UserDTO
            UserDTO userDTO = new UserDTO();
            userDTO.setName(productUserDTO.getUserName());
            userDTO.setEmail(productUserDTO.getUserEmail());

            // 3. Criar usuário
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<UserDTO> userRequest = new HttpEntity<>(userDTO, headers);
            ResponseEntity<String> userResponse = restTemplate.postForEntity(USER_API_URL, userRequest, String.class);
            System.out.println("Resposta da API de usuários: " + userResponse.getStatusCode() + " - " + userResponse.getBody());

            // 4. Obter o objeto User do usuário criado
            User user = null;
            if (userResponse.getStatusCode().is2xxSuccessful() && userResponse.getBody() != null) {
                user = objectMapper.readValue(userResponse.getBody(), User.class);
            }
            if (user == null || user.getId() == null || user.getId() == 0) {
                System.err.println("Não foi possível obter o usuário criado.");
                return;
            }

            // 5. Montar ProductDTO
            ProductDTO productDTO = new ProductDTO();
            productDTO.setName(productUserDTO.getName());
            productDTO.setDescription(productUserDTO.getDescription());
            productDTO.setPrice(productUserDTO.getPrice());
            productDTO.setQuantity(productUserDTO.getQuantity());
            productDTO.setUserId(user.getId());

            // 6. Criar produto
            HttpEntity<ProductDTO> productRequest = new HttpEntity<>(productDTO, headers);
            ResponseEntity<String> productResponse = restTemplate.postForEntity(PRODUCT_API_URL, productRequest, String.class);
            System.out.println("Resposta da API de produtos: " + productResponse.getStatusCode() + " - " + productResponse.getBody());
        } catch (Exception e) {
            System.err.println("Erro no processamento da mensagem: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 