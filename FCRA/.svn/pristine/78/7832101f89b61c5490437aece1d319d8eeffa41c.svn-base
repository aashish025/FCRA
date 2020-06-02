package web;

import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import utilities.TokenException;

public class TokenController {
	protected static synchronized Boolean isTokenValid(String tokenKey, String requestToken) throws Exception {
        String sessionToken = getSessionToken(tokenKey);
        if (requestToken == null) {
            // The hidden field wasn't provided
            throw new TokenException("Missing synchronizer token in request");
        }
        if (sessionToken == null) {
            // The session has lost the token.
            throw new TokenException("Missing synchronizer token in session");
        }
        if (sessionToken.equals(requestToken)) {
            // Accept the submission and increment the token so this form can't
            // be submitted again ...
        	generateAndSaveToken(tokenKey);
            return true;
        }
        return false;
	}
	
	public static String nextToken() {
        long seed = System.currentTimeMillis(); 
        Random r = new Random();
        r.setSeed(seed);
        return Long.toString(seed) + Long.toString(Math.abs(r.nextLong()));
    }
	
	public static String getSessionToken(String tokenKey) {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpSession session = attr.getRequest().getSession(true);
        String sessionToken = (String)session.getAttribute(tokenKey);
        
        return sessionToken;
    }

	public static String generateAndSaveToken(String tokenKey) {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpSession session = attr.getRequest().getSession(true);
        String sessionToken = nextToken();
        session.setAttribute(tokenKey, sessionToken);
        return sessionToken;
    }
}
