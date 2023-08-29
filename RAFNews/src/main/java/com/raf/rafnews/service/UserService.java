package com.raf.rafnews.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.raf.rafnews.entities.User;
import com.raf.rafnews.entities.enums.Type;
import com.raf.rafnews.repository.user.UserRepository;
import org.apache.commons.codec.digest.DigestUtils;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import java.util.Date;
import java.util.List;

public class UserService {
    @Inject
    private UserRepository userRepository;

    public int getCount(){
        return this.userRepository.countAllUsers();
    }

    public List<User> allUsers(int page, int perPage) {
        int offset = (page - 1) * perPage;
        return this.userRepository.allUsersPaginated(offset, perPage);
    }

    public String login(String email, String password){
        String hashedPassword = DigestUtils.sha256Hex(password);
        User user = this.userRepository.findUser(email);
        if (user == null || !user.getPassword().equals(hashedPassword)) {
            return null;
        }

        Date issuedAt = new Date();
        Date expiresAt = new Date(issuedAt.getTime() + 24*60*60*1000);

        Algorithm algorithm = Algorithm.HMAC256("193bd2dd9a2f7032e322679cf643219d58fb835c87a8883a50d7154fcd921098");

        return JWT.create()
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiresAt)
                .withSubject(email)
                .withClaim("type", user.getType().getValue())
                .withClaim("status", user.getStatus().getValue())
                .withClaim("name", user.getFirst_name() + " " + user.getLast_name())
                .withClaim("user_id",user.getId())
                .sign(algorithm);
    }

    public boolean isAuthorized(String token, ContainerRequestContext containerRequestContext){
        Algorithm algorithm = Algorithm.HMAC256("193bd2dd9a2f7032e322679cf643219d58fb835c87a8883a50d7154fcd921098");
        JWTVerifier verifier = JWT.require(algorithm)
                .build();
        DecodedJWT jwt = verifier.verify(token);

        String username = jwt.getSubject();
        Type type = Type.fromString(jwt.getClaim("type").asString());
        User user = this.userRepository.findUser(username);

        if (user == null){
            return false;
        }

        if (containerRequestContext.getUriInfo().getPath().contains("users") && (type == null || !type.equals(Type.ADMIN))){
            return false;
        }

        return true;
    }
    public List<User> allUsers(){
        return this.userRepository.allUsers();
    }
    public User addUser(User user){
        return this.userRepository.addUser(user);
    }
    public User findUser(String email){
        return this.userRepository.findUser(email);
    }
    public void deleteUser(String email){
        this.userRepository.deleteUser(email);
    }
    public void changeUserActivity(String email){
        this.userRepository.changeUserActivity(email);
    }
    public User updateUser(User user, String email){
        return this.userRepository.updateUser(user, email);
    }
    public User findUserById(Integer id){ return this.userRepository.findUserById(id); }
    public User updateUserById(User user, Integer id){ return this.userRepository.updateUserById(user, id); }
}
