package com.intuit.craftdemo.entities;

import com.intuit.craftdemo.entities.Driver;
import com.intuit.craftdemo.entities.Token;
import com.intuit.craftdemo.enums.TokenType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TokenTest {

    @Test
    public void testGetterAndSetter() {
        Driver driver = new Driver();
        driver.setId(1L);
        Token token = new Token();
        token.setId(1);
        token.setToken("testToken");
        token.setTokenType(TokenType.BEARER);
        token.setExpired(false);
        token.setRevoked(false);
        token.setDriver(driver);
        assertEquals(1, token.getId());
        assertEquals("testToken", token.getToken());
        assertEquals(TokenType.BEARER, token.getTokenType());
        assertFalse(token.isExpired());
        assertFalse(token.isRevoked());
        assertEquals(driver, token.getDriver());
    }

    @Test
    public void testEqualsAndHashCode() {
        Driver driver1 = new Driver(1L, "John", "Doe", "123456789", "Address1", "ABC123",
                "Address2", "Country", "City", "State", "12345", "PhD",
                "john.doe@example.com", "password", null, null, true, "Previous Employer", true, null, null);

        Token token1 = new Token(1, "testToken", TokenType.BEARER, false, false, driver1);

        Driver driver2 = new Driver(2L, "Jane", "Doe", "987654321", "Address1", "XYZ789",
                "Address2", "Country", "City", "State", "54321", "MS",
                "jane.doe@example.com", "password", null, null, false, "Current Employer", false, null, null);

        Token token2 = new Token(1, "testToken", TokenType.BEARER, false, false, driver2);

        Token token3 = new Token(2, "anotherToken", TokenType.BEARER, true, false, driver1);

        assertNotEquals(token1, token3);
        assertNotEquals(token2, token3);
        assertNotEquals(token1.hashCode(), token3.hashCode());
        assertNotEquals(token2.hashCode(), token3.hashCode());
    }

    @Test
    public void testToString() {
        Driver driver = new Driver(1L, "John", "Doe", "123456789", "Address1", "ABC123",
                "Address2", "Country", "City", "State", "12345", "PhD",
                "john.doe@example.com", "password", null, null, true, "Previous Employer", true, null, null);
        Token token = new Token(1, "testToken", TokenType.BEARER, false, false, driver);
        assertEquals("Token(id=1, token=testToken, tokenType=BEARER, expired=false, revoked=false, driver=Driver(id=1, firstName=John, " +
                "lastName=Doe, phoneNumber=123456789, addressLine1=Address1, licenseNumber=ABC123, " +
                "addressLine2=Address2, country=Country, city=City, state=State, pinCode=12345, " +
                "highestQualification=PhD, email=john.doe@example.com, password=password, documents=null, " +
                "knownLanguages=null, hasDrivingExperience=true, lastEmployerDetails=Previous Employer, " +
                "acceptTerms=true, status=null, tokens=null))", token.toString());
    }
}

