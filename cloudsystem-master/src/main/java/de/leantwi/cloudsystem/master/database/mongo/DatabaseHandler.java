package de.leantwi.cloudsystem.master.database.mongo;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import de.leantwi.cloudsystem.master.MasterBootstrap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.bson.Document;

@Getter
public class DatabaseHandler {


  public MongoDatabase getMongoDatabase() {
    return MasterBootstrap.getInstance().getCloudSystemAPI().getMongoDBConnectorAPI().getMongoDatabase();
  }

  public MongoCollection<Document> getCollection(String name) {
    return getMongoDatabase().getCollection(name);
  }

  public FindIterable<Document> getIterable(String collection) {
    return getCollection(collection).find();
  }

  public FindIterable<Document> getIterable(String collection, Document query) {
    return getCollection(collection).find(query);
  }

  public FindIterable<Document> getIterable(String collection, String key, Object value) {
    return getIterable(collection, new Document(key, value));
  }

  public FindIterable<Document> getIterable(String collection, Map<String, Object> fields) {
    return getIterable(collection, new Document(fields));
  }

  public Document getDocument(String collection, Document query) {
    return getCollection(collection).find(query).first();
  }

  public Document getDocument(String collection, String key, Object value) {
    return getDocument(collection, new Document(key, value));
  }

  public Document getDocument(String collection, Map<String, Object> fields) {
    return getDocument(collection, new Document(fields));
  }

  public List<Document> getDocumentList(String collection, Document query) {
    final List<Document> documents = new ArrayList<>();
    getIterable(collection, query).forEach((Block<Document>) documents::add);
    return documents;
  }

  public List<Document> getDocumentList(String collection, String key, Object value) {
    return getDocumentList(collection, new Document(key, value));
  }

  public List<Document> getDocumentList(String collection, Map<String, Object> fields) {
    return getDocumentList(collection, new Document(fields));
  }

  public Boolean contains(Document document, String collection) {
    return getCollection(collection).countDocuments(document) != 0;
  }

  public Boolean contains(String key, Object value, String collection) {
    return contains(new Document(key, value), collection);
  }

  public Boolean contains(Map<String, Object> fields, String collection) {
    return contains(new Document(fields), collection);
  }

  public void insert(String collection, Document document) {
    getCollection(collection).insertOne(document);
  }

  public void insert(String collection, List<? extends Document> documents) {
    getCollection(collection).insertMany(documents);
  }

  public void delete(String collection, Document document) {
    getCollection(collection).deleteOne(document);
  }

  public void delete(String collection, String key, Object value) {
    delete(collection, new Document(key, value));
  }

  public void delete(String collection, Map<String, Object> fields) {
    delete(collection, new Document(fields));
  }

  public void delete(final String collection, List<? extends Document> documents) {
    for (Document document : documents) {
      delete(collection, document);
    }
  }

  public void update(Document query, Document update, String collection) {
    getCollection(collection).updateOne(query, update);
  }

  public void update(String database, Document query, Document update, String collection) {
    MasterBootstrap.getInstance().getCloudSystemAPI().getMongoDBClient().getDatabase(database).getCollection(collection).updateOne(query, update);
  }

  public void update(String key, Object value, String operator, String field, Object into, String collection) {
    update(new Document(key, value), new Document(operator, new Document(field, into)), collection);
  }

  public boolean collectionExists(final String collectionName) {
    return getMongoDatabase().listCollectionNames()
        .into(new ArrayList<>()).contains(collectionName);
  }

  public boolean collectionExists(final String database, final String collectionName) {
    return MasterBootstrap.getInstance().getCloudSystemAPI().getMongoDBClient().getDatabase(database).listCollectionNames()
        .into(new ArrayList<>()).contains(collectionName);
  }


}
