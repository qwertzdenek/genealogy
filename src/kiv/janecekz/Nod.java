/*
 * Genealogy expert system.
 * Semestral work for the University of West Bohemia.
 * 
 * Written by Zdeněk Janeček, 2013
 * Share it freely under conditions of GNU GPL v3
 */

package kiv.janecekz;

public class Nod {
    private String name;
    private int id;
    private Nod mother;
    private Nod father;
    private boolean male;
    private Nod partner;

    public Nod(int id, String name, Nod mother, Nod father, boolean male,
            Nod partner) {
        super();
        this.name = name;
        this.id = id;
        this.mother = mother;
        this.father = father;
        this.male = male;
        this.partner = partner;
    }

    public Nod getPartner() {
        return partner;
    }

    public void setPartner(Nod partner) {
        this.partner = partner;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public Nod getMother() {
        return mother;
    }

    public Nod getFather() {
        return father;
    }

    public boolean isMale() {
        return male;
    }

    public boolean updateInfo(String name, Nod mother, Nod father, Nod partner) {
        if (this.name == null)
            this.name = name;
        else
            return false;
        
        if (this.mother == null)
            this.mother = mother;
        else
            return false;
        
        if (this.father == null)
            this.father = father;
        else
            return false;
        
        if (this.partner == null)
            this.partner = partner;
        else
            return false;
        
        return true;
    }
}
