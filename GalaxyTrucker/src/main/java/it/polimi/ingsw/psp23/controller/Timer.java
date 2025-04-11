package it.polimi.ingsw.psp23.controller;
import java.util.concurrent.*;

public class Timer {
    private final ScheduledExecutorService scheduler;
    private ScheduledFuture<?> countdownTask;

    public Timer() {
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * Avvia il countdown.
     *
     * @param seconds durata in secondi
     * @param onTimeout cosa eseguire allo scadere del tempo
     */
    public void startCountdown(int seconds, Runnable onTimeout) {
        // Se c'è già un countdown attivo, lo cancello
        if (countdownTask != null && !countdownTask.isDone()) {
            countdownTask.cancel(false);
        }

        countdownTask = scheduler.schedule(onTimeout, seconds, TimeUnit.SECONDS);
    }

    /**
     * Annulla il timer, se attivo
     */
    public void cancel() {
        if (countdownTask != null && !countdownTask.isDone()) {
            countdownTask.cancel(false);
        }
    }

    /**
     * Ferma il timer e chiude il thread (da chiamare alla fine della fase di building)
     */
    public void shutdown() {
        scheduler.shutdownNow();
    }
}
