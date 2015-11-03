package de.fau.cs.mad.fablab.rest.server.core;



import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

/**
 * Simple class to export database schema to a file
 */
public class SchemaExporter {

    private SchemaExport export;

    public SchemaExporter(Configuration configuration, String filename) {

        export = new SchemaExport(configuration);
        export.setOutputFile(filename);
        export.setDelimiter(";");
    }
    public void export() {
        export.create(true,false);
    }
}
