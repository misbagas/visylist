package org.example;

import org.example.entity.User;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @GET
    public List<User> getAllUsers() {
        return User.listAll();
    }

    @POST
    @Transactional
    public Response createUser(User user) {
        if (user == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Payload user kosong.").build();
        }

        if (user.full_name == null || user.full_name.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("full_name wajib diisi.").build();
        }

        if (user.username == null || user.username.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("username wajib diisi.").build();
        }

        if (user.password_hash == null || user.password_hash.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("password_hash wajib diisi.").build();
        }

        user.full_name = user.full_name.trim();
        user.username = user.username.trim();
        user.password_hash = user.password_hash.trim();

        try {
            user.persistAndFlush();
            return Response.status(Response.Status.CREATED).entity(user).build();
        } catch (PersistenceException exception) {
            final String detail = findRootMessage(exception);
            if (isDuplicateError(detail)) {
                return Response.status(Response.Status.CONFLICT).entity("Username sudah digunakan.").build();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Gagal menyimpan user ke Oracle: " + detail)
                .build();
        }
    }

    private static String findRootMessage(Throwable throwable) {
        Throwable current = throwable;
        while (current.getCause() != null) {
            current = current.getCause();
        }
        String message = current.getMessage();
        return message == null || message.isBlank() ? current.getClass().getSimpleName() : message;
    }

    private static boolean isDuplicateError(String message) {
        if (message == null) {
            return false;
        }
        return message.contains("ORA-00001")
            || message.toLowerCase().contains("unique")
            || message.toLowerCase().contains("duplicate");
    }
}