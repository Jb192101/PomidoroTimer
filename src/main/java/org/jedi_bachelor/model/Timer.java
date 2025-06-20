package org.jedi_bachelor.model;

import lombok.Getter;
import lombok.Setter;

public class Timer {
    @Setter
    protected State state;
    protected TimerPeriod period;
    @Getter
    protected int seconds;
    protected TimerThread timerThread;
    protected int countOfChanges = 0;

    // Константы
    @Getter
    private int workTime = 25;
    @Getter
    private int relaxTime = 5;
    @Getter
    private int bigRelaxTime = 30;

    public Timer() {
        this.state = State.PAUSE;
        this.period = TimerPeriod.WORK;
        this.seconds = 60*workTime;

        this.timerThread = new TimerThread();
    }

    public void setWorkTime(int _m) {
        if(_m >= 0 && _m <= 60) {
            workTime = _m;
            seconds = 60 * workTime;
        }
    }

    public void setRelaxTime(int _m) {
        if(_m >= 0 && _m <= 60) {
            relaxTime = _m;
        }
    }

    public void setBigRelaxTime(int _m) {
        if(_m >= 0 && _m <= 60) {
            bigRelaxTime = _m;
        }
    }

    public void startProcessing() {
        timerThread.start();
    }

    public void stopProcessing() throws InterruptedException {
        timerThread.interrupt();
    }

    class TimerThread extends Thread {
        @Override
        public void run() {
            while (true) {
                if(seconds == 0) {
                    try {
                        sleep(27000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (state == org.jedi_bachelor.model.State.WORK) {
                    if (countOfChanges == 7) {
                        period = TimerPeriod.RELAX;
                        System.out.println(period);
                        seconds = bigRelaxTime * 60;
                        countOfChanges = 0;
                    } else if (period == TimerPeriod.WORK && seconds == 0) {
                        period = TimerPeriod.RELAX;
                        System.out.println(period);
                        seconds = relaxTime * 60;
                        countOfChanges++;
                    } else if (period == TimerPeriod.RELAX && seconds == 0) {
                        period = TimerPeriod.WORK;
                        System.out.println(period);
                        seconds = workTime * 60;
                        countOfChanges++;
                    }

                    seconds--;
                }

                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }
}
