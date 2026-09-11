package com.example.ecommerce.user.config;


import io.jsonwebtoken.Claims;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String secreteKey ="480fb56844d04018d5d11bf3848ba84525c2aa74b3c25d74422f72d73f8dad86";

    //the claims::getSubject is a method in the claims interface the claims interface has other methods like getExpirationDate etc

    //and the getSubject extracts the username from the token that is the subject it can either be username or email but in my application i am using email


    public String extractUsername(String token) {

        return extractClaims(token,Claims::getSubject);
    }


    public Date extractExpirationDate(String token){
        return extractClaims(token,Claims::getExpiration);
    }

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(),userDetails);
    }




    public String generateToken(Map<String,Object> extraClaims, UserDetails userDetails){
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSignInKey())
                .compact();
    }

    //this part is using the extractUserName function to retrieve the username from the token
    //and see if it matches the userDetails
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    //returns true if token is expired

    private boolean isTokenExpired(String token) {

        final Date expirationDate = extractExpirationDate(token);
        return expirationDate.before(new Date());
    }

    //this method is a method used to extract claims to use
    //the method is of type t because it can extract date, username or etc date uses Date and username would've returned  String so T means it can return any type
    //extractClaims is the name of the method
    //and it takes the token which is obviously needed inorder to extract a claim
    //and it also takes a Funtion provide by the java library called claimsResolver
    //finally the claimsResolver function has the parameter Claims and a Type of claim it resolving  since our method resolves a claim of any type we make of type t
    //and declare an object of claims and make it = our private extractAllClaims method
    // we the claimsResolverFunction and call on the claims that was passed in the  parameter and return it

    public <T> T extractClaims(String token, Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    //this is basically for verification

    //method extract claims by one jwts.parser() prepares the tool
    //.verifyWith(getSignInKey()) shows what the key will be compare to
    //.parseSignedClaims(token) means passing the token variable to be checked
    //getting access to the claims that is users details
    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    //this private helper method helops convert the string to byte
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secreteKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
