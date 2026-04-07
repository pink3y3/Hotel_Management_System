package com.hotel.util;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

/**
 * Generic DataStore<T> — stores, retrieves, and persists any serializable entity.
 *
 * Demonstrates:
 *   - Generic class (T extends Serializable)
 *   - ArrayList and Iterator usage
 *   - File-based serialization for persistent storage
 */
public class DataStore<T extends Serializable> implements Serializable {
    private static final long serialVersionUID = 100L;

    private ArrayList<T> items;
    private final String filePath;

    public DataStore(String filePath) {
        this.filePath = filePath;
        this.items = load();
    }

    // ── CRUD Operations using ArrayList ─────────────────────────────────────

    public void add(T item) {
        items.add(item);
        save();
    }

    public void remove(T item) {
        items.remove(item);
        save();
    }

    /** Remove items matching a predicate — demonstrates Iterator usage */
    public void removeIf(Predicate<T> predicate) {
        Iterator<T> iterator = items.iterator();
        while (iterator.hasNext()) {
            T item = iterator.next();
            if (predicate.test(item)) {
                iterator.remove();  // Safe removal during iteration
            }
        }
        save();
    }

    public void update(T item) {
        // Item is already in the list (reference update); just save
        save();
    }

    public List<T> getAll() { return new ArrayList<>(items); }

    public int size() { return items.size(); }

    public boolean contains(T item) { return items.contains(item); }

    /** Generic filter — returns a new list of items matching the predicate */
    public List<T> filter(Predicate<T> predicate) {
        List<T> result = new ArrayList<>();
        for (T item : items) {
            if (predicate.test(item)) {
                result.add(item);
            }
        }
        return result;
    }

    // ── Persistence ──────────────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    private ArrayList<T> load() {
        File file = new File(filePath);
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (ArrayList<T>) ois.readObject();
        } catch (Exception e) {
            System.err.println("DataStore: could not load " + filePath + " — " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void save() {
        File file = new File(filePath);
        file.getParentFile().mkdirs();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(items);
        } catch (IOException e) {
            System.err.println("DataStore: could not save " + filePath + " — " + e.getMessage());
        }
    }

    public void saveAll(List<T> newItems) {
        items = new ArrayList<>(newItems);
        save();
    }
}
