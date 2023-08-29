package com.raf.rafnews.resource;

import com.raf.rafnews.entities.User;
import com.raf.rafnews.request.LoginRequest;
import com.raf.rafnews.service.UserService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/users")
public class UserResource {
    @Inject
    private UserService userService;

    @GET
    @Path("/count")
    public int getNewsCount(){
        return this.userService.getCount();
    }

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@Valid LoginRequest loginRequest){
        Map<String, String> response = new HashMap<>();
        String jwt = this.userService.login(loginRequest.getEmail(), loginRequest.getPassword());
        if (jwt == null) {
            response.put("message", "Incorrect username or password.");
            return Response.status(422, "Unprocessable Entity").entity(response).build();
        }
        response.put("jwt", jwt);

        return Response.ok(response).build();
    }

    @GET
    @Path("/allUsers")
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> allUsers(
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("perPage") @DefaultValue("10") int perPage
    ) {
        return this.userService.allUsers(page, perPage);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> allUsers(){
        return this.userService.allUsers();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public User addUser(User user){
        return this.userService.addUser(user);
    }

    @GET
    @Path("/find/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public User findUser(@PathParam("email") String email) {
        return this.userService.findUser(email);
    }

    @DELETE
    @Path("/delete/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteUser(@PathParam("email") String email){
        this.userService.deleteUser(email);
    }

    @GET
    @Path("/status/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public void changeUserActivity(@PathParam("email") String email) {
        this.userService.changeUserActivity(email);
    }

    @POST
    @Path("/update/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public User updateUser(User user, @PathParam("email") String email){
        return this.userService.updateUser(user, email);
    }

    @GET
    @Path("/id/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public User findUserById(@PathParam("id") Integer id){
        return this.userService.findUserById(id);
    }

    @POST
    @Path("/updateid/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public User updateUserById(User user, @PathParam("id") Integer id){
        return this.userService.updateUserById(user, id);
    }
}
