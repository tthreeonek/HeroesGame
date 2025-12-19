package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.*;

public class GeneratePresetImpl implements GeneratePreset {

    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {
        // Создаем список для юнитов армии компьютера
        List<Unit> computerUnits = new ArrayList<>();
        int currentPoints = 0;

        // Счетчики для порядковых номеров юнитов разных типов
        int archerCounter = 1;
        int knightCounter = 1;
        int swordsmanCounter = 1;
        int spearmanCounter = 1;

        // Счетчики количества юнитов каждого типа
        int archerCount = 0;
        int knightCount = 0;
        int swordsmanCount = 0;
        int spearmanCount = 0;

        Random random = new Random();

        // Координаты для размещения юнитов
        int xCoordinate;
        int yCoordinate;

        // Сортируем список юнитов по эффективности (атака/стоимость и здоровье/стоимость)
        unitList.sort(Comparator.comparingDouble(unit -> ((Unit) unit).getBaseAttack() / (double) ((Unit) unit).getCost()
                + ((Unit) unit).getHealth() / (double) ((Unit) unit).getCost()).reversed());

        // Проходим по всем типам юнитов и добавляем их в армию
        for (Unit unit : unitList) {
            int maxUnitsForType = Math.min(11, maxPoints / unit.getCost());
            for (int i = 0; i < maxUnitsForType; i++) {
                if (currentPoints + unit.getCost() > maxPoints) {
                    break;
                }

                // Генерируем случайные координаты X (от 0 до 2) и Y (от 0 до 20)
                xCoordinate = random.nextInt(3);
                yCoordinate = random.nextInt(21);

                // Создаем нового юнита с использованием конструктора
                String unitName;
                switch (unit.getUnitType()) {
                    case "Archer":
                        unitName = unit.getUnitType() + " " + archerCounter++;
                        archerCount++;
                        break;
                    case "Knight":
                        unitName = unit.getUnitType() + " " + knightCounter++;
                        knightCount++;
                        break;
                    case "Swordsman":
                        unitName = unit.getUnitType() + " " + swordsmanCounter++;
                        swordsmanCount++;
                        break;
                    case "Spearman":
                        unitName = unit.getUnitType() + " " + spearmanCounter++;
                        spearmanCount++;
                        break;
                    default:
                        unitName = unit.getUnitType() + " 1";
                }

                Unit newUnit = new Unit(
                        unitName,                          // Имя юнита
                        unit.getUnitType(),                // Тип юнита
                        unit.getHealth(),                  // Здоровье
                        unit.getBaseAttack(),              // Атака
                        unit.getCost(),                    // Стоимость
                        unit.getAttackType(),              // Тип атаки
                        unit.getAttackBonuses(),           // Бонусы атаки
                        unit.getDefenceBonuses(),          // Бонусы защиты
                        xCoordinate,                       // Координата X
                        yCoordinate                        // Координата Y
                );

                // Добавляем юнита в армию
                computerUnits.add(newUnit);
                currentPoints += unit.getCost();
            }
        }

        // Логирование результатов
        System.out.println("Army generated:");
        System.out.println("Total units: " + computerUnits.size());
        System.out.println("Archer count: " + archerCount);
        System.out.println("Knight count: " + knightCount);
        System.out.println("Swordsman count: " + swordsmanCount);
        System.out.println("Pikeman count: " + spearmanCount);
        System.out.println("Total points used: " + currentPoints);

        // Возвращаем сформированную армию
        return new Army(computerUnits);
    }
}