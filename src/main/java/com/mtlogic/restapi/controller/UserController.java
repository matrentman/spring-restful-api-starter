package com.mtlogic.restapi.controller;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mtlogic.restapi.entity.User;
import com.mtlogic.restapi.entity.repository.UserRepository;
import com.mtlogic.restapi.security.AuthenticationToken;

@RestController
public class UserController 
{
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/user/{userID}", method = RequestMethod.GET)
    public User user(@PathVariable("userID") Long userID, @RequestHeader(value="token") String token, HttpServletResponse responsz) 
    {
        LOGGER.debug("==== in /user GET ====");

        User user = null;
        AuthenticationToken authToken = new AuthenticationToken(token, userRepository);
        
        if ( authToken.validate() )
        {
            user = userRepository.findById(userID);
        }
        else
        {
            LOGGER.debug("Invalid token");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        
        return user;
    }
    
    @RequestMapping(value = "/user/{userID}", method = RequestMethod.DELETE)
    public User deleteUser(@PathVariable("userID") Long userID, @RequestHeader(value="token") String token, HttpServletResponse response) 
    {
        LOGGER.debug("==== in /user GET ====");

        User user = null;
        AuthenticationToken authToken = new AuthenticationToken(token, userRepository);
        
        if ( authToken.validate() )
        {
            user = userRepository.findById(userID);
            try {
                userRepository.delete(user);
            }
            catch (Exception e) {
                LOGGER.debug("Could not delete user data! " + e.getLocalizedMessage());
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
        else
        {
            LOGGER.debug("Invalid token");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        
        return user;
    }
    
    @RequestMapping(value = "/user", method = RequestMethod.PUT)
    public User updateUser(@RequestBody User input, @RequestHeader(value="token") String token, HttpServletResponse response) 
    {
        LOGGER.debug("==== in /user POST ====");
        
        LOGGER.debug("jsonPayload: " + input);
        
        User user = null;
        
        AuthenticationToken authToken = new AuthenticationToken(token, userRepository);
        if ( authToken.validate() )
        {
            if (input.getId() != null)
            {
                user = userRepository.findById(input.getId());
                if (user != null) 
                {
                    user.setEmailAddress(input.getEmailAddress());
                    user.setFirstName(input.getFirstName());
                    user.setLastName(input.getLastName());
                    user.setPassword(input.getPassword());
                    
                    try 
                    {
                        userRepository.save(user);
                    }
                    catch (Exception e) 
                    {
                        LOGGER.debug("Could not persist user data! " + e.getLocalizedMessage());
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    }
                }
            }
        }
        else
        {
            LOGGER.debug("Invalid token");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        
        return user;
    }
    
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public User createUser(@RequestBody User input, @RequestHeader(value="token") String token, HttpServletResponse response) 
    {
        LOGGER.debug("==== in /user POST ====");
        
        LOGGER.debug("jsonPayload: " + input);
        
        User user = null;
        
        AuthenticationToken authToken = new AuthenticationToken(token, userRepository);
        if ( authToken.validate() )
        {
            user = new User();
            
            user.setEmailAddress(input.getEmailAddress());
            user.setFirstName(input.getFirstName());
            user.setLastName(input.getLastName());
            user.setPassword(input.getPassword());
            
            try 
            {
                userRepository.save(user);
            }
            catch (Exception e) 
            {
                LOGGER.debug("Could not persist new user data! " + e.getLocalizedMessage());
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
        else
        {
            LOGGER.debug("Invalid token");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        
        return user;
    }
}