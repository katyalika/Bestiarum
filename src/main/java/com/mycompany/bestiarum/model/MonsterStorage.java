package com.mycompany.bestiarum.model;

import java.io.File;
import java.util.*;

/**
 *
 * @author lihac
 */
public class MonsterStorage {
    private final List<Monster> monsters = new ArrayList<>();
    private final Map<String, List<Monster>> monstersBySource = new HashMap<>();
    private final Map<File, List<Monster>> monstersByFile = new HashMap<>();

    public void addMonster(Monster monster, File sourceFile) {
        if (monster != null && sourceFile != null) {
            monsters.add(monster);
            monstersBySource.computeIfAbsent(monster.getSource(), k -> new ArrayList<>()).add(monster);
            monstersByFile.computeIfAbsent(sourceFile, k -> new ArrayList<>()).add(monster);
        }
    }

    public List<Monster> getMonstersByFile(File file) {
        return Collections.unmodifiableList(monstersByFile.getOrDefault(file, Collections.emptyList()));
    }

    public List<Monster> getMonsters() {
        return Collections.unmodifiableList(monsters);
    }

    public Optional<Monster> getMonsterById(UUID id) {
        return monsters.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst();
    }

    public List<Monster> getMonstersBySource(String source) {
        return Collections.unmodifiableList(monstersBySource.getOrDefault(source, Collections.emptyList()));
    }

    public boolean updateMonster(UUID id, Monster newData) {
        Optional<Monster> existing = getMonsterById(id);
        if (existing.isPresent()) {
            Monster monster = existing.get();
            String originalSource = monster.getSource();
            monster.setName(newData.getName());
            monster.setDescription(newData.getDescription());
            monster.setDangerLevel(newData.getDangerLevel());
            monster.setHabitats(newData.getHabitats());
            monster.setFirstMentioned(newData.getFirstMentioned());
            monster.setVulnerabilities(newData.getVulnerabilities());
            monster.setImmunities(newData.getImmunities());
            monster.setActivity(newData.getActivity());
            monster.getParameters().clear();
            monster.getParameters().putAll(newData.getParameters());
            monster.getRecipe().clear();
            monster.getRecipe().addAll(newData.getRecipe());

            if (!originalSource.equals(newData.getSource())) {
                monstersBySource.get(originalSource).remove(monster);
                monstersBySource.computeIfAbsent(newData.getSource(), k -> new ArrayList<>()).add(monster);
                monster.setSource(newData.getSource());
            }

            return true;
        }
        return false;
    }

    public Set<String> getAllSources() {
        return monstersBySource.keySet();
    }
}
