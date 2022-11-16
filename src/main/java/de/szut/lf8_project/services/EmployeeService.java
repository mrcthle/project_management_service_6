package de.szut.lf8_project.services;

import de.szut.lf8_project.dtos.employeeDto.EmployeeDTO;
import de.szut.lf8_project.exceptionHandling.NoResponseFromApiException;
import de.szut.lf8_project.exceptionHandling.ResourceNotFoundException;
import de.szut.lf8_project.helpers.JwtToken;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

import java.time.LocalTime;
import java.util.List;

@Service
public class EmployeeService {

    private final RestTemplate restTemplate;
    private final String url = "https://employee.szut.dev/employees";
    private JwtToken jwtToken;
    private static EmployeeService instance;   
    
    private EmployeeService() {
        restTemplate = new RestTemplate();
        refreshJwtToken();
    }
    
    public static EmployeeService getInstance() {
        if (instance == null) {
            instance = new EmployeeService();
        }
        return instance;
    }

    public EmployeeDTO getEmployee(final Long id) {
        if (jwtToken.isExpired()) {
            refreshJwtToken();
        }
        HttpHeaders httpsHeaders = new HttpHeaders();
        httpsHeaders.set("Authorization", "Bearer " + jwtToken.getToken());
        EmployeeDTO employeeDTO;
        try {
            ResponseEntity<EmployeeDTO> response =
                    restTemplate.exchange(url + "/" + id, HttpMethod.GET, new HttpEntity<String>(httpsHeaders), EmployeeDTO.class);
            employeeDTO  = response.getBody();
        } catch (HttpClientErrorException e) {
            throw new ResourceNotFoundException("Employee with id = " + id + " not found.");
        }
        return employeeDTO;
    }
    
    private void refreshJwtToken() {
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
        if (response == null) {
            throw new NoResponseFromApiException("No response from employee API.");
        }
        JSONObject tokenAsJson = new JSONObject(response);
        String accessToken = tokenAsJson.get("access_token").toString();
        LocalTime localTime = LocalTime.now();
        Long expireTime = Long.parseLong(tokenAsJson.get("expires_in").toString()) - 2L; // makes up for time loss in lines before
        jwtToken = new JwtToken(accessToken, localTime, expireTime);
    }
}
