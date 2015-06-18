package com.mtlogic.restapi.dto;

import org.hibernate.validator.constraints.NotEmpty;

public class Token 
{

    @NotEmpty
    private String token;
    
    public Token() {}
    
    public Token(String token) 
    {
        this.token = token;
    }
    
    public String getToken() 
    {
        return token;
    }
    
    public void setToken(String token) 
    {
        this.token = token;
    }
    
}
