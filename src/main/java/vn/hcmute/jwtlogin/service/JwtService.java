package vn.hcmute.jwtlogin.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

/** Tạo và kiểm tra JWT (HS256) bằng Nimbus JOSE + JWT. */
@Slf4j
@Service
public class JwtService {

    private static final String ISSUER = "jwt-login-app";

    private final byte[] secret;

    @Getter
    private final long expirationMs;

    public JwtService(@Value("${security.jwt.secret-key}") String secretKey,
                      @Value("${security.jwt.expiration-time}") long expirationMs) {
        this.secret = Base64.getDecoder().decode(secretKey);
        if (secret.length < 32) {
            throw new IllegalStateException("security.jwt.secret-key phải >= 32 byte (256 bit) sau khi giải mã Base64");
        }
        this.expirationMs = expirationMs;
    }

    public String generateToken(UserDetails user) {
        Date now = new Date();
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .issuer(ISSUER)
                .subject(user.getUsername())
                .issueTime(now)
                .expirationTime(new Date(now.getTime() + expirationMs))
                .jwtID(UUID.randomUUID().toString())
                .claim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .build();

        SignedJWT jwt = new SignedJWT(
                new JWSHeader.Builder(JWSAlgorithm.HS256).type(JOSEObjectType.JWT).build(), claims);
        try {
            jwt.sign(new MACSigner(secret));
        } catch (JOSEException e) {
            throw new IllegalStateException("Không thể ký JWT", e);
        }
        return jwt.serialize();
    }

    /**
     * Kiểm tra chữ ký, thuật toán, issuer và thời hạn.
     *
     * @return username (subject) nếu token hợp lệ, ngược lại Optional.empty()
     */
    public Optional<String> validateAndGetUsername(String token) {
        try {
            SignedJWT jwt = SignedJWT.parse(token);

            if (!JWSAlgorithm.HS256.equals(jwt.getHeader().getAlgorithm())) {
                return Optional.empty();
            }
            if (!jwt.verify(new MACVerifier(secret))) {
                log.debug("JWT signature không hợp lệ");
                return Optional.empty();
            }
            JWTClaimsSet claims = jwt.getJWTClaimsSet();
            if (!ISSUER.equals(claims.getIssuer())) {
                return Optional.empty();
            }
            Date exp = claims.getExpirationTime();
            if (exp == null || exp.before(new Date())) {
                log.debug("JWT đã hết hạn");
                return Optional.empty();
            }
            return Optional.ofNullable(claims.getSubject());
        } catch (ParseException | JOSEException e) {
            log.debug("JWT không hợp lệ: {}", e.getMessage());
            return Optional.empty();
        }
    }
}
