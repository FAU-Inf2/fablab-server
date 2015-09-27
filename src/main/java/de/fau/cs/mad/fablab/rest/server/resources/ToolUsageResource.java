package de.fau.cs.mad.fablab.rest.server.resources;

import de.fau.cs.mad.fablab.rest.api.ToolUsageApi;
import de.fau.cs.mad.fablab.rest.core.Roles;
import de.fau.cs.mad.fablab.rest.core.ToolUsage;
import de.fau.cs.mad.fablab.rest.core.User;
import de.fau.cs.mad.fablab.rest.server.core.toolusage.ToolUsageFacade;
import de.fau.cs.mad.fablab.rest.server.exceptions.Http401Exception;
import de.fau.cs.mad.fablab.rest.server.exceptions.Http404Exception;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Resource to handle registration/administration for machine/tool usage
 */
public class ToolUsageResource implements ToolUsageApi {
    private ToolUsageFacade mFacade;

    public ToolUsageResource (ToolUsageFacade facade) {
        mFacade = facade;
    }

    @Override
    @UnitOfWork
    public ToolUsage addUsage(User user, String token, long toolId, ToolUsage usage) {
        usage.setToolId(toolId);

        if (user == null && (token == null || token.isEmpty()))
            throw new Http401Exception("Authentication or token required.");

        else if (user != null) {
            usage.setUser(user.getUsername());
        }
        else if (!token.isEmpty()) {
            usage.setToken(token);
        }
        else {
            throw new Http401Exception("Authentication or token required.");
        }

        return mFacade.create(usage);
    }

    @Override
    @UnitOfWork
    public List<ToolUsage> getUsageForTool(long toolId) {

        List<ToolUsage> toolUsages = mFacade.getUsageForTool(toolId);

        return toolUsages;
    }

    @Override
    @UnitOfWork
    public ToolUsage getUsage(long toolId, long usageId) {
        ToolUsage usage = mFacade.getUsage(toolId, usageId);

        if (usage != null)
            return usage;

        throw new Http404Exception("Usage not found.");
    }

    @Override
    @UnitOfWork
    public Response removeUsage(User user, String token, long toolId, long usageId) {

        if (user == null && (token == null || token.isEmpty()))
            throw new Http401Exception("Authentication or token required.");

        ToolUsage usage = mFacade.getUsage(toolId, usageId);
        if (usage == null)
            throw new Http404Exception("Usage not found");

        if (user != null) {
            if (!usage.getUser().equals(user.getUsername()) && !user.hasRole(Roles.ADMIN))
                throw new Http401Exception("Username not identical.");
        }
        else if (!token.isEmpty()) {
            if (!usage.getToken().equals(token))
                throw new Http401Exception("Token not identical.");
        }
        else {
            throw new Http401Exception("Authentication or token required.");
        }

        if (mFacade.removeUsage(toolId, usageId)) {
            return Response.ok().build();
        }

        throw new Http404Exception("Usage not found");
    }

    @Override
    @UnitOfWork
    public Response removeUsagesForTool(User user, long toolId) {
        if (!user.hasRole(Roles.ADMIN)){
            throw new Http401Exception("Only for admins.");
        }

        mFacade.clearUsageForTool(toolId);

        return Response.ok().build();
    }

    @Override
    @UnitOfWork
    /**
     * @see de.fau.cs.mad.fablab.rest.server.core.toolusage.ToolUsageDAO#moveAfter(ToolUsage, ToolUsage)
     */
    public Response moveAfter(User user, long toolId, long usageId, long afterId) {

        if (!user.hasRole(Roles.ADMIN)) {
            throw new Http401Exception("Only for admins.");
        }

        if (mFacade.moveAfter(toolId, usageId, afterId))
            return Response.ok().build();

        throw new Http404Exception("Usage not found.");
    }
}
