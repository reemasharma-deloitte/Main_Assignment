package com.example.demo.model;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.sql.Timestamp;

/**
 * Token that has been sent to the users email for verification.
 */
@Entity
@Table(name = "verification_token")
public class VerificationToken {

  /** The unique id for the record. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;
  /** The token that was sent to the user. */
  @Lob
  @Column(name = "token", nullable = false, unique = true)
  private String token;
  /** The timestamp of when the token was created. */
  @Column(name = "created_timestamp", nullable = false)
  private Timestamp createdTimestamp;
  /** The user this verification token is for. */
  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private LocalUser user;


  
  public LocalUser getUser() {
    return user;
  }

  
  public void setUser(LocalUser user) {
    this.user = user;
  }

 
  public Timestamp getCreatedTimestamp() {
    return createdTimestamp;
  }

 
  public void setCreatedTimestamp(Timestamp createdTimestamp) {
    this.createdTimestamp = createdTimestamp;
  }

 
  public String getToken() {
    return token;
  }

  
  public void setToken(String token) {
    this.token = token;
  }

  
  public Long getId() {
    return id;
  }

  
  public void setId(Long id) {
    this.id = id;
  }

}