package de.fau.cs.mad.fablab.rest.server.resources;

import de.fau.cs.mad.fablab.rest.api.ToolUsageApi;
import de.fau.cs.mad.fablab.rest.core.ToolUsage;
import de.fau.cs.mad.fablab.rest.server.core.toolusage.ToolUsageFacade;
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
    public ToolUsage addUsage(long toolId, ToolUsage usage) {
        usage.setToolId(toolId);
        return mFacade.create(usage);
    }

    @Override
    @UnitOfWork
    public List<ToolUsage> getUsageForTool(long toolId) {
        return mFacade.getUsageForTool(toolId);
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
    public Response removeUsage(long toolId, long usageId) {

        if (mFacade.removeUsage(toolId, usageId)) {
            return Response.ok().build();
        }

        throw new Http404Exception("Usage not found");
    }

    @Override
    @UnitOfWork
    public Response removeUsagesForTool(long toolId) {
        mFacade.clearUsageForTool(toolId);

        return Response.ok().build();
    }

    @Override
    @UnitOfWork
    /**
     * @see de.fau.cs.mad.fablab.rest.server.core.toolusage.ToolUsageDAO#moveAfter(ToolUsage, ToolUsage)
     */
    public Response moveAfter(long toolId, long usageId, long afterId) {

        if (mFacade.moveAfter(toolId, usageId, afterId))
            return Response.ok().build();

        throw new Http404Exception("Usage not found.");
    }
}
