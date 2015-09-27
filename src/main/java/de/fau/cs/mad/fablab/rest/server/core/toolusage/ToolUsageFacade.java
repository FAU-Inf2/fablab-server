package de.fau.cs.mad.fablab.rest.server.core.toolusage;

import de.fau.cs.mad.fablab.rest.core.FabTool;
import de.fau.cs.mad.fablab.rest.core.ToolUsage;

import java.util.ArrayList;
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

        //we need to sort list
        List<ToolUsage> list = mDAO.getUsageForTool(id);

        if (list.size() == 0)
            return list;

        List<ToolUsage> sortedList = new ArrayList<>();

        ToolUsage first = null;

        // find the one without ancestor
        for (ToolUsage usage : list) {

            boolean isAcestor = false;
            for (ToolUsage sub : list) {
                // usage is successor of sub
                if (usage.getId() == sub.getSuccessorId()) {
                    isAcestor = true;
                    break;
                }
            }

            if (!isAcestor) {
                first = usage;
                break;
            }
        }

        sortedList.add(first);
        while ((first = first.getSuccessor()) != null)
            sortedList.add(first);

        return sortedList;
    }

    public ToolUsage getUsage(long toolId, long usageId) {
        ToolUsage usage = mDAO.getUsage(usageId);

        if (usage == null || usage.getTool().getId() != toolId)
            return null;

        return usage;
    }

    public boolean removeUsage(long toolId, long usageId) {
        return mDAO.delete(toolId, usageId);
    }

    public void clearUsageForTool(long toolId) {
        mDAO.clearUsageForTool(toolId);
    }

    public boolean moveAfter(long toolId, long usageId, long afterId) {

        ToolUsage usage = mDAO.getUsage(usageId);
        ToolUsage afterUsage = mDAO.getUsage(afterId);

        if (usage == null ||
                afterUsage == null ||
                usage.getTool().getId() != toolId ||
                afterUsage.getTool().getId() != toolId)
        {
            return false;
        }

        return mDAO.moveAfter(usage, afterUsage);
    }

    public List<FabTool> getEnabledTools() {
        return mDAO.getEnabledTools();
    }

    public boolean setToolEnabled(long toolId, boolean flag) {
        return mDAO.setToolEnabled(toolId, flag);
    }

    public boolean checkIfToolsEnabled(long toolId){

        FabTool tool = mDAO.getTool(toolId);
        if (tool == null)
            return false;

        return tool.getEnabledForMachineUsage();
    }
}
