package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;

import java.util.*;

public class SimulateBattleImpl implements SimulateBattle {
    private PrintBattleLog printBattleLog; // Позволяет логировать. Использовать после каждой атаки юнита

    /*public SimulateBattleImpl(PrintBattleLog printBattleLog) {
        this.printBattleLog = printBattleLog;
    }*/

    @Override
    public void simulate(Army playerArmy, Army computerArmy) {
        // Получаем списки юнитов обеих армий
        List<Unit> playerUnits = new ArrayList<>(playerArmy.getUnits());
        List<Unit> computerUnits = new ArrayList<>(computerArmy.getUnits());

        // Флаг завершения боя
        boolean battleOver = false;

        while (!battleOver) {
            // Фильтрация только живых юнитов
            playerUnits.removeIf(unit -> !unit.isAlive());
            computerUnits.removeIf(unit -> !unit.isAlive());

            // Проверка на завершение боя
            if (playerUnits.isEmpty() || computerUnits.isEmpty()) {
                battleOver = true;
                break;
            }

            // Сортировка по убыванию атаки
            List<Unit> allUnits = new ArrayList<>();
            allUnits.addAll(playerUnits);
            allUnits.addAll(computerUnits);
            allUnits.sort(Comparator.comparingInt(Unit::getBaseAttack).reversed());

            // Выполнение ходов
            for (Unit unit : allUnits) {
                if (!unit.isAlive()) continue; // Пропускаем мертвых юнитов

                try {
                    Unit target = unit.getProgram().attack(); // Атакуем цель
                    if (target != null) {
                        printBattleLog.printBattleLog(unit, target); // Логируем атаку
                    }
                } catch (InterruptedException e) {
                    System.err.println("Attack interrupted for unit: " + unit.getName());
                    Thread.currentThread().interrupt(); // Восстанавливаем статус прерывания
                }

                // Проверяем состояние армии после каждой атаки
                playerUnits.removeIf(u -> !u.isAlive());
                computerUnits.removeIf(u -> !u.isAlive());

                if (playerUnits.isEmpty() || computerUnits.isEmpty()) {
                    battleOver = true;
                    break;
                }
            }
        }

        // Лог завершения боя
        if (playerUnits.isEmpty()) {
            System.out.println("Computer wins the battle!");
        } else if (computerUnits.isEmpty()) {
            System.out.println("Player wins the battle!");
        }
    }
}