package de.leantwi.cloudsystem.api.database.mongodb;

import org.bson.Document;

public interface DBDocument {

    public abstract Document create();

    public abstract void fetch(Document document);

}
