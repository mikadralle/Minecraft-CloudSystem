package de.leantwi.cloudsystem.master.database.group;

import de.leantwi.cloudsystem.master.database.mongo.DBDocument;
import de.leantwi.cloudsystem.master.MasterBootstrap;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

@Getter
@Setter

public class GroupDB extends DBDocument {

  private String groupName;
  private List<SubGroupDB> subGroupDBList = new ArrayList<>();

  public GroupDB() {
    MasterBootstrap.getInstance().getCore().getGroupsDBList().add(this);
  }

  @Override
  public Document create() {
    Document document = new Document();

    List<Document> documents = new ArrayList<>();
    this.subGroupDBList.forEach(subGroupDB -> documents.add(subGroupDB.create()));
    document.append("groupName", this.groupName).append("groups", documents);
    return document;
  }

  @Override
  public void fetch(Document document) {

    if (document.containsKey("groupName")) {
      this.groupName = document.getString("groupName");
    }
    if (document.containsKey("groups")) {

      List<Document> list = document.getList("groups", Document.class);
      list.forEach(subGroupDocument -> {
        SubGroupDB subGroupDB = new SubGroupDB(this);
        subGroupDB.fetch(subGroupDocument);
        this.subGroupDBList.add(subGroupDB);


      });

    }

  }
}
