package com.mtlogic.restapi.controller;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mtlogic.restapi.dto.Credentials;
import com.mtlogic.restapi.dto.Token;
import com.mtlogic.restapi.entity.User;
import com.mtlogic.restapi.entity.repository.UserRepository;
import com.mtlogic.restapi.security.SecureHash;

@RestController
public class LoginController 
{

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
    
    @Autowired
    private UserRepository userRepository;
 
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Token login(@RequestBody Credentials input, HttpServletResponse response) 
    {
        LOGGER.debug("==== in /login POST ====");
        
        LOGGER.debug("jsonPayload: " + input);
        
        User user = userRepository.findByEmailAddress(input.getName());
        Token token = null;

        if (user != null)
        {
            try 
            {
                if(SecureHash.validatePassword(input.getPassword(), user.getPassword()))
                {
                    user.createToken();
                    userRepository.save(user);
                    token = new Token(user.getAuthToken());
                }
                else
                {
                    LOGGER.debug("Invalid password");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }
            }
            catch (Exception e) 
            {
                LOGGER.debug("Exception thrown from SecureHash " + e.getLocalizedMessage());
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
        
        return token;
    }
    
}
