package com.narxoz.rpg.adapter;

import com.narxoz.rpg.battle.Combatant;
import com.narxoz.rpg.enemy.Enemy;

public class EnemyCombatantAdapter implements Combatant {
    private final Enemy enemy;

    public EnemyCombatantAdapter(Enemy enemy) {
        this.enemy = enemy;
    }

    @Override
    public String getName() {
        return enemy.getTitle();
    }

    @Override
    public int getAttackPower() {
        return enemy.getDamage();
    }

    @Override
    public void takeDamage(int amount) {
        enemy.applyDamage(amount);
    }

    @Override
    public boolean isAlive() {
        return !enemy.isDefeated();
    }
}