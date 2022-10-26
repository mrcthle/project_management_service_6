package de.szut.lf8_project.services;

import de.szut.lf8_project.dtos.employeeDto.EmployeeDTO;
import de.szut.lf8_project.exceptionHandling.ResourceNotFoundException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class EmployeeService {

    private final RestTemplate restTemplate;
    private final String url = "https://employee.szut.dev/employees";
    private final String jwtToken;

    private static EmployeeService instance;   
    
    private EmployeeService() {
        restTemplate = new RestTemplate();
        jwtToken = getJwtToken();
    }
    
    public static EmployeeService getInstance() {
        if (instance == null) {
            instance = new EmployeeService();
        }
        return instance;
    }

    public EmployeeDTO getEmployee(final Long id) {
        HttpHeaders httpsHeaders = new HttpHeaders();
        httpsHeaders.set("Authorization", "Bearer " + jwtToken);
        ResponseEntity<EmployeeDTO> response =
                restTemplate.exchange(url + "/" + id, HttpMethod.GET, new HttpEntity<String>(httpsHeaders), EmployeeDTO.class);
        EmployeeDTO employeeDTO = response.getBody();
        if (employeeDTO == null) {
            throw new ResourceNotFoundException("Employee with id = " + id + " not found.");
        }
        return employeeDTO;
    }
    
    public List<EmployeeDTO> getEmployees() {
        HttpHeaders httpsHeaders = new HttpHeaders();
        httpsHeaders.set("Authorization", "Bearer " + jwtToken);
        ResponseEntity<EmployeeDTO[]> response =
                restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<String>(httpsHeaders), EmployeeDTO[].class);
        return response.getBody() == null ? null : List.of(response.getBody());
    }
    
    private String getJwtToken() { //Todo: wann neuen Token abfragen (abgelaufen)
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> bodyParamMap = new LinkedMultiValueMap<>();
        bodyParamMap.add("grant_type", "password");
        bodyParamMap.add("client_id", "employee-management-service");
        bodyParamMap.add("username", "user");
        bodyParamMap.add("password", "test");
        
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(bodyParamMap, headers);

        String postUrl = "https://keycloak.szut.dev/auth/realms/szut/protocol/openid-connect/token";
        
        String response = restTemplate.postForEntity(postUrl, entity, String.class).getBody();
        assert response != null;
        String token = getJwtTokenFromResponse(response);
        return token;
    }
    
    private String getJwtTokenFromResponse(String response) {
        int endOfAccessToken = response.indexOf("\",\"expires_in\":");
        String accessToken = response.substring(
                0, endOfAccessToken
        );
        return accessToken.replace("{\"access_token\":\"", "");
    }
}
