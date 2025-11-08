package com.onixx.apolloveiculos.api.Infra;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onixx.apolloveiculos.api.DTO.ResponseAnyDTO;
import com.onixx.apolloveiculos.api.Domains.User.User;
import com.onixx.apolloveiculos.api.Domains.User.UserRoles;
import com.onixx.apolloveiculos.api.Utils.Views;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Interceptor para aplicar automaticamente JsonView baseado na role do usuário
 * Admins verão todos os campos (AdminView), usuários normais verão apenas campos básicos (UserView)
 */
@Slf4j
@ControllerAdvice
public class JsonViewResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // Aplica em todos os responses
        return true;
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response) {

        // Se o método já tem @JsonView definido, não sobrescreve
        if (returnType.getMethodAnnotation(JsonView.class) != null) {
            return body;
        }

        // Obtém o usuário autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Class<?> viewClass = Views.UserView.class; // Default para usuários normais

        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal())
                && authentication.getPrincipal() instanceof User user) {

            // Se for admin, usa AdminView (que inclui tudo)
            if (user.getRole() == UserRoles.ROLE_ADMIN) {
                viewClass = Views.AdminView.class;
                log.debug("Aplicando AdminView para usuário: {}", user.getEmail());
            } else {
                log.debug("Aplicando UserView para usuário: {}", user.getEmail());
            }
        } else {
            log.debug("Aplicando UserView para requisição não autenticada ou não autorizada");
        }

        // Se for ResponseAnyDTO, precisa reprocessar o data
        if (body instanceof ResponseAnyDTO responseDto && responseDto.data() != null) {
            try {
                // Serializa o data com a view apropriada e deserializa de volta
                String jsonData = objectMapper.writerWithView(viewClass).writeValueAsString(responseDto.data());
                Object filteredData = objectMapper.readValue(jsonData, Object.class);

                // Cria um Map para simular o record
                Map<String, Object> filteredResponse = new HashMap<>();
                filteredResponse.put("status", responseDto.status());
                filteredResponse.put("error", responseDto.error());
                filteredResponse.put("message", responseDto.message());
                filteredResponse.put("data", filteredData);

                return filteredResponse;
            } catch (JsonProcessingException e) {
                log.error("Erro ao aplicar JsonView no ResponseAnyDTO", e);
                return body;
            }
        }

        return body;
    }
}

