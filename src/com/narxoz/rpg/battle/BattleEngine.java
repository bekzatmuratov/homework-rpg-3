package com.narxoz.rpg.battle;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public final class BattleEngine {
    private static BattleEngine instance;
    private Random random = new Random(1L);

    private BattleEngine() { }

    public static BattleEngine getInstance() {
        if (instance == null) {
            instance = new BattleEngine();
        }
        return instance;
    }

    public BattleEngine setRandomSeed(long seed) {
        this.random = new Random(seed);
        return this;
    }

    public void reset() {
        this.random = new Random(1L);
    }

    public EncounterResult runEncounter(List<Combatant> teamA, List<Combatant> teamB) {
        Objects.requireNonNull(teamA, "teamA must not be null");
        Objects.requireNonNull(teamB, "teamB must not be null");

        // Copies: не ломаем списки, которые передали из Main
        List<Combatant> a = new ArrayList<>(teamA);
        List<Combatant> b = new ArrayList<>(teamB);
        a.removeIf(Objects::isNull);
        b.removeIf(Objects::isNull);

        EncounterResult result = new EncounterResult();

        if (a.isEmpty() || b.isEmpty()) {
            result.setRounds(0);
            result.setWinner(a.isEmpty() && b.isEmpty() ? "No contest" : (a.isEmpty() ? "Team B" : "Team A"));
            result.addLog("Battle cannot start: one (or both) team(s) is empty.");
            return result;
        }

        int rounds = 0;
        result.addLog("Battle started: Team A (" + a.size() + ") vs Team B (" + b.size() + ")");

        while (!a.isEmpty() && !b.isEmpty()) {
            rounds++;
            result.addLog("\n--- Round " + rounds + " ---");

            teamAttacks("Team A", a, "Team B", b, result);
            if (b.isEmpty()) break;

            teamAttacks("Team B", b, "Team A", a, result);
        }

        result.setRounds(rounds);
        result.setWinner(a.isEmpty() ? "Team B" : "Team A");
        result.addLog("\nBattle finished. Winner: " + result.getWinner());
        return result;
    }

    private void teamAttacks(
            String attackerTeamName,
            List<Combatant> attackers,
            String defenderTeamName,
            List<Combatant> defenders,
            EncounterResult result
    ) {
        for (int i = 0; i < attackers.size(); i++) {
            Combatant attacker = attackers.get(i);

            if (!attacker.isAlive()) {
                result.addLog(attackerTeamName + ": " + attacker.getName() + " cannot attack (defeated).");
                continue;
            }

            if (defenders.isEmpty()) return;

            int targetIndex = random.nextInt(defenders.size());
            Combatant target = defenders.get(targetIndex);

            int dmg = Math.max(0, attacker.getAttackPower());
            result.addLog(attackerTeamName + ": " + attacker.getName()
                    + " attacks " + defenderTeamName + ": " + target.getName()
                    + " for " + dmg + " damage.");

            target.takeDamage(dmg);

            if (!target.isAlive()) {
                result.addLog(defenderTeamName + ": " + target.getName() + " has been defeated!");
                defenders.remove(targetIndex);
            }
        }
    }
}