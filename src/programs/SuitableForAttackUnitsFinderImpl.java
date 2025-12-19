package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.ArrayList;
import java.util.List;

public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {

    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        // Список для подходящих юнитов
        List<Unit> suitableUnits = new ArrayList<>();

        // Проходим по каждому ряду
        for (List<Unit> row : unitsByRow) {
            // Проверяем каждый юнит в ряду
            for (int i = 0; i < row.size(); i++) {
                Unit currentUnit = row.get(i);

                if (!currentUnit.isAlive()) {
                    continue; // Пропускаем мертвых юнитов
                }

                // Проверяем, закрыт ли юнит другим юнитом
                boolean isBlocked = false;
                if (isLeftArmyTarget) {
                    // Проверяем, закрыт ли юнит слева (для армии игрока)
                    for (int j = 0; j < i; j++) {
                        if (row.get(j).isAlive()) {
                            isBlocked = true;
                            break;
                        }
                    }
                } else {
                    // Проверяем, закрыт ли юнит справа (для армии компьютера)
                    for (int j = i + 1; j < row.size(); j++) {
                        if (row.get(j).isAlive()) {
                            isBlocked = true;
                            break;
                        }
                    }
                }

                // Если юнит не заблокирован, добавляем его в список
                if (!isBlocked) {
                    suitableUnits.add(currentUnit);
                }
            }
        }

        return suitableUnits;
    }
}
