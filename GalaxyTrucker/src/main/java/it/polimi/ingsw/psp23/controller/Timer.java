package it.polimi.ingsw.psp23.controller;
import java.util.concurrent.*;

public class Timer {
    private final ScheduledExecutorService scheduler;
    private ScheduledFuture<?> countdownTask;

    public Timer() {
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * Starts a countdown for the specified amount of seconds, triggering the provided
     * callback when the countdown ends. If a countdown is already active, it will be canceled
     * before starting the new one.
     *
     * @param seconds the duration of the countdown in seconds
     * @param onTimeout the callback to execute when the countdown ends
     */
    public void startCountdown(int seconds, Runnable onTimeout) {
        // Se c'è già un countdown attivo, lo cancello
        if (countdownTask != null && !countdownTask.isDone()) {
            countdownTask.cancel(false);
        }

        countdownTask = scheduler.schedule(onTimeout, seconds, TimeUnit.SECONDS);
    }


    /**
     * Cancels the currently active countdown task, if one is running.
     * If there is no active countdown task, this method will have no effect.
     * The cancellation will not interrupt the currently executing task,
     * but will prevent it from running if it has not yet started.
     */
    public void cancel() {
        if (countdownTask != null && !countdownTask.isDone()) {
            countdownTask.cancel(false);
        }
    }

    /**
     * Shuts down the scheduled executor service, halting the execution of any running tasks immediately
     * and preventing any further tasks from being scheduled. This method should be called when the
     * Timer instance is no longer needed to release resources.
     *
     * It is important to note that tasks currently executing will be interrupted, and all
     * scheduled-but-not-yet-executed tasks will be canceled.
     */
    public void shutdown() {
        scheduler.shutdownNow();
    }
}
