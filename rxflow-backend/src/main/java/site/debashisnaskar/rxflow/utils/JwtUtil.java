package site.debashisnaskar.rxflow.utils;

import io.github.cdimascio.dotenv.Dotenv;
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

    private static final Dotenv dotenv = Dotenv.load();

    public static final String SECRET = dotenv.get("JWT_SECRET");
    private static final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());
    private static final long EXPIRATION = 1000 * 60 * Long.parseLong(dotenv.get("JWT_EXPIRY","1"));


    public static String generateToken(User user) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole());
        return createToken(claims,user.getUsername());
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
        return username.equals(user.getUsername()) && !isTokenExpired(token);
    }

}
