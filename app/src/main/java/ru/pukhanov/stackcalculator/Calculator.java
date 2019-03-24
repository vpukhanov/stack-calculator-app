package ru.pukhanov.stackcalculator;

import java.util.ArrayList;
import java.util.List;

public class Calculator {
    private ArrayList<Double> numbers;
    private OnUpdateListener updateListener;

    public Calculator() {
        numbers = new ArrayList<>();
    }

    public Calculator(ArrayList<Double> numbers) {
        this.numbers = numbers;
    }

    public void setOnUpdateListener(OnUpdateListener listener) {
        updateListener = listener;
    }

    public ArrayList<Double> getNumbers() {
        return numbers;
    }

    public boolean isUnaryOperationAvailable() {
        return numbers.size() >= 1;
    }

    public boolean isBinaryOperationAvailable() {
        return numbers.size() >= 2;
    }

    private void notifyUpdate() {
        if (updateListener != null) {
            updateListener.OnUpdate(numbers);
        }
    }

    public void addNumber(double number) {
        numbers.add(0, number);
        notifyUpdate();
    }

    public void deleteTopNumber() {
        numbers.remove(0);
        notifyUpdate();
    }

    public void swapTopNumbers() {
        Double t = numbers.get(0);
        numbers.set(0, numbers.get(1));
        numbers.set(1, t);
        notifyUpdate();
    }

    public void clear() {
        numbers.clear();
        notifyUpdate();
    }

    public void addTopNumbers() {
        double a = numbers.get(0);
        double b = numbers.get(1);

        numbers.set(1, a + b);
        numbers.remove(0);

        notifyUpdate();
    }

    public void subtractTopNumbers() {
        double a = numbers.get(0);
        double b = numbers.get(1);

        numbers.set(1, a - b);
        numbers.remove(0);

        notifyUpdate();
    }

    public void multiplyTopNumbers() {
        double a = numbers.get(0);
        double b = numbers.get(1);

        numbers.set(1, a * b);
        numbers.remove(0);

        notifyUpdate();
    }

    public void divideTopNumbers() {
        double a = numbers.get(0);
        double b = numbers.get(1);

        numbers.set(1, a / b);
        numbers.remove(0);

        notifyUpdate();
    }

    public void powerTopNumbers() {
        double a = numbers.get(0);
        double b = numbers.get(1);

        numbers.set(1, Math.pow(a, b));
        numbers.remove(0);

        notifyUpdate();
    }

    public void rootTopNumbers() {
        double a = numbers.get(0);
        double b = numbers.get(1);

        numbers.set(1, Math.pow(Math.E, Math.log(a) / b));
        numbers.remove(0);

        notifyUpdate();
    }

    public void logTopNumbers() {
        double a = numbers.get(0);
        double b = numbers.get(1);

        numbers.set(1, Math.log(a) / Math.log(b));
        numbers.remove(0);

        notifyUpdate();
    }

    public void sinTopNumber() {
        double a = numbers.get(0);
        numbers.set(0, Math.sin(a));
        notifyUpdate();
    }

    public void cosTopNumber() {
        double a = numbers.get(0);
        numbers.set(0, Math.cos(a));
        notifyUpdate();
    }

    public void tgTopNumber() {
        double a = numbers.get(0);
        numbers.set(0, Math.tan(a));
        notifyUpdate();
    }

    public void ctgTopNumber() {
        double a = numbers.get(0);
        numbers.set(0, Math.cos(a) / Math.sin(a));
        notifyUpdate();
    }

    public boolean isDivisionAvailable() {
        return isBinaryOperationAvailable() && numbers.get(1) != 0.0;
    }

    public boolean isRootAvailable() {
        if (!isBinaryOperationAvailable()) {
            return false;
        }

        double base = numbers.get(0);
        double power = numbers.get(1);

        return base > 0 && power > 0 && power != 1;
    }

    public boolean isLogAvailable() {
        if (!isBinaryOperationAvailable()) {
            return false;
        }

        double num = numbers.get(0);
        double base = numbers.get(1);

        return num > 0 && base > 0 && base != 1;
    }

    public interface OnUpdateListener {
        void OnUpdate(List<Double> numbers);
    }
}
