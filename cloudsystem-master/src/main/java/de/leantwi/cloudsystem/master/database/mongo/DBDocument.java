package de.leantwi.cloudsystem.master.database.mongo;

import org.bson.Document;

public abstract class DBDocument {

  public abstract Document create();

  public abstract void fetch(Document document);

}