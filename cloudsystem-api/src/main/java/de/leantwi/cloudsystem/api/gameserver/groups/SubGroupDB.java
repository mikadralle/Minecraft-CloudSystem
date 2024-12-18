package de.leantwi.cloudsystem.api.gameserver.groups;

import de.leantwi.cloudsystem.api.database.mongodb.DBDocument;
import de.leantwi.cloudsystem.api.gameserver.server.ServerDB;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bson.Document;

@Getter
@Setter
@RequiredArgsConstructor
public class SubGroupDB implements DBDocument {

    private final String mainGroupName;
    private String subGroupName;
    private ServerDB serverDB = new ServerDB();
    //private List<SessionServer> startSessionServerList = new ArrayList<>();
    //private List<SessionServer> sessionServerList = new ArrayList<>();


    @Override
    public Document create() {
        return new Document("subGroupName", this.subGroupName).append("server", this.serverDB.create());
    }

    @Override
    public void fetch(Document document) {
        if (document.containsKey("subGroupName")) {
            this.subGroupName = document.getString("subGroupName");
        }
        if (document.containsKey("server")) {
            this.serverDB.fetch(document.get("server", Document.class));
        }

        //Master.getInstance().getCore().getGroupDBStringMap().put(this.groupDB, this.subGroupName);
      //  MasterBootstrap.getInstance().getCore().getSubGroupDBString().put(this.subGroupName, this);
    }
/*
    public Integer getMissServerAmount() {
        //return this.serverDB.getMinOnlineAmount() - (this.startSessionServerList.size() + this.sessionServerList.size());
    }


 */
   /*
    public List<SessionServer> getAllServerList() {
        List<SessionServer> list = new ArrayList<>();
        list.addAll(this.startSessionServerList);
        list.addAll(this.sessionServerList);
        return list;
    }
    
    */

}
