package org.jedi_bachelor.viewmodel;

import org.jedi_bachelor.model.State;
import org.jedi_bachelor.model.Timer;
import org.jedi_bachelor.view.View;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ViewModel {
    private final View view;
    private final Timer timer;

    public ViewModel() throws IOException {
        Properties prop = new Properties();
        InputStream in = getClass().getResourceAsStream("/timerSettings.properties");
        prop.load(in);
        in.close();

        this.timer = new Timer(Integer.parseInt(prop.getProperty("workTime")),
                Integer.parseInt(prop.getProperty("relaxTime")),
                Integer.parseInt(prop.getProperty("bigRelaxTime")));

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

    public int getRelaxTime() {
        return timer.getRelaxTime();
    }

    public int getBigRelaxTime() {
        return timer.getBigRelaxTime();
    }

    public void saveSettings(String _workTime, String _relaxTime, String _bigRelaxTime) throws IOException {
        Properties prop = new Properties();
        prop.setProperty("workTime", _workTime);
        prop.setProperty("relaxTime", _relaxTime);
        prop.setProperty("bigRelaxTime", _bigRelaxTime);

        String path = getClass().getResource("/timerSettings.properties").getPath();
        try(FileOutputStream fos = new FileOutputStream(path)) {
            prop.store(fos, null);
        }
    }
}
