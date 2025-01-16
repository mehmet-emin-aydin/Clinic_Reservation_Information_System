package MehmetEminAydin;

import java.io.Serializable;
import java.util.LinkedList;

public class Hospital implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int id;
    private String name;
    private LinkedList<Section> sections;
    
    public Hospital(int id, String name) {
        this.id = id;
        this.name = name;
        this.sections = new LinkedList<>();
    }
    
    public Section getSection(int id) throws IDException {
        for (Section section : sections) {
            if (section.getId() == id) {
                return section;
            }
        }
        throw new IDException("Bölüm bulunamadı: " + id);
    }
    
    public Section getSection(String name) throws IDException {
        for (Section section : sections) {
            if (section.getName().equals(name)) {
                return section;
            }
        }
        throw new IDException("Bölüm bulunamadı: " + name);
    }
    
    public void addSection(Section section) {
        sections.add(section);
    }
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public LinkedList<Section> getSections() {
        return sections;
    }
    
    public void removeSection(int sectionId) {
        sections.removeIf(s -> s.getId() == sectionId);
    }
    
    @Override
    public String toString() {
        return getName();
    }
} 