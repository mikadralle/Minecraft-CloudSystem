package de.leantwi.cloudsystem.proxy.database.mongodb;

import org.bson.Document;

public abstract class DBDocument {

    public abstract Document create();

    public abstract void fetch(Document document);

}

