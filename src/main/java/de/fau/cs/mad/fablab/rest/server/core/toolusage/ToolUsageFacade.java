package de.fau.cs.mad.fablab.rest.server.core.toolusage;

import de.fau.cs.mad.fablab.rest.core.ToolUsage;

import java.util.List;

/**
 * Facade to handle machine/tool usage
 */
public class ToolUsageFacade {

    ToolUsageDAO mDAO;

    public ToolUsageFacade (ToolUsageDAO dao) {
        this.mDAO = dao;
    }

    public ToolUsage create(ToolUsage usage) {
        return mDAO.create(usage);
    }

    public List<ToolUsage> getUsageForTool(long id) {
        return mDAO.getUsageForTool(id);
    }

    public ToolUsage getUsage(long id) {
        return mDAO.getUsage(id);
    }

    public boolean removeUsage(long toolId, long usageId) {
        return mDAO.delete(toolId, usageId);
    }

    public void clearUsageForTool(long toolId) {
        mDAO.clearUsageForTool(toolId);
    }
}
