package com.raf.rafnews.filter;

import com.raf.rafnews.resource.NewsResource;
import com.raf.rafnews.resource.UserResource;
import com.raf.rafnews.service.UserService;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.List;

@Provider
public class AuthFilter implements ContainerRequestFilter {

    @Inject
    private UserService userService;

    private static final String[] ALLOWED_PATHS = {
            "login", "allNews", "find", "byViews", "comments", "count", "search", "like", "mostreactedto", "similartags"
    };

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        if (!this.isAuthRequired(requestContext)) {
            return;
        }

        try {
            String token = requestContext.getHeaderString("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.replace("Bearer ", "");
            }

            if (!this.userService.isAuthorized(token, requestContext)) {
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            }
        } catch (Exception exception) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }

    private boolean isAuthRequired(ContainerRequestContext req) {
        String path = req.getUriInfo().getPath();

        for (String allowedPath : ALLOWED_PATHS) {
            if (path.contains(allowedPath)) {
                return false;
            }
        }

        List<Object> matchedResources = req.getUriInfo().getMatchedResources();
        for (Object matchedResource : matchedResources) {
            if (matchedResource instanceof NewsResource || matchedResource instanceof UserResource) {
                if (path.contains("category") || path.contains("tag")) continue;
                return true;
            }
        }

        return false;
    }
}
