package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CredentialService {

    private CredentialMapper credentialMapper;
    private EncryptionService encryptionService;

    @Autowired
    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public List<Credential> getCredentialsByUserId(Integer userId) {
        List<Credential> list =   credentialMapper.findByUserId(userId);
        for (Credential item: list) {
            item.setDecryptedPassword(encryptionService.decryptValue(item.getPassword(),item.getKey()));
        }
        return  list;
    }

    public int createCredential(Credential credential) {
        String key = RandomStringUtils.randomAlphanumeric(16);
        String encodedPassword = encryptionService.encryptValue(credential.getPassword(),key);
        credential.setPassword(encodedPassword);
        credential.setKey(key);
        return credentialMapper.insertCredential(credential);
    }

    public int updateCredential(Credential credential) {
        String key = RandomStringUtils.randomAlphanumeric(16);
        credential.setKey(key);
        String encodedPassword = encryptionService.encryptValue(credential.getPassword(),key);
        credential.setPassword(encodedPassword);
        return credentialMapper.updateCredential(credential);
    }

    public int removeCredential(int credentialId) {
        return credentialMapper.deleteCredential(credentialId);
    }
}
