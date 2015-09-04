package de.fau.cs.mad.fablab.rest.server.core.toolusage;

import de.fau.cs.mad.fablab.rest.core.FabTool;
import de.fau.cs.mad.fablab.rest.core.ToolUsage;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * DAO to handle machine/tool usage.
 */
public class ToolUsageDAO extends AbstractDAO<ToolUsage> {

    private static final String TABLE_NAME = "ToolUsage";

    public ToolUsageDAO (SessionFactory factory) {
        super(factory);
    }

    ToolUsage create(ToolUsage usage) {

        // look for tool and connect it to Usage
        List<FabTool> toolList = super.currentSession().createQuery("FROM FabTool WHERE id = :tool_id")
                .setParameter("tool_id", usage.getToolId())
                .list();

        if (toolList.size() != 1)
            return null;

        usage.setTool(toolList.get(0));

        // persist!
        ToolUsage newUsage = persist(usage);

        // look for ancestor and set (me) new successor
        List<ToolUsage> usageList = super.currentSession().createQuery("FROM " + TABLE_NAME + " WHERE tool_id = :tool_id AND successor_id IS NULL AND id != :usage_id")
                .setParameter("tool_id", usage.getToolId())
                .setParameter("usage_id", newUsage.getId())
                .list();
        if (usageList.size() >= 1) {

            ToolUsage ancestor = usageList.get(0);
            ancestor.setSuccessor(newUsage);

            currentSession().update(ancestor);
        }

        return newUsage;
    }

    List<ToolUsage> getUsageForTool(long id) {
        return super.currentSession().
                createQuery("FROM " + TABLE_NAME + " WHERE tool_id = :toolId")
                .setParameter("toolId", id)
                .list();
    }

    ToolUsage getUsage(long id) {
        return get(id);
    }

    boolean delete(long toolId, long usageId) {
        ToolUsage usage = get(usageId);

        if (usage == null)
            return false;

        if (usage.getTool().getId() == toolId) {

                //find ancestor and set new successor
                List<ToolUsage> usageList = super.currentSession().createQuery("FROM " + TABLE_NAME + " WHERE tool_id = :tool_id AND successor_id = :successor_id")
                        .setParameter("tool_id", usage.getToolId())
                        .setParameter("successor_id", usage.getId())
                        .list();

                if (usageList.size() == 1) {
                    ToolUsage ancestor = usageList.get(0);

                    ancestor.setSuccessor(usage.getSuccessor());
                    currentSession().update(ancestor);
                }

            currentSession().delete(usage);
            return true;
        }

        return false;
    }

    void clearUsageForTool(long toolId) {
        super.currentSession().createQuery("DELETE FROM " + TABLE_NAME + " WHERE tool_id = :tool_id")
                .setParameter("tool_id", toolId)
                .executeUpdate();
    }
}
