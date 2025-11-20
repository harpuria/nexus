package com.qwerty.nexus.domain.auth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.qwerty.nexus.domain.auth.dto.request.AuthRequestDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Log4j2
@Service
public class GoogleVerifierService {
    public GoogleIdToken verifyGoogleIdToken(AuthRequestDto dto) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singleton(dto.getClientId()))
                .build();

        GoogleIdToken idToken = verifier.verify(dto.getIdToken());
        if(idToken != null){
            return idToken; // idToken 반환 후 .getPayload() 로 추출 가능
        }else{
            throw new RuntimeException("Invalid idToken");
        }
    }
}
