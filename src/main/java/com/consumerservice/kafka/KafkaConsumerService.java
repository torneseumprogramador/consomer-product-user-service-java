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
import org.springframework.beans.factory.annotation.Value;

@Service
public class KafkaConsumerService {

    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${spring.kafka.consumer.user-api-url}")
    private String userApiUrl;

    @Value("${spring.kafka.consumer.product-api-url}")
    private String productApiUrl;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "${spring.kafka.consumer.topic}", groupId = "${spring.kafka.consumer.group-id}")
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
            ResponseEntity<String> userResponse = restTemplate.postForEntity(userApiUrl, userRequest, String.class);
            System.out.println("Resposta da API de usuários: " + userResponse.getStatusCode() + " - " + userResponse.getBody());

            // 4. Obter o objeto User do usuário criado
            User user = null;
            if (userResponse.getStatusCode().is2xxSuccessful() && userResponse.getBody() != null) {
                user = objectMapper.readValue(userResponse.getBody(), User.class);
            }
            if (user == null || user.getId() == null || user.getId() == 0) {
                throw new RuntimeException("Não foi possível obter o usuário criado.");
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
            ResponseEntity<String> productResponse = restTemplate.postForEntity(productApiUrl, productRequest, String.class);
            System.out.println("Resposta da API de produtos: " + productResponse.getStatusCode() + " - " + productResponse.getBody());

            if (!productResponse.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Falha ao criar produto: " + productResponse.getBody());
            }
        } catch (Exception e) {
            System.err.println("Erro no processamento da mensagem: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e); // relança para o Kafka tentar novamente
        }
    }
} 