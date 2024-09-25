package fr.twitmund.keaver.config;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.twitmund.keaver.db.model.JwtUserDetails;
import fr.twitmund.keaver.utils.CommonError;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -9077248927410675523L;

    public static final long JWT_TOKEN_VALIDITY = 2592000000L;

    private final String secret = "SECRET";

    // retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // for retrieving any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        //return Jwts.parser().setSigningKey(secret).parse(token).getBody();
        return (Claims) Jwts.parser().setSigningKey(secret).build().parse(token).getBody();
    }

    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);

        return expiration.before(new Date());
    }

    //generate token for user
    public String generateToken(UserDetails userDetails , Map<String, String> header) {
        //Map<String, Object> claims = new HashMap<>();

        return doGenerateToken(userDetails.getUsername(),header );
    }

    //while creating the token -
    //1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
    //2. Sign the JWT using the HS512 algorithm and secret key.
    //3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
    //   compaction of the JWT to a URL-safe string
    private String doGenerateToken(String subject, Map<String,String> header) {
        JsonObject jsonArray = new JsonObject();
        header.forEach(jsonArray::addProperty);


        return Jwts
                .builder()
                .claims(header)
                    .addClaims(header)
                .subject(subject)
                .issuer("KeaverFr")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS512,  secret)
                .compact();
    }

    //validate token
    public Boolean validateToken(String token) {
        return (!isTokenExpired(token));
    }

    public JsonObject retrieveUserFromToken(String token) {
        JsonObject jsonObject = new JsonObject();
        try {
            if (!validateToken(token)) {
                jsonObject.addProperty("status", "Error");
                jsonObject.addProperty("message", CommonError.InvalidToken.getValue());
                return jsonObject;
            }
            else {

                try {
                    JsonObject payload = (JsonObject) JsonParser.parseString(Jwts.parser().setSigningKey(secret).build().parse(token).getPayload().toString());
                    jsonObject.addProperty("status", "Success");
                    jsonObject.add("payload", payload);
                }catch (Exception e){
                    jsonObject.addProperty("status", "Error");
                    jsonObject.addProperty("message", e.getMessage());
                    return jsonObject;
                }

                return jsonObject;
            }
        } catch (SignatureException | ExpiredJwtException e) {
            jsonObject.addProperty("status", "Error");
            jsonObject.addProperty("message", e.getMessage());
            return jsonObject;
        }
    }

    public Optional<String> resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return Optional.of(bearerToken.substring(7));
        }
        return Optional.empty();
    }

}
