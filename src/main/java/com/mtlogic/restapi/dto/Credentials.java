package com.mtlogic.restapi.dto;

import org.hibernate.validator.constraints.NotEmpty;

public class Credentials 
{
        
    @NotEmpty
    private String name;
    
    @NotEmpty
    private String password;
    
    public String getPassword() 
    {
        return password;
    }
    
    public void setPassword(String password) 
    {
        this.password = password;
    }
    
    public String getName() 
    {
        return name;
    }
    
    public void setName(String name) 
    {
        this.name = name;
    }
    
}
