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
    @Setter
    @Getter
    private int relaxTime = 5;
    @Setter
    @Getter
    private int bigRelaxTime = 30;

    public Timer() {
        this.state = State.PAUSE;
        this.period = TimerPeriod.WORK;
        this.seconds = 60*workTime;

        this.timerThread = new TimerThread();
    }

    public void setWorkTime(int _m) {
        workTime = _m;
        seconds = 60 * workTime;
    }

    public void startProcessing() {
        timerThread.start();
    }

    class TimerThread extends Thread {
        @Override
        public void run() {
            while (true) {
                if(seconds == 0) {
                    try {
                        sleep(3000);
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
