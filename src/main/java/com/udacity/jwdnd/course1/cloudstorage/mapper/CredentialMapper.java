package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CredentialMapper {

  @Select("SELECT * FROM CREDENTIALS WHERE credentialid = #{credentialId}")
  Credential findCredentialById(int credentialId);

  @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userid}")
  List<Credential> findByUserId(int userid);

  @Insert(
      "INSERT INTO CREDENTIALS (url, username, key, password, userid) VALUES (#{url}, #{username}, #{key}, #{password}, #{userId})")
  @Options(useGeneratedKeys = true, keyProperty = "credentialId")
  int insertCredential(Credential credential);

  @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{credentialId}")
  int deleteCredential(int credentialId);

  @Update(
      "UPDATE CREDENTIALS SET url = #{url}, username = #{username}, key = #{key}, password = #{password} WHERE credentialid = #{credentialId}")
  int updateCredential(Credential credential);

}
