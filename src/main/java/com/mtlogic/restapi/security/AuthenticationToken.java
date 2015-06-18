package com.mtlogic.restapi.security;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.Repository;

import com.mtlogic.restapi.controller.LoginController;
import com.mtlogic.restapi.entity.User;
import com.mtlogic.restapi.entity.repository.UserRepository;

public class AuthenticationToken 
{    
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
    
    private UserRepository userRepository;
    
    public static final int TOKEN_DURATION = 30;
    
    private String token;
    
    public String getToken() 
    {
        return token;
    }
    
    public void setToken(String token) 
    {
        this.token = token;
    }
    
    public AuthenticationToken(String token, UserRepository repo) 
    {
        this.token = token;
        this.userRepository = repo;
    }
    
    public boolean validate()
    {
        LOGGER.debug("==== in AuthenticationToken:validate() ====");
        
        boolean isTokenAlive = false;
        
        User user = userRepository.findByAuthToken(token);
        if (user != null)
        {
            long minutesElapsed = getDateDiff(user.getTokenCreatedDate(), new Date(), TimeUnit.MINUTES);

            if (minutesElapsed < TOKEN_DURATION)
            {
                isTokenAlive = true;
            }
        }
      
        // If the token is valid 
        // then reset the token create date in order to keep this session active
        if (isTokenAlive) 
        {
            user.setTokenCreatedDate(new Date());
            userRepository.save(user);
        }
        
        return isTokenAlive;
    }
    
    /**
     * Calculates the difference between two dates
     * @param date1 the oldest date
     * @param date2 the newest date
     * @param timeUnit the unit in which you want the difference
     * @return the difference value, in the provided unit
     */
    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) 
    {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }

}