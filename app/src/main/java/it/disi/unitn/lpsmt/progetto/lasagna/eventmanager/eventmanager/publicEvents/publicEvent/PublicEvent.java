package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.publicEvents.publicEvent;

public class PublicEvent {
    private String id, eventid, self, name, category;

    public PublicEvent(String id, String evId, String s, String n, String c) {
        this.id = id;
        eventid = evId;
        self = s;
        name = n;
        category = c;
    }

    public String getString(String field) throws Exception {
        switch(field) {
            case "id": {
                return id;
            }
            case "eventid": {
                return eventid;
            }
            case "self": {
                return self;
            }
            case "name": {
                return name;
            }
            case "category": {
                return category;
            }
            default: {
                throw new Exception("No field using this name.");
            }
        }
    }
}
