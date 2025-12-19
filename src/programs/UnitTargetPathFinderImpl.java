package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.UnitTargetPathFinder;

import com.battle.heroes.army.programs.EdgeDistance;

import java.util.*;

public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {

    private static final int WIDTH = 27; // Ширина поля
    private static final int HEIGHT = 21; // Высота поля

    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {
        // Инициализация
        int[][] dist = new int[WIDTH][HEIGHT];
        boolean[][] visited = new boolean[WIDTH][HEIGHT];
        EdgeDistance[][] predecessors = new EdgeDistance[WIDTH][HEIGHT];
        PriorityQueue<EdgeDistance> queue = new PriorityQueue<>(Comparator.comparingInt(EdgeDistance::getDistance));

        // Заполняем расстояния бесконечностью
        for (int[] row : dist) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }

        // Начальная точка
        int startX = attackUnit.getxCoordinate();
        int startY = attackUnit.getyCoordinate();
        int targetX = targetUnit.getxCoordinate();
        int targetY = targetUnit.getyCoordinate();
        dist[startX][startY] = 0;
        queue.add(new EdgeDistance(startX, startY, 0));

        // Множество занятых клеток
        Set<String> occupied = new HashSet<>();
        for (Unit unit : existingUnitList) {
            if (unit.isAlive()) {
                occupied.add(unit.getxCoordinate() + "," + unit.getyCoordinate());
            }
        }

        // Убедимся, что начальная и целевая клетки не заняты
        occupied.remove(startX + "," + startY);
        occupied.remove(targetX + "," + targetY);

        // Направления для перемещения (включая диагонали)
        int[][] directions = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1}, // Прямые направления
                {-1, -1}, {-1, 1}, {1, -1}, {1, 1} // Диагонали
        };

        // Алгоритм Дейкстры
        while (!queue.isEmpty()) {
            EdgeDistance current = queue.poll();

            if (visited[current.getX()][current.getY()]) continue;
            visited[current.getX()][current.getY()] = true;

            // Проверяем соседние клетки
            for (int[] direction : directions) {
                int newX = current.getX() + direction[0];
                int newY = current.getY() + direction[1];

                // Проверка границ поля
                if (newX < 0 || newX >= WIDTH || newY < 0 || newY >= HEIGHT) {
                    continue;
                }

                // Проверка препятствий
                if (occupied.contains(newX + "," + newY)) {
                    continue;
                }

                // Новое расстояние
                int newDist = dist[current.getX()][current.getY()] + 1;
                if (newDist < dist[newX][newY]) {
                    dist[newX][newY] = newDist;
                    predecessors[newX][newY] = new EdgeDistance(current.getX(), current.getY(), newDist);
                    queue.add(new EdgeDistance(newX, newY, newDist));
                }
            }
        }

        // Построение пути
        List<Edge> path = new ArrayList<>();
        int x = targetX;
        int y = targetY;

        while (predecessors[x][y] != null) {
            path.add(0, new Edge(x, y));
            EdgeDistance predecessor = predecessors[x][y];
            x = predecessor.getX();
            y = predecessor.getY();
        }

        // Добавляем начальную точку, если путь найден
        if (x == startX && y == startY) {
            path.add(0, new Edge(startX, startY));
        }

        return path;
    }
}
