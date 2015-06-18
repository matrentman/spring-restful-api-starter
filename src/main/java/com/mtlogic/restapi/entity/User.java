package com.mtlogic.restapi.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtlogic.restapi.controller.UserController;
import com.mtlogic.restapi.security.SecureHash;

@Entity
public class User  extends BaseModel<Long> 
{    
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name = "email_address", length = 128, nullable = false, unique = true)
    private String emailAddress;
    
    @Column(name = "password", length = 255)
    private String password;
    
    @Column(name = "auth_token", length = 255)
    private String authToken;
    
    @Column(name = "token_created_date", nullable = true)
    private Date tokenCreatedDate;
    
    @Column(name = "first_name", length = 64, nullable = false, unique = false)
    private String firstName;
    
    @Column(name = "last_name", length = 64, nullable = false, unique = false)
    private String lastName;
    
    public User() {}
            
    public User(Long id, String emailAddress, String password, String firstName, String lastName) 
    {
        this.id = id;
        this.emailAddress = emailAddress;
        this.password = password; 
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    @Override
    public Long getId() 
    {
        return id;
    }
    
    public String getFirstName() 
    {
        return firstName;
    }
    
    public void setFirstName(String firstName) 
    {
        this.firstName = firstName;
    }

    public String getLastName() 
    {
        return lastName;
    }
    
    public void setLastName(String lastName) 
    {
        this.lastName = lastName;
    }
    
    public String getEmailAddress() 
    {
        return emailAddress;
    }
    
    public void setEmailAddress(String emailAddress) 
    {
        this.emailAddress = emailAddress;
    }
    
    public String getPassword() 
    {
        return password;
    }
    
    public void setPassword(String password) 
    {    
        LOGGER.info("Entering User:setPassword()...");
        try
        {
            this.password = SecureHash.generateStrongPasswordHash(password);
            LOGGER.info("Password = " + this.password);
        }
        catch(Exception e)
        {
            LOGGER.info("Could not set password!!!");
        }
        LOGGER.info("Exiting User:setPassword()...");   
    }
    
    public String getAuthToken() 
    {
        return authToken;
    }
    
    public void setAuthToken(String authToken) 
    {
        this.authToken = authToken;
    }

    public String createToken() 
    {
        authToken = UUID.randomUUID().toString();
        this.tokenCreatedDate = new Date();
        return authToken;
    }
    
    public Date getTokenCreatedDate() 
    {
        return tokenCreatedDate;
    }
    
    public void setTokenCreatedDate(Date tokenCreatedDate) 
    {
        this.tokenCreatedDate = tokenCreatedDate;
    }
    
    @Override
    public String toString() 
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            return mapper.writeValueAsString(this);
        }
        catch (Exception e)
        {
            LOGGER.info("Could not map User object to json string!");
            return "Invalid JSON! - JSON could not be created for User object";
        }
    }
}
