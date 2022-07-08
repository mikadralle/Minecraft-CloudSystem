package de.leantwi.cloudsystem.api.gameserver.groups;

import de.leantwi.cloudsystem.api.database.mongodb.DBDocument;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class GroupDB implements DBDocument {
    private String groupName;
    private List<SubGroupDB> subGroupDBList = new ArrayList<>();

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
                SubGroupDB subGroupDB = new SubGroupDB(groupName);
                subGroupDB.fetch(subGroupDocument);
                this.subGroupDBList.add(subGroupDB);


            });

        }

    }
}
