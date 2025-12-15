package com.example.demo;

import java.util.ArrayList;

public class StarModel {

    private ArrayList<Groupable> items =  new ArrayList<>();
    private ArrayList<iModelListener> subs = new ArrayList<>();

    public StarModel() {
        //stars.add(new Star(750, 450, 200, 200, 0));
    }
    public ArrayList<Groupable> getStars() {
        return items;
    }

    public void addSub(iModelListener sub){
        subs.add(sub);
    }

    public void notifySubs(){
        for(iModelListener sub : subs){
            sub.modelChanged();
        }
    }

    public ArrayList<Groupable> getItems() {
        return items;
    }
    public void addItem(Groupable item){
        items.add(item);
    }
    public void removeItem(Groupable item){
        items.remove(item);
    }

    //get item with id
    public Groupable getIDitem(int id){
        for(Groupable item : items){
            if (item.getID() == id){
                return item;
            }
            if (item instanceof StarGroup){
                Groupable found = checkgroup((StarGroup) item, id); // check revursively
                if(found != null){
                    return found;
                }
            }
        }
        return null;
    }

    /// // REFACTOR THIS, i can just call this fucntion above ^^
    private Groupable checkgroup(StarGroup group, int id){
        for(Groupable child : group.getChildren()){
            if(child.getID() == id){return child;}
            if (child instanceof StarGroup){
                Groupable found = checkgroup((StarGroup) child, id);
                if(found != null){return found;}

            }
        }
        return null;
    }

    public StarGroup groupItems(ArrayList<Groupable> selection){
        if(selection.isEmpty() || selection.size() < 2){
            return null;
        }
        double left = Double.MAX_VALUE;
        double right = -Double.MAX_VALUE;
        double top  = Double.MAX_VALUE;
        double bottom = -Double.MAX_VALUE;

        for (Groupable item : selection) {
            left = Math.min(left, item.getLeft());
            right = Math.max(right, item.getRight());
            top = Math.min(top, item.getTop());
            bottom = Math.max(bottom, item.getBottom());

        }
        StarGroup group = new StarGroup();
        group.setX((left+right)/2.0);
        group.setY((top+bottom)/2.0);
        //group.setAngle(90);
        group.setAngle(0);
        for(Groupable item : selection){
            items.remove(item);
            group.addChild(item);
        }
        items.add(group);
        notifySubs();
        return group;
    }

    public void ungroup(StarGroup group){
        if(group == null){return;}

        items.remove(group);
        for(Groupable item : group.getChildren()){
            items.add(item);
        }
        notifySubs();
    }



}
