package me.jwhz.core.manager;

import me.jwhz.core.Core;
import me.jwhz.core.config.ConfigFile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class Manager<T extends ManagerObject> extends ConfigFile {

    protected ArrayList<T> list = new ArrayList<T>();

    public Core core = Core.getInstance();

    public Manager(String fileName) {
        super(fileName);
    }

    public int getIndex(T t) {

        for (int i = 0; i < list.size(); i++)
            if (list.get(i).equals(t))
                return i;

        return -1;

    }

    public boolean contains(Object identifier) {

        for (T t : list)
            try {
                if (t.getClass().getMethod("getIndentifier").invoke(t).equals(identifier))
                    return true;
            } catch (Exception ignored) {
            }

        return false;

    }

    public boolean remove(Object identifier) {

        boolean removed = false;

        Iterator<T> iterator = list.iterator();

        while (iterator.hasNext())
            try {

                T next = iterator.next();

                if (next.getClass().getMethod("getIdentifier").invoke(next).equals(identifier)) {

                    iterator.remove();
                    removed = true;
                    break;

                }
            } catch (Exception ignored) {
            }

        return removed;

    }

    public T get(Object identifier) {

        for (T t : list)
            try {
                if (t.getClass().getMethod("getIdentifier").invoke(t).equals(identifier))
                    return t;
            } catch (Exception ignored) {
            }

        return null;

    }

    public List<T> getList(){

        return list;

    }

    public void add(Object obj){}

}