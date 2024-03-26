package net.mafuyu33.mafishmod;

import java.util.Timer;
import java.util.TimerTask;

public class DelayedOperation {
    private Timer timer;

    public DelayedOperation() {
        timer = new Timer();
    }

    public void startDelayedOperation() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // 执行你想要延迟执行的操作
                System.out.println("Delayed operation executed.");
                // 重置计时器
                restartTimer();
            }
        }, 5000); // 延迟5秒执行，单位为毫秒
    }

    private void restartTimer() {
        // 取消当前任务
        timer.cancel();
        // 创建一个新的Timer对象，用于重置计时器
        timer = new Timer();
        // 重新启动延迟操作
        startDelayedOperation();
    }

    public static void main(String[] args) {
        DelayedOperation delayedOperation = new DelayedOperation();
        delayedOperation.startDelayedOperation();
    }
}

