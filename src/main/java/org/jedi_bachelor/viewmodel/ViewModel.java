package org.jedi_bachelor.viewmodel;

import org.jedi_bachelor.model.State;
import org.jedi_bachelor.model.Timer;
import org.jedi_bachelor.view.View;

public class ViewModel {
    private final View view;
    private final Timer timer;

    public ViewModel() {
        this.timer = new Timer();
        this.view = new View(this);

        timer.startProcessing();
        view.startCycle();
    }

    public void stopWorkingThreads() {
        try {
            timer.stopProcessing();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void startApp() {
        view.show();
    }

    public void startTimer() {
        timer.setState(State.WORK);
    }

    public void pauseTimer() {
        timer.setState(State.PAUSE);
    }

    public int getTime() {
        return timer.getSeconds();
    }

    // Сеттеры
    public void setWorkTime(int _m) {
        timer.setWorkTime(_m);
        view.updateTimeDisplay();
    }

    public void setRelaxTime(int _m) {
        timer.setRelaxTime(_m);
    }

    public void setBigRelaxTime(int _m) {
        timer.setBigRelaxTime(_m);
    }

    public int getWorkTime() {
        return timer.getWorkTime();
    }
}
