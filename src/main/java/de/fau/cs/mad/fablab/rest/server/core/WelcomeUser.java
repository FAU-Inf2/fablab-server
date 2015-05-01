package de.fau.cs.mad.fablab.rest.server.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

/**
 * This class is a representation of our JSON return type
 */
public class WelcomeUser
{
    private long id;

    @Length(max = 10)
    private String message;

    private long greetings;

    public WelcomeUser()
    {
        // Jackson deserialization
    }

    public WelcomeUser(long id, String message, long greetings)
    {
        this.id = id;
        this.message = message;
        this.greetings = greetings;
    }

    @JsonProperty
    public long getId() {
        return id;
    }

    @JsonProperty
    public String getMessage() {
        return message;
    }

    @JsonProperty
    public long getGreetings()
    {
        return greetings;
    }
}
