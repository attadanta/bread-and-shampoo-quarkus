package net.mischung.breadandshampoo.rest;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import net.mischung.breadandshampoo.model.ListManagementException;
import net.mischung.breadandshampoo.service.ItemDoesNotExistException;
import net.mischung.breadandshampoo.service.WrongItemOwnerException;

public class ListManagementExceptionMapper implements ExceptionMapper<ListManagementException> {

    @Override
    public Response toResponse(ListManagementException exception) {
        if (exception instanceof ItemDoesNotExistException || exception instanceof WrongItemOwnerException) {
            // Due to time constraints, we don't implement an error representation. The response
            // code is intentional, however. Since list items are globally unique, we don't want
            // to reveal the fact that someone has accessed an item of a different user.
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

}
