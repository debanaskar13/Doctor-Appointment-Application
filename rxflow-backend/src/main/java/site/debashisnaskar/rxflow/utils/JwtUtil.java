package site.debashisnaskar.rxflow.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import site.debashisnaskar.rxflow.model.User;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class JwtUtil {

    public static final String SECRET = "5367566859703373367639792F423F452848284D6251655468576D5A71347437";
    private static final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());
    private static final long EXPIRATION = 1000 * 60 * 60 * 24;


    public static String generateToken(String username) {
        HashMap<String, Object> claims = new HashMap<>();
        return createToken(claims,username);
    }
    public static String createToken(Map<String,Object> claims, String username) {
        return Jwts.builder()
                .subject(username)
                .claims(claims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(key)
                .compact();
    }

    public static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private static Claims extractAllClaims(String token) {
        return (Claims) Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parse(token)
                .getPayload();
    }

    public static String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public static Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public static boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public static boolean validateToken(String token, User user) {
        String username = extractUsername(token);
        return username.equals(user.getUsername()) && isTokenExpired(token);
    }

}
